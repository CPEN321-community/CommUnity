package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.example.community.classes.GlobalUtil;
import com.example.community.classes.UserWithScore;
import com.example.community.ui.leaderboard.LeaderboardFragment;
import com.example.community.ui.leaderboard.LeaderboardViewModel;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class LeaderboardTest {

    @Before
    public void before() throws Exception {
        GlobalUtil.setIsTest(true);
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.S2S_TOKEN);
    }

    @Test
    public void GetLeaderboardStats() {
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        onView(withId(R.id.navigation_leaderboard)).perform(click());
        Bundle args = new Bundle();
        int theme = R.style.Theme_Community;
        FragmentScenario<LeaderboardFragment> navhostScenario = FragmentScenario.launchInContainer(LeaderboardFragment.class, args, theme, Lifecycle.State.STARTED);
        navhostScenario.onFragment(fragment -> {
            LeaderboardViewModel viewModel = fragment.getViewModel();
            ArrayList<UserWithScore> users = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                UserWithScore user = new UserWithScore("TestUser", i + "", "", 1000, 1000, 10000);
                users.add(user);
            }
            viewModel.setData(users);
        });
            DoLeaderboardTest();
            onView(withId(R.id.button_refresh_leaderboard)).perform(click());
            DoLeaderboardTest();
    }

    public void DoLeaderboardTest() {
//        onView(withId(R.id.navigation_leaderboard)).perform(click());
        Matcher<View> list = withId(R.id.leaderboard_list);
//        onView(list).check(matches(TestUtils.withListOfAtMost(10)));
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
        try {

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
            onView(withId(R.id.my_score)).check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException e) {
            // I am not in the top 10, so my score should be shown at the bottom
            onView(withId(R.id.my_score)).check(matches(isDisplayed()));
        }


    }
}
