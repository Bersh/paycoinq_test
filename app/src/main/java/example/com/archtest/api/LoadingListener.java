package example.com.archtest.api;

public interface LoadingListener {
	/**
	 * Called after each page loaded
	 * @param pageNumber loaded page number
	 */
	void onPageLoaded(int pageNumber);

	/**
	 * Called when there is no more data to load from network
	 */
	void onAllDataLoaded();

	/**
	 * Called when network request failed.
	 * Here you can display error message to user if needed
	 * @param message error message
	 */
	void onLoadError(String message);
}