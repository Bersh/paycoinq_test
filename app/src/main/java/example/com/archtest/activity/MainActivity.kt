package example.com.archtest.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import example.com.archtest.R
import example.com.archtest.adapter.ReposAdapter
import example.com.archtest.api.LoadingListener
import example.com.archtest.data.RepoInfo
import example.com.archtest.viewmodel.ReposViewModel
import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoadingListener {
    private val LOAD_MORE_THRESHOLD = 3

    private lateinit var progressBar: ProgressBar
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var model: ReposViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = progress_bar

        model = ViewModelProviders.of(this).get(ReposViewModel::class.java)

        subscribeOnDataUpdates()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerRepos = repos_recycler
        layoutManager = LinearLayoutManager(this)
        recyclerRepos.layoutManager = layoutManager

        val dataFromDb : MutableList<RepoInfo> = model.data.value ?: ArrayList()
        if (dataFromDb.isEmpty()) {
            model.loadNextPage(this)
        }
        reposAdapter = ReposAdapter(dataFromDb)
        recyclerRepos.adapter = reposAdapter

        recyclerRepos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !model.isLoading() && !model.isAllDataLoaded()) { //scroll down and not already loading
                    val totalItemCount = layoutManager.itemCount
                    val lastItem = layoutManager.findLastVisibleItemPosition()
                    if (lastItem >= totalItemCount - LOAD_MORE_THRESHOLD) {
                        model.loadNextPage(this@MainActivity)
                        reposAdapter.addProgressFooter()
                    }
                }
            }
        })
    }

    private fun subscribeOnDataUpdates() {
        model.data.observe(this, Observer<MutableList<RepoInfo>> { data ->
            reposAdapter.removeProgressFooter()
            data?.let {
                reposAdapter.setData(data)
                reposAdapter.removeProgressFooter()
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onLoadError(message: String) {
        reposAdapter.removeProgressFooter()
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }

    override fun onAllDataLoaded() {
        reposAdapter.removeProgressFooter()
        Toast.makeText(application, R.string.all_data_loaded, Toast.LENGTH_SHORT).show()
    }

    override fun onPageLoaded(pageNumber: Int) {
        val pageLoadedMessage = application.resources.getString(R.string.page_loaded, pageNumber)
        Toast.makeText(application, pageLoadedMessage, Toast.LENGTH_SHORT).show()
    }
}