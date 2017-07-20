package mmkeri.quicksugars;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class SymptomRecordShould {

    private SymptomRecord testSymptomRecord;
    private LocalTime testTime;
    private int dateAsInt = 20170601;

    @Before
    public void setUp(){
        testTime = new LocalTime();
        testSymptomRecord = new SymptomRecord("Pain", testTime, dateAsInt);
    }

    @Test
    public void returnTheCorrectSymptomWhenGetSymptomValueIsCalled(){
        String result = testSymptomRecord.getSymptomValue();
        assertEquals("Pain", result);
    }

    @Test
    public void returnTheCorrectTimeWhenGetSymptomInputTimeIsCaller(){
        LocalTime result = testSymptomRecord.getSymptomInputTime();
        assertEquals(testTime, result);
    }

    @Test
    public void returnTheCorrectDateAsAnIntegerWhenGetDateAsIntIsCalled(){
        int result = testSymptomRecord.getDateAsInt();
        assertEquals(20170601, result);
    }
}
