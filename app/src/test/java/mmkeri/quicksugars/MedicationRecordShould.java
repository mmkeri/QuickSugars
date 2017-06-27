package mmkeri.quicksugars;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
/**
 * Created by mmkeri on 19/06/2017.
 */

public final class MedicationRecordShould {

    private LocalTime currentTime;
    private LocalTime scheduledTime;
    private MedicationRecord testMedRecord;

    @Before
    public void setUp(){
        currentTime = new LocalTime();
        scheduledTime = new LocalTime(11, 14, 45);
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
        LocalTime result = testMedRecord.getActualAdministrationTime();
        LocalTime expected = currentTime;
        assertEquals(expected, result);
    }

    @Test
    public void identifyTheCorrectMedicationNameAndAdminTimeAndChangeToNewMedicationName(){
        LocalTime innerTestTime = new LocalTime();
        SimpleDateFormat innerDateAndTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String innerTestDateAndTime = innerDateAndTime.format(new Date());
        String initialMedicationName = testMedRecord.getMedicationName();
        String newMedicationName = testMedRecord.changeMedicationName(initialMedicationName, testMedRecord.getActualAdministrationTime(), "Glucophage", innerTestTime);
        assertEquals(initialMedicationName, "Metformin");
        assertEquals(newMedicationName, "Glucophage");
    }

    @Test
    public void identifyTheCorrectActualAdministrationTimeAndChangeToANewTime(){
        LocalTime innerTestTime = new LocalTime();
        LocalTime newAdminTime = testMedRecord.changeAdministrationTime(testMedRecord.getMedicationName(), testMedRecord.getActualAdministrationTime(), innerTestTime);
        assertEquals(newAdminTime, innerTestTime);
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
