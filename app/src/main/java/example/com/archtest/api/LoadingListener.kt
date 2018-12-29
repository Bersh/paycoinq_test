package example.com.archtest.api

interface LoadingListener {
    /**
     * Called after each page loaded
     * @param pageNumber loaded page number
     */
    fun onPageLoaded(pageNumber: Int)

    /**
     * Called when there is no more data to load from network
     */
    fun onAllDataLoaded()

    /**
     * Called when network request failed.
     * Here you can display error message to user if needed
     * @param message error message
     */
    fun onLoadError(message: String)
}