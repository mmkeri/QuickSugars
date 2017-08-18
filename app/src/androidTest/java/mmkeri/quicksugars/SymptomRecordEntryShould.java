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

    private static final int idNumber1 = 20170101;
    private static final int idNUmber2 = 20170201;
    private static final int idNumber3 = 20170301;
    private static final LocalDate testDate1 = new LocalDate(2017, 01, 01);
    private static final LocalDate testDate2 = new LocalDate(2017, 02, 01);
    private static final LocalTime firstTime = new LocalTime(12, 12, 12);
    private static final LocalTime secondTime = new LocalTime(14, 14, 14);
    private static final LocalTime thirdTime = new LocalTime(16, 16, 16);

    @Before
    public void setUp(){

        Context context = InstrumentationRegistry.getTargetContext();
        DBTestUtils.resetDatabase(context);

        mSymptomRecordEntry = mActivityRule.getActivity();
    }

    @After
    public void cleanUp(){
    }

    @Test
    public void returnTwoWhenTwoRecordsHaveBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnOneWhenOneRecordHasBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnThreeWhenThreeRecordsHaveBeenAddedToTheDatabaseUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber3, "Fever", thirdTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(3, result);
    }

    @Test
    public void returnTheCorrectTwoRecordsWhenUsingAddTodaysReadingToDB(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNUmber2, "Dizziness", secondTime);
        List<SymptomRecord> list1 = mSymptomRecordEntry.getSymptomRecord(testDate1);
        List<SymptomRecord> list2 = mSymptomRecordEntry.getSymptomRecord(testDate2);
        assertEquals("Pain", list1.get(0).getSymptomValue());
        assertEquals("Dizziness", list2.get(0).getSymptomValue());
    }

    @Test
    public void returnOneWhenTwoRecordsAddedOnTheSameToday(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Dizziness", secondTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTheCorrectTwoRecordsWhenTheyAreAddedOnTheSameDay(){
        mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Pain", firstTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addTodaysReadingToDB(idNumber1, "Dizziness", secondTime);
        List<SymptomRecord> result = mSymptomRecordEntry.getSymptomRecord(testDate1);
        assertEquals("Pain", result.get(0).getSymptomValue());
        assertEquals("Dizziness", result.get(1).getSymptomValue());
    }

    @Test
    public void returnOneWhenOneRecordAddedUsingAddPreviousReadingToDB(){
        SQLiteDatabase testDB = mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTwoWhenTwoRecordsAreAddedUsingAddPreviousReadingToDB(){
        mSymptomRecordEntry.addPreviousReadingToDB(idNumber1, "Pain", firstTime);
        SQLiteDatabase testDB = mSymptomRecordEntry.addPreviousReadingToDB(idNUmber2, "Dizziness", secondTime);
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
