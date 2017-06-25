package example.com.paycoinqtest.data;


import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReposLiveData extends LiveData<List<RepoInfo>> {
	private RealmResults<RepoInfo> targetData;
	private Realm realm;

	public ReposLiveData(Context context) {
		Realm.init(context);
		loadData();
	}

	@Override
	protected void onActive() {
		if (realm == null) {
			realm = Realm.getDefaultInstance();
		}
		targetData.addChangeListener((results, changeSet) -> setValue(targetData));
	}

	@Override
	protected void onInactive() {
		targetData.removeAllChangeListeners();
		realm.close();
		realm = null;
	}

	private void loadData() {
		Realm realm = Realm.getDefaultInstance();
		targetData = realm.where(RepoInfo.class).findAll();
		setValue(targetData);
	}
}
