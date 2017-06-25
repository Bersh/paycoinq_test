package example.com.paycoinqtest.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import example.com.paycoinqtest.PaycoinqApp;
import example.com.paycoinqtest.PreferencesManager;
import example.com.paycoinqtest.R;
import example.com.paycoinqtest.activity.MainActivity;
import example.com.paycoinqtest.data.ReposLiveData;
import example.com.paycoinqtest.api.ApiManager;
import example.com.paycoinqtest.data.RepoInfo;
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
		apiManager = ((PaycoinqApp) application).getApiManager();
		LAST_LOADED_PAGE = PreferencesManager.getLoadedPage(application);
	}

	public LiveData<List<RepoInfo>> getData() {
		return data;
	}

	public void loadNextPage() {
		if (!allDataLoaded) {
			apiManager.getReposPage(++LAST_LOADED_PAGE, new DataCallback());
			loading = true;
		}
	}

	public boolean isAllDataLoaded() {
		return allDataLoaded;
	}

	public boolean isLoading() {
		return loading;
	}

	private class DataCallback implements Callback<List<RepoInfo>> {

		@Override
		public void onResponse(Call<List<RepoInfo>> call, Response<List<RepoInfo>> response) {
			loading = false;
			allDataLoaded = LAST_LOADED_PAGE > 2; //TODO remove
			List<RepoInfo> items = response.body();
			if (items == null || items.isEmpty()) {
				Toast.makeText(getApplication(), "No more data to load", Toast.LENGTH_SHORT).show();
				allDataLoaded = true;
				return;
			}

			Realm realm = Realm.getDefaultInstance();
			realm.beginTransaction();
			try {
				realm.insertOrUpdate(response.body());
				realm.commitTransaction();
			} catch (Exception e) {
				realm.cancelTransaction();
			}
			PreferencesManager.saveLoadedPage(getApplication(), LAST_LOADED_PAGE);
			Toast.makeText(getApplication(), "Page " + LAST_LOADED_PAGE + " loaded", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFailure(Call<List<RepoInfo>> call, Throwable t) {
			loading = false;
			Log.e(MainActivity.class.getName(), "Network error", t);
			Toast.makeText(getApplication(), R.string.network_error, Toast.LENGTH_LONG).show();
		}
	}
}
