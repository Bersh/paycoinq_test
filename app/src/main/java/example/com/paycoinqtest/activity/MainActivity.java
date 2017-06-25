package example.com.paycoinqtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import example.com.paycoinqtest.PaycoinqApp;
import example.com.paycoinqtest.R;
import example.com.paycoinqtest.adapter.ReposAdapter;
import example.com.paycoinqtest.api.ApiManager;
import example.com.paycoinqtest.model.RepoInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
	private static final int LOAD_MORE_TRESHOLD = 3;
	private static int LAST_LOADED_PAGE = 0;
	private static boolean allDataLoaded = false;
	private static boolean loading = false;

	private RecyclerView recyclerRepos;
	private ApiManager apiManager;
	private ProgressBar progressBar;
	private ReposAdapter reposAdapter;
	private LinearLayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		apiManager = ((PaycoinqApp) getApplication()).getApiManager();
		recyclerRepos = (RecyclerView) findViewById(R.id.repos_recycler);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		layoutManager = new LinearLayoutManager(this);
		recyclerRepos.setLayoutManager(layoutManager);

		loadNextPage();

		recyclerRepos.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (dy > 0 && !loading) { //scroll down and not already loading
					int totalItemCount = layoutManager.getItemCount();
					int lastItem = layoutManager.findLastVisibleItemPosition();
					if (lastItem >= totalItemCount - LOAD_MORE_TRESHOLD) {
						loadNextPage();
					}
				}
			}
		});
	}

	private void loadNextPage() {
		if (!allDataLoaded) {
			apiManager.getReposPage(++LAST_LOADED_PAGE, new DataCallback());
			loading = true;
			if(reposAdapter != null) {
				reposAdapter.addProgressFooter();
			}
		}
	}

	private class DataCallback implements Callback<List<RepoInfo>> {

		@Override
		public void onResponse(Call<List<RepoInfo>> call, Response<List<RepoInfo>> response) {
			try {
				allDataLoaded = LAST_LOADED_PAGE > 2; //TODO remove
				List<RepoInfo> items = response.body();
				if (items == null || items.isEmpty()) {
					Toast.makeText(MainActivity.this, "No more data to load", Toast.LENGTH_SHORT).show();
					allDataLoaded = true;
				} else {
					if (reposAdapter == null) {
						reposAdapter = new ReposAdapter(items);
						recyclerRepos.setAdapter(reposAdapter);
					} else {
						reposAdapter.removeProgressFooter();
						reposAdapter.addAll(items);
					}
					Toast.makeText(MainActivity.this, "Page " + LAST_LOADED_PAGE + " loaded", Toast.LENGTH_SHORT).show(); //TODO remove
				}
				progressBar.setVisibility(View.GONE);
			} finally {
				loading = false;
			}
		}

		@Override
		public void onFailure(Call<List<RepoInfo>> call, Throwable t) {
			if(reposAdapter != null) {
				reposAdapter.removeProgressFooter();
			}
			loading = false;
			Log.e(MainActivity.class.getName(), "Network error", t);
			Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
		}
	}
}
