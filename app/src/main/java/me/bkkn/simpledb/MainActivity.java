package me.bkkn.simpledb;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WeatherDataSource weatherDataSource;     // Источник данных
    private WeatherDataReader weatherDataReader;      // Читатель данных
    private WeatherAdapter adapter;                // Адаптер для RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDataSource();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WeatherAdapter(weatherDataReader);
        adapter.setOnMenuItemClickListener(new WeatherAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemEditClick(DataItem dataItem) {
                editElement(dataItem);
            }

            @Override
            public void onItemDeleteClick(DataItem dataItem) {
                deleteElement(dataItem);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initDataSource() {
        weatherDataSource = new WeatherDataSource(getApplicationContext());
        weatherDataSource.open();
        weatherDataReader = weatherDataSource.getWeatherDataReader();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // обработка нажатий
        switch (item.getItemId()) {
            case R.id.menu_add:
                addElement();
                return true;
            case R.id.menu_clear:
                clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearList() {
        weatherDataSource.deleteAll();
        dataUpdated();
    }

    private void addElement() {
        // Выведем диалоговое окно для ввода новой записи
        LayoutInflater factory = LayoutInflater.from(this);
        // alertView пригодится в дальнейшем для поиска пользовательских элементов
        final View alertView = factory.inflate(R.layout.layout_add_weather, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.setTitle(R.string.alert_title_add);
        builder.setNegativeButton(R.string.alert_cancel, null);
        builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editTextTemp = alertView.findViewById(R.id.et_temp);
                EditText editTextCity = alertView.findViewById(R.id.et_city);
                // если использовать findViewById без alertView, то всегда будем получать
                // editText = null
                weatherDataSource.addDataItem(editTextCity.getText().toString(),
                        editTextTemp.getText().toString());
                dataUpdated();
            }
        });
        builder.show();
    }

    private void editElement(DataItem dataItem) {
        // Выведем диалоговое окно для ввода новой записи
        final LayoutInflater[] factory = {LayoutInflater.from(this)};
        // alertView пригодится в дальнейшем для поиска пользовательских элементов
        final View alertView = factory[0].inflate(R.layout.layout_add_weather, null);
        final String[] newCity = new String[1];
        final String[] newTemp = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.setTitle(R.string.alert_title_edit);
        builder.setNegativeButton(R.string.alert_cancel, null);
        builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editTextTemp = alertView.findViewById(R.id.et_temp);
                EditText editTextCity = alertView.findViewById(R.id.et_city);
                // если использовать findViewById без alertView, то всегда будем получать
                // editText = null
                newCity[0] = editTextCity.getText().toString();
                newTemp[0] = editTextTemp.getText().toString();
                weatherDataSource.addDataItem(newCity[0], newTemp[0]);
                dataUpdated();
            }
        });
        builder.show();
        weatherDataSource.editEntry(dataItem, newTemp[0], newCity[0]);
        dataUpdated();
    }

    private void deleteElement(DataItem dataItem) {
        weatherDataSource.deleteEntry(dataItem);
        dataUpdated();
    }

    private void dataUpdated() {
        weatherDataReader.Refresh();
        adapter.notifyDataSetChanged();
    }
}
