package example.com.archtest.data

interface ReposDataSource {
    fun getRepos(pageNumber: Int, pageSize: Int, callback: LoadReposCallback)

    interface LoadReposCallback {
        fun onReposLoaded(repos: List<RepoInfo>)
        fun onLoadingError(errorMsg: String)
    }
}