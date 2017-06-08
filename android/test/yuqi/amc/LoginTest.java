package yuqi.amc;

import android.app.Activity;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by yuqi on 28/5/17.
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    // Testing values
    private static String EMAIL = "ywan418@student.monash.edu";
    private static String PASSWORD = "00000000";

    //
    private static String INVALID_EMAIL = "afasdasdas00as";
    private static String NO_EMAIL = "";
    private static String NO_PASSWORD = "";

    @Rule
    public ActivityTestRule<Login> activityTestRule = new ActivityTestRule<Login>(Login.class);

//    @Test
//    public void loginWithProvidedInfo(){
//        onView(withId(R.id.inputLoginEmail)).perform(typeText(EMAIL));
//        onView(withId(R.id.inputLoginPassword)).perform(typeText(PASSWORD));
//        onView(withId(R.id.btnLogin)).perform(click()).check(matches(withError("Please enter your email address.")));
//    }

    @Test
    public void allEmpty(){
//        onView(withId(R.id.inputLoginEmail)).perform(typeText(NO_EMAIL));
//        onView(withId(R.id.inputLoginPassword)).perform(typeText(NO_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
        SystemClock.sleep(2000);
        onView(withText(R.string.msg_reg_no_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void emailIsEmpty(){
        onView(withId(R.id.inputLoginEmail)).perform(typeText(NO_EMAIL));
        onView(withId(R.id.inputLoginPassword)).perform(typeText(PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
        SystemClock.sleep(2000);
        onView(withText(R.string.msg_reg_no_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

//    @Test
//    public void emailIsInvalid(){
//        onView(withId(R.id.inputLoginEmail)).perform(typeText(INVALID_EMAIL));
//        onView(withId(R.id.inputLoginPassword)).perform(typeText(PASSWORD));
//        onView(withId(R.id.btnLogin)).perform(click()).check(matches(withError("Please enter your email address.")));
//    }

    @Test
    public void passwordIsEmpty(){
        onView(withId(R.id.inputLoginEmail)).perform(typeText(EMAIL));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click(), UiController.loopMainThreadForAtLeast(5000));
        onView(withText(R.string.msg_reg_no_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
