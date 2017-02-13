package com.myweather.tvrtkosternak.myweatherapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Models.WeatherModel;
import Services.ButtonsAdapter;
import Services.WeatherDataService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocationListener{

    @BindView(R.id.LocationMain) TextView location;
    @BindView(R.id.SunSetRiseMain) TextView sunData;
    @BindView(R.id.activity_main) RelativeLayout mainWindow;
    @BindView(R.id.loadingPanel) RelativeLayout loadingPanel;

    @BindView(R.id.loadingPanelText) RelativeLayout loadingPanelText;
    @BindView(R.id.rvButtons) RecyclerView buttonsRecycleTable;

    WeatherDataService weatherDataService=new WeatherDataService();
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

        if(!(this.checkCallingOrSelfPermission(getString(R.string.access_location_req))==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
        } else {
            Toast.makeText(this, getString(R.string.permisson_denied), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String url=getString(R.string.urltemplate);
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
        Dialog dialog=createGPSDialog();
        try {
            dialog.show();
        } catch (Exception e){

        }
    }

    private void initGui(){
        location.setText(weatherDataService.getLocation());
        sunData.setText(getString(R.string.sunrise)+weatherDataService.getSunRise()+"\n"+getString(R.string.sunset)+weatherDataService.getSunSet());


        createWeatherButtons();

        String iconCode=weatherDataService.getWeathers().get(0).getWeatherIconId();
        mainWindow.setBackgroundResource(getApplicationContext().getResources().getIdentifier("drawable/b" + iconCode, null, getApplicationContext().getPackageName()));
    }


    private void createWeatherButtons(){
        List<Button> buttonsList=new ArrayList<>();
        int id=0;
        for(WeatherModel w:weatherDataService.getWeathers()){
            buttonsList.add(createWeatherButton(w));
            id++;
            if(id==10) break;
        }

        ButtonsAdapter adapter = new ButtonsAdapter(this, buttonsList);
        buttonsRecycleTable.setAdapter(adapter);
        buttonsRecycleTable.setLayoutManager(new LinearLayoutManager(this));
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
        temperature=temperature+getString(R.string.celsius);

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

                detailedWeatherScreen.putExtra(getString(R.string.fromVal), weather.getTimeFrom());
                detailedWeatherScreen.putExtra(getString(R.string.toVal), weather.getTimeTo());
                detailedWeatherScreen.putExtra(getString(R.string.tempVal),weather.getTemperatureValue()+getString(R.string.celsius));
                detailedWeatherScreen.putExtra(getString(R.string.tempMin), weather.getTemperatureMin()+getString(R.string.celsius));
                detailedWeatherScreen.putExtra(getString(R.string.tempMax), weather.getTemperatureMax()+getString(R.string.celsius));
                detailedWeatherScreen.putExtra(getString(R.string.precipType), weather.getPrecitipationType());
                detailedWeatherScreen.putExtra(getString(R.string.precipVal), weather.getPrecitipationValue());
                detailedWeatherScreen.putExtra(getString(R.string.windName), weather.getWindSpeedName());
                detailedWeatherScreen.putExtra(getString(R.string.windVal), weather.getWindSpeedValue());
                detailedWeatherScreen.putExtra(getString(R.string.windDirectionName), weather.getWindDirectionName());
                detailedWeatherScreen.putExtra(getString(R.string.humidityVal), weather.getHumidityValue()+"%");
                detailedWeatherScreen.putExtra(getString(R.string.pressureVal), weather.getPressureValue()+" "+weather.getPressureUnit());
                detailedWeatherScreen.putExtra(getString(R.string.cloudsVal), weather.getCloudsValue());
                detailedWeatherScreen.putExtra(getString(R.string.cloudsAll), weather.getCloudsAll()+"%");
                detailedWeatherScreen.putExtra(getString(R.string.backgroundCode), weatherDataService.getWeathers().get(0).getWeatherIconId());

                startActivity(detailedWeatherScreen);
            }
        });


        return weatherButton;
    }

    public Dialog createGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.dialog_gps_not_on)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        return builder.create();
    }

}
