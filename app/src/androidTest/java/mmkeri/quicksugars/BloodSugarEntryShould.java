package mmkeri.quicksugars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BloodSugarEntryShould {

    @Rule
    public ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(BloodSugarEntry.class);

    private BloodSugarEntry mBloodSugarEntry;

    private LocalDate firstDate = new LocalDate(2017, 1, 1);
    private LocalDate secondDate = new LocalDate(2017, 2, 1);
    private LocalDate thirdDate = new LocalDate(2017, 3, 1);
    private Date date1 = firstDate.toDate();
    private Date date2 = secondDate.toDate();
    private Date date3 = thirdDate.toDate();
    private double sugar1 = 12.2;
    private double sugar2 = 14.4;
    private double sugar3 = 16.6;
    private LocalTime testTime = new LocalTime(12, 12, 12);
    private MyDBHandler testHandler;
    private SQLiteDatabase testDB;
    private BloodSugarMeasurement bsEntry1;
    private BloodSugarMeasurement bsEntry2;


    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testHandler.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS);
        testHandler.onCreate(db);

        mBloodSugarEntry = mActivityRule.getActivity();
        bsEntry1 = new BloodSugarMeasurement(12.2, testTime);
        bsEntry2 = new BloodSugarMeasurement(14.4, testTime);
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testHandler.deleteDatabase();
        testDB = null;
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddTodaysRecordToDB(){
        mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        testDB = mBloodSugarEntry.addTodaysReadingToDB(20170106, 14.4, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        List<BloodSugarMeasurement> resultList2 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 06));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
        assertEquals(14.4, resultList2.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddTodaysRecordToDB(){
        testDB = mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenTwoRecordsEnteredOnTheSameDayUsingAddTodaysRecordToDB(){
        mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        testDB = mBloodSugarEntry.addTodaysReadingToDB(20170105, 14.4, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords;", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddPreviousRecordToDB(){
        mBloodSugarEntry.addPreviousReadingToDB(20170105, 12.2, testTime);
        testDB = mBloodSugarEntry.addPreviousReadingToDB(20170106, 14.4, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        List<BloodSugarMeasurement> resultList2 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 06));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
        assertEquals(14.4, resultList2.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddPreviousRecordToDB(){
        testDB = mBloodSugarEntry.addPreviousReadingToDB(20170105, 12.2, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
    }

    @Test
    public void convertAnIntToLocalDateCorrectly(){
        LocalDate result = DateConversion.convertDateAsIntToLocalDate(20170101);
        assertEquals(firstDate, result);
    }

    @Test(expected = NumberFormatException.class)
    public void throwAnExceptionWhenAnInvalidIntIsSubmittedAsADate(){
        DateConversion.convertDateAsIntToLocalDate(22001201);
    }

    @Test
    public void convertALocalDateObjectToIntCorrectly(){
        int result = DateConversion.convertLocalDateToInt(firstDate);
        assertEquals(20170101, result);
    }

    @Test
    public void returnAnAccurateListOfWeightMeasurementsWhenGetWeightMeasureIsCalled(){
        ArrayList<BloodSugarMeasurement> expected = new ArrayList<>();
        expected.add(bsEntry1);
        expected.add(bsEntry2);
        SQLiteDatabase one = mBloodSugarEntry.addTodaysReadingToDB(20170101, 12.2, testTime);
        SQLiteDatabase two = mBloodSugarEntry.addTodaysReadingToDB(20170101, 14.4, testTime);
        LocalDate testDate = new LocalDate(2017, 01, 01);
        List<BloodSugarMeasurement> result = mBloodSugarEntry.
                getBSReading(testDate);
        assertEquals(expected.get(0).getBloodSugarReading(), result.get(0).getBloodSugarReading());
        assertEquals(expected.get(0).getTimeOfBloodSugarCheck(), result.get(0).getTimeOfBloodSugarCheck());
        assertEquals(expected.get(1).getBloodSugarReading(), result.get(1).getBloodSugarReading());
        assertEquals(expected.get(1).getTimeOfBloodSugarCheck(), result.get(1).getTimeOfBloodSugarCheck());
    }
}
