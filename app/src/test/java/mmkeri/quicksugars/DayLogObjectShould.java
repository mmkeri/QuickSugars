package mmkeri.quicksugars;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by mmkeri on 19/06/2017.
 */

public class DayLogObjectShould {

    private LocalDate testDate;
    private DayLogObject testObject;
    private BloodSugarMeasurement testRecord1;
    private BloodSugarMeasurement testRecord2;
    private SymptomRecord dizzinessRecord;
    private SymptomRecord headacheRecord;
    private MedicationRecord metformin;
    private MedicationRecord glucophage;
    private FoodItemRecord pizza;
    private FoodItemRecord spaghetti;
    private WeightMeasurement light;
    private WeightMeasurement heavy;
    private LocalTime testTime;
    private LocalTime testTime2;
    private int testDateAsInt1;
    private int testDateAsInt2;

    @Before
    public void setUp(){
        testDate = new LocalDate();
        testTime = new LocalTime(1498406153);
        testTime2 = new LocalTime(12, 45, 30);
        testDateAsInt1 = 20170515;
        testDateAsInt2 = 20170615;
        testObject = new DayLogObject(testDate);
        testRecord1 = new BloodSugarMeasurement(14.5, testTime, testDateAsInt1);
        testRecord2 = new BloodSugarMeasurement(12.2, testTime2, testDateAsInt2);
        dizzinessRecord = new SymptomRecord("Dizziness", testTime);
        headacheRecord = new SymptomRecord("Headache", testTime);
        metformin = new MedicationRecord("Metformin", 875, testTime, testTime);
        glucophage = new MedicationRecord("Glucophage", 875, testTime, testTime);
        pizza = new FoodItemRecord("Pizza", testTime);
        spaghetti = new FoodItemRecord("Spaghetti", testTime);
        light = new WeightMeasurement(120, testTime);
        heavy = new WeightMeasurement(175, testTime);
    }

    @After
    public void cleanUp(){
        testObject = null;
        testDate = null;
        dizzinessRecord = null;
        headacheRecord = null;
    }

    @Test
    public void returnTheCorrectDateForTheRecord(){
        assertEquals(testDate, testObject.getCurrentDate());
    }

    @Test
    public void addANewBloodSugarMeasurementToTheLogCorrectlyWithOneRecordAdded(){
        List<BloodSugarMeasurement> result = testObject.addNewBSMeasurement(new BloodSugarMeasurement(14.5, testTime, testDateAsInt1));
        assertEquals(1, result.size());
        assertEquals(14.5, result.get(0).getBloodSugarReading(), 0.0);
        assertEquals(testTime, result.get(0).getTimeOfBloodSugarCheck());
    }

    @Test
    public void returnTheCorrectResultsWhenTwoRecordsAreAddedForBloodSugarMeasurements(){
        testObject.addNewBSMeasurement(new BloodSugarMeasurement(14.5, testTime, testDateAsInt1));
        List<BloodSugarMeasurement> results = testObject.addNewBSMeasurement(new BloodSugarMeasurement(12.2, testTime2, testDateAsInt2));
        assertEquals(2, results.size());
        assertEquals(14.5, results.get(0).getBloodSugarReading(), 0.0);
        assertEquals(testTime, results.get(0).getTimeOfBloodSugarCheck());
        assertEquals(12.2, results.get(1).getBloodSugarReading(), 0.0);
        assertEquals(testTime2, results.get(1).getTimeOfBloodSugarCheck());
    }

    @Test
    public void returnTheCorrectCountWhenARecordIsDeleted(){
        testObject.addNewBSMeasurement(testRecord1);
        int preDeletionSize = testObject.addNewBSMeasurement(testRecord2).size();
        int postDeletionSize = testObject.deleteBloodSugarMeasurement(14.5, testTime).size();
        assertEquals(2, preDeletionSize);
        assertEquals(1, postDeletionSize);
    }

