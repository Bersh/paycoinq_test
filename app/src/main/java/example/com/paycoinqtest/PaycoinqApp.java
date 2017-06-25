package example.com.paycoinqtest;


import android.app.Application;

import example.com.paycoinqtest.api.ApiManager;

public class PaycoinqApp extends Application {
	private ApiManager apiManager;

	public PaycoinqApp() {
		apiManager = new ApiManager();
	}

	public ApiManager getApiManager() {
		return apiManager;
	}

	public void setApiManager(ApiManager apiManager) {
		this.apiManager = apiManager;
	}
}
