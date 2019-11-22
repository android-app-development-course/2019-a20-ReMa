package com.example.signup;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class UITest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void uITestFail() {
        Espresso.onView(withId(R.id.text_account)).perform(typeText("accountssss"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.text_pwd)).perform(typeText("122222"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.button_sign_up)).perform(click());

    }

    @Test
    public void uITestSuccess() {
        Espresso.onView(withId(R.id.text_account)).perform(typeText("account"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.text_pwd)).perform(typeText("123456"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.button_sign_up)).perform(click());

    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
