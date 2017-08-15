package mmkeri.quicksugars;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class BloodSugarEntryShould {

    @Rule
    public final ActivityTestRule<BloodSugarEntry> mActivityRule =
            new ActivityTestRule(BloodSugarEntry.class);

    private final LocalTime testTime = new LocalTime(12, 12, 12);

    private BloodSugarEntry mBloodSugarEntry;
    private MyDBHandler testHandler;
    private BloodSugarMeasurement bsEntry1;
    private BloodSugarMeasurement bsEntry2;


    @Before
    public void setUp(){

        final int firstDateAsInt = 20170101;
        final int secondDateAsInt = 20170201;
        final Context context = InstrumentationRegistry.getTargetContext();

        testHandler = DBTestUtils.resetDatabase(context);
        mBloodSugarEntry = mActivityRule.getActivity();
        bsEntry1 = new BloodSugarMeasurement(12.2, testTime, firstDateAsInt);
        bsEntry2 = new BloodSugarMeasurement(14.4, testTime, secondDateAsInt);
    }

    @After
    public void cleanUp(){
        testHandler.deleteDatabase();
        mBloodSugarEntry = null;
        bsEntry1 = null;
        bsEntry2 = null;
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddTodaysRecordToDB(){
        mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        mBloodSugarEntry.addTodaysReadingToDB(20170106, 14.4, testTime);
        SQLiteDatabase testDB = mBloodSugarEntry.addTodaysReadingToDB(20170203, 15.2, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(3, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        List<BloodSugarMeasurement> resultList2 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 06));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
        assertEquals(14.4, resultList2.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddTodaysRecordToDB(){
        SQLiteDatabase testDB = mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenTwoRecordsEnteredOnTheSameDayUsingAddTodaysRecordToDB(){
        mBloodSugarEntry.addTodaysReadingToDB(20170105, 12.2, testTime);
        SQLiteDatabase testDB = mBloodSugarEntry.addTodaysReadingToDB(20170105, 14.4, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords;", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
    }

    @Test
    public void returnTheCorrectCountWhenTwoRecordsAddedUsingAddPreviousRecordToDB(){
        mBloodSugarEntry.addPreviousReadingToDB(20170105, 12.2, testTime);
        SQLiteDatabase testDB = mBloodSugarEntry.addPreviousReadingToDB(20170106, 14.4, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(2, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        List<BloodSugarMeasurement> resultList2 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 06));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
        assertEquals(14.4, resultList2.get(0).getBloodSugarReading());
    }

    @Test
    public void returnCountOfOneWhenASingleRecordIsAddedUsingAddPreviousRecordToDB(){
        SQLiteDatabase testDB = mBloodSugarEntry.addPreviousReadingToDB(20170105, 12.2, testTime);
        int result = testDB.rawQuery("SELECT * FROM logRecords", null).getCount();
        assertEquals(1, result);
        List<BloodSugarMeasurement> resultList1 = mBloodSugarEntry.getBSReading(new LocalDate(2017, 01, 05));
        assertEquals(12.2, resultList1.get(0).getBloodSugarReading());
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
    /*
    @Test
    public void sendAndIntentToTheVoiceRecognizerWhenVoiceButtonTapped(){
        onView(withId(R.id.btn_speak)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_CALL),
                toPackage(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)));
    }
    */
}
