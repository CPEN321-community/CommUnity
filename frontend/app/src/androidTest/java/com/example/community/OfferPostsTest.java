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
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.GlobalUtil;
import com.example.community.classes.OfferPostObj;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OfferPostsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() throws Exception {
        SetTestUserData();
        addOfferPost();
    }

    @After
    public void after() throws Exception {
        addOfferPost();
    }

    private void SetTestUserData() {
        GlobalUtil.setIsTest(true);
        GlobalUtil.setId("testuserid");
        GlobalUtil.setGivenName("Community Tester");
        GlobalUtil.setHeaderToken(BuildConfig.S2S_TOKEN);
    }

    @Test
    private void addOfferPost() {
        activityRule.getScenario().recreate();
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.addOfferPostButton)).perform(click());

        Matcher<View> itemNameField = withId(R.id.offer_name_input);
        onView(itemNameField).perform(typeTextIntoFocusedView(""));

        Matcher<View> submitButton = withId(R.id.create_offer_button);
        //Required input not filled
        onView(submitButton).check(matches(not(isEnabled())));

        onView(itemNameField).perform(typeTextIntoFocusedView("Strawberries"));

        Matcher<View> quantityField = withId(R.id.quantity_input);
        onView(quantityField).perform(typeTextIntoFocusedView("0"));
        //Invalid quantity amount
        onView(submitButton).check(matches(not(isEnabled())));

        //User correctly fills out the form
        onView(quantityField).perform(typeTextIntoFocusedView("1"));
        Matcher<View> bestBeforeField = withId(R.id.best_before_input);
        onView(bestBeforeField).perform(typeTextIntoFocusedView("2022-08-02'T'11:11:11.SSSXXX"));

        Matcher<View> pickupLocField = withId(R.id.pickup_location_input);
        onView(pickupLocField).perform(typeTextIntoFocusedView("1111 Main St, Vancouver, V4T2P1"));

        Matcher<View> descriptionField = withId(R.id.description_input);
        onView(descriptionField).perform(typeTextIntoFocusedView("Fresh yummy strawberries"));
        onView(submitButton).check(matches(isEnabled()));

    }

    //Currently have not implemented the edit post feature yet so certain lines are commented out
    @Test
    public void editOfferPost(){
        activityRule.getScenario().recreate();
        onView(withId(R.id.navigation_home)).perform(click());
        Matcher<View> list = withId(R.id.offer_post_list);

        //TODO: create an edit and delete button associated with each offer you created
        //onView(withId(R.id.edit_offer_button)).perform(click());
        //onView(withId(R.id.delete_offer_button)).perform(click());

        //Matcher<View> submitButton = withId(R.id.update_offer_button);

        Matcher<View> quantityField = withId(R.id.quantity_input);
        onView(quantityField).perform(typeTextIntoFocusedView("0"));
        //Invalid quantity amount
        //onView(submitButton).check(matches(not(isEnabled())));

        //User properly edits the form
        onView(quantityField).perform(typeTextIntoFocusedView("1"));
        //onView(submitButton).check(matches(isEnabled()));
    }
}
