package mmkeri.quicksugars;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import org.joda.time.*;
/**
 * Created by mmkeri on 19/06/2017.
 */

public class BloodSugarMeasurementObjectShould {

    private BloodSugarMeasurement testBSMeasure;
    private LocalTime jdt;
    private int dateAsInt = 20170615;

    @Before
    public void setUp(){
        jdt = new LocalTime();
        testBSMeasure = new BloodSugarMeasurement(13.5, jdt, dateAsInt);
    }

    @Test
    public void giveTheTimeOfBloodSugarCheck(){
        assertEquals(jdt, testBSMeasure.getTimeOfBloodSugarCheck());
    }

    @Test
    public void giveTheValueOfTheBloodSugarCheck(){
        assertEquals(13.5, testBSMeasure.getBloodSugarReading(), 0.0);
    }

     @Test
    public void giveTheSameTimeForOriginalJodaObjectAndOneMadeFromItsString(){
         String time = jdt.toString();
         int hour = jdt.getHourOfDay();
         int minutes = jdt.getMinuteOfHour();
         int seconds = jdt.getSecondOfMinute();
         String expected = "" + hour + ":" + minutes + ":" + seconds + "";
         LocalTime other = LocalTime.parse(time);
         assertEquals(other, jdt);
     }
}
