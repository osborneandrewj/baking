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
 * This test demos a user selecting a recipe card from the RecipeCardsFragment and verifying
 * that it properly opens the RecipeDetailActivity.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeCard_openDetailActivity() {

        // Select a recipe card from the recyclerView
        onView(withId(R.id.recipe_cards_recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

        // Check that RecipeDetailActivity is opened by finding one of its views
        onView(withId(R.id.main_container));
    }
}
