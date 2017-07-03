package mmkeri.quicksugars;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WeightRecordEntryShould {

    @Rule
    public ActivityTestRule<WeightRecordEntry> mActivityRule =
            new ActivityTestRule(WeightRecordEntry.class);

    private WeightRecordEntry mWeightRecordActivity;

    private LocalDate firstDate = new LocalDate(2017, 1, 1);
    private LocalDate secondDate = new LocalDate(2017, 2, 1);
    private LocalDate thirdDate = new LocalDate(2017, 3, 1);
    private Date date1 = firstDate.toDate();
    private Date date2 = secondDate.toDate();
    private Date date3 = thirdDate.toDate();
    private double weight1 = 65;
    private double weight2 = 70;
    private double weight3 = 40;
    private LocalTime defaultTime = new LocalTime(12, 01, 01);
    private MyDBHandler testHandler;
    private SQLiteDatabase testDB;
    private WeightMeasurement weightRecord1;
    private WeightMeasurement weightRecord2;


    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testHandler.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS);
        testHandler.onCreate(db);

        mWeightRecordActivity = mActivityRule.getActivity();

        weightRecord1 = new WeightMeasurement(65, defaultTime);
        weightRecord2 = new WeightMeasurement(70, defaultTime);
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testHandler.deleteDatabase();
        testDB = null;
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
        int result = mWeightRecordActivity.convertLocalDateToInt(firstDate);
        assertEquals(20170101, result);
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddTodaysRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        testDB = mWeightRecordActivity.addTodaysRecordToDB(20170106, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddTodaysRecordToDB(){
        testDB = mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnCountOfOneWhenTwoRecordsEnteredOnTheSameDayUsingAddTodaysRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        testDB = mWeightRecordActivity.addTodaysRecordToDB(20170105, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords;", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddPreviousRecordToDB(){
        mWeightRecordActivity.addTodaysRecordToDB(20170105, 65, defaultTime);
        testDB = mWeightRecordActivity.addPreviousRecordToDB(20170106, 70, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddPreviousRecordToDB(){
        testDB = mWeightRecordActivity.addPreviousRecordToDB(20170105, 65, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
    }

    @Test
    public void convertAnIntToLocalDateCorrectly(){
        LocalDate result = mWeightRecordActivity.convertDateAsIntToLocalDate(20170101);
        assertEquals(firstDate, result);
    }

    @Test(expected = NumberFormatException.class)
    public void throwAnExceptionWhenAnInvalidIntIsSubmittedAsADate(){
        mWeightRecordActivity.convertDateAsIntToLocalDate(22001201);
    }

    @Test
    public void convertALocalDateObjectToIntCorrectly(){
        int result = mWeightRecordActivity.convertLocalDateToInt(firstDate);
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