    @Test
    public void returnZeroIfAllRecordsAreDelted(){
        testObject.addNewBSMeasurement(testRecord1);
        testObject.addNewBSMeasurement(testRecord2);
        testObject.deleteBloodSugarMeasurement(14.5, testTime);
        int result = testObject.deleteBloodSugarMeasurement(12.2, testTime2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenOneRecordAddedForSymptoms(){
        List<SymptomRecord> returnedList = testObject.addNewSymptom(new SymptomRecord("Headache", testTime));
        int result = returnedList.size();
        assertEquals(1, result);
        assertEquals("Headache", returnedList.get(0).getSymptomValue());
        assertEquals(testTime, returnedList.get(0).getSymptomInputTime());
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenTwoRecordsAreAddedForSymptoms(){
        testObject.addNewSymptom(dizzinessRecord);
        List<SymptomRecord> returnedList = testObject.addNewSymptom(headacheRecord);
        int result = returnedList.size();
        assertEquals(2, result);
        assertEquals("Dizziness", returnedList.get(0).getSymptomValue());
        assertEquals(testTime, returnedList.get(0).getSymptomInputTime());
        assertEquals("Headache", returnedList.get(1).getSymptomValue());
        assertEquals(testTime, returnedList.get(1).getSymptomInputTime());
    }

    @Test
    public void returnTheCorrectCountWhenOneRecordIsDeletedFromAListOfTwo(){
        testObject.addNewSymptom(dizzinessRecord);
        testObject.addNewSymptom(headacheRecord);
        List<SymptomRecord> returnedList = testObject.deleteSymptom("Headache", testTime);
        assertEquals(1, returnedList.size());
    }

    @Test
    public void returnZeroWhenAllRecordsAreDeletedFromTheList(){
        testObject.addNewSymptom(dizzinessRecord);
        testObject.addNewSymptom(headacheRecord);
        testObject.deleteSymptom("Headache", testTime);
        List<SymptomRecord> returnedList = testObject.deleteSymptom("Dizziness", testTime);
        assertEquals(0, returnedList.size());
    }

    @Test
    public void returnTheCorrectCountWhenOneNewMedicationRecordIsAdded(){
        assertEquals(1, testObject.addNewMedicationRecord(metformin).size());
    }

    @Test
    public void returnTheCorrectCountWhenTwoMedicationRecordsAreAdded(){
        testObject.addNewMedicationRecord(metformin);
        int result = testObject.addNewMedicationRecord(glucophage).size();
        assertEquals(2, result);
    }

    @Test
    public void returnTheCorrectNumberWhenOneRecordDeletedFromTheRecord(){
        testObject.addNewMedicationRecord(metformin);
        int initialNumber = testObject.addNewMedicationRecord(glucophage).size();
        int sizeAfterDeletion = testObject.deleteMedicationRecord("Metformin", testTime).size();
        assertEquals(2, initialNumber);
        assertEquals(1, sizeAfterDeletion);
    }

    @Test
    public void returnZeroWhenAllREcordsAreDeleted(){
        testObject.addNewMedicationRecord(metformin);
        int sizeBeforeDeletion = testObject.addNewMedicationRecord(glucophage).size();
        testObject.deleteMedicationRecord("Metformin", testTime);
        int sizeAfterDeletion = testObject.deleteMedicationRecord("Glucophage", testTime).size();
        assertEquals(2, sizeBeforeDeletion);
        assertEquals(0, sizeAfterDeletion);
    }

    @Test
    public void returnTheCorrectCountWhenOneFoodItemIsAdded(){
        assertEquals(1, testObject.addNewFoodRecord(pizza).size());
    }

    @Test
    public void returnTheCorrectCountWhenTwoItemsAreAdded(){
        testObject.addNewFoodRecord(pizza);
        int result = testObject.addNewFoodRecord(spaghetti).size();
        assertEquals(2, result);
    }

    @Test
    public void returnTheCorrectCountWhenOneFoodItemIsDeleted(){
        testObject.addNewFoodRecord(pizza);
        int numberBeforeDeletion = testObject.addNewFoodRecord(spaghetti).size();
        int result = testObject.deleteFoodRecord("Pizza", testTime).size();
        assertEquals(2, numberBeforeDeletion);
        assertEquals(1, result);
    }

    @Test
    public void returnZeroWhenAllItemsAreRemovedFromTheRecord(){
        testObject.addNewFoodRecord(pizza);
        int numberBeforeDeletion = testObject.addNewFoodRecord(spaghetti).size();
        testObject.deleteFoodRecord("Pizza", testTime);
        int numberAfterDeletion = testObject.deleteFoodRecord("Spaghetti", testTime).size();
        assertEquals(2, numberBeforeDeletion);
        assertEquals(0, numberAfterDeletion);
    }

    @Test
    public void returnTheCorrectCountWhenOneItemIsAddedToTheRecord(){
        assertEquals(1, testObject.addWeightRecord(light).size());
    }

    @Test
    public void returnTheCorrectCountWhenTwoItemsAreAddedToTheRecord(){
        testObject.addWeightRecord(light);
        int result = testObject.addWeightRecord(heavy).size();
        assertEquals(2, result);
    }

    @Test
    public void returnTheCorrectCountWhenOneItemIsDeletedFromTheRecord(){
        testObject.addWeightRecord(light);
        int numberBeforeDeletion = testObject.addWeightRecord(heavy).size();
        int numberAfterDeletion = testObject.deleteWeightRecord(120, testTime).size();
        assertEquals(1, numberAfterDeletion);
    }

    @Test
    public void returnZeroWhenAllItemsAreDeletedFromTheRecord(){
        testObject.addWeightRecord(light);
        int numberBeforeDeletion = testObject.addWeightRecord(heavy).size();
        testObject.deleteWeightRecord(120, testTime);
        int numberAfterDeletion = testObject.deleteWeightRecord(175, testTime).size();
        assertEquals(0, numberAfterDeletion);
    }
}
