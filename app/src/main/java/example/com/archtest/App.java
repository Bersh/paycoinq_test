package example.com.archtest;


import android.app.Application;

import example.com.archtest.api.ApiManager;

public class App extends Application {
	private ApiManager apiManager;

	public App() {
		apiManager = new ApiManager();
	}

	public ApiManager getApiManager() {
		return apiManager;
	}

	public void setApiManager(ApiManager apiManager) {
		this.apiManager = apiManager;
	}
}
