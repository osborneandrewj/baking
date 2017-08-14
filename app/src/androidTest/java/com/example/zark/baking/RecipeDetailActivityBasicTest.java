package com.example.zark.baking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Andrew Osborne on 8/14/2017.
 *
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityBasicTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void clickRecipeStep_openStepDetail() {

    }
}
