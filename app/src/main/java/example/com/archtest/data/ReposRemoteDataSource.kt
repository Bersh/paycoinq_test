package example.com.archtest.data

import android.util.Log
import example.com.archtest.api.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposRemoteDataSource : ReposDataSource {
    private val apiManager = ApiManager()

    override fun getRepos(pageNumber: Int, pageSize: Int, callback: ReposDataSource.LoadReposCallback) {
        apiManager.getReposPage(pageNumber, pageSize, object : Callback<List<RepoInfo>> {
            override fun onFailure(call: Call<List<RepoInfo>>, t: Throwable) {
                Log.e(DataRepository::class.java.name, "Network error", t)
            }

            override fun onResponse(call: Call<List<RepoInfo>>, response: Response<List<RepoInfo>>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onReposLoaded(response.body() as List<RepoInfo>)
                } else {
                    Log.e(DataRepository::class.java.name, "Network error")
                    Log.e(DataRepository::class.java.name, response.errorBody().toString())
                    callback.onLoadingError(response.errorBody().toString())
                }
            }
        })
    }
}