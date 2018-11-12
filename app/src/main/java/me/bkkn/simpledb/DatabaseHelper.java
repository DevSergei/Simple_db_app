package me.bkkn.simpledb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

// Класс установки базы данных, создать базу даных, если ее нет, проапгрейдить ее
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    private static final int DATABASE_VERSION = 2; // версия базы данных

    public static final String TABLE_WEATHER = "weather"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_CITY = "city";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // вызывается при попытке доступа к базе данных, но когда еще эта база данных не создана
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WEATHER + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TEMP + " TEXT," +
                COLUMN_CITY + " TEXT);");
    }

    // вызывается, когда необходимо обновление базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        if ((oldVersion == 1) && (newVersion == 2)) {
            String upgradeQuery = "ALTER TABLE " + TABLE_WEATHER + " ADD COLUMN " +
                    COLUMN_TEMP + " TEXT DEFAULT '18 C'";
            db.execSQL(upgradeQuery);
        }
    }
}
