package com.example.community;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.GlobalUtil;
import com.example.community.classes.UserWithScore;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LeaderboardTest {
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
    public void GetLeaderboardStats() {
        onView(withId(R.id.navigation_leaderboard)).perform(click());
        Matcher<View> list = withId(R.id.leaderboard_list);
        onView(list).check(matches(TestUtils.withListOfAtMost(10)));
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
        int numItems = count[0];
        assert numItems <= 10;

        // Get my user card if its in the list
        final View[] myLeaderboardUser = {null};

        onView(allOf(
                hasDescendant(
                        allOf(withText("Community T."), withId(R.id.leaderboard_name))
                ),
                withId(R.id.leaderboard_user_card)))
                .perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(LinearLayout.class);
            }

            @Override
            public String getDescription() {
                return "Finding Test User in list";
            }

            @Override
            public void perform(UiController uiController, View view) {
                LinearLayout ll = (LinearLayout) view;
                myLeaderboardUser[0] = ll;
            }
        });

        if (myLeaderboardUser[0] != null) {
            // I am not in the top 10, so my score should be shown at the bottom
            onView(withId(R.id.my_score)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.my_score)).check(matches(not(isDisplayed())));
        }

    }
}
