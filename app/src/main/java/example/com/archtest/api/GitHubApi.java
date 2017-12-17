package example.com.archtest.api;

import java.util.List;

import example.com.archtest.data.RepoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static example.com.archtest.Constants.API.REPOS_LIST_URL;

interface GitHubApi {

	@GET(REPOS_LIST_URL)
	public Call<List<RepoInfo>> getWeatherInfo(@Query("page") int pageNum, @Query("per_page") int itemsPerPage);

}
