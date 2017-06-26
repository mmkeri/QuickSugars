package mmkeri.quicksugars;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
/**
 * Created by mmkeri on 19/06/2017.
 */

public final class MedicationRecordShould {

    private DateTime currentTime;
    private DateTime scheduledTime;
    private MedicationRecord testMedRecord;

    @Before
    public void setUp(){
        currentTime = new DateTime();
        scheduledTime = new DateTime(2017, 9, 11, 14, 45);
        SimpleDateFormat testDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        testMedRecord = new MedicationRecord("Metformin", 875, scheduledTime, currentTime);
    }

    @Test
    public void returnTheCorrectDrugName(){
        String result = testMedRecord.getMedicationName();
        String expected = "Metformin";
        assertEquals(expected, result);
    }

    @Test
    public void returnTheCorrectDrugDose(){
        double result = testMedRecord.getMedicationDose();
        double expected = 875;
        assertEquals(expected, result, 0);
    }

    @Test
    public void returnTheCorrectTimeAndDate(){
        DateTime result = testMedRecord.getActualAdministrationTime();
        DateTime expected = currentTime;
        assertEquals(expected, result);
    }

    @Test
    public void identifyTheCorrectMedicationNameAndAdminTimeAndChangeToNewMedicationName(){
        DateTime innerTestDateTime = new DateTime();
        SimpleDateFormat innerDateAndTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String innerTestDateAndTime = innerDateAndTime.format(new Date());
        String initialMedicationName = testMedRecord.getMedicationName();
        String newMedicationName = testMedRecord.changeMedicationName(initialMedicationName, testMedRecord.getActualAdministrationTime(), "Glucophage", innerTestDateTime);
        assertEquals(initialMedicationName, "Metformin");
        assertEquals(newMedicationName, "Glucophage");
    }

    @Test
    public void identifyTheCorrectActualAdministrationTimeAndChangeToANewTime(){
        DateTime innerTestDateTime = new DateTime();
        SimpleDateFormat innerDateAndTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String innerTestDateAndTime = innerDateAndTime.format(new Date());
        DateTime newAdminTime = testMedRecord.changeAdministrationTime(testMedRecord.getMedicationName(), testMedRecord.getActualAdministrationTime(), innerTestDateTime);
        assertEquals(newAdminTime, innerTestDateTime);
    }

    @Test
    public void returnTheScheduledAdministrationTime(){
        assertEquals(scheduledTime, testMedRecord.getScheduledAdministrationTime());
    }

    @Test
    public void returnFalseIfMedicationIsNotRecordedAsTaken(){
        assertEquals(false, testMedRecord.wasMedTaken());
    }

    @Test
    public void returnTrueIfMedicationIsMarkedAsTaken(){
        testMedRecord.markMedicationAsTaken();
        assertEquals(true, testMedRecord.wasMedTaken());
    }
}
