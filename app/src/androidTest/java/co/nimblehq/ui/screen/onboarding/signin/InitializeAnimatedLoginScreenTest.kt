package co.nimblehq.ui.screen.onboarding.signin

import co.nimblehq.R
import co.nimblehq.ui.screen.onboarding.OnboardingActivity
import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The kotlin equivalent to the AnimateLoginScreenTest, that
 * showcases simple view matchers and actions like [ViewMatchers.withId],
 * [ViewActions.click] and [ViewActions.typeText], and ActivityScenarioRule
 *
 *
 * Note that there is no need to tell Espresso that a view is in a different [Activity].
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class InitializeAnimatedLoginScreenTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<OnboardingActivity>()

    @Test
    fun initializeAnimatedLoginScreen_sameActivity() {
        // Check that the initial text is empty.
        onView(withId(R.id.etSignInEmail)).check(matches(withText("")))
        onView(withId(R.id.etSignInPassword)).check(matches(withText("")))
    }
}
