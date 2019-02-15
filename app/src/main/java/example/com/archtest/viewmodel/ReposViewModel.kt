package example.com.archtest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import example.com.archtest.data.LoadingState
import example.com.archtest.data.RepoInfo
import example.com.archtest.data.ReposDataFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ReposViewModel(application: Application) : AndroidViewModel(application) {
//    val data: ReposLiveData = ReposLiveData(application)
    private val executor: Executor = Executors.newFixedThreadPool(5)
    var reposLiveData: LiveData<PagedList<RepoInfo>>

    val loadingState = MutableLiveData<LoadingState>()

    init {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(20).build()

        val reposDataFactory = ReposDataFactory()
        reposLiveData = LivePagedListBuilder(reposDataFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()
    }

//    fun loadNextPage(listener: LoadingListener?) {
//        data.loadNextPage(listener)
//    }

//    fun getLastLoadedPage(): Int {
//        //Count pages starting from 1 to not confuse user
//        return data.getLastLoadedPage() + 1
//    }
}