package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.joda.time.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.google.gson.Gson;

public class MyDBHandlerShould extends AndroidTestCase{

    private MyDBHandler testDBHandler;
    private DayLogObject testLogObject;
    private LocalDate testDate;
    private LocalTime testTime;
    private SQLiteDatabase.CursorFactory cursorFactory;
    private BloodSugarMeasurement bsMeasure;
    private DayLogObject logObject;
    private SQLiteDatabase db;
    private static final String TEST_FILE_PREFIX = "test_";

    @Before
    public void setUp() throws Exception{
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
        testDate = new LocalDate();
        testTime = new LocalTime();
        testLogObject = new DayLogObject(testDate);
        bsMeasure = new BloodSugarMeasurement(12.5, testTime);
        logObject = new DayLogObject(testDate);
        testDBHandler = new MyDBHandler(context);
    }

    @After
    public void cleanUp() throws Exception{
        testDBHandler.deleteDatabase();
    }

}
