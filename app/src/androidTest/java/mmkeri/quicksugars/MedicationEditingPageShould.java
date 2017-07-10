package mmkeri.quicksugars;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by mmkeri on 10/07/2017.
 */

public class MedicationEditingPageShould {

    @Rule
    public ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(MedicationEditingPage.class);

    @Before
    public void setUp() throws Exception {

        Context context = InstrumentationRegistry.getTargetContext();
    }
    @Test
    public void returnTheSelectedTimeFromTheTimespinner(){
        onView(withId(R.id.timeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("0100"))).perform(click());
        onView(withId(R.id.timeSpinner))
                .check(matches(withText(containsString("0100"))));
    }
}
