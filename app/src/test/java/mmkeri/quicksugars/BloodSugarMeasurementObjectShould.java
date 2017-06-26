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
    private BloodSugarMeasurement testBSMeasure2;
    String testDateAndTime;
    private DateTime jdt;
    private SimpleDateFormat sdf;

    @Before
    public void setUp(){
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        jdt = new DateTime();
        testDateAndTime = sdf.format(new Date());
        testBSMeasure2 = new BloodSugarMeasurement(13.5, jdt);
        //testBSMeasure = new BloodSugarMeasurement(13.5, testDateAndTime);
    }

    @Test
    public void giveTheTimeOfBloodSugarCheck(){
        assertEquals(jdt, testBSMeasure2.getTimeOfBloodSugarCheck());
        //assertEquals(testDateAndTime, testBSMeasure.getTimeOfBloodSugarCheck());
    }

    @Test
    public void giveTheValueOfTheBloodSugarCheck(){
        assertEquals(13.5, testBSMeasure2.getBloodSugarReading(), 0.0);
    }
     @Test
    public void changeTheBloodSugarMeasurementFromCurrentValueToANewValue(){
         double oldBSValue = testBSMeasure2.getBloodSugarReading();
         double newBSValue = testBSMeasure2.changeBSReadingValue(oldBSValue, jdt, 15.5);
         System.out.println(testDateAndTime);
         assertEquals(oldBSValue, 13.5, 0.0);
         assertEquals(newBSValue, 15.5, 0.0);
     }

     @Test
    public void changeTheBloodSugarMeasurementAdministrationTime(){
         DateTime testTime = new DateTime(1498406153);
         DateTime newInputTime = testBSMeasure2.changeBSReadingTime(testBSMeasure2.getBloodSugarReading(), jdt, testTime);
         assertEquals(testTime, newInputTime);
     }
}
