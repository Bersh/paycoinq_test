package example.com.paycoinqtest.api;

import java.io.IOException;
import java.util.List;

import example.com.paycoinqtest.Constants;
import example.com.paycoinqtest.data.RepoInfo;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is responsible for all web api interactions
 */
public class ApiManager {
	private GitHubApi gitHubApi;
	private static final String TAG = ApiManager.class.getName();
	public static final int ITEMS_PER_PAGE = 15;

	public ApiManager() {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Constants.API.BASE_URL)
				.client(clientBuilder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		gitHubApi = retrofit.create(GitHubApi.class);
	}

	/**
	 * This constructor should be used in unit tests to inject mock server responses
	 *
	 * @param mockInterceptor interceptor that will return mock data
	 */
	ApiManager(Interceptor mockInterceptor) {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		clientBuilder.addInterceptor(mockInterceptor);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Constants.API.BASE_URL)
				.client(clientBuilder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		gitHubApi = retrofit.create(GitHubApi.class);
	}

	public void getReposPage(int pageNumber, Callback<List<RepoInfo>> callback) {
		gitHubApi.getWeatherInfo(pageNumber, ITEMS_PER_PAGE).enqueue(callback);
	}

	Response<List<RepoInfo>> getReposPage(int pageNumber) throws IOException {
		return gitHubApi.getWeatherInfo(pageNumber, ITEMS_PER_PAGE).execute();
	}

}
