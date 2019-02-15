package example.com.archtest.data

import android.arch.paging.DataSource
import android.arch.lifecycle.MutableLiveData


class ReposDataFactory() : DataSource.Factory<Int, RepoInfo>() {
    private val mutableLiveData: MutableLiveData<DataRepository> = MutableLiveData()
    lateinit var reposDataSource: DataRepository

    override fun create(): DataSource<Int, RepoInfo> {
        reposDataSource = DataRepository()
        mutableLiveData.postValue(reposDataSource)
        return reposDataSource
    }
}