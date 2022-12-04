package com.example.campusdiscovery.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.campusdiscovery.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LaunchActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<LaunchActivity> activityRule =
            new ActivityScenarioRule<>(LaunchActivity.class);

    @Test
    public void view_isCorrect() {
        onView(withText("Discover stuff around your campus!")).check(matches(isDisplayed()));
    }

    @Test
    public void welcomeClick_LoginPage() {
        onView(withId(R.id.button)).perform(click());

        // validate login activity launched
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    @Test
    public void loginClick_EventPage() {
        welcomeClick_LoginPage();
        // Type text and then press the button.
        onView(withId(R.id.userName)).perform(typeText("User"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        // validate login activity launched
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void mapViewClick_ToggleView() {
        loginClick_EventPage();

        onView(withId(R.id.mapViewToggle)).perform(click());

        onView(withId(R.id.map)).check(matches(isDisplayed()));

        onView(withId(R.id.mapViewToggle)).perform(click());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.campusdiscovery", appContext.getPackageName());
    }
}

