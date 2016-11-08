package com.touchnote.renclav.touchnotesampleapp.clue;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.util.EspressoTestsMatchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CluesActivityScreenTest {

    @Rule
    public ActivityTestRule<CluesActivity> cluesActivityActivityTestRule = new ActivityTestRule<>(CluesActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                cluesActivityActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void cluesActivityTest() {

        onView(withId(R.id.listLayoutMenuItem)).check(matches(withContentDescription("Switch to Grid")));

        ViewInteraction appToolbar = onView(
                allOf(withId(R.id.toolbar),
                        childAtPosition(
                                allOf(withId(R.id.app_bar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                0),
                        isDisplayed()));
        appToolbar.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.clue_list),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(R.id.frameLayout),
                                                0)),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

    }

    @Test
    public void changeLayoutTest() {

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.listLayoutMenuItem), withContentDescription("Switch to Grid"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.listLayoutMenuItem), withContentDescription("Switch to List"), isDisplayed()));
        actionMenuItemView2.perform(longClick());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                cluesActivityActivityTestRule.getActivity().getCountingIdlingResource());
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
