package mmkeri.quicksugars;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by mmkeri on 30/06/2017.
 */

public class MedicationSelectionPageShould {

    ArrayList<MedicationObject> returnedList;

    @Rule
    public ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(MedicationSelectionPage.class);

    @Before
    public void setUp() throws Exception {

        Context context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void returnTheCorrectFirstRecordInTheXMLFile(){
        returnedList = MedicationSelectionPage.getMedicationObjectsList();
        String tradeName = "Hypurion Bovine Neutral";
        String genericNAme = "Insulin";
        assertEquals(tradeName, returnedList.get(0).getTradeName());
        assertEquals(genericNAme, returnedList.get(0).getGenericName());
    }
}
