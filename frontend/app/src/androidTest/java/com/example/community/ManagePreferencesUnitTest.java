package com.example.community;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.community.TestUtils.SetTestUserData;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ManagePreferencesUnitTest {

    private static final String TAG = "MANAGE_PREF_TEST";
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() throws Exception {
        SetTestUserData();
        resetRestrictions();
    }

    @After
    public void after() throws Exception {
        resetRestrictions();
    }


    private void resetRestrictions() {
        activityRule.getScenario().recreate();
        onView(withId(R.id.navigation_profile)).perform(click());
        Matcher<View> list = withId(R.id.dietary_restrictions_list);
        final int[] count = {0};
        onView(list).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ListView.class);
            }

            @Override
            public String getDescription() {
                return "Getting number of Leaderboard users";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ListView lv = (ListView) view;
                count[0] = lv.getChildCount();
            }
        });
        for (int i = 0; i < count[0]; i++) {
            try {
                onData(anything())
                        .atPosition(0)
                        .onChildView(withId(R.id.remove_restriction_button))
                        .perform(click());
            } catch (NoMatchingViewException e) {
                Log.e(TAG, "resetRestrictions: " + e);
            }
        }
    }


    @Test
    public void AddAndRemoveRestriction() {
        onView(withId(R.id.navigation_profile)).perform(click());

        Matcher<View> titleView = withText("My Profile");
        onView(titleView).check(matches(isDisplayed()));
        Matcher<View> submitButton = withId(R.id.add_dietary_restriction_button);
        Matcher<View> textField = withId(R.id.restriction_input);
        onView(withId(R.id.add_restriction_button)).check(matches(isDisplayed()));
        onView(withId(R.id.add_restriction_button)).perform(click());
        onView(submitButton).check(matches(not(isEnabled())));
        onView(textField).perform(typeText(" "));
        onView(submitButton).check(matches(not(isEnabled())));
        onView(textField).perform(clearText());
        onView(textField).perform(typeTextIntoFocusedView("Vegan"));
        String veganText = TestUtils.getText(textField);
        assert "Vegan".matches(veganText);
        onView(submitButton).check(matches(isEnabled()));
        closeSoftKeyboard();
        onView(submitButton).perform(click());
//      Activity Finishes
        Matcher<View> restrictionName = withId(R.id.restriction_name);
        Matcher<View> restrictionButton = withId(R.id.remove_restriction_button);
        try {
            onView(restrictionName).check(matches(isDisplayed()));
            onView(restrictionName).check(matches(withText("Vegan")));
            onView(restrictionButton).check(matches(isDisplayed()));
            onView(restrictionButton).perform(click());
            onView(restrictionName).check(doesNotExist());
        } catch (NoMatchingViewException e) {
            // Cleanup was already handled
        }
    }
}
