package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.os.SystemClock;
import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.GlobalUtil;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ManagePreferencesUnitTest {

    @Before
    public void before() throws Exception {
        GlobalUtil.setIsTest(true);
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.S2S_TOKEN);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void AddRestrictions() {
        onView(withId(R.id.navigation_profile)).perform(click());
        Matcher<View> titleView = withText("My Profile");
        onView(titleView).check(matches(isDisplayed()));
        Matcher<View> submitButton = withId(R.id.add_dietary_restriction_button);
        onView(withId(R.id.add_restriction_button)).check(matches(isDisplayed()));
        onView(withId(R.id.add_restriction_button)).perform(click());
        onView(submitButton).check(matches(not(isEnabled())));
        onView(withId(R.id.restriction_input)).perform(typeText(" "));
        onView(submitButton).check(matches(not(isEnabled())));
        onView(withId(R.id.restriction_input)).check(matches(withText("Vegan")));
        onView(submitButton).check(matches(isEnabled()));
        onView(submitButton).perform(click());
        onView(submitButton).check(matches(not(isEnabled())));
        onView(submitButton).check(matches())

        SystemClock.sleep(10000);
    }
}
