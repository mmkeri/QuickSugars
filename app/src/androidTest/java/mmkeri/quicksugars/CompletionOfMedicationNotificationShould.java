package mmkeri.quicksugars;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by mmkeri on 22/07/2017.
 */

public class CompletionOfMedicationNotificationShould {

    @Rule
    public final ActivityTestRule<CompletionOfMedicationNotification> mActivityRule =
            new ActivityTestRule(CompletionOfMedicationNotification.class);

    private CompletionOfMedicationNotification mCompletionOfMedicationNotification;
    @Before
    public void setUp(){

        mCompletionOfMedicationNotification = mActivityRule.getActivity();
    }

    @After
    public void cleanUp(){

        mCompletionOfMedicationNotification = null;
    }

    @Test
    public void returnTrueWhenPassedADoubleValueAsAString(){
        assertTrue(CompletionOfMedicationNotification.isNumeric("125.45"));
    }

    @Test
    public void returnFalseWhenPassedAStringOfLetters(){
        assertFalse(CompletionOfMedicationNotification.isNumeric("hello"));
    }
}
