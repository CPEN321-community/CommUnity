package com.example.community;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.community.TestUtils.SetTestUserData;
import static com.example.community.TestUtils.getActivityInstance;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.community.classes.ChatHelper;
import com.example.community.classes.ChatRoom;
import com.example.community.classes.ChatUser;
import com.example.community.offer_list.NewOfferForm;
import com.example.community.request_list.NewRequestForm;
import com.example.community.ui.chat.ChatActivity;
import com.example.community.ui.chat.message.MessageActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

public class NonFunctionalNavigationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private int navigations;

    @Before
    public void before() throws Exception {
        SetTestUserData();
        this.navigations = 0;
    }

    @After
    public void after() throws Exception {
        this.navigations = 0;
    }

    @Test
    public void TestChatTakes4Navigations() {
        onView(withId(R.id.chat_button)).perform(click());
        NavigateTo(ChatActivity.class);
        ChatUser me = new ChatUser("Community", "Tester", "");
        ChatUser other = new ChatUser("Community", "Tester", "");

        ChatRoom chat = new ChatRoom(me, other, "testpostid");
        HashMap<String, ChatRoom> chats = new HashMap<>();
        chats.put("testpostid", chat);
        ChatHelper.SetChats(chats);
        onData(anything())
                .atPosition(0)
                .perform(click());
        NavigateTo(MessageActivity.class);
        assert navigations <= 4;
    }

    @Test
    public void TestLeaderboardTakes4Navigations() {
        onView(withId(R.id.navigation_leaderboard)).perform(click());
        NavigateTo(R.id.leaderboard_fragment);
        assert navigations <= 4;
    }

    @Test
    public void TestAddOfferPostTakes4Navigations() {
        onView(withText("Offers")).perform(click());
        navigations++;
        onView(withId(R.id.addPostButton)).perform(click());
        NavigateTo(NewOfferForm.class);
        assert navigations <= 4;
    }

    @Test
    public void TestAddRequestPostTakes4Navigations() {
        onView(withText("Requests")).perform(click());
        navigations++;
        onView(withId(R.id.addPostButton)).perform(click());
        NavigateTo(NewRequestForm.class);
        assert navigations <= 4;
    }

    @Test
    public void TestRestrictionsTakes4Navigations() {
        onView(withId(R.id.navigation_profile)).perform(click());
        NavigateTo(R.id.profile_fragment);
        onView(withId(R.id.add_restriction_button)).perform(click());
        NavigateTo(EditRestrictionsActivity.class);
        assert navigations <= 4;
    }

    public void NavigateTo(Class<? extends Activity> c) {
        navigations++;
        assert getActivityInstance().getClass() == c;
    }

    public void NavigateTo(int id) {
        navigations++;
        onView(withId(id)).check(matches(not(doesNotExist())));
    }
}
