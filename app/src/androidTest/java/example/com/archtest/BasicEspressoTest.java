package example.com.archtest;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import example.com.archtest.activity.MainActivity;
import example.com.archtest.api.ApiManager;
import example.com.archtest.util.RecyclerViewItemCountAssertion;
import io.realm.Realm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class BasicEspressoTest {

	@Rule
	public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		Realm realm = null;
		try {
			realm = Realm.getDefaultInstance();
			realm.executeTransaction(realm1 -> realm1.deleteAll());
		} finally {
			if (realm != null) {
				realm.close();
			}
		}
		PreferencesManager.saveLoadedPage(InstrumentationRegistry.getTargetContext(), 0);
	}

	@Test
	public void checkEmptyItemsCount() throws Exception {
		onView(withId(R.id.repos_recycler)).check(new RecyclerViewItemCountAssertion(0));
	}

	@Test
	public void checkFirstPageLoaded() throws Exception {
		RecyclerView recyclerView = (RecyclerView) activityRule.getActivity().findViewById(R.id.repos_recycler);
		Espresso.registerIdlingResources(new RecyclerItemsCountIdlingResource(recyclerView));
		onView(withId(R.id.repos_recycler)).check(new RecyclerViewItemCountAssertion(ApiManager.ITEMS_PER_PAGE));
	}


	/**
	 * Will wait until given recycler view has example.com.paycoinqtest.api.ApiManager#ITEMS_PER_PAGE items
	 */
	private class RecyclerItemsCountIdlingResource implements IdlingResource {
		private RecyclerView.Adapter adapter;
		private ResourceCallback resourceCallback;

		RecyclerItemsCountIdlingResource(RecyclerView recyclerView) {
			this.adapter = recyclerView.getAdapter();
		}

		@Override
		public String getName() {
			return "RecyclerItemsCountIdlingResource";
		}

		@Override
		public boolean isIdleNow() {
			boolean idle = adapter.getItemCount() == ApiManager.ITEMS_PER_PAGE;
			if (idle) {
				resourceCallback.onTransitionToIdle();
			}
			return idle;
		}

		@Override
		public void registerIdleTransitionCallback(ResourceCallback callback) {
			this.resourceCallback = callback;
		}
	}
}
