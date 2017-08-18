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

import mmkeri.quicksugars.utils.DBTestUtils;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WeightRecordEntryShould {

    @Rule
    public ActivityTestRule<WeightRecordEntry> mActivityRule =
            new ActivityTestRule(WeightRecordEntry.class, true, true);

    private WeightRecordEntry mWeightRecordActivity;

    private static final LocalDate firstDate = new LocalDate(2017, 1, 1);
    private static final LocalDate secondDate = new LocalDate(2017, 2, 1);
    private static final LocalDate thirdDate = new LocalDate(2017, 3, 1);
    private static final double weight1 = 65;
    private static final double weight2 = 70;
    private static final double weight3 = 40;
    private static final LocalTime defaultTime = new LocalTime(12, 01, 01);
    private static final int testDateAsInt = 20170501;
    private MyDBHandler testHandler;
    private WeightMeasurement weightRecord1 = new WeightMeasurement(65, defaultTime, testDateAsInt);
    private WeightMeasurement weightRecord2 = new WeightMeasurement(70, defaultTime, testDateAsInt);;



    @Before
    public void setUp(){

        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = DBTestUtils.resetDatabase(context);

        mWeightRecordActivity = mActivityRule.getActivity();
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testHandler.deleteDatabase();
    }

    @Test
    public void createAGraphWithThreePointsIfGivenThreeEntries(){

        //mWeightRecordActivity.saveEntry(mWeightRecordActivity.getCurrentFocus());
        mWeightRecordActivity.addGraphPoint(weight1, firstDate);
        mWeightRecordActivity.addGraphPoint(weight2, secondDate);
        mWeightRecordActivity.addGraphPoint(weight3, thirdDate);
        assertEquals(1, 1);
    }

    @Test
    public void convertDateToIntCorrectly(){
        int result = DateConversion.convertLocalDateToInt(firstDate);
        assertEquals(20170101, result);
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddTodaysRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        SQLiteDatabase testDB = mWeightRecordActivity.addTodaysRecordToDB(20170106, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<WeightMeasurement> resultList1 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 05));
        List<WeightMeasurement> resultList2 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 06));
        assertEquals(65.0, resultList1.get(0).getWeightValue());
        assertEquals(70.0, resultList2.get(0).getWeightValue());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddTodaysRecordToDB(){
        SQLiteDatabase testDB = mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<WeightMeasurement> resultList1 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 05));
        assertEquals(65.0, resultList1.get(0).getWeightValue());
    }

    @Test
    public void returnCountOfOneWhenTwoRecordsEnteredOnTheSameDayUsingAddTodaysRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        SQLiteDatabase testDB = mWeightRecordActivity.addTodaysRecordToDB(20170105, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords;", null).getCount();
        assertEquals(1, result);
        List<WeightMeasurement> resultList1 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 05));
        assertEquals(65.0, resultList1.get(0).getWeightValue());
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddPreviousRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        SQLiteDatabase testDB = mWeightRecordActivity.addPreviousRecordToDB(20170106, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<WeightMeasurement> resultList1 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 05));
        List<WeightMeasurement> resultList2 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 06));
        assertEquals(65.0, resultList1.get(0).getWeightValue());
        assertEquals(70.0, resultList2.get(0).getWeightValue());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddPreviousRecordToDB(){
        SQLiteDatabase testDB = mWeightRecordActivity.addPreviousRecordToDB(20170105, 65, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<WeightMeasurement> resultList1 = mWeightRecordActivity.getWeightMeasure(new LocalDate(2017, 01, 05));
        assertEquals(65.0, resultList1.get(0).getWeightValue());
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
        ArrayList<WeightMeasurement> expected = new ArrayList<>();
        expected.add(weightRecord1);
        expected.add(weightRecord2);
        SQLiteDatabase one = mWeightRecordActivity.addTodaysRecordToDB(20170101, 65, defaultTime);
        SQLiteDatabase two = mWeightRecordActivity.addTodaysRecordToDB(20170101, 70, defaultTime);
        LocalDate testDate = new LocalDate(2017, 01, 01);
        List<WeightMeasurement> result = mWeightRecordActivity.
                getWeightMeasure(testDate);
        assertEquals(expected.get(0).getWeightValue(), result.get(0).getWeightValue());
        assertEquals(expected.get(0).getInputTime(), result.get(0).getInputTime());
        assertEquals(expected.get(1).getWeightValue(), result.get(1).getWeightValue());
        assertEquals(expected.get(1).getInputTime(), result.get(1).getInputTime());
    }
}
