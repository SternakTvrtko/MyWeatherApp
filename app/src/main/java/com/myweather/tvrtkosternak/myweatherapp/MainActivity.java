package com.myweather.tvrtkosternak.myweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import Models.WeatherModel;
import Services.WeatherDataService;

public class MainActivity extends AppCompatActivity implements LocationListener{
    WeatherDataService weatherDataService=new WeatherDataService();
    String urlTemplate="http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=8c5a31a1d1144a3e27d506c3fbef59e3&mode=xml";
    String url;
    LocationManager locationManager;
    String mprovider;
    final Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {
                weatherDataService.update(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String latitude="0";
        String longitude="0";

        url=urlTemplate.replace("{lat}","0");
        url=url.replace("{lon}","0");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 0, 1000, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }

        /*thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initGui();*/
    }

    @Override
    public void onLocationChanged(Location location) {
        url=urlTemplate.replace("{lon}",Double.toString(location.getLongitude()));
        url=url.replace("{lat}", Double.toString(location.getLatitude()));
        Toast.makeText(getBaseContext(), "lon:"+Double.toString(location.getLongitude())+"  lat:"+Double.toString(location.getLatitude()), Toast.LENGTH_SHORT).show();

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initGui();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void initGui(){
        TextView location=(TextView) findViewById(R.id.LocationMain);
        TextView sunData=(TextView) findViewById(R.id.SunSetRiseMain);
        ScrollView mainWindow=(ScrollView) findViewById(R.id.activity_main);

        location.setText(weatherDataService.getLocation());
        sunData.setText("Sunrise: "+weatherDataService.getSunRise()+"\n"+"Sunset: "+weatherDataService.getSunSet());


        createWeatherButtons();

        String iconCode=weatherDataService.getWeathers().get(0).getWeatherIconId();
        mainWindow.setBackgroundResource(getApplicationContext().getResources().getIdentifier("drawable/b" + iconCode, null, getApplicationContext().getPackageName()));
    }


    private void createWeatherButtons(){
        TableLayout buttonTable=(TableLayout) findViewById(R.id.weatherButtonTable);
        buttonTable.removeAllViews();
        int id=0;
        for(WeatherModel w:weatherDataService.getWeathers()){
            TableRow row=new TableRow(this);

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            buttonTable.addView(row);
            row.addView(createWeatherButton(id,w));

            id++;
            if(id==10) break;
        }
    }

    private Button createWeatherButton(int id, final WeatherModel weather){
        Button weatherButton=new Button(this);

        weatherButton.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        ));

        String hourFrom=weather.getTimeFrom();
        hourFrom=hourFrom.substring(hourFrom.indexOf("T")+1,hourFrom.indexOf(":"));

        String hourTo=weather.getTimeTo();
        hourTo=hourTo.substring(hourTo.indexOf("T")+1,hourTo.indexOf(":"));

        String temperature=weather.getTemperatureValue();
        temperature=temperature+"°C";

        String type=weather.getSymbolName();

        weatherButton.setBackgroundResource(R.drawable.buttonstyle);

        weatherButton.setText(hourFrom+"-"+hourTo+"h    "+temperature);
        weatherButton.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        weatherButton.setTextSize(18);
        String iconCode=weather.getWeatherIconId();
        iconCode=iconCode.toLowerCase();
        weatherButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, getApplicationContext().getResources().getIdentifier("drawable/p" + iconCode, null, getApplicationContext().getPackageName()), 0);


        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailedWeatherScreen = new Intent(getApplicationContext(), SecondScreenActivity.class);

                detailedWeatherScreen.putExtra("timeFrom", weather.getTimeFrom());
                detailedWeatherScreen.putExtra("timeTo", weather.getTimeTo());
                detailedWeatherScreen.putExtra("tempValue",weather.getTemperatureValue()+"°C");
                detailedWeatherScreen.putExtra("tempMin", weather.getTemperatureMin()+"°C");
                detailedWeatherScreen.putExtra("tempMax", weather.getTemperatureMax()+"°C");
                detailedWeatherScreen.putExtra("precipType", weather.getPrecitipationType());
                detailedWeatherScreen.putExtra("precipVal", weather.getPrecitipationValue());
                detailedWeatherScreen.putExtra("windName", weather.getWindSpeedName());
                detailedWeatherScreen.putExtra("windVal", weather.getWindSpeedValue());
                detailedWeatherScreen.putExtra("windDirectionName", weather.getWindDirectionName());
                detailedWeatherScreen.putExtra("humidityVal", weather.getHumidityValue()+"%");
                detailedWeatherScreen.putExtra("pressureVal", weather.getPressureValue()+" "+weather.getPressureUnit());
                detailedWeatherScreen.putExtra("cloudsValue", weather.getCloudsValue());
                detailedWeatherScreen.putExtra("cloudsAll", weather.getCloudsAll()+"%");
                detailedWeatherScreen.putExtra("backgroundCode", weatherDataService.getWeathers().get(0).getWeatherIconId());

                startActivity(detailedWeatherScreen);
            }
        });


        return weatherButton;
    }


}
