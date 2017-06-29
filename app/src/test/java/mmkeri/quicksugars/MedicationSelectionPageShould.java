package mmkeri.quicksugars;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class MedicationSelectionPageShould {
    /*
    private XmlPullParserFactory pullParserFactory;

    @Before
    public void setUp(){
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            //InputStream in_s = new InputStream(getClass().getResourceAsStream("Medications.xml"));
            InputStream in_s = getApplicationContext().getAssets().open("Medications.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
        */
}
