package mmkeri.quicksugars;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by mmkeri on 24/07/2017.
 */

public class ValidateDatesShould {

    private String[] months = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    @Test
    public void convertFebruaryToTwoUsingConvertMonthToInt(){
        int result = ValidateDate.convertMonthToInt("February", months);
        assertEquals(2, result);
    }

    @Test
    public void returnFalseFor2017UsingDetermineLeapYearStatus(){
        assertFalse(ValidateDate.determineLeapYearStatus(2017));
    }

    @Test
    public void returnTrueFor2020UsingDetermineLeapYearStatus(){
        assertTrue(ValidateDate.determineLeapYearStatus(2020));
    }

    @Test
    public void returnFalseForFebruary31UsingTestValidDate(){
        assertFalse(ValidateDate.testValidDate(2017, 2, 31));
    }
}
