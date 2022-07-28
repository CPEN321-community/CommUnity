package com.example.community;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;

import com.example.community.classes.GlobalUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ManagePreferencesUnitTest {

    private UiDevice mUiDevice;

    @Before
    public void before() throws Exception {
        GlobalUtil.setIsTest(true);
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.s2sToken);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void SwitchPressChangesText() {

//        onView(withText("Choose an account"))
//                .inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView()))))
//                .check(matches(isDisplayed()));
//        onView(withText("Profile")).perform(click());

    }
}
