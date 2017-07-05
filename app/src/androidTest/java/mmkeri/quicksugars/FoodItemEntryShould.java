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

public class FoodItemEntryShould {

    @Rule
    public ActivityTestRule<FoodItemEntry> mActivityRule =
            new ActivityTestRule(FoodItemEntry.class);

    private FoodItemEntry mFoodRecordActivity;

    private LocalDate firstDate = new LocalDate(2017, 1, 1);
    private LocalDate secondDate = new LocalDate(2017, 2, 1);
    private LocalDate thirdDate = new LocalDate(2017, 3, 1);
    private Date date1 = firstDate.toDate();
    private Date date2 = secondDate.toDate();
    private Date date3 = thirdDate.toDate();
    private String item1 = "Bannana";
    private String item2 = "Apple";
    private String item3 = "Orange";
    private LocalTime defaultTime = new LocalTime(12, 01, 01);
    private MyDBHandler testHandler;
    private SQLiteDatabase testDB;
    private FoodItemRecord foodItemRecord1;
    private FoodItemRecord foodItemRecord2;


    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testHandler.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS);
        testHandler.onCreate(db);

        mFoodRecordActivity = mActivityRule.getActivity();

        foodItemRecord1 = new FoodItemRecord(item1, defaultTime);
        foodItemRecord2 = new FoodItemRecord(item2, defaultTime);
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testHandler.deleteDatabase();
        testDB = null;
    }

    @Test
    public void convertDateToIntCorrectly(){
        int result = DateConversion.convertLocalDateToInt(firstDate);
        assertEquals(20170101, result);
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddTodaysRecordToDB(){
        mFoodRecordActivity.addTodaysRecordToDB(20170105, item1, defaultTime);
        testDB = mFoodRecordActivity.addTodaysRecordToDB(20170106, item2, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<FoodItemRecord> resultList1 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 05));
        List<FoodItemRecord> resultList2 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 06));
        assertEquals(item1, resultList1.get(0).getFoodItem());
        assertEquals(item2, resultList2.get(0).getFoodItem());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddTodaysRecordToDB(){
        testDB = mFoodRecordActivity.addTodaysRecordToDB(20170105, item1, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<FoodItemRecord> resultList1 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 05));
        assertEquals(item1, resultList1.get(0).getFoodItem());
    }

    @Test
    public void returnCountOfOneWhenTwoRecordsEnteredOnTheSameDayUsingAddTodaysRecordToDB(){
        mFoodRecordActivity.addTodaysRecordToDB(20170105, item1, defaultTime);
        testDB = mFoodRecordActivity.addTodaysRecordToDB(20170105, item2, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords;", null).getCount();
        assertEquals(1, result);
        List<FoodItemRecord> resultList1 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 05));
        assertEquals(item1, resultList1.get(0).getFoodItem());
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddPreviousRecordToDB(){
        mFoodRecordActivity.addTodaysRecordToDB(20170105, item1, defaultTime);
        testDB = mFoodRecordActivity.addPreviousRecordToDB(20170106, item2, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<FoodItemRecord> resultList1 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 05));
        List<FoodItemRecord> resultList2 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 06));
        assertEquals(item1, resultList1.get(0).getFoodItem());
        assertEquals(item2, resultList2.get(0).getFoodItem());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddPreviousRecordToDB(){
        testDB = mFoodRecordActivity.addPreviousRecordToDB(20170105, item1, defaultTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<FoodItemRecord> resultList1 = mFoodRecordActivity.getFoodItems(new LocalDate(2017, 01, 05));
        assertEquals(item1, resultList1.get(0).getFoodItem());
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
        ArrayList<FoodItemRecord> expected = new ArrayList<>();
        expected.add(foodItemRecord1);
        expected.add(foodItemRecord2);
        SQLiteDatabase one = mFoodRecordActivity.addTodaysRecordToDB(20170101, item1, defaultTime);
        SQLiteDatabase two = mFoodRecordActivity.addTodaysRecordToDB(20170101, item2, defaultTime);
        LocalDate testDate = new LocalDate(2017, 01, 01);
        List<FoodItemRecord> result = mFoodRecordActivity.getFoodItems(testDate);
        assertEquals(expected.get(0).getFoodItem(), result.get(0).getFoodItem());
        assertEquals(expected.get(0).getInputTime(), result.get(0).getInputTime());
        assertEquals(expected.get(1).getFoodItem(), result.get(1).getFoodItem());
        assertEquals(expected.get(1).getInputTime(), result.get(1).getInputTime());
    }
}
