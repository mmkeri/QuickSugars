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

import java.util.List;

import mmkeri.quicksugars.utils.DBTestUtils;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class SymptomRecordEntryShould {

    @Rule
    public ActivityTestRule<SymptomRecordEntry> mActivityRule =
            new ActivityTestRule(SymptomRecordEntry.class);

    private SymptomRecordEntry mSymptomRecordEntry;

    private int idNumber1 = 20170101;
    private int idNUmber2 = 20170201;
    private int idNumber3 = 20170301;
    private LocalDate testDate1 = new LocalDate(2017, 01, 01);
    private LocalDate testDate2 = new LocalDate(2017, 02, 01);
    private LocalDate testDate3 = new LocalDate(2017, 03, 01);
    private LocalTime firstTime = new LocalTime(12, 12, 12);
    private LocalTime secondTime = new LocalTime(14, 14, 14);
    private LocalTime thirdTime = new LocalTime(16, 16, 16);
    private int firstDateAsInt = 20170101;
    private int secondDateAsInt = 20170201;
    private int thirdDateAsInt = 20170301;
    private LocalTime testTime = new LocalTime(12, 12, 12);
    private SQLiteDatabase testDB;
    private SymptomRecord testSymptomRecord1;
    private SymptomRecord testSymptomRecord2;
    private SymptomRecord testSmptomRecord3;

    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        DBTestUtils.resetDatabase(context);

        mSymptomRecordEntry = mActivityRule.getActivity();

        testSymptomRecord1 = new SymptomRecord("Pain", firstTime, firstDateAsInt);
        testSymptomRecord2 = new SymptomRecord("Dizziness", secondTime, secondDateAsInt);
        testSmptomRecord3 = new SymptomRecord("Fever", thirdTime, thirdDateAsInt);
        testDB = null;
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testDB = null;
    }

    @Test
    public void returnTwoWhenTwoRecordsHaveBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnOneWhenOneRecordHasBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnThreeWhenThreeRecordsHaveBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber3, "Fever", thirdTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(3, result);
    }

    @Test
    public void returnTheCorrectTwoRecordsWhenUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        List<SymptomRecord> list1 = mSymptomRecordEntry.getSymptomRecord(testDate1);
        List<SymptomRecord> list2 = mSymptomRecordEntry.getSymptomRecord(testDate2);
        assertEquals("Pain", list1.get(0).getSymptomValue());
        assertEquals("Dizziness", list2.get(0).getSymptomValue());
    }

    @Test
    public void returnOneWhenTwoRecordsAddedOnTheSameToday(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Dizziness", secondTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTheCorrectTwoRecordsWhenTheyAreAddedOnTheSameDay(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Dizziness", secondTime);
        List<SymptomRecord> result = mSymptomRecordEntry.getSymptomRecord(testDate1);
        assertEquals("Pain", result.get(0).getSymptomValue());
        assertEquals("Dizziness", result.get(1).getSymptomValue());
    }

    @Test
    public void returnOneWhenOneRecordAddedUsingAddPreviousReadingToDB(){
        testDB = mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTwoWhenTwoRecordsAreAddedUsingAddPreviousReadingToDB(){
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        testDB = mSymptomRecordEntry.addPreviousReadingToDB(idNUmber2, "Dizziness", secondTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnTheCorrectRrcordsWhenTwoRecordsAddedUsingAddPreviousReadintToDB(){
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        mSymptomRecordEntry.addPreviousReadingToDB(idNUmber2, "Dizziness", secondTime);
        List<SymptomRecord> result1 = mSymptomRecordEntry.getSymptomRecord(testDate1);
        List<SymptomRecord> result2 = mSymptomRecordEntry.getSymptomRecord(testDate2);
        assertEquals("Pain", result1.get(0).getSymptomValue());
        assertEquals("Dizziness", result2.get(0).getSymptomValue());
    }

    @Test
    public void returnOneWhenTwoRecordsAddedToTheSameDayUsingAddPreviousReadingToDB(){
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Dizziness", secondTime);
        List<SymptomRecord> result = mSymptomRecordEntry.getSymptomRecord(testDate1);
        assertEquals("Pain", result.get(0).getSymptomValue());
        assertEquals("Dizziness", result.get(1).getSymptomValue());
    }

    @Test
    public void returnTheCorrectRecordWhenGetSymptomRecordCalled(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        List<SymptomRecord> result = mSymptomRecordEntry.getSymptomRecord(testDate1);
        assertEquals("Pain", result.get(0).getSymptomValue());
    }

    @Test
    public void returnTheCorrectRecordWhenSeveralRecordsAddedToDBAndGetSymptomRecordIsCalled(){
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Dizziness", secondTime);
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Fever", thirdTime);
        List<SymptomRecord> result = mSymptomRecordEntry.getSymptomRecord(testDate1);
        assertEquals("Fever", result.get(2).getSymptomValue());
    }
}
