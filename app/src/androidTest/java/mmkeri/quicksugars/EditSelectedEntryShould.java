package mmkeri.quicksugars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class EditSelectedEntryShould {

    @Rule
    public final ActivityTestRule<EditSelectedEntry> mActivityRule =
            new ActivityTestRule(EditSelectedEntry.class);

    private final int testDateAsInt = 20170601;
    private final LocalTime testTime = new LocalTime();
    private final ArrayList<DayLogObject> testDayLogObjectList1 = new ArrayList<>();
    private final ArrayList<DayLogObject> testDayLogObjectList2 = new ArrayList<>();

    private MyDBHandler testHandler;
    private EditSelectedEntry mEditSelectedEntry;
    private DayLogObject testDayLogObject;
    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testHandler.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS);
        //testHandler.onCreate(db);

        mEditSelectedEntry = mActivityRule.getActivity();

        LocalDate testDate = new LocalDate();
        testDayLogObject = new DayLogObject(testDate);
        BloodSugarMeasurement testBloodSugarMeasurement1 = new BloodSugarMeasurement(12.5,testTime, testDateAsInt);
        BloodSugarMeasurement testTestBloodSugarMeasurement2 = new BloodSugarMeasurement(10, testTime, testDateAsInt);
        WeightMeasurement testWeightMeasurement1 = new WeightMeasurement(65, testTime, testDateAsInt);
        WeightMeasurement testWeightMeasurement2 = new WeightMeasurement(70, testTime, testDateAsInt);
        SymptomRecord testSymptomRecord1 = new SymptomRecord("Pain", testTime, testDateAsInt);
        SymptomRecord testSymptomRecord2 = new SymptomRecord("Dizziness", testTime, testDateAsInt);
        FoodItemRecord testFoodItemRecord1 = new FoodItemRecord("Apple", testTime, testDateAsInt);
        FoodItemRecord testFoodItemRecord2 = new FoodItemRecord("Orange", testTime, testDateAsInt);
        testDayLogObject.addNewBSMeasurement(testBloodSugarMeasurement1);
        testDayLogObject.addNewBSMeasurement(testTestBloodSugarMeasurement2);
        testDayLogObject.addWeightRecord(testWeightMeasurement1);
        testDayLogObject.addWeightRecord(testWeightMeasurement2);
        testDayLogObject.addNewSymptom(testSymptomRecord1);
        testDayLogObject.addNewSymptom(testSymptomRecord2);
        testDayLogObject.addNewFoodRecord(testFoodItemRecord1);
        testDayLogObject.addNewFoodRecord(testFoodItemRecord2);
        sqLiteDatabase = testHandler.putLogRecord(testDayLogObject);
        testDayLogObjectList1.add(testDayLogObject);
    }

    @After
    public void cleanUp(){
        testHandler.deleteDatabase();
        testDayLogObject = null;
    }

    @Test
    public void returnOneDayLogObjectWhenGetDayLogObjectsFromDatabaseIsCalled(){
        int countOfDatabaseRecords = sqLiteDatabase.rawQuery("SELECT * FROM logRecords", null).getCount();
        ArrayList<DayLogObject> returnedDayLogObjectList = mEditSelectedEntry.getDayLogObjectsFromDatabase();
        int countOfList = returnedDayLogObjectList.size();
        assertEquals(1, countOfDatabaseRecords);
        assertEquals(1, countOfList);
    }

    @Test
    public void returnCountOfTwoForGetBloodSugarMeasurementsFromDatabase(){
        int result = mEditSelectedEntry.getBloodSugarMeasurementsFromDatabase(testDayLogObjectList1).size();
        assertEquals(2, result);
    }

    @Test
    public void returnZeroForGetBloodSugarMeasurementsFromDatabaseIfEmpty(){
        int result = mEditSelectedEntry.getBloodSugarMeasurementsFromDatabase(testDayLogObjectList2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectBSMeasurementRecordsWhenGetBloodSugarMeasurementsFromDatabaseIsCalled(){
        ArrayList<BloodSugarMeasurement> returnedList = mEditSelectedEntry.
                getBloodSugarMeasurementsFromDatabase(testDayLogObjectList1);
        assertEquals(12.5, returnedList.get(0).getBloodSugarReading());
        assertEquals(testTime, returnedList.get(0).getTimeOfBloodSugarCheck());
        assertEquals(testDateAsInt, returnedList.get(0).getDateAsInt());
        assertEquals(10.0, returnedList.get(1).getBloodSugarReading());
        assertEquals(testTime, returnedList.get(1).getTimeOfBloodSugarCheck());
        assertEquals(testDateAsInt, returnedList.get(1).getDateAsInt());
    }

    @Test
    public void returnCountOfTwoForGetWeightMeasurementsFromDatabase(){
        int result = mEditSelectedEntry.getWeightMeasurementsFromDatabase(testDayLogObjectList1).size();
        assertEquals(2, result);
    }

    @Test
    public void returnCountOfZeroForGetWeightMeasurementsFromDatabaseifDatabaseEmpty(){
        int result = mEditSelectedEntry.getWeightMeasurementsFromDatabase(testDayLogObjectList2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectWeightMeasurementsWhenGetWeightMeasurementsFromDatabaseIsCalled(){
        ArrayList<WeightMeasurement> returnedList = mEditSelectedEntry.
                getWeightMeasurementsFromDatabase(testDayLogObjectList1);
        assertEquals(65.0, returnedList.get(0).getWeightValue());
        assertEquals(testTime, returnedList.get(0).getInputTime());
        assertEquals(testDateAsInt, returnedList.get(0).getDateAsInt());
        assertEquals(70.0, returnedList.get(1).getWeightValue());
        assertEquals(testTime, returnedList.get(1).getInputTime());
        assertEquals(testDateAsInt, returnedList.get(1).getDateAsInt());
    }

    @Test
    public void returnCountOfTwoForGetSymptomRecordsFromDatabase(){
        int result = mEditSelectedEntry.getSymptomRecordsFromDatabase(testDayLogObjectList1).size();
        assertEquals(2, result);
    }

    @Test
    public void returnCountOfZeroForGetSymptomREcordsFromDatabaseIfDatabseEmpty(){
        int result = mEditSelectedEntry.getSymptomRecordsFromDatabase(testDayLogObjectList2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectSymptomRecordsWhenGetSymptomRecordsFromDatabaseIsCalled(){
        ArrayList<SymptomRecord> returnedList = mEditSelectedEntry.
                getSymptomRecordsFromDatabase(testDayLogObjectList1);
        assertEquals("Pain", returnedList.get(0).getSymptomValue());
        assertEquals(testTime, returnedList.get(0).getSymptomInputTime());
        assertEquals(testDateAsInt, returnedList.get(0).getDateAsInt());
        assertEquals("Dizziness", returnedList.get(1).getSymptomValue());
        assertEquals(testTime, returnedList.get(1).getSymptomInputTime());
        assertEquals(testDateAsInt, returnedList.get(1).getDateAsInt());
    }

    @Test
    public void returnCountOfTwoForGetFoodItemRecordsFromDatabase(){
        int result = mEditSelectedEntry.getFoodItemRecordsFromDatabase(testDayLogObjectList1).size();
        assertEquals(2, result);
    }

    @Test
    public void returnCountOfZeroForGetFoodItemsRecordsFromDatabaseIfDatabaseEmpty(){
        int result = mEditSelectedEntry.getFoodItemRecordsFromDatabase(testDayLogObjectList2).size();
        assertEquals(0, result);
    }

    @Test
    public void returnTheCorrectFoodItemRecordsWhenGetFoodItemRecordsFromDatabaseIsCalled(){
        ArrayList<FoodItemRecord> returnedList = mEditSelectedEntry.
                getFoodItemRecordsFromDatabase(testDayLogObjectList1);
        assertEquals("Apple", returnedList.get(0).getFoodItem());
        assertEquals(testTime, returnedList.get(0).getInputTime());
        assertEquals(testDateAsInt, returnedList.get(0).getDateAsInt());
        assertEquals("Orange", returnedList.get(1).getFoodItem());
        assertEquals(testTime, returnedList.get(1).getInputTime());
        assertEquals(testDateAsInt, returnedList.get(1).getDateAsInt());
    }
}
