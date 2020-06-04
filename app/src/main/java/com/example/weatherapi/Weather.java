package com.example.weatherapi;

public class Weather {
    private String time,text,icon,winddir;
    private Double temp,windmph;
    private int isday,humidity,chanceofrain;

    public Weather(String time, String text, String icon, String winddir, Double temp, Double windmph, int isday, int humidity, int chanceofrain) {
        this.time = time;
        this.text = text;
        this.icon = icon;
        this.winddir = winddir;
        this.temp = temp;
        this.windmph = windmph;
        this.isday = isday;
        this.humidity = humidity;
        this.chanceofrain = chanceofrain;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWinddir() {
        return winddir;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getWindmph() {
        return windmph;
    }

    public void setWindmph(Double windmph) {
        this.windmph = windmph;
    }

    public int getIsday() {
        return isday;
    }

    public void setIsday(int isday) {
        this.isday = isday;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getChanceofrain() {
        return chanceofrain;
    }

    public void setChanceofrain(int chanceofrain) {
        this.chanceofrain = chanceofrain;
    }
}
