package example.com.archtest.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource

class DataRepository : PageKeyedDataSource<Int, RepoInfo>() {
    var loadingState = MutableLiveData<LoadingState>().apply { value = LoadingState.ALL_DATA_LOADED }

    val localStorage = ReposLocalDataSource()
    val remoteStorage = ReposRemoteDataSource()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RepoInfo>) {
        if(LoadingState.ALL_DATA_LOADED == loadingState || LoadingState.LOADING == loadingState) {
            return
        }

        localStorage.getRepos(0, params.requestedLoadSize, LocalRemoteInitLoadingCallback(callback, params.requestedLoadSize))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepoInfo>) {
        if(LoadingState.ALL_DATA_LOADED == loadingState || LoadingState.LOADING == loadingState) {
            return
        }

        localStorage.getRepos(0, params.requestedLoadSize, LocalRemoteLoadingCallback(callback, params.key, params.requestedLoadSize))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepoInfo>) {
    }

    inner class LocalRemoteInitLoadingCallback(val callback: LoadInitialCallback<Int, RepoInfo>, private val requestedLoadSize: Int) : ReposDataSource.LoadReposCallback {
        override fun onReposLoaded(repos: List<RepoInfo>) {
            if (!repos.isEmpty()) {
                callback.onResult(repos, 0, 1)
                loadingState.postValue(LoadingState.LOAD_FINISHED)
            } else {
                remoteStorage.getRepos(0, requestedLoadSize, object: ReposDataSource.LoadReposCallback {
                    override fun onReposLoaded(repos: List<RepoInfo>) {
                        if(repos.isEmpty()) {
                            loadingState.postValue(LoadingState.ALL_DATA_LOADED)
                        } else {
                            callback.onResult(repos, 0, 1)
                            localStorage.saveToLocalStorage(repos)
                            loadingState.postValue(LoadingState.LOAD_FINISHED)
                        }
                    }

                    override fun onLoadingError(errorMsg: String) {
                        loadingState.postValue(LoadingState.ERROR)
                    }

                } )
            }
        }

        override fun onLoadingError(errorMsg: String) {
            loadingState.postValue(LoadingState.ERROR)
        }
    }

    inner class LocalRemoteLoadingCallback(val callback: LoadCallback<Int, RepoInfo>, val pageNum: Int, private val requestedLoadSize: Int) : ReposDataSource.LoadReposCallback {
        override fun onReposLoaded(repos: List<RepoInfo>) {
            if (!repos.isEmpty()) {
                callback.onResult(repos, pageNum + 1)
                loadingState.postValue(LoadingState.LOAD_FINISHED)
            } else {
                remoteStorage.getRepos(0, requestedLoadSize, object: ReposDataSource.LoadReposCallback {
                    override fun onReposLoaded(repos: List<RepoInfo>) {
                        if(repos.isEmpty()) {
                            loadingState.postValue(LoadingState.ALL_DATA_LOADED)
                        } else {
                            callback.onResult(repos, pageNum + 1)
                            localStorage.saveToLocalStorage(repos)
                            loadingState.postValue(LoadingState.LOAD_FINISHED)
                        }
                    }

                    override fun onLoadingError(errorMsg: String) {
                        loadingState.postValue(LoadingState.ERROR)
                    }

                } )
            }
        }

        override fun onLoadingError(errorMsg: String) {
            loadingState.postValue(LoadingState.ERROR)
        }
    }
}