package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mmkeri on 21/07/2017.
 */

public class FoodItemWithNutrientsShould {

    private FoodItemWithNutrients testFoodItemWithNutrients;

    @Before
    public void setUp(){
        testFoodItemWithNutrients = new FoodItemWithNutrients("Banana", "9", "12", "0.1", "35", "125");
    }
    @After
    public void cleanUp(){
        testFoodItemWithNutrients = null;
    }

    @Test
    public void returnTheCorrectFoodItemNameWhenGetFoodNameIsCalled(){
        assertEquals("Banana", testFoodItemWithNutrients.getFoodName());
    }

    @Test
    public void returnTheCorrectProteinValueWhenGetProteinIsCalled(){
        assertEquals("9", testFoodItemWithNutrients.getProtein());
    }

    @Test
    public void returnTheCorrectCarbohydrateWhenGetCarbohydrateIsCalled(){
        assertEquals("12", testFoodItemWithNutrients.getCarbohydrate());
    }

    @Test
    public void returnTheCorrectFatValueWhenGetFatIsCalled(){
        assertEquals("0.1", testFoodItemWithNutrients.getFat());
    }

    @Test
    public void returnTheCorrectKiloCalValueWhenGetKiloCalsIsCalled(){
        assertEquals("35", testFoodItemWithNutrients.getKilocals());
    }

    @Test
    public void returnTheCorrectKiloJoulesWhenGetKiloJoulesIsCalled(){
        assertEquals("125", testFoodItemWithNutrients.getKilojoules());
    }
}
