package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.joda.time.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

public class MyDBHandlerShould extends AndroidTestCase{

    private MyDBHandler testDBHandler;
    private DayLogObject testLogObject;
    private LocalDate testDate;
    private LocalTime testTime;
    private SQLiteDatabase.CursorFactory cursorFactory;
    private BloodSugarMeasurement bsMeasure;
    private DayLogObject logObject;

    @Before
    public void setUp() throws Exception{
        testDate = new LocalDate();
        testTime = new LocalTime();
        testLogObject = new DayLogObject(testDate);
        bsMeasure = new BloodSugarMeasurement(12.5, testTime);
        logObject = new DayLogObject(testDate);
    }

    @After
    public void cleanUp() throws Exception{
        testDBHandler.close();
    }
    /*
    public void testDropDB(){
        assertTrue(mContext.deleteDatabase(MyDBHandler.TABLE_LOGS));
    }
    */
    /*
    public void testCreateDB(){
        testDBHandler = new MyDBHandler(mContext);
        SQLiteDatabase db = testDBHandler.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }
    */
    public void serializeAndDeserializeObjectCorrectly(){
        Gson gson = new Gson();
        String serialResult = gson.toJson(logObject, DayLogObject.class);
        DayLogObject result = gson.fromJson(serialResult, DayLogObject.class);
        assertEquals(logObject.getId(), result.getId());
    }
}
