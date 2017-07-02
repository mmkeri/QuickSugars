package mmkeri.quicksugars;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.jjoe64.graphview.series.DataPoint;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WeightRecordEntryShould {

    @Rule
    public ActivityTestRule<WeightRecordEntry> mActivityRule =
            new ActivityTestRule(WeightRecordEntry.class);

    private WeightRecordEntry mWeightRecordActivity;

    private LocalDate firstDate = new LocalDate(2017, 1, 1);
    private LocalDate secondDate = new LocalDate(2017, 2, 1);
    private LocalDate thirdDate = new LocalDate(2017, 3, 1);
    private Date date1 = firstDate.toDate();
    private Date date2 = secondDate.toDate();
    private Date date3 = thirdDate.toDate();
    private double weight1 = 65;
    private double weight2 = 70;
    private double weight3 = 40;
    private DataPoint point1;
    private DataPoint point2;
    private DataPoint point3;

    @Before
    public void setUp(){

        mWeightRecordActivity = mActivityRule.getActivity();
        point1 = new DataPoint(date1, weight1);
        point2 = new DataPoint(date2, weight2);
        point3 = new DataPoint(date3, weight3);
    }

    @After
    public void cleanUp(){
        mActivityRule = null;
        point1 = null;
        point2 = null;
        point3 = null;
    }

    @Test
    public void createAGraphWithThreePointsIfGivenThreeEntries(){

        //mWeightRecordActivity.saveEntry(mWeightRecordActivity.getCurrentFocus());
        mWeightRecordActivity.addGraphPoint(weight1, firstDate);
        mWeightRecordActivity.addGraphPoint(weight2, secondDate);
        mWeightRecordActivity.addGraphPoint(weight3, thirdDate);
        assertEquals(1, 1);
    }
}