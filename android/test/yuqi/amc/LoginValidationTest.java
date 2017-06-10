package yuqi.amc;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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

@RunWith(AndroidJUnit4.class)
public class LoginValidationTest {

    private final long WAIT_TIME = 2500;

    // Testing values
    private static String EMAIL = "ywan418@student.monash.edu";
    private static String PASSWORD = "00000000";
    private static String INVALID_EMAIL = "afasdasdas00as";

    @Rule
    public ActivityTestRule<Login> activityTestRule = new ActivityTestRule<Login>(Login.class);

    @Test
    public void noEntry(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.msg_reg_no_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void invalidEmail(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.inputLoginEmail)).perform(typeText(INVALID_EMAIL));
        onView(withId(R.id.inputLoginPassword)).perform(typeText(PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.msg_reg_invalid_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noPassword(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.inputLoginEmail)).perform(typeText(EMAIL));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.msg_reg_no_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
