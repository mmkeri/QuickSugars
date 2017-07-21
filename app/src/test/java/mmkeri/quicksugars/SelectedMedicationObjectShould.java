package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class SelectedMedicationObjectShould {

    private SelectedMedicationObject testSelectedMedicationObject;
    private SelectedMedicationObject testSelectedMedicationObject2;
    private SelectedMedicationObject testSelectedMedicationObject3;
    private String testDose = "125";
    private ArrayList<String> testTimes;
    private String testRoute = "orally";
    private String testFrequency = "daily";
    private MedicationObject testMedicationObject;
    private ArrayList<String> secondList;


    @Before
    public void setUp(){
        testMedicationObject = new MedicationObject();
        testSelectedMedicationObject = new SelectedMedicationObject(testMedicationObject);
        testSelectedMedicationObject2 = new SelectedMedicationObject(testMedicationObject);
        testSelectedMedicationObject3 = new SelectedMedicationObject(testMedicationObject);
        testTimes = new ArrayList<>();
        testTimes.add("121212");
        secondList = new ArrayList<>();
        secondList.add("141414");
        testSelectedMedicationObject.setDose(testDose);
        testSelectedMedicationObject.setFrequency(testFrequency);
        testSelectedMedicationObject.setTimes(testTimes);
        testSelectedMedicationObject.setRoute(testRoute);
        testSelectedMedicationObject2.setDose(testDose);
        testSelectedMedicationObject2.setFrequency(testFrequency);
        testSelectedMedicationObject2.setTimes(secondList);
        testSelectedMedicationObject2.setRoute(testRoute);
    }

    @After
    public void cleanUp(){
        testSelectedMedicationObject = null;
    }

    @Test
    public void returnTheCorrectDoseWhenGetDoseIsCalled(){
        String result = testSelectedMedicationObject.getDose();
        assertEquals("125", result);
    }

    @Test
    public void returnAnEptyStringWhenNoDoseHasBeenSet(){
        String result = testSelectedMedicationObject3.getDose();
        assertEquals("", result);
    }

    @Test
    public void changeTheDoseCorrectlyWhensetDoseIsCalled(){
        testSelectedMedicationObject.setDose("150");
        String result = testSelectedMedicationObject.getDose();
        assertEquals("150", result);
    }

    @Test
    public void returnTheCorrectFrequencyWhenGetFrequencyIsCalled(){
        String result = testSelectedMedicationObject.getFrequency();
        assertEquals("daily", result);
    }

    @Test
    public void returnAnEmptyStringWhenNoFrequencyHasBeenSet(){
        String result = testSelectedMedicationObject3.getFrequency();
        assertEquals("", result);
    }

    @Test
    public void changeTheFrequencyCorrectlyWhenSetFrequencyIsCalled(){
        testSelectedMedicationObject.setFrequency("twice daily");
        String result = testSelectedMedicationObject.getFrequency();
        assertEquals("twice daily", result);
    }

    @Test
    public void returnTheCorrectRouteWhenGetRouteIsCalled(){
        String result = testSelectedMedicationObject.getRoute();
        assertEquals("orally", result);
    }

    @Test
    public void returnAnEmptyStringWhenRouteHasNotBeenSet(){
        String result = testSelectedMedicationObject3.getRoute();
        assertEquals("", result);
    }

    @Test
    public void changeTheRouteCorrectlyWhenSetRouteIsCalled(){
        testSelectedMedicationObject.setRoute("IM");
        String result = testSelectedMedicationObject.getRoute();
        assertEquals("IM", result);
    }

    @Test
    public void returnTheCorrectListOfTimesWhenGetTimesIsCalled(){
        List<String> result = testSelectedMedicationObject.getTimes();
        assertEquals(testTimes, result);
    }

    @Test
    public void returnAnEmptyListIfNoTimesHaveBeenSet(){
        int result = testSelectedMedicationObject3.getTimes().size();
        assertEquals(0, result);
    }

    @Test
    public void changeTheTimesValuesCorrectlyWhenSetTimesIsCalled(){
        testSelectedMedicationObject.setTimes(secondList);
        List<String> result = testSelectedMedicationObject.getTimes();
        assertEquals(secondList, result);
    }

    @Test
    public void returnTheCorrectMedicationObjectWhenGetMedInfoIsCalled(){
        MedicationObject result = testSelectedMedicationObject.getMedInfo();
        assertEquals(testMedicationObject, result);
    }
}
