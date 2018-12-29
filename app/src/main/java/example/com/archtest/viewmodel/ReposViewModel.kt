package example.com.archtest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import example.com.archtest.api.LoadingListener
import example.com.archtest.data.RepoInfo
import example.com.archtest.data.ReposLiveData

class ReposViewModel(application: Application) : AndroidViewModel(application) {
    val data: ReposLiveData = ReposLiveData(application)

    fun loadNextPage(listener: LoadingListener?) {
        data.loadNextPage(listener)
    }

    fun getLastLoadedPage(): Int {
        //Count pages starting from 1 to not confuse user
        return data.getLastLoadedPage() + 1
    }

    fun isAllDataLoaded(): Boolean {
        return data.allDataLoaded
    }

    fun isLoading(): Boolean {
        return data.loading
    }
}