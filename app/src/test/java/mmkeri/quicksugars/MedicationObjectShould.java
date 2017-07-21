package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

/**
 * Created by mmkeri on 21/07/2017.
 */

public class MedicationObjectShould {

    private MedicationObject testMedicationObject;

    @Before
    public void setUp(){
        testMedicationObject = new MedicationObject();
    }

    @After
    public void cleanUp(){
        testMedicationObject = null;
    }

    @Test
    public void returnTheCorrectListOfDosesWhenGetDoseIsCalled(){
        testMedicationObject.addDose("150");
        testMedicationObject.addDose("300");
        ArrayList<String> result = testMedicationObject.getDose();
        assertEquals(2, result.size());
        assertEquals("150", result.get(0));
        assertEquals("300", result.get(1));
    }

    @Test
    public void returnTheCorrectListOfRoutesWhenGetRouteIsCalled(){
        testMedicationObject.setAdministrationRoute("PO");
        testMedicationObject.setAdministrationRoute("IM");
        ArrayList<String> result = testMedicationObject.getAdministrationRoute();
        assertEquals(2, result.size());
        assertEquals("PO", result.get(0));
        assertEquals("IM", result.get(1));
    }

    @Test
    public void returnTheCorrectListOfFrequenciesWhenGetFrequenciesIsCalled(){
        testMedicationObject.setFrequency("daily");
        testMedicationObject.setFrequency("twice daily");
        ArrayList<String> result = testMedicationObject.getFrequency();
        assertEquals(2, result.size());
        assertEquals("daily", result.get(0));
        assertEquals("twice daily", result.get(1));
    }

    @Test
    public void returnTheCorrectListOfSideEffectsWhenGetSideEffectsIsCalled(){
        testMedicationObject.setSideEffects("Rash");
        testMedicationObject.setSideEffects("Nausea");
        ArrayList<String> result = testMedicationObject.getSideEffects();
        assertEquals(2, result.size());
        assertEquals("Rash", result.get(0));
        assertEquals("Nausea", result.get(1));
    }

    @Test
    public void returnTheCorrectTradeNameWhenGetTradeNameIsCalled(){
        testMedicationObject.setTradeName("Lipitor");
        assertEquals("Lipitor", testMedicationObject.getTradeName());
    }

    @Test
    public void returnTheCorrectGenericNameWhenGetGenericNameIsCalled(){
        testMedicationObject.setGenericName("Atorvistatin");
        assertEquals("Atorvistatin", testMedicationObject.getGenericName());
    }
}
