package com.example.community;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.GlobalUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class NonFunctionalNavigationTest {

    private static final int[] viewIdsThatNavigate = {
            R.id.navigation_home,
            R.id.navigation_leaderboard,
            R.id.navigation_profile,

    };
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() throws Exception {
        SetTestUserData();
    }

    @After
    public void after() throws Exception {
    }

    private void SetTestUserData() {
        GlobalUtil.setIsTest(true);
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.S2S_TOKEN);
    }

    @Test
    public void TestActionsTake4Navigations() {
        ArrayList<Context> adj_list = new ArrayList<>();

    }
}
