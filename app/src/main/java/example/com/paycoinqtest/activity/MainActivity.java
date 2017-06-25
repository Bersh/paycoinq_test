package example.com.paycoinqtest.activity;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import example.com.paycoinqtest.R;
import example.com.paycoinqtest.adapter.ReposAdapter;
import example.com.paycoinqtest.data.RepoInfo;
import example.com.paycoinqtest.viewmodel.ReposViewModel;

public class MainActivity extends LifecycleActivity implements ReposViewModel.LoadingFinishedListener {
	private static final int LOAD_MORE_TRESHOLD = 3;

	private ProgressBar progressBar;
	private ReposAdapter reposAdapter;
	private LinearLayoutManager layoutManager;
	private ReposViewModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		model = ViewModelProviders.of(this).get(ReposViewModel.class);
		model.getData().observe(this, data -> {
			reposAdapter.removeProgressFooter();
			if (data != null) {
				reposAdapter.setData(data);
				reposAdapter.removeProgressFooter();
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});

		RecyclerView recyclerRepos = (RecyclerView) findViewById(R.id.repos_recycler);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

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
					if (lastItem >= totalItemCount - LOAD_MORE_TRESHOLD) {
						model.loadNextPage(MainActivity.this);
						reposAdapter.addProgressFooter();
					}
				}
			}
		});
	}

	@Override
	public void onLoadingFinished() {
		if(reposAdapter != null) {
			reposAdapter.removeProgressFooter();
		}
	}
}
