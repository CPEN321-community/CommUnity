package com.example.community;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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
import org.junit.BeforeClass;
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
        TestUtils.waitFor(1000);
        Matcher<View> restrictionName = withId(R.id.restriction_name);
        onView(restrictionName).check(matches(isDisplayed()));
        onView(restrictionName).check(matches(withText("Vegan")));
        onView(withId(R.id.remove_restriction_button)).check(matches(isDisplayed()));
        onView(withId(R.id.remove_restriction_button)).perform(click());
        onView(restrictionName).check(doesNotExist());


    }
}
