package me.bkkn.simpledb;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

//  Источник данных, позволяет изменять данные в таблице
// Создает и содержит в себе читатель данных
public class WeatherDataSource implements Closeable {

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private WeatherDataReader weatherDataReader;

    public WeatherDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Открывает базу данных
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        // создать читателя и открыть его
        weatherDataReader = new WeatherDataReader(database);
        weatherDataReader.open();
    }

    // Закрыть базу данных
    public void close() {
        weatherDataReader.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public DataItem addDataItem(String city, String temp) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CITY, temp);
        values.put(DatabaseHelper.COLUMN_TEMP, city);
        // Добавление записи
        long insertId = database.insert(DatabaseHelper.TABLE_WEATHER, null,
                values);
        DataItem newDataItem = new DataItem();
        newDataItem.setCity(temp);
        newDataItem.setTemp(city);
        newDataItem.setId(insertId);
        return newDataItem;
    }

    // Изменить запись
    public void editNote(DataItem dataItem, String temp, String city) {
        ContentValues editedNote = new ContentValues();
        editedNote.put(DatabaseHelper.COLUMN_ID, dataItem.getId());
        editedNote.put(DatabaseHelper.COLUMN_TEMP, temp);
        editedNote.put(DatabaseHelper.COLUMN_CITY, city);
        // изменение записи
        database.update(DatabaseHelper.TABLE_WEATHER,
                editedNote,
                DatabaseHelper.COLUMN_ID + " = " + dataItem.getId(),
                null);
    }

    // Удалить запись
    public void deleteNote(DataItem dataItem) {
        long id = dataItem.getId();
        database.delete(DatabaseHelper.TABLE_WEATHER, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    // Очистить таблицу
    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_WEATHER, null, null);
    }

    // вернуть читателя (он потребуется в других местах)
    public WeatherDataReader getWeatherDataReader() {
        return weatherDataReader;
    }
}
