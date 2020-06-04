package com.example.weatherapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private int mresource;
    private Context mcontext;

    public WeatherAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
        mresource = resource;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);

        Weather weather = getItem(position);

        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
        TextView tvTemp = (TextView) convertView.findViewById(R.id.tvTemp);

        tvDate.setText(weather.getTime());
        tvStatus.setText(weather.getText());
        tvTemp.setText(weather.getTemp()+"° C");
        String icon = "https:" + weather.getIcon();
        Glide.with(mcontext).load(icon).into(ivIcon);

        return  convertView;
    }
}
