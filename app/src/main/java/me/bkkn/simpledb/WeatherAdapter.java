package me.bkkn.simpledb;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Адаптер для RecycleView
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    // Здесь нам нужен только читатель данных
    private final WeatherDataReader weatherDataReader;
    // Слушатель, будет устанавливаться извне
    private OnMenuItemClickListener itemMenuClickListener;

    public WeatherAdapter(WeatherDataReader weatherDataReader) {
        this.weatherDataReader = weatherDataReader;
    }

    // вызывается при создании новой карточки списка
    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаём новый элемент пользовательского интерфейса
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        // Здесь можно установить всякие параметры
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Привязываем данные к карточке
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        holder.bind(weatherDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return weatherDataReader.getCount();
    }

    // установка слушателя
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemMenuClickListener = onMenuItemClickListener;
    }

    // интерфейс для обработки меню
    public interface OnMenuItemClickListener {
        void onItemEditClick(DataItem dataItem);
        void onItemDeleteClick(DataItem dataItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView etWeatherItem;
        private DataItem dataItem;

        public ViewHolder(View itemView) {
            super(itemView);
            etWeatherItem = itemView.findViewById(R.id.textTitle);

            // при тапе на элементе - покажем  меню
            etWeatherItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemMenuClickListener != null)
                        showPopupMenu(etWeatherItem);
                }
            });
        }

        public void bind(DataItem dataItem) {
            this.dataItem = dataItem;
            etWeatherItem.setText(dataItem.getTemp());
        }

        private void showPopupMenu(View view) {
            // Покажем меню для элемента
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                // обработка выбора пункта меню
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // делегируем обработку слушателю
                    switch (item.getItemId()) {
                        case R.id.menu_edit:
                            itemMenuClickListener.onItemEditClick(dataItem);
                            return true;
                        case R.id.menu_delete:
                            itemMenuClickListener.onItemDeleteClick(dataItem);
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }
}
