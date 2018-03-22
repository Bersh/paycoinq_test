package example.com.archtest.data;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import example.com.archtest.PreferencesManager;
import example.com.archtest.R;
import example.com.archtest.activity.MainActivity;
import example.com.archtest.api.ApiManager;
import example.com.archtest.api.LoadingListener;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposLiveData extends LiveData<List<RepoInfo>> {
	private RealmResults<RepoInfo> targetData;
	private Realm realm;
	private int LAST_LOADED_PAGE;
	private boolean allDataLoaded = false;
	private boolean loading = false;
	private ApiManager apiManager;
	private Context context;


	public ReposLiveData(Context context) {
		Realm.init(context);
		this.context = context;
		this.apiManager = new ApiManager();
		LAST_LOADED_PAGE = PreferencesManager.getLoadedPage(context);
		loadData();
	}

	@Override
	protected void onActive() {
		if (realm == null) {
			realm = Realm.getDefaultInstance();
		}
		targetData.addChangeListener((results, changeSet) -> setValue(targetData));
	}

	@Override
	protected void onInactive() {
		targetData.removeAllChangeListeners();
		realm.close();
		realm = null;
	}

	private void loadData() {
		Realm realm = Realm.getDefaultInstance();
		targetData = realm.where(RepoInfo.class).findAll();
		setValue(targetData);
	}

	public void loadNextPage(@Nullable LoadingListener listener) {
		if (!allDataLoaded) {
			loading = true;
			apiManager.getReposPage(++LAST_LOADED_PAGE, new DataCallback(listener));
		}
	}

	public boolean isLoading() {
		return loading;
	}

	public boolean isAllDataLoaded() {
		return allDataLoaded;
	}

	/**
	 * @return last loaded page number starting from 0
	 */
	public int getLastLoadedPage() {
		return PreferencesManager.getLoadedPage(context);
	}

	private class DataCallback implements Callback<List<RepoInfo>> {
		@Nullable
		private LoadingListener listener;

		@SuppressWarnings("WeakerAccess")
		public DataCallback(@Nullable LoadingListener listener) {
			this.listener = listener;
		}

		@Override
		public void onResponse(@NonNull Call<List<RepoInfo>> call, @NonNull Response<List<RepoInfo>> response) {
			loading = false;
			List<RepoInfo> items = response.body();
			if (items == null || items.isEmpty()) {
				allDataLoaded = true;
				if (listener != null) {
					listener.onAllDataLoaded();
				}
				return;
			}

			Realm realm = null;
			try {
				realm = Realm.getDefaultInstance();
				realm.executeTransaction(realm1 -> realm1.insertOrUpdate(items));
			} finally {
				if (realm != null) {
					realm.close();
				}
			}
			PreferencesManager.saveLoadedPage(context, LAST_LOADED_PAGE);
			if (listener != null) {
				listener.onPageLoaded(LAST_LOADED_PAGE);
			}
		}

		@Override
		public void onFailure(@NonNull Call<List<RepoInfo>> call, @NonNull Throwable t) {
			loading = false;
			if (listener != null) {
				listener.onLoadError(context.getString(R.string.network_error));
			}
			Log.e(MainActivity.class.getName(), "Network error", t);
		}
	}
}
