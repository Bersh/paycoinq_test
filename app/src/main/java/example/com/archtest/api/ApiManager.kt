package example.com.archtest.api

import example.com.archtest.Constants
import example.com.archtest.data.RepoInfo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * This class is responsible for all web api interactions
 */
class ApiManager() {
    private var gitHubApi: GitHubApi
    private val TAG = ApiManager::class.java.name
    val ITEMS_PER_PAGE = 15

    init {
        val clientBuilder = OkHttpClient.Builder()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API.BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)
    }

    /**
     * This constructor should be used in unit tests to inject mock server responses
     *
     * @param mockInterceptor interceptor that will return mock data
     */
    constructor(mockInterceptor: Interceptor) : this() {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(mockInterceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API.BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)
    }

    fun getReposPage(pageNumber: Int, callback: Callback<List<RepoInfo>>) {
        gitHubApi.getWeatherInfo(pageNumber, ITEMS_PER_PAGE).enqueue(callback)
    }

    @Throws(IOException::class)
    fun getReposPage(pageNumber: Int): Response<List<RepoInfo>> {
        return gitHubApi.getWeatherInfo(pageNumber, ITEMS_PER_PAGE).execute()
    }

}