package example.com.archtest.data

import android.arch.lifecycle.LiveData
import android.content.Context
import android.util.Log
import example.com.archtest.PreferencesManager
import example.com.archtest.R
import example.com.archtest.activity.MainActivity
import example.com.archtest.api.ApiManager
import example.com.archtest.api.LoadingListener
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposLiveData(val context: Context) : LiveData<MutableList<RepoInfo>>() {
    private lateinit var targetData: RealmResults<RepoInfo>
    private var realm: Realm? = null
    private var LAST_LOADED_PAGE: Int = 0
    var allDataLoaded = false
    var loading = false
    private val apiManager: ApiManager

    init {
        Realm.init(context)
        this.apiManager = ApiManager()
        LAST_LOADED_PAGE = PreferencesManager.getLoadedPage(context)
        loadData()
    }

    override fun onActive() {
        if (realm == null) {
            realm = Realm.getDefaultInstance()
        }
        targetData.addChangeListener { results, changeSet -> setValue(targetData) }
    }

    override fun onInactive() {
        targetData.removeAllChangeListeners()
        realm?.close()
        realm = null
    }

    private fun loadData() {
        val realm = Realm.getDefaultInstance()
        targetData = realm.where(RepoInfo::class.java).findAll()
        value = targetData
    }

    fun loadNextPage(listener: LoadingListener?) {
        if (!allDataLoaded) {
            loading = true
            apiManager.getReposPage(++LAST_LOADED_PAGE, DataCallback(listener))
        }
    }

    /**
     * @return last loaded page number starting from 0
     */
    fun getLastLoadedPage(): Int {
        return PreferencesManager.getLoadedPage(context)
    }

    private inner class DataCallback(private val listener: LoadingListener?) : Callback<List<RepoInfo>> {

        override fun onResponse(call: Call<List<RepoInfo>>, response: Response<List<RepoInfo>>) {
            loading = false
            val items = response.body()
            if (items == null || items.isEmpty()) {
                allDataLoaded = true
                listener?.onAllDataLoaded()
                return
            }

            var realm: Realm? = null
            try {
                realm = Realm.getDefaultInstance()
                realm?.executeTransaction { realm1 -> realm1.insertOrUpdate(items) }
            } finally {
                realm?.close()
            }
            PreferencesManager.saveLoadedPage(context, LAST_LOADED_PAGE)
            listener?.onPageLoaded(LAST_LOADED_PAGE)
        }

        override fun onFailure(call: Call<List<RepoInfo>>, t: Throwable) {
            loading = false
            listener?.onLoadError(context.getString(R.string.network_error))
            Log.e(MainActivity::class.java.name, "Network error", t)
        }
    }

}