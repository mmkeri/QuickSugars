package mmkeri.quicksugars;

import org.joda.time.LocalDate;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 20/07/2017.
 */

public class DateConversionShould {

    private LocalDate firstDate = new LocalDate(2017, 01, 01);

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
}
