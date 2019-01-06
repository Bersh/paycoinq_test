package example.com.archtest.data

import android.arch.paging.PageKeyedDataSource
import android.content.Context
import android.util.Log
import example.com.archtest.api.ApiManager
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposDataSource(context : Context) : PageKeyedDataSource<Int, RepoInfo>() {
    private val apiManager = ApiManager()
    private lateinit var targetData: RealmResults<RepoInfo>
    private var realm: Realm? = null
    var allDataLoaded = false
    var loading = false

    init {
        Realm.init(context)
        realm = Realm.getDefaultInstance()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RepoInfo>) {
        if(allDataLoaded) {
            return
        }


        apiManager.getReposPage(0, params.requestedLoadSize, object : Callback<List<RepoInfo>> {
            override fun onFailure(call: Call<List<RepoInfo>>, t: Throwable) {
                Log.e(ReposDataSource::class.java.name, "Network error", t)
            }

            override fun onResponse(call: Call<List<RepoInfo>>, response: Response<List<RepoInfo>>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onResult(response.body() as List<RepoInfo>, 0, 1)
                } else {
                    Log.e(ReposDataSource::class.java.name, "Network error")
                    Log.e(ReposDataSource::class.java.name, response.errorBody().toString())
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepoInfo>) {
        if(allDataLoaded) {
            return
        }
        apiManager.getReposPage(params.key, params.requestedLoadSize, object : Callback<List<RepoInfo>> {
            override fun onFailure(call: Call<List<RepoInfo>>, t: Throwable) {
                Log.e(ReposDataSource::class.java.name, "Network error", t)
            }

            override fun onResponse(call: Call<List<RepoInfo>>, response: Response<List<RepoInfo>>) {
                processLoadedData(callback, response, params.key)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepoInfo>) {
    }

    fun processLoadedData(callback: LoadCallback<Int, RepoInfo>,
                          response: Response<List<RepoInfo>>, currentPage: Int) {
        if (response.isSuccessful && response.body() != null) {
            val nextKey = currentPage + 1
            callback.onResult(response.body() as List<RepoInfo>, nextKey)
        } else if(response.body() == null || (response.body() as List<RepoInfo>).isEmpty()) {
            allDataLoaded = true
        } else {
            Log.e(ReposDataSource::class.java.name, "Network error")
            Log.e(ReposDataSource::class.java.name, response.errorBody().toString())
        }
    }
}