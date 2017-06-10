package yuqi.amc;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

// This test case has the same logic for Login: <Retrieve Password>, Account: <Update Address> and <Update Login Information>

@RunWith(AndroidJUnit4.class)
public class RegisterValidationTest {

    private final long WAIT_TIME = 2500;

    private static String NAME = "Yuqi Wang";
    private static String ADDRESS = "900 Dandenong Rd";
    private static String SUBURB = "Caulfield East";
    private static String POSTCODE = "3145";
    private static String STATE = "Victoria";
    private static String EMAIL = "ywan418@student.monash.edu";
    private static String INVALID_EMAIL = "afasdasdas00as";
    private static String PASSWORD = "00000000";
    private static String UNMATCHED_PASSWORD = "11111111";
    private static String INVALID_PASSWORD_SHORT = "0000";
    private static String INVALID_PASSWORD_LONG = "0000000000000000000000000000";

    @Rule
    public ActivityTestRule<Register> activityTestRule = new ActivityTestRule<Register>(Register.class);

    @Test
    public void noEntry(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_name)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noAddress(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_address)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noSuburb(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_suburb)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noPostcode(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_postcode)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noState(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_state)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noCountry(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_country)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noEmail(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void invalidEmail(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.textRegEmail)).perform(scrollTo(), typeText(INVALID_EMAIL));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_invalid_email)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void noPassword(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.textRegEmail)).perform(scrollTo(), typeText(EMAIL));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_no_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void shortPassword(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.textRegEmail)).perform(scrollTo(), typeText(EMAIL));
        onView(withId(R.id.textRegPassword)).perform(scrollTo(), typeText(INVALID_PASSWORD_SHORT));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_invalid_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void longPassword(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.textRegEmail)).perform(scrollTo(), typeText(EMAIL));
        onView(withId(R.id.textRegPassword)).perform(scrollTo(), typeText(INVALID_PASSWORD_LONG));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_invalid_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void unmatchedPassword(){
        SystemClock.sleep(WAIT_TIME);
        onView(withId(R.id.textRegName)).perform(typeText(NAME));
        onView(withId(R.id.textRegAddress)).perform(typeText(ADDRESS));
        onView(withId(R.id.textRegSuburb)).perform(typeText(SUBURB));
        onView(withId(R.id.textRegPostcode)).perform(typeText(POSTCODE));
        onView(withId(R.id.textRegState)).perform(scrollTo(), typeText(STATE));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spinnerRegCountry)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.textRegEmail)).perform(scrollTo(), typeText(EMAIL));
        onView(withId(R.id.textRegPassword)).perform(scrollTo(), typeText(PASSWORD));
        onView(withId(R.id.textRegPasswordConfirm)).perform(scrollTo(), typeText(UNMATCHED_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegRegister)).perform(scrollTo(), click());
        onView(withText(R.string.msg_reg_unmatched_password)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
