package moe.ibox.parkingmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "plates_lib.db";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE plates(rfid text, plate text, approach_time datetime, approached boolean default 0 not null)");
        ContentValues plate1 = new ContentValues();
        plate1.put("rfid", "E2 00 00 1D 33 0E 00 95 24 80 42 8B");
        plate1.put("plate", "渝C KG74X");
        plate1.put("approach_time", "");
        ContentValues plate2 = new ContentValues();
        plate2.put("rfid", "E2 00 00 1D 33 0E 00 98 24 80 43 B5");
        plate2.put("plate", "川B Z2PKI");
        plate2.put("approach_time", "");
        ContentValues plate3 = new ContentValues();
        plate3.put("rfid", "E2 00 00 1D 33 0E 01 40 25 50 6E 9A");
        plate3.put("plate", "苏E Z09F9");
        plate3.put("approach_time", "");
        db.insert("plates", null, plate3);
        db.insert("plates", null, plate2);
        db.insert("plates", null, plate1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
