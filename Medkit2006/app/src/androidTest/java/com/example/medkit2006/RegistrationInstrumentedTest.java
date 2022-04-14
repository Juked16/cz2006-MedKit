package com.example.medkit2006;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.medkit2006.boundary.RegistrationUI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {

    @Rule
    public ActivityTestRule<RegistrationUI> mActivityRule =
            new ActivityTestRule<>(RegistrationUI.class);

    private final DB db = new DB(null, null);

    @Test
    public void shortUsername() {
        onView(withId(R.id.regUsername)).perform(typeText("t"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test3@gmail.com"));
        onView(withId(R.id.regPassword)).perform(typeText("testtest"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("testtest"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Username must be at least 3 characters")));
    }

    @Test
    public void invalidEmail() {
        onView(withId(R.id.regUsername)).perform(typeText("test3"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test3@gmail"));
        onView(withId(R.id.regPassword)).perform(typeText("testtest"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("testtest"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Invalid email")));
    }

    @Test
    public void duplicateUsername() {
        onView(withId(R.id.regUsername)).perform(typeText("test"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test3@gmail.com"));
        onView(withId(R.id.regPassword)).perform(typeText("testtest"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("testtest"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Please use another username")));
    }

    @Test
    public void duplicateEmail() {
        onView(withId(R.id.regUsername)).perform(typeText("test3"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.regPassword)).perform(typeText("testtest"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("testtest"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Please use another email")));
    }

    @Test
    public void differentPassword() {
        onView(withId(R.id.regUsername)).perform(typeText("test3"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test3@gmail.com"));
        onView(withId(R.id.regPassword)).perform(typeText("testtest"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("testtest2"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Passwords do not match")));
    }

    @Test
    public void shortPassword() {
        onView(withId(R.id.regUsername)).perform(typeText("test3"));
        onView(withId(R.id.regEmailAddress)).perform(typeText("test3@gmail.com"));
        onView(withId(R.id.regPassword)).perform(typeText("test"));
        onView(withId(R.id.regConfirmPassword)).perform(typeText("test"));
        onView(withId(R.id.btnRegister)).perform(closeSoftKeyboard()).perform(click());
        onView(withId(R.id.regError)).check(matches(withText("Password length must be at least 8 characters")));
    }
}