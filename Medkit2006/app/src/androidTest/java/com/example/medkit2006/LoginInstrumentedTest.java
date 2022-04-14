package com.example.medkit2006;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.rule.ActivityTestRule;

import com.example.medkit2006.boundary.LoginUI;

import org.junit.Rule;
import org.junit.Test;

public class LoginInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginUI> mActivityRule =
            new ActivityTestRule<>(LoginUI.class);

    private final DB db = new DB(null, null);

    @Test
    public void blankUsername() {
        onView(withId(R.id.loginLoginBtn)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.loginStatus)).check(matches(withText("Please input username")));
    }

    @Test
    public void blankPassword() {
        onView(withId(R.id.loginUsername)).perform(typeText("test"));
        onView(withId(R.id.loginLoginBtn)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.loginStatus)).check(matches(withText("Please input password")));
    }

    @Test
    public void wrongPassword(){
        onView(withId(R.id.loginUsername)).perform(typeText("test"));
        onView(withId(R.id.loginPassword)).perform(typeText("test"));
        onView(withId(R.id.loginLoginBtn)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.loginStatus)).check(matches(withText("Invalid username or password")));
    }
}
