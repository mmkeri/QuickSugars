package mmkeri.quicksugars.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import mmkeri.quicksugars.MyDBHandler;

/**
 * Created by mmkeri on 08/08/2017.
 */

public class DBTestUtils {
    public static MyDBHandler resetDatabase(Context context) {
        MyDBHandler testHandler = new MyDBHandler(context);

        // cause the database to be opened or created
        SQLiteDatabase db = testHandler.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_LOGS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_FOODS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_MEDICATIONS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MyDBHandler.TABLE_WEIGHTS);
        testHandler.onCreate(db);
        return  testHandler;
    }
}
