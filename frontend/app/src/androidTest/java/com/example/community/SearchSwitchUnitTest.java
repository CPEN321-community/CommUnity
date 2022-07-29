package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchSwitchUnitTest {

    @Rule
    public ActivityScenarioRule<SearchActivity> activityRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void SwitchPressChangesText() {
        Matcher<View> switchView = withId(R.id.req_or_offer_switch);
        onView(switchView).check(matches(isDisplayed()));
        onView(switchView).check(matches(isNotChecked()));
        onView(withText("Request")).check(matches(isDisplayed()));
        onView(switchView).perform(click());
        onView(switchView).check(matches(isChecked()));
        onView(withText("Offers")).check(matches(isDisplayed()));
    }
}