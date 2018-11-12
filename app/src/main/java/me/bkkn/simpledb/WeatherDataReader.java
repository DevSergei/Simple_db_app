package me.bkkn.simpledb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

// Читатель источника данных на основе курсора
// Этот класс был вынесен из WeatherDataSource, чтобы разгрузить его
public class WeatherDataReader implements Closeable {

    private Cursor cursor;              // Курсор: фактически это подготовенный запрос,
                                        // но сами данные считываются только по необходимости
    private final SQLiteDatabase database;

    private final String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TEMP,
            DatabaseHelper.COLUMN_CITY
    };

    public WeatherDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open() {
        query();
        cursor.moveToFirst();
    }

    public void close() {
        cursor.close();
    }

    // Перечитать таблицу
    public void Refresh() {
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    // создание запроса
    private void query() {
        cursor = database.query(DatabaseHelper.TABLE_WEATHER,
                notesAllColumn, null, null,
                null, null, null);
    }

    // прочитать данные по определенной позиции
    public DataItem getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    // преобразователь курсора в объект
    private DataItem cursorToNote() {
        DataItem dataItem = new DataItem();
        dataItem.setId(cursor.getLong(0));
        dataItem.setCity(cursor.getString(1));
        dataItem.setTemp(cursor.getString(2));
        return dataItem;
    }
}
