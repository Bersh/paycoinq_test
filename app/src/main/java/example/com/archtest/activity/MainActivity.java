package example.com.archtest.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.archtest.R;
import example.com.archtest.adapter.ReposAdapter;
import example.com.archtest.api.LoadingListener;
import example.com.archtest.data.RepoInfo;
import example.com.archtest.viewmodel.ReposViewModel;

public class MainActivity extends AppCompatActivity implements LoadingListener {
	private static final int LOAD_MORE_THRESHOLD = 3;

	private ProgressBar progressBar;
	private ReposAdapter reposAdapter;
	private LinearLayoutManager layoutManager;
	private ReposViewModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = findViewById(R.id.progress_bar);
		model = ViewModelProviders.of(this).get(ReposViewModel.class);

		subscribeOnDataUpdates();
		setupRecyclerView();
	}

	private void setupRecyclerView() {
		RecyclerView recyclerRepos = findViewById(R.id.repos_recycler);
		layoutManager = new LinearLayoutManager(this);
		recyclerRepos.setLayoutManager(layoutManager);

		List<RepoInfo> dataFromDb = model.getData().getValue();
		if (dataFromDb == null || dataFromDb.isEmpty()) {
			dataFromDb = new ArrayList<>();
			model.loadNextPage(this);
		}
		reposAdapter = new ReposAdapter(dataFromDb);
		recyclerRepos.setAdapter(reposAdapter);

		recyclerRepos.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (dy > 0 && !model.isLoading() && !model.isAllDataLoaded()) { //scroll down and not already loading
					int totalItemCount = layoutManager.getItemCount();
					int lastItem = layoutManager.findLastVisibleItemPosition();
					if (lastItem >= totalItemCount - LOAD_MORE_THRESHOLD) {
						model.loadNextPage(MainActivity.this);
						reposAdapter.addProgressFooter();
					}
				}
			}
		});
	}

	private void subscribeOnDataUpdates() {
		model.getData().observe(this, data -> {
			reposAdapter.removeProgressFooter();
			if (data != null) {
				reposAdapter.setData(data);
				reposAdapter.removeProgressFooter();
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}

			int loadedPageNumber = model.getLastLoadedPage();
			String pageLoadedMessage = getApplication().getResources().getString(R.string.page_loaded, loadedPageNumber);
			Toast.makeText(getApplication(), pageLoadedMessage, Toast.LENGTH_SHORT).show();
		});
	}

	public void onLoadError(String message) {
		if (reposAdapter != null) {
			reposAdapter.removeProgressFooter();
		}
		Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
	}

	public void onAllDataLoaded() {
		if (reposAdapter != null) {
			reposAdapter.removeProgressFooter();
		}
		Toast.makeText(getApplication(), R.string.all_data_loaded, Toast.LENGTH_SHORT).show();
	}
}
