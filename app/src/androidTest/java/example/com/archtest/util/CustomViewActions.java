package example.com.archtest.util;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.view.View;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CustomViewActions {
	private static int DEFAULT_TIMEOUT_SEC = 30;

	public static ViewAction waitView(final Matcher<View> matcher) {
		return waitView(matcher, TimeUnit.SECONDS.toMillis(DEFAULT_TIMEOUT_SEC));
	}

	public static ViewAction waitView(final Matcher<View> matcher, final long timeout) {
		return new ViewAction() {

			@Override
			public Matcher<View> getConstraints() {
				return CoreMatchers.any(View.class);
			}

			@Override
			public String getDescription() {
				return "wait for a specific view matching " + matcher.toString() + " during " + timeout + " timeout.";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				uiController.loopMainThreadUntilIdle();
				final long startTime = System.currentTimeMillis();
				final long endTime = startTime + timeout;

				do {
					for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
						// found view with required ID
						if (matcher.matches(child)) {
							return;
						}
					}

					uiController.loopMainThreadForAtLeast(250);
				}
				while (System.currentTimeMillis() < endTime);

				// timeout happens
				throw new PerformException.Builder()
						.withActionDescription(this.getDescription())
						.withViewDescription(HumanReadables.describe(view))
						.withCause(new TimeoutException())
						.build();
			}
		};
	}
}
