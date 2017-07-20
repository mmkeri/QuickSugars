package mmkeri.quicksugars;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class WeightMeasurementObjectShould {

    private WeightMeasurement testWeightMeasurement;
    private LocalTime testTime;
    private int dateAsInt = 20170601;

    @Before
    public void setUp(){
        testTime = new LocalTime();
        testWeightMeasurement = new WeightMeasurement(65, testTime, dateAsInt);
    }

    @Test
    public void returnTheCorrectWeightWhenGetWeightValueIsCalled(){
        double result = testWeightMeasurement.getWeightValue();
        assertEquals(65.0, result);
    }

    @Test
    public void returnTheCorrectTimeWhenGetInputTimeIsCalled(){
        LocalTime result = testWeightMeasurement.getInputTime();
        assertEquals(testTime, result);
    }

    @Test
    public void returnTheCorrectDateValueWhenGetDateAsIntIsCalled(){
        int result = testWeightMeasurement.getDateAsInt();
        assertEquals(20170601, result);
    }
}
