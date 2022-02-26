package moe.ibox.parkingmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import moe.ibox.parkingmanagement.entity.Plate;

public class PlatesDao {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private volatile static PlatesDao instance;

    private PlatesDao(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static PlatesDao getInstance(Context context) {
        if (instance == null) {
            synchronized (PlatesDao.class) {
                if (instance == null) {
                    instance = new PlatesDao(context);
                }
            }
        }
        return instance;
    }

    public int approach(String rfid, boolean approached) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("approached", approached ? 1 : 0);
        contentValues.put("approach_time", simpleDateFormat.format(new Date()));
        return db.update("plates", contentValues, "rfid is ?", new String[]{rfid});
    }

    public boolean isApproached(String rfid) {
        Cursor cursor = db.query("plates", new String[]{"approached"}, "rfid is ?", new String[]{rfid}, null, null, null);
        if (cursor.moveToFirst()) {
            int ret = cursor.getInt(cursor.getColumnIndexOrThrow("approached"));
            cursor.close();
            return ret == 1;
        }
        cursor.close();
        return false;
    }

    public Plate getPlateInfoByRfid(String rfid) throws ParseException {
        Cursor cursor = db.query("plates", null, "rfid is ?", new String[]{rfid}, null, null, null);
        if (cursor.moveToFirst()) {
            Plate ret = new Plate()
                    .setRfid(rfid)
                    .setPlate(cursor.getString(cursor.getColumnIndexOrThrow("plate")))
                    .setApproached(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow("approached"))))
                    .setApproachTime(simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("approach_time"))));
            cursor.close();
            return ret;
        } else {
            return null;
        }
    }
}
