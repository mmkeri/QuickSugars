package mmkeri.quicksugars;

import org.joda.time.LocalTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class TimeConversionShould {

    private LocalTime testTimeObject = new LocalTime(12, 01, 01);
    private LocalTime minutesAfterMidnight = new LocalTime(00, 10, 00);
    private LocalTime secondsAfterMidnight = new LocalTime(00, 00, 10);
    private LocalTime midnight = new LocalTime(0, 0, 0);
    private int testTimeAsInt = 120101;

    @Test
    public void returnTheCorrectJodaTimeObjectForAnIntegerTimeValueSubmitted(){
        LocalTime result = TimeConversion.convertTimeAsIntToLocalTime(testTimeAsInt);
        assertEquals(testTimeObject, result);
    }

    @Test(expected = NumberFormatException.class)
    public void throwAnErrorWhenAnIntegerTimeWithAnIncorrectHourIsSubmitted(){
        TimeConversion.convertTimeAsIntToLocalTime(250101);
    }

    @Test(expected = NumberFormatException.class)
    public void throwAnErrorWhenAnIntegerTimeWithAnIncorrectMinuteIsSubmitted(){
        TimeConversion.convertTimeAsIntToLocalTime(126501);
    }

    @Test(expected = NumberFormatException.class)
    public void throwAnErrorWhenAnIntegerTimeWithAnIncorrectSecondIsSubmitted(){
        TimeConversion.convertTimeAsIntToLocalTime(120165);
    }

    @Test
    public void returnTheCorrectJodaTimeObjectWhenATimeMinutesAfterMidnightIsSubmitted(){
        LocalTime result = TimeConversion.convertTimeAsIntToLocalTime(1000);
        assertEquals(minutesAfterMidnight, result);
    }

    @Test
    public void returnTheCorrectJodaTimeObjectWhenATimeSecondsAfterMidnightIsSubmitted(){
        LocalTime result = TimeConversion.convertTimeAsIntToLocalTime(10);
        assertEquals(secondsAfterMidnight, result);
    }

    @Test
    public void returnTheCorrectJodaTimeObjectWhenZeroIsSubmittedRepresentingMidnight(){
        LocalTime result = TimeConversion.convertTimeAsIntToLocalTime(0);
        assertEquals(midnight, result);
    }
}
