package mmkeri.quicksugars;

import org.joda.time.DateTime;
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

    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf2;
    private String testDate;
    private String testDateAndTime;
    private DayLogObject testObject;
    private BloodSugarMeasurement testRecord1;
    private BloodSugarMeasurement getTestRecord2;
    private SymptomRecord dizzinessRecord;
    private SymptomRecord headacheRecord;
    private MedicationRecord metformin;
    private MedicationRecord glucophage;
    private FoodItemRecord pizza;
    private FoodItemRecord spaghetti;
    private WeightMeasurement light;
    private WeightMeasurement heavy;
    private DateTime testTime;
    private DateTime testTime2;

    @Before
    public void setUp(){
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        testDate = sdf.format(new Date());
        sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        testDateAndTime = sdf2.format(new Date());
        testTime = new DateTime(1498406153);
        testTime2 = new DateTime(2015, 11, 25, 12, 45, 30);
        testObject = new DayLogObject(testDate);
        testRecord1 = new BloodSugarMeasurement(14.5, testTime);
        getTestRecord2 = new BloodSugarMeasurement(12.2, testTime2);
        dizzinessRecord = new SymptomRecord("Dizziness", testDateAndTime);
        headacheRecord = new SymptomRecord("Headache", testDateAndTime);
        metformin = new MedicationRecord("Metformin", 875, testTime, testTime);
        glucophage = new MedicationRecord("Glucophage", 875, testTime, testTime);
        pizza = new FoodItemRecord("Pizza", testDateAndTime);
        spaghetti = new FoodItemRecord("Spaghetti", testDateAndTime);
        light = new WeightMeasurement(120, testDateAndTime);
        heavy = new WeightMeasurement(175, testDateAndTime);
    }

    @After
    public void cleanUp(){
        testObject = null;
        sdf = null;
        testDate = null;
        sdf2 = null;
        testDateAndTime = null;
        dizzinessRecord = null;
        headacheRecord = null;
    }

    @Test
    public void returnTheCorrectDateForTheRecord(){
        assertEquals(testDate, testObject.getCurrentDate());
    }

    @Test
    public void addANewBloodSugarMeasurementToTheLogCorrectlyWithOneRecordAdded(){
        List<BloodSugarMeasurement> result = testObject.addNewBSMeasurement(new BloodSugarMeasurement(14.5, testTime));
        assertEquals(1, result.size());
        assertEquals(14.5, result.get(0).getBloodSugarReading(), 0.0);
        assertEquals(testTime, result.get(0).getTimeOfBloodSugarCheck());
    }

    @Test
    public void returnTheCorrectResultsWhenTwoRecordsAreAddedForBloodSugarMeasurements(){
        testObject.addNewBSMeasurement(new BloodSugarMeasurement(14.5, testTime));
        List<BloodSugarMeasurement> results = testObject.addNewBSMeasurement(new BloodSugarMeasurement(12.2, testTime2));
        assertEquals(2, results.size());
        assertEquals(14.5, results.get(0).getBloodSugarReading(), 0.0);
        assertEquals(testTime, results.get(0).getTimeOfBloodSugarCheck());
        assertEquals(12.2, results.get(1).getBloodSugarReading(), 0.0);
        assertEquals(testTime2, results.get(1).getTimeOfBloodSugarCheck());
    }

    @Test
    public void returnTheCorrectCountWhenARecordIsDeleted(){
        testObject.addNewBSMeasurement(testRecord1);
        int preDeletionSize = testObject.addNewBSMeasurement(getTestRecord2).size();
        int postDeletionSize = testObject.deleteBloodSugarMeasurement(14.5, testTime).size();
        assertEquals(2, preDeletionSize);
        assertEquals(1, postDeletionSize);
    }

    @Test
    public void returnZeroIfAllRecordsAreDelted(){
        testObject.addNewBSMeasurement(testRecord1);
        testObject.addNewBSMeasurement(getTestRecord2);
        testObject.deleteBloodSugarMeasurement(14.5, testTime);
        int result = testObject.deleteBloodSugarMeasurement(12.2, testTime2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenOneRecordAddedForSymptoms(){
        List<SymptomRecord> returnedList = testObject.addNewSymptom(new SymptomRecord("Headache", testDateAndTime));
        int result = returnedList.size();
        assertEquals(1, result);
        assertEquals("Headache", returnedList.get(0).getSymptomValue());
        assertEquals(testDateAndTime, returnedList.get(0).getSymptomInputTime());
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenTwoRecordsAreAddedForSymptoms(){
        testObject.addNewSymptom(dizzinessRecord);
        List<SymptomRecord> returnedList = testObject.addNewSymptom(headacheRecord);
        int result = returnedList.size();
        assertEquals(2, result);
        assertEquals("Dizziness", returnedList.get(0).getSymptomValue());
        assertEquals(testDateAndTime, returnedList.get(0).getSymptomInputTime());
        assertEquals("Headache", returnedList.get(1).getSymptomValue());
        assertEquals(testDateAndTime, returnedList.get(1).getSymptomInputTime());
    }

    @Test
    public void returnTheCorrectCountWhenOneRecordIsDeletedFromAListOfTwo(){
        testObject.addNewSymptom(dizzinessRecord);
        testObject.addNewSymptom(headacheRecord);
        List<SymptomRecord> returnedList = testObject.deleteSymptom("Headache", testDateAndTime);
        assertEquals(1, returnedList.size());
    }

    @Test
    public void returnZeroWhenAllRecordsAreDeletedFromTheList(){
        testObject.addNewSymptom(dizzinessRecord);
        testObject.addNewSymptom(headacheRecord);
        testObject.deleteSymptom("Headache", testDateAndTime);
        List<SymptomRecord> returnedList = testObject.deleteSymptom("Dizziness", testDateAndTime);
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
        int result = testObject.deleteFoodRecord("Pizza", testDateAndTime).size();
        assertEquals(2, numberBeforeDeletion);
        assertEquals(1, result);
    }

    @Test
    public void returnZeroWhenAllItemsAreRemovedFromTheRecord(){
        testObject.addNewFoodRecord(pizza);
        int numberBeforeDeletion = testObject.addNewFoodRecord(spaghetti).size();
        testObject.deleteFoodRecord("Pizza", testDateAndTime);
        int numberAfterDeletion = testObject.deleteFoodRecord("Spaghetti", testDateAndTime).size();
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
        int numberAfterDeletion = testObject.deleteWeightRecord(120, testDateAndTime).size();
        assertEquals(1, numberAfterDeletion);
    }

    @Test
    public void returnZeroWhenAllItemsAreDeletedFromTheRecord(){
        testObject.addWeightRecord(light);
        int numberBeforeDeletion = testObject.addWeightRecord(heavy).size();
        testObject.deleteWeightRecord(120, testDateAndTime);
        int numberAfterDeletion = testObject.deleteWeightRecord(175, testDateAndTime).size();
        assertEquals(0, numberAfterDeletion);
    }
}
