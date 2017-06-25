package example.com.paycoinqtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import example.com.paycoinqtest.PaycoinqApp;
import example.com.paycoinqtest.R;
import example.com.paycoinqtest.adapter.ReposAdapter;
import example.com.paycoinqtest.api.ApiManager;

public class MainActivity extends AppCompatActivity {

	private RecyclerView recyclerRepos;
	private ApiManager apiManager;
	private ProgressBar progressBar;
	private ReposAdapter reposAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		apiManager = ((PaycoinqApp) getApplication()).getApiManager();
		recyclerRepos = (RecyclerView) findViewById(R.id.repos_recycler);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
	}


}
