package mmkeri.quicksugars;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import mmkeri.quicksugars.utils.DBTestUtils;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 17/07/2017.
 */

public class GraphsShould {

    @Rule
    public ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(BloodSugarEntry.class);

    private MyDBHandler testHandler;
    private SQLiteDatabase testDB;
    private LocalDate pastDay1 = new LocalDate(2017, 4, 19);
    private LocalDate pastDay2 = new LocalDate(2017, 5, 19);
    private LocalDate pastDay3 = new LocalDate(2017, 6, 10);
    private DayLogObject day1 = new DayLogObject(pastDay1);
    private DayLogObject day2 = new DayLogObject(pastDay2);
    private DayLogObject day3 = new DayLogObject(pastDay3);
    private String intervalDateAsString = "20170417";

    @Before
    public void setUp(){

        //Context context = InstrumentationRegistry.getContext();
        Context context = InstrumentationRegistry.getTargetContext();
        testHandler = DBTestUtils.resetDatabase(context);
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        testHandler.deleteDatabase();
        testDB = null;
    }

    @Test
    public void returnThreeRecordsWhenFirstStartingUp(){
        SQLiteDatabase db = testHandler.getReadableDatabase();
        testHandler.putLogRecord(day1);
        testHandler.putLogRecord(day2);
        testHandler.putLogRecord(day3);
        Cursor cursor = db.rawQuery("SELECT * FROM logRecords where id >= ? ;", new String[]{intervalDateAsString});
        int result = cursor.getCount();
        assertEquals(3, result);
    }
}
