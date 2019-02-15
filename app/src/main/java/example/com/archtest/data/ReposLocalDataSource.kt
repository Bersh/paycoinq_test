package example.com.archtest.data

import android.content.Context
import io.realm.Realm

class ReposLocalDataSource : ReposDataSource {
    private var realm: Realm = Realm.getDefaultInstance()

    override fun getRepos(pageNumber: Int, pageSize: Int, callback: ReposDataSource.LoadReposCallback) {
        try {
            val targetData = realm.where(RepoInfo::class.java).findAll()
            //TODO here we need to really load data from db page by page
            callback.onReposLoaded(realm.copyFromRealm(targetData).subList(pageNumber * pageSize, (pageNumber + 1) * pageSize))
        } catch  (e: Exception) {
            callback.onLoadingError(e.message ?: "")
        }
    }

    fun saveToLocalStorage(repos: List<RepoInfo>) {
        realm.executeTransaction {
            it.copyToRealm(repos)
        }
    }
}