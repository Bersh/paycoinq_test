package example.com.archtest.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import example.com.archtest.App;
import example.com.archtest.PreferencesManager;
import example.com.archtest.R;
import example.com.archtest.activity.MainActivity;
import example.com.archtest.api.ApiManager;
import example.com.archtest.data.RepoInfo;
import example.com.archtest.data.ReposLiveData;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposViewModel extends AndroidViewModel {
	private int LAST_LOADED_PAGE;
	private boolean allDataLoaded = false;
	private boolean loading = false;

	private final ReposLiveData data;
	private ApiManager apiManager;

	public ReposViewModel(Application application) {
		super(application);
		data = new ReposLiveData(application);
		apiManager = ((App) application).getApiManager();
		LAST_LOADED_PAGE = PreferencesManager.getLoadedPage(application);
	}

	public LiveData<List<RepoInfo>> getData() {
		return data;
	}

	public void loadNextPage(@Nullable LoadingFinishedListener listener) {
		if (!allDataLoaded) {
			apiManager.getReposPage(++LAST_LOADED_PAGE, new DataCallback(listener));
			loading = true;
		}
	}

	public boolean isAllDataLoaded() {
		return allDataLoaded;
	}

	public boolean isLoading() {
		return loading;
	}

	public interface LoadingFinishedListener {
		/**
		 * You can do cleanup here if needed
		 */
		void onLoadingFinished();
	}

	private class DataCallback implements Callback<List<RepoInfo>> {
		@Nullable
		private LoadingFinishedListener listener;

		public DataCallback(@Nullable LoadingFinishedListener listener) {
			this.listener = listener;
		}

		@Override
		public void onResponse(Call<List<RepoInfo>> call, Response<List<RepoInfo>> response) {
			loading = false;
			if (listener != null) {
				listener.onLoadingFinished();
			}
			List<RepoInfo> items = response.body();
			if (items == null || items.isEmpty()) {
				Toast.makeText(getApplication(), R.string.all_data_loaded, Toast.LENGTH_SHORT).show();
				allDataLoaded = true;
				return;
			}

			Realm realm = null;
			try {
				realm = Realm.getDefaultInstance();
				realm.executeTransaction(realm1 -> realm1.insertOrUpdate(response.body()));
			} finally {
				if (realm != null) {
					realm.close();
				}
			}
			PreferencesManager.saveLoadedPage(getApplication(), LAST_LOADED_PAGE);
			String pageLoadedMessage = getApplication().getResources().getString(R.string.page_loaded, LAST_LOADED_PAGE);
			Toast.makeText(getApplication(), pageLoadedMessage, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFailure(Call<List<RepoInfo>> call, Throwable t) {
			loading = false;
			if (listener != null) {
				listener.onLoadingFinished();
			}
			Log.e(MainActivity.class.getName(), "Network error", t);
			Toast.makeText(getApplication(), R.string.network_error, Toast.LENGTH_LONG).show();
		}
	}
}