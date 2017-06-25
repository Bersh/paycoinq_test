package example.com.paycoinqtest.api;

import example.com.paycoinqtest.model.RepoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static example.com.paycoinqtest.Constants.API.REPOS_LIST_URL;

interface GitHubApi {

	@GET(REPOS_LIST_URL)
	public Call<RepoInfo> getWeatherInfo(@Query("page") int pageNum, @Query("per_page") int itemsPerPage);

}
