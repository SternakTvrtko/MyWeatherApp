package Models;

import java.text.DecimalFormat;

/**
 * Created by Tvrtko on 6.2.2017..
 */

public class WeatherModel {
    private String timeFrom;
    private String timeTo;
    private String symbolNum;
    private String symbolName;
    private String precipitationUnit;
    private String precipitationValue;
    private String precipitationType;
    private String windDirectionCode;
    private String windDirectionName;
    private String windSpeedValue;
    private String windSpeedName;
    private String temperatureUnit;
    private String temperatureValue;
    private String temperatureMin;
    private String temperatureMax;
    private String pressureUnit;
    private String pressureValue;
    private String humidityValue;
    private String cloudsValue;
    private String cloudsAll;
    private String weatherIconId;


    public WeatherModel(String timeFrom, String timeTo, String symbolNum, String symbolName, String precitipationUnit,
                        String precitipationValue, String precitipationType, String windDirectionCode, String windDirectionName,
                        String windSpeedValue, String windSpeedName, String temperatureUnit, String temperatureValue,
                        String temperatureMin, String temperatureMax, String pressureUnit, String pressureValue,
                        String humidityValue, String cloudsValue, String cloudsAll, String weatherIconId) {
        super();
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.symbolNum = symbolNum;
        this.symbolName = symbolName;
        this.precipitationUnit = precitipationUnit;
        this.precipitationValue = precitipationValue;
        this.precipitationType = precitipationType;
        this.windDirectionCode = windDirectionCode;
        this.windDirectionName = windDirectionName;
        this.windSpeedValue = windSpeedValue;
        this.windSpeedName = windSpeedName;
        this.temperatureUnit = temperatureUnit;
        this.temperatureValue = checkKelvinBug(temperatureValue);
        this.temperatureMin = checkKelvinBug(temperatureMin);
        this.temperatureMax = checkKelvinBug(temperatureMax);
        this.pressureUnit = pressureUnit;
        this.pressureValue = pressureValue;
        this.humidityValue = humidityValue;
        this.cloudsValue = cloudsValue;
        this.cloudsAll = cloudsAll;
        this.weatherIconId=weatherIconId;
    }

    public String getTimeFrom() {
        return timeFrom;
    }
    public String getTimeTo() {
        return timeTo;
    }
    public String getSymbolNum() {
        return symbolNum;
    }
    public String getSymbolName() {
        return symbolName;
    }
    public String getPrecitipationUnit() {
        return precipitationUnit;
    }
    public String getPrecitipationValue() {
        return precipitationValue;
    }
    public String getPrecitipationType() {
        return precipitationType;
    }
    public String getWindDirectionCode() {
        return windDirectionCode;
    }
    public String getWindDirectionName() {
        return windDirectionName;
    }
    public String getWindSpeedValue() {
        return windSpeedValue;
    }
    public String getWindSpeedName() {
        return windSpeedName;
    }
    public String getTemperatureUnit() {
        return temperatureUnit;
    }
    public String getTemperatureValue() {
        return temperatureValue;
    }
    public String getTemperatureMin() {
        return temperatureMin;
    }
    public String getTemperatureMax() {
        return temperatureMax;
    }
    public String getPressureUnit() {
        return pressureUnit;
    }
    public String getPressureValue() {
        return pressureValue;
    }
    public String getHumidityValue() {
        return humidityValue;
    }
    public String getCloudsValue() {
        return cloudsValue;
    }
    public String getCloudsAll() {
        return cloudsAll;
    }
    public String getWeatherIconId() {
        return weatherIconId;
    }

    @Override
    public String toString() {
        return "WeatherModel [timeFrom=" + timeFrom + ", timeTo=" + timeTo + ", symbolNum=" + symbolNum
                + ", symbolName=" + symbolName + ", precipitationUnit=" + precipitationUnit + ", precipitationValue="
                + precipitationValue + ", precipitationType=" + precipitationType + ", windDirectionCode="
                + windDirectionCode + ", windDirectionName=" + windDirectionName + ", windSpeedValue=" + windSpeedValue
                + ", windSpeedName=" + windSpeedName + ", temperatureUnit=" + temperatureUnit + ", temperatureValue="
                + temperatureValue + ", temperatureMin=" + temperatureMin + ", temperatureMax=" + temperatureMax
                + ", pressureUnit=" + pressureUnit + ", pressureValue=" + pressureValue + ", humidityValue="
                + humidityValue + ", cloudsValue=" + cloudsValue + ", cloudsAll=" + cloudsAll + "]";
    }

    private String checkKelvinBug(String temp){
        Double temperature=Double.parseDouble(temp);
        DecimalFormat df = new DecimalFormat("#.##");

        if(temperature>200){
            temperature=temperature-273.15;
        }
        return df.format(temperature);
    }
}
