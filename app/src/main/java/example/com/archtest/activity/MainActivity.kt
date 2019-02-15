package example.com.archtest.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import example.com.archtest.R
import example.com.archtest.adapter.ReposAdapter
import example.com.archtest.adapter.ReposAdapterNew
import example.com.archtest.api.LoadingListener
import example.com.archtest.data.LoadingState
import example.com.archtest.data.RepoInfo
import example.com.archtest.viewmodel.ReposViewModel
import io.realm.Realm
import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoadingListener {
    private val LOAD_MORE_THRESHOLD = 3

    private lateinit var progressBar: ProgressBar
    private lateinit var reposAdapter: ReposAdapterNew
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var model: ReposViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = progress_bar
        Realm.init(this)

        model = ViewModelProviders.of(this).get(ReposViewModel::class.java)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerRepos = repos_recycler
        layoutManager = LinearLayoutManager(this)
        recyclerRepos.layoutManager = layoutManager

        reposAdapter = ReposAdapterNew()
        recyclerRepos.adapter = reposAdapter
        model.reposLiveData.observe(this,
                Observer<PagedList<RepoInfo>> { t ->
                    reposAdapter.submitList(t)
                    progressBar.visibility = View.GONE
                })

        model.loadingState.observe(this, Observer<LoadingState> {
            when(it) {
                LoadingState.LOADING -> {}
                LoadingState.LOAD_FINISHED -> onPageLoaded(1)
                LoadingState.ALL_DATA_LOADED -> onAllDataLoaded()
                LoadingState.ERROR -> onLoadError("aaa")
            }
        })

    }

    override fun onLoadError(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }

    override fun onAllDataLoaded() {
        Toast.makeText(application, R.string.all_data_loaded, Toast.LENGTH_SHORT).show()
    }

    override fun onPageLoaded(pageNumber: Int) {
        val pageLoadedMessage = application.resources.getString(R.string.page_loaded, pageNumber)
        Toast.makeText(application, pageLoadedMessage, Toast.LENGTH_SHORT).show()
    }
}