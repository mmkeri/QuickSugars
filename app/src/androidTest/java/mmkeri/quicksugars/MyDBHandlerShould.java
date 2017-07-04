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

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MyDBHandlerShould {

    @Rule
    public ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(BloodSugarEntry.class);

    private MyDBHandler testDBHandler;
    private DayLogObject testPastLogObject;
    private DayLogObject testFutureLogObject;
    private LocalDate testDate;
    private LocalDate testPastDate;
    private LocalDate testFutureDate;
    private LocalTime testTime;
    private LocalTime testPastTime;
    private LocalTime testFutureTime;
    private SQLiteDatabase.CursorFactory cursorFactory;
    private BloodSugarMeasurement bsMeasure;
    private BloodSugarMeasurement testPastBSMeasure;
    private DayLogObject logObject;
    private SQLiteDatabase db;
    private static final String TEST_FILE_PREFIX = "test_";

    @Before
    public void setUp() throws Exception{

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testDBHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testDBHandler.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS);
        testDBHandler.onCreate(db);

        //RenamingDelegatingContext context
        //        = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
        testDate = new LocalDate();
        testPastDate = new LocalDate(2016, 01, 01);
        testFutureDate = new LocalDate(2018, 06, 06);
        testTime = new LocalTime();
        testPastTime = new LocalTime(12, 01, 01);
        testFutureTime = new LocalTime(15, 15, 15);
        testPastLogObject = new DayLogObject(testPastDate);
        testFutureLogObject = new DayLogObject(testFutureDate);
        bsMeasure = new BloodSugarMeasurement(12.5, testTime);
        testPastBSMeasure = new BloodSugarMeasurement(10.5, testPastTime);
        logObject = new DayLogObject(testDate);

    }

    @After
    public void cleanUp() throws Exception{
        testDBHandler.deleteDatabase();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("mmkeri.quicksugars", appContext.getPackageName());
    }

    @Test
    public void returnTheCorrectCountOfRecordsInTheDatabaseUsingAddLogRecordIfOnlyOneRecordAdded(){
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
    public void returnTheSameRecordAddedUsingAddLogRecord(){
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
        Gson gson = new Gson();
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
}
