package com.myweather.tvrtkosternak.myweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import Models.WeatherModel;
import Services.WeatherDataService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocationListener{
    @BindView(R.id.LocationMain) TextView location;
    @BindView(R.id.SunSetRiseMain) TextView sunData;
    @BindView(R.id.activity_main) ScrollView mainWindow;
    @BindView(R.id.weatherButtonTable) TableLayout buttonTable;
    @BindView(R.id.loadingPanel) RelativeLayout loadingPanel;
    @BindView(R.id.loadingPanelText) RelativeLayout loadingPanelText;

    WeatherDataService weatherDataService=new WeatherDataService();
    String url;
    LocationManager locationManager;

    public class WeatherUpdater extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            weatherDataService.update(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, getString(R.string.updatingMsg), Toast.LENGTH_SHORT ).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void s) {
            initGui();
            Toast.makeText(MainActivity.this, getString(R.string.updateDoneMsg), Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!(this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, this);
        } else {

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        url=getString(R.string.urltemplate);
        url=url.replace(getString(R.string.latitudeToken), Double.toString(location.getLatitude()));
        url=url.replace(getString(R.string.longitudeToken), Double.toString(location.getLongitude()));

        WeatherUpdater updater=new WeatherUpdater();

        loadingPanel.setVisibility(View.GONE);
        loadingPanelText.setVisibility(View.GONE);

        updater.execute(url);

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
        location.setText(weatherDataService.getLocation());
        sunData.setText("Sunrise: "+weatherDataService.getSunRise()+"\n"+"Sunset: "+weatherDataService.getSunSet());


        createWeatherButtons();

        String iconCode=weatherDataService.getWeathers().get(0).getWeatherIconId();
        mainWindow.setBackgroundResource(getApplicationContext().getResources().getIdentifier("drawable/b" + iconCode, null, getApplicationContext().getPackageName()));
    }


    private void createWeatherButtons(){
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
            row.addView(createWeatherButton(w));

            id++;
            if(id==10) break;
        }
    }

    private Button createWeatherButton(final WeatherModel weather){
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
