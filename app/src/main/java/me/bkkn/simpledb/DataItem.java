package me.bkkn.simpledb;

// Класс - отражение строк из таблицы
public class DataItem {

    private long id;
    private String city;
    private String temp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String note) {
        this.city = note;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
