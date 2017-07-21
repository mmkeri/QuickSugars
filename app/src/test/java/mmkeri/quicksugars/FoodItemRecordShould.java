package mmkeri.quicksugars;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 21/07/2017.
 */

public class FoodItemRecordShould {

    private FoodItemWithNutrients testFoodItemWithNutrients1;
    private FoodItemWithNutrients testFoodItemWithNutrients2;
    private FoodItemRecord testFoodItemRecord;
    private LocalDate testDate;
    private LocalTime testTime;

    @Before
    public void setUp() {
        testDate = new LocalDate();
        testTime = new LocalTime();
        testFoodItemWithNutrients1 = new FoodItemWithNutrients("Apple", "2.5", "15", "0.5", "85", "1145");
        testFoodItemRecord = new FoodItemRecord("apple", testTime, 20170601);
        testFoodItemRecord.setDetailedFoodItem(testFoodItemWithNutrients1);
    }

    @After
    public void cleanUp() {
        testFoodItemRecord = null;
        testFoodItemWithNutrients1 = null;
        testFoodItemWithNutrients2 = null;
    }

    @Test
    public void returnTrueWhenAFoodItemWithNutrientsHasBeenAddedToTheFoodItem() {
        testFoodItemRecord.setDetailedFoodItem(testFoodItemWithNutrients2);
        boolean result = testFoodItemRecord.isDetailedFoodItemSet();
        assertEquals(true, result);
    }

    @Test
    public void returnTheCorrectFoodItemWhenGetFoodItemIsCalled() {
        String result = testFoodItemRecord.getFoodItem();
        assertEquals("Apple", result);
    }

    @Test
    public void returnTheCorrectProteinValueWhenGetProteinIsCalled() {
        String result = testFoodItemRecord.getProtein();
        assertEquals("2.5", result);
    }

    @Test
    public void returnTheCorrectCarbohydrateValueWhenGetCarbohydrateIsCalled() {
        String result = testFoodItemRecord.getCarbohydrate();
        assertEquals("15", result);
    }

    @Test
    public void returnTheCorrectFatValueWhenGetFatIsCalled() {
        String result = testFoodItemRecord.getFat();
        assertEquals("0.5", result);
    }

    @Test
    public void returnTheCorrectKiloCalValueWhenGetKiloCalIsCalled(){
        String result = testFoodItemRecord.getKilocals();
        assertEquals("85", result);
    }

    @Test
    public void returnTheCorrectKiloJoulesValueWhenGetKiloJoulesIsCalled(){
        String result = testFoodItemRecord.getKilojoules();
        assertEquals("1145", result);
    }
}
