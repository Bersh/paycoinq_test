package example.com.archtest.api

import example.com.archtest.Constants.API.REPOS_LIST_URL
import example.com.archtest.data.RepoInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET(REPOS_LIST_URL)
    abstract fun getWeatherInfo(@Query("page") pageNum: Int, @Query("per_page") itemsPerPage: Int): Call<List<RepoInfo>>

}