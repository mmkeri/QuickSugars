package mmkeri.quicksugars;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import org.joda.time.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.junit.runner.RunWith;

import java.util.ArrayList;

import mmkeri.quicksugars.utils.DBTestUtils;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MyDBHandlerShould {

    @Rule
    public final ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(BloodSugarEntry.class);

    private final LocalDate testDate = new LocalDate();
    private final LocalDate testPastDate = new LocalDate(2016, 01, 01);
    private final LocalDate testFutureDate = new LocalDate(2018, 06, 06);
    private final LocalTime testTime = new LocalTime();
    private final LocalTime testPastTime = new LocalTime(12, 01, 01);
    private LocalTime testFutureTime = new LocalTime(15, 15, 15);
    private final int firstDateAsInt = 20170401;
    private final int secondDateAsInt = 20170612;
    private final FoodItemWithNutrients testFoodItem = new FoodItemWithNutrients("apple", "1.2", "2.3", "3.4", "45", "55");
    private final FoodItemWithNutrients testFoodItem2 = new FoodItemWithNutrients("orange", "2.3", "3.4", "4.5", "56", "67");
    private final FoodItemWithNutrients testFoodItem3 = new FoodItemWithNutrients("Bramley_apple", "1.1", "2.2", "3.3", "44", "55");
    private final Gson gson = new Gson();
    private final MedicationObject testMedObject1 = new MedicationObject();
    private final MedicationObject testMedObject2 = new MedicationObject();
    private final MedicationObject testMedObject3 = new MedicationObject();

    private MyDBHandler testDBHandler;
    private DayLogObject testPastLogObject;
    private DayLogObject testFutureLogObject;
    private BloodSugarMeasurement bsMeasure;
    private BloodSugarMeasurement testPastBSMeasure;
    private DayLogObject logObject;

    @Before
    public void setUp() throws Exception{

        Context context = InstrumentationRegistry.getTargetContext();

        testDBHandler = DBTestUtils.resetDatabase(context);
        testPastLogObject = new DayLogObject(testPastDate);
        testFutureLogObject = new DayLogObject(testFutureDate);
        bsMeasure = new BloodSugarMeasurement(12.5, testTime, firstDateAsInt);
        testPastBSMeasure = new BloodSugarMeasurement(10.5, testPastTime, secondDateAsInt);
        logObject = new DayLogObject(testDate);
    }

    @After
    public void cleanUp() throws Exception{
        testDBHandler.deleteDatabase();
    }

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("mmkeri.quicksugars", appContext.getPackageName());
    }

    @Test
    public void returnTheCorrectCountOfRecordsInTheDatabaseUsingPutLogRecordIfOnlyOneRecordAdded(){
        SQLiteDatabase returnedDatabase = testDBHandler.putLogRecord(logObject);
        int result = returnedDatabase.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenTwoRecordsAreAdded(){
        logObject.addNewBSMeasurement(bsMeasure);
        testPastLogObject.addNewBSMeasurement(testPastBSMeasure);
        testDBHandler.putLogRecord(logObject);
        SQLiteDatabase returnedDatabase = testDBHandler.putLogRecord(testPastLogObject);
        int result = returnedDatabase.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnTheSameRecordAddedUsingPutLogRecord(){
        logObject.addNewBSMeasurement(bsMeasure);
        SQLiteDatabase returnedDatabase = testDBHandler.putLogRecord(logObject);
        Cursor cursor = returnedDatabase.rawQuery("SELECT * FROM logRecords", null);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        String stringResult = cursor.getString(1);
        Gson gson = new Gson();
        DayLogObject recordResult = gson.fromJson(stringResult, DayLogObject.class);
        assertEquals(logObject.getId(), recordResult.getId());
        assertEquals(logObject.getCurrentDate(), recordResult.getCurrentDate());
    }

    @Test
    public void returnCorrectRecordsWhenTwoLogObjectsAddedToDatabase(){
        logObject.addNewBSMeasurement(bsMeasure);
        testPastLogObject.addNewBSMeasurement(testPastBSMeasure);
        testDBHandler.putLogRecord(logObject);
        SQLiteDatabase returnedDatabase = testDBHandler.putLogRecord(testPastLogObject);
        Cursor cursor = returnedDatabase.rawQuery("SELECT * FROM logRecords", null);
        cursor.moveToFirst();
        String firstRecord = cursor.getString(1);
        DayLogObject firstRecordResult = gson.fromJson(firstRecord, DayLogObject.class);
        cursor.moveToNext();
        String secondRecord = cursor.getString(1);
        DayLogObject secondRecordResult = gson.fromJson(secondRecord, DayLogObject.class);
        assertEquals(testPastLogObject.getId(), firstRecordResult.getId());
        assertEquals(testPastLogObject.getCurrentDate(), firstRecordResult.getCurrentDate());
        assertEquals(logObject.getId(), secondRecordResult.getId());
        assertEquals(logObject.getCurrentDate(), secondRecordResult.getCurrentDate());
    }

    @Test
    public void returnACountOfThreeWhenGetAllDayLogObjectsIsCalled(){
        testDBHandler.putLogRecord(logObject);
        testDBHandler.putLogRecord(testPastLogObject);
        testDBHandler.putLogRecord(testFutureLogObject);
        Cursor returnedCursor = testDBHandler.getAllDayLogObjects();
        int result = returnedCursor.getCount();
        assertEquals(3, result);
    }

    @Test
    public void returnACountOfZeroWhenGetAllDayLogObjectsIsCalledOnAnEmptyDatabase(){
        Cursor returnedCursor = testDBHandler.getAllDayLogObjects();
        int result = returnedCursor.getCount();
        assertEquals(0, result);
    }

    @Test
    public void returnTheExpectedObjectsWhenGetAllDayLOgObjectsIsCalled(){
        testDBHandler.putLogRecord(logObject);
        testDBHandler.putLogRecord(testPastLogObject);
        testDBHandler.putLogRecord(testFutureLogObject);
        Cursor returnedCursor = testDBHandler.getAllDayLogObjects();
        returnedCursor.moveToFirst();
        DayLogObject result1 = gson.fromJson(returnedCursor.getString(1), DayLogObject.class);
        returnedCursor.moveToNext();
        DayLogObject result2 = gson.fromJson(returnedCursor.getString(1), DayLogObject.class);
        returnedCursor.moveToNext();
        DayLogObject result3 = gson.fromJson(returnedCursor.getString(1), DayLogObject.class);
        assertEquals(result1.getId(), testPastLogObject.getId());
        assertEquals(result2.getId(), logObject.getId());
        assertEquals(result3.getId(), testFutureLogObject.getId());
    }

    @Test
    public void correctlyUpdateTheDatabaseWhenUpdateDayLogObjectIsCalled(){
        WeightMeasurement testWeight = new WeightMeasurement(125, testTime, firstDateAsInt);
        logObject.addNewBSMeasurement(bsMeasure);
        testDBHandler.putLogRecord(logObject);
        DayLogObject returnedDLO = testDBHandler.getDayLogObject(logObject.getId());
        returnedDLO.addWeightRecord(testWeight);
        SQLiteDatabase returnedDB = testDBHandler.updateDayLogObject(returnedDLO);
        Cursor returnedCursor = returnedDB.rawQuery("SELECT * FROM logRecords", null);
        returnedCursor.moveToFirst();
        DayLogObject secondReturnedDLO = gson.fromJson(returnedCursor.getString(1), DayLogObject.class);
        assertEquals(testWeight.getWeightValue(), secondReturnedDLO.getWeightRecord().get(0).getWeightValue(), 1);
        assertEquals(returnedDLO.getId(), secondReturnedDLO.getId());
    }

    @Test
    public void returnTheRequestedDayLogObjetWhenGetDayLogObjectIsCalled(){
        testDBHandler.putLogRecord(logObject);
        DayLogObject result = testDBHandler.getDayLogObject(logObject.getId());
        assertEquals(logObject.getCurrentDate(), result.getCurrentDate());
    }

    @Test
    public void returnTheCorrectObjectWhenMultipleDayLogObjectsAddedAndGetDayLogObjectIsCalled(){
        testDBHandler.putLogRecord(testPastLogObject);
        testDBHandler.putLogRecord(testFutureLogObject);
        testDBHandler.putLogRecord(logObject);
        DayLogObject result = testDBHandler.getDayLogObject(testPastLogObject.getId());
        assertEquals(testPastLogObject.getCurrentDate(), result.getCurrentDate());
    }

    @Test
    public void returnAnEmptyDayLogObjectWhenGetDayLOgObjectIsCalledOnRecordThatDoesntExist(){
        WeightMeasurement testWeight = new WeightMeasurement(125, testTime, firstDateAsInt);
        testPastLogObject.addWeightRecord(testWeight);
        testFutureLogObject.addWeightRecord(testWeight);
        logObject.addWeightRecord(testWeight);
        testDBHandler.putLogRecord(testPastLogObject);
        testDBHandler.putLogRecord(testFutureLogObject);
        testDBHandler.putLogRecord(logObject);
        DayLogObject result = testDBHandler.getDayLogObject(20150201);
        assertEquals(20150201, result.getId());
        assertEquals(0, result.getWeightRecord().size());
    }

    @Test
    public void deleteARecordCorrectlyAndGiveCountOfOneWhenTwoRecordsPreviouslyEntered(){
        logObject.addNewBSMeasurement(bsMeasure);
        testPastLogObject.addNewBSMeasurement(testPastBSMeasure);
        testDBHandler.putLogRecord(logObject);
        SQLiteDatabase databaseWithTwoREcords = testDBHandler.putLogRecord(testPastLogObject);
        int countBeforeDeletion = databaseWithTwoREcords.rawQuery("SELECT * FROM logRecords", null).getCount();
        SQLiteDatabase databaseAfterDeletion = testDBHandler.deleteRecord(testPastLogObject.getId());
        int countAfterDeletion = databaseAfterDeletion.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, countBeforeDeletion);
        assertEquals(1, countAfterDeletion);
    }
    @Test
    public void zeroWhenTheLastRecordIsDeletedFromTheDatabase(){
        logObject.addNewBSMeasurement(bsMeasure);
        testDBHandler.putLogRecord(logObject);
        SQLiteDatabase returnedDatabase = testDBHandler.deleteRecord(logObject.getId());
        int countAfterDeletion = returnedDatabase.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(0, countAfterDeletion);
    }

    @Test
    public void returnTheCorrectRecordWhenGetRecordIsCalled(){
        logObject.addNewBSMeasurement(bsMeasure);
        testPastLogObject.addNewBSMeasurement(testPastBSMeasure);
        testDBHandler.putLogRecord(logObject);
        testDBHandler.putLogRecord(testPastLogObject);
        DayLogObject returnedLogObject = testDBHandler.getLogObject(testPastLogObject.getId());
        assertEquals(testPastLogObject.getId(), returnedLogObject.getId());
        assertEquals(testPastLogObject.getCurrentDate(), returnedLogObject.getCurrentDate());
    }

    @Test
    public void notRemoveTheRecordWhenGetRecordIsCalled(){
        logObject.addNewBSMeasurement(bsMeasure);
        testPastLogObject.addNewBSMeasurement(testPastBSMeasure);
        testDBHandler.putLogRecord(logObject);
        SQLiteDatabase db = testDBHandler.putLogRecord(testPastLogObject);
        int countBeforeCallingMethod = db.rawQuery("SELECT * FROM logRecords", null).getCount();
        testDBHandler.getLogObject(logObject.getId());
        db = testDBHandler.putLogRecord(testFutureLogObject);
        int countAfterMethodAndAddingOneRecord = db.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, countBeforeCallingMethod);
        assertEquals(3, countAfterMethodAndAddingOneRecord);
    }

    @Test
    public void returnTheCorrectCountOfRecordsInTheDatabaseUsingPutFoodRecordIfOnlyOneRecordAdded(){
        SQLiteDatabase returnedDatabase = testDBHandler.putFoodRecord(testFoodItem);
        int result = returnedDatabase.rawQuery("SELECT * FROM foodItems", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTheCorrectCountOfRecordsWhenTwoRecordsAreAddedToFoodTable(){
        testDBHandler.putFoodRecord(testFoodItem);
        SQLiteDatabase returnedDatabase = testDBHandler.putFoodRecord(testFoodItem2);
        int result = returnedDatabase.rawQuery("SELECT * FROM foodItems", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnACountOfOneWhenQueriedWithAp(){
        testDBHandler.putFoodRecord(testFoodItem);
        testDBHandler.putFoodRecord(testFoodItem2);
        ArrayList<FoodItemWithNutrients> returnedList = testDBHandler.getFoodName("Ap");
        assertEquals(1, returnedList.size());
    }

    @Test
    public void returnACountOfTwoWhenQueriedWithApAndTwoAppleRecordsAdded(){
        testDBHandler.putFoodRecord(testFoodItem);
        testDBHandler.putFoodRecord(testFoodItem3);
        ArrayList<FoodItemWithNutrients> returnedList = testDBHandler.getFoodName("AP");
        assertEquals(2, returnedList.size());
    }

    @Test
    public void returnTheExpectedResultsOfTheQuery(){
        testDBHandler.putFoodRecord(testFoodItem2);
        ArrayList<FoodItemWithNutrients> returnedList = testDBHandler.getFoodName("or");
        assertEquals(1, returnedList.size());
        assertEquals("orange", returnedList.get(0).getFoodName());
        assertEquals("2.3", returnedList.get(0).getProtein());
        assertEquals("3.4", returnedList.get(0).getCarbohydrate());
        assertEquals("4.5", returnedList.get(0).getFat());
        assertEquals("56", returnedList.get(0).getKilocals());
        assertEquals("67", returnedList.get(0).getKilojoules());
    }

    @Test
    public void returnACountOfOneWhenASingleMedicationIsAddedToDBUsingPutMedicationRecord(){
        testMedObject1.setTradeName("Lipitor");
        SQLiteDatabase returnedDB = testDBHandler
                .putMedicationRecord(20170601, "121212", gson.toJson(testMedObject1));
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        assertEquals(1, cursor.getCount());
    }

    @Test
    public void returnACountOfTwoWhenPutMedicationRecordIsUsedToAddTwoRecordsToTheDB(){
        testDBHandler.putMedicationRecord(20170601, "121212", gson.toJson(testMedObject1));
        SQLiteDatabase returnedDB = testDBHandler.putMedicationRecord(20170701, "141414", gson.toJson(testMedObject2));
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        assertEquals(2, cursor.getCount());
    }

    @Test
    public void storeTheCorrectRecordInTheDBWhenPutMedicationIsCalled(){
        testMedObject1.setTradeName("Lipitor");
        SQLiteDatabase returnedDB = testDBHandler.putMedicationRecord(20170601, "121212", gson.toJson(testMedObject1));
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        cursor.moveToFirst();
        assertEquals(testMedObject1.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
    }

    @Test
    public void storeTheCorrectRecordsInTheDBWhenPutMedicationIsCalledAndTwoRecordsHaveBeenAdded(){
        testMedObject1.setTradeName("Lipitor");
        testMedObject2.setTradeName("Tylenol");
        testDBHandler.putMedicationRecord(20170701, "141414", gson.toJson(testMedObject2));
        SQLiteDatabase returnedDB = testDBHandler.putMedicationRecord(20170601, "121212", gson.toJson(testMedObject1));
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        cursor.moveToFirst();
        assertEquals(testMedObject1.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
        cursor.moveToNext();
        assertEquals(testMedObject2.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
    }

    @Test
    public void returnTheCorrectRecordWhenGetMedicationRecordIsCalled(){
        testMedObject1.setTradeName("Lipitor");
        testDBHandler.putMedicationRecord(20170601, "121212", gson.toJson(testMedObject1));
        Cursor cursor = testDBHandler.getMedicationRecord(20170601);
        int count = cursor.getCount();
        cursor.moveToFirst();
        assertEquals(1, count);
        assertEquals(testMedObject1.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
    }

    @Test
    public void returnAnEmptyCursorWhenGetMedicationIsCalledAndRecordDoesNotExist(){
        Cursor cursor = testDBHandler.getMedicationRecord(20170801);
        int count = cursor.getCount();
        assertEquals(0, count);
    }

    @Test
    public void correctlyReturnAllRecordsInTheDBWhenGetAllMedicationRecordsIsCalled(){
        testMedObject1.setTradeName("Lipitor");
        testMedObject2.setTradeName("Januvia");
        testMedObject3.setTradeName("Lispro");
        testDBHandler.putMedicationRecord(20170501, "121212", gson.toJson(testMedObject1));
        testDBHandler.putMedicationRecord(20170601, "141414", gson.toJson(testMedObject2));
        testDBHandler.putMedicationRecord(20170701, "161616", gson.toJson(testMedObject3));
        Cursor cursor = testDBHandler.getAllMedicationRecords();
        int count = cursor.getCount();
        cursor.moveToFirst();
        MedicationObject result1 = gson.fromJson(cursor.getString(2), MedicationObject.class);
        cursor.moveToNext();
        MedicationObject result2 = gson.fromJson(cursor.getString(2), MedicationObject.class);
        cursor.moveToNext();
        MedicationObject result3 = gson.fromJson(cursor.getString(2), MedicationObject.class);
        assertEquals(3, count);
        assertEquals(testMedObject1.getTradeName(), result1.getTradeName());
        assertEquals(testMedObject2.getTradeName(), result2.getTradeName());
        assertEquals(testMedObject3.getTradeName(), result3.getTradeName());
    }

    @Test
    public void returnAnEmptyCursorIfTheDBIsEmptyAndGetAllMedicationRecordsIsCalled(){
        Cursor cursor = testDBHandler.getAllMedicationRecords();
        int count = cursor.getCount();
        assertEquals(0, count);
    }

    @Test
    public void returnTwoWhenWhenDeleteScheduledMedicationRecordIsCalledOnADBWithThreeRecords(){
        testMedObject1.setTradeName("Lipitor");
        testMedObject2.setTradeName("Januvia");
        testMedObject3.setTradeName("Lispro");
        testDBHandler.putMedicationRecord(20170501, "121212", gson.toJson(testMedObject1));
        testDBHandler.putMedicationRecord(20170601, "141414", gson.toJson(testMedObject2));
        SQLiteDatabase returnedDB = testDBHandler
                .putMedicationRecord(20170701, "161616", gson.toJson(testMedObject3));
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        int countBeforeDeletion = cursor.getCount();
        returnedDB = testDBHandler.deleteScheduledMedicationRecord(20170501);
        cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        int countAfterDeletion = cursor.getCount();
    }

    @Test
    public void deleteTheCorrectRecordWhenDeleteScheduledMedicationRecordIsCalled(){
        testMedObject1.setTradeName("Lipitor");
        testMedObject2.setTradeName("Januvia");
        testMedObject3.setTradeName("Lispro");
        testDBHandler.putMedicationRecord(20170501, "121212", gson.toJson(testMedObject1));
        testDBHandler.putMedicationRecord(20170601, "141414", gson.toJson(testMedObject2));
        testDBHandler.putMedicationRecord(20170701, "161616", gson.toJson(testMedObject3));
        SQLiteDatabase returnedDB = testDBHandler.deleteScheduledMedicationRecord(20170501);
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        cursor.moveToFirst();
        assertEquals(testMedObject2.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
        cursor.moveToNext();
        assertEquals(testMedObject3.getTradeName(), gson.fromJson(cursor.getString(2), MedicationObject.class).getTradeName());
    }

    @Test
    public void notDeleteAnyRecordsIfTheIDNumberPassedToDeleteScheduledMedicationRecordsDoesNotExist(){
        testMedObject1.setTradeName("Lipitor");
        testMedObject2.setTradeName("Januvia");
        testMedObject3.setTradeName("Lispro");
        testDBHandler.putMedicationRecord(20170501, "121212", gson.toJson(testMedObject1));
        testDBHandler.putMedicationRecord(20170601, "141414", gson.toJson(testMedObject2));
        testDBHandler.putMedicationRecord(20170701, "161616", gson.toJson(testMedObject3));
        SQLiteDatabase returnedDB = testDBHandler.deleteScheduledMedicationRecord(20160203);
        Cursor cursor = returnedDB.rawQuery("SELECT * FROM scheduledMedications", null);
        assertEquals(3, cursor.getCount());
    }

    @Test
    public void showOneRecordSavedWhenOnlyOneSetOfValuesPassedToPutWeightRecord(){
        SQLiteDatabase db = testDBHandler.putWeightRecord(20170101, 125.5);
        Cursor cursor = db.rawQuery("SELECT * FROM weightRecords", null);
        assertEquals(1, cursor.getCount());
    }

    @Test
    public void showTwoRecordsSavedWhenTwoSetsOfValuesPassedToPutWeightRecord(){
        testDBHandler.putWeightRecord(20170101, 125.5);
        SQLiteDatabase db = testDBHandler.putWeightRecord(20170202, 130);
        Cursor cursor = db.rawQuery("SELECT * FROM weightRecords", null);
        assertEquals(2, cursor.getCount());
    }

    @Test
    public void returnTheExpectedResultsWhenGetWeightRecordsIsCalled(){
        testDBHandler.putWeightRecord(20170101, 125.5);
        testDBHandler.putWeightRecord(20170202, 130);
        Cursor cursor = testDBHandler.getWeightRecords();
        cursor.moveToFirst();
        assertEquals(20170101, cursor.getInt(0));
        assertEquals(125.5, cursor.getDouble(1), 0.5);
        cursor.moveToNext();
        assertEquals(20170202, cursor.getInt(0));
        assertEquals(130, cursor.getDouble(1), 0.5);
    }
}
