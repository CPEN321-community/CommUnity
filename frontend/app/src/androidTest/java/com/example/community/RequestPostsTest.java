package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.GlobalUtil;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RequestPostsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() throws Exception {
        SetTestUserData();
        addReqPost();
    }

    @After
    public void after() throws Exception {
        addReqPost();
    }

    private void SetTestUserData() {
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.S2S_TOKEN);
    }

    @Test
    private void addReqPost() {
        activityRule.getScenario().recreate();
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.tabLayout)).perform(TestUtils.selectTabAtPosition(1));
        onView(withId(R.id.addPostButton)).perform(click());

        Matcher<View> itemNameField = withId(R.id.request_name_input);
        onView(itemNameField).perform(typeTextIntoFocusedView(""));

        Matcher<View> submitButton = withId(R.id.create_request_button);
        //Required input not filled
        onView(submitButton).check(matches(not(isEnabled())));

        onView(itemNameField).perform(typeTextIntoFocusedView("Oranges"));

        Matcher<View> quantityField = withId(R.id.quantity_input);
        onView(quantityField).perform(typeTextIntoFocusedView("0"));
        //Invalid quantity amount
        onView(submitButton).check(matches(not(isEnabled())));

        //User correctly fills out the form
        onView(quantityField).perform(typeTextIntoFocusedView("1"));
        Matcher<View> descriptionField = withId(R.id.description_input);
        onView(descriptionField).perform(typeTextIntoFocusedView("Would like some oranges for juice"));
        onView(submitButton).check(matches(isEnabled()));
    }

    //Currently have not implemented the edit post feature yet so certain lines are commented out
    @Test
    public void editReqPost() {
        activityRule.getScenario().recreate();
        onView(withId(R.id.navigation_home)).perform(click());
//        Matcher<View> list = withId(R.id.tab_view_pager);

        //TODO: create an edit and delete button associated with each offer you created
        //onView(withId(R.id.edit_offer_button)).perform(click());
        //onView(withId(R.id.delete_offer_button)).perform(click());

        //Matcher<View> submitButton = withId(R.id.update_request_button);

        Matcher<View> quantityField = withId(R.id.quantity_input);
        onView(quantityField).perform(typeTextIntoFocusedView("0"));
        //Invalid quantity amount
        //onView(submitButton).check(matches(not(isEnabled())));

        //User properly edits the form
        onView(quantityField).perform(typeTextIntoFocusedView("1"));
        //onView(submitButton).check(matches(isEnabled()));
    }
}
