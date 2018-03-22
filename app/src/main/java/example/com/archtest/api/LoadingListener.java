package example.com.archtest.api;

public interface LoadingListener {
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