package org.tqs.deti.ua.homework.entities;

public class ForecastDay {
    private String forecastDate;
    private String tMin;
    private String tMax;
    private String precipitaProb;
    private String predWindDir;

    public ForecastDay() {
    }

    // Getters and Setters
    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getTMin() {
        return tMin;
    }

    public void setTMin(String tMin) {
        this.tMin = tMin;
    }

    public String getTMax() {
        return tMax;
    }

    public void setTMax(String tMax) {
        this.tMax = tMax;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    @Override
    public String toString() {
        return "ForecastDay{" +
                "forecastDate='" + forecastDate + '\'' +
                ", tMin='" + tMin + '\'' +
                ", tMax='" + tMax + '\'' +
                ", precipitaProb='" + precipitaProb + '\'' +
                ", predWindDir='" + predWindDir + '\'' +
                '}';
    }
}
