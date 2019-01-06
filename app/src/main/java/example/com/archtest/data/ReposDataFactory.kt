package example.com.archtest.data

import android.arch.paging.DataSource
import android.arch.lifecycle.MutableLiveData



class ReposDataFactory : DataSource.Factory<Int, RepoInfo>() {
    private val mutableLiveData: MutableLiveData<ReposDataSource> = MutableLiveData()
    lateinit var reposDataSource: ReposDataSource

    override fun create(): DataSource<Int, RepoInfo> {
        reposDataSource = ReposDataSource()
        mutableLiveData.postValue(reposDataSource)
        return reposDataSource
    }
}