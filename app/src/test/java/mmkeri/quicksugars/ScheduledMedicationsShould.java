package mmkeri.quicksugars;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 21/07/2017.
 */

public class ScheduledMedicationsShould {

    private ScheduledMedications testScheduledMedications;
    private SelectedMedicationObject testSelectedMedicationObject;
    private MedicationObject testMedicationObject;
    private String time = "121212";
    private int idNumber = 20170706;

    @Before
    public void setUp(){
        testMedicationObject = new MedicationObject();
        testSelectedMedicationObject = new SelectedMedicationObject(testMedicationObject);
        testScheduledMedications = new ScheduledMedications(idNumber, time, testSelectedMedicationObject);
    }

    @Test
    public void returnTheCorrectIDNumberwhenGetIDNumberIsCalled(){
        int result = testScheduledMedications.getIdNumber();
        assertEquals(idNumber, result);
    }

    @Test
    public void returnTheCorrectTimeWhenGetTimeIsCalled(){
        String result = testScheduledMedications.getTime();
        assertEquals(time, result);
    }

    @Test
    public void returnTheCorrectSelectedMedicationObjectWhenGetMedicationObjectIsCalled(){
        SelectedMedicationObject result = testScheduledMedications.getMedicationObject();
        assertEquals(testSelectedMedicationObject, result);
    }
}
