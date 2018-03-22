package example.com.archtest.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import java.util.List;

import example.com.archtest.api.LoadingListener;
import example.com.archtest.data.RepoInfo;
import example.com.archtest.data.ReposLiveData;

public class ReposViewModel extends AndroidViewModel {
	private final ReposLiveData data;

	public ReposViewModel(Application application) {
		super(application);
		data = new ReposLiveData(application);
	}

	public LiveData<List<RepoInfo>> getData() {
		return data;
	}

	public void loadNextPage(@Nullable LoadingListener listener) {
		data.loadNextPage(listener);
	}

	public int getLastLoadedPage() {
		//Count pages starting from 1 to not confuse user
		return data.getLastLoadedPage() + 1;
	}

	public boolean isAllDataLoaded() {
		return data.isAllDataLoaded();
	}

	public boolean isLoading() {
		return data.isLoading();
	}
}
