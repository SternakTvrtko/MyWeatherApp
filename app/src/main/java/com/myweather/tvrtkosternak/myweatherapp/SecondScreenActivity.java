package com.myweather.tvrtkosternak.myweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tvrtko on 5.2.2017..
 */

public class SecondScreenActivity extends Activity{
    @BindView(R.id.activity_second) RelativeLayout secondScreeen;
    @BindView(R.id.TimeSecond) TextView time;
    @BindView(R.id.temperatureVal) TextView tempVal;
    @BindView(R.id.temperatureMin) TextView tempMin;
    @BindView(R.id.temperatureMax) TextView tempMax;
    @BindView(R.id.precipitationType) TextView precType;
    @BindView(R.id.precipitationValue) TextView precValue;
    @BindView(R.id.windName) TextView windName;
    @BindView(R.id.windSpeed) TextView windSpeed;
    @BindView(R.id.windDirectionName) TextView windDirection;
    @BindView(R.id.humidityValue) TextView humidityValue;
    @BindView(R.id.pressureValue) TextView pressureVal;
    @BindView(R.id.cloudsValue) TextView cloudsVal;
    @BindView(R.id.cloudsAll) TextView cloudsAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_secondscreen);
        ButterKnife.bind(this);

        initGui();
    }

    private void initGui(){

        Intent i=getIntent();

        String tempMinString=getString(R.string.MinTempStart)+i.getStringExtra(getString(R.string.tempMin));
        String tempMaxString=getString(R.string.MaxTempStart)+i.getStringExtra(getString(R.string.tempMax));

        String precipValueString=i.getStringExtra(getString(R.string.precipVal))+getString(R.string.milimeters);
        String windSpeedString=i.getStringExtra(getString(R.string.windVal))+getString(R.string.milesPerHour);

        String hourFrom=i.getStringExtra(getString(R.string.fromVal));
        hourFrom=hourFrom.substring(hourFrom.indexOf("T")+1,hourFrom.indexOf(":"));

        String hourTo=i.getStringExtra(getString(R.string.toVal));
        hourTo=hourTo.substring(hourTo.indexOf("T")+1,hourTo.indexOf(":"));

        time.setText(hourFrom+"-"+hourTo+"h");
        tempVal.setText(i.getStringExtra(getString(R.string.tempVal)));
        tempMin.setText(tempMinString);
        tempMax.setText(tempMaxString);

        if(i.getStringExtra(getString(R.string.precipType))==null){
            precType.setText(getString(R.string.noPrecipitationType));
            precValue.setText(getString(R.string.noPrecipitationValue));
        } else {
            precType.setText(i.getStringExtra(getString(R.string.precipType)));
            precValue.setText(precipValueString);
        }


        windName.setText(i.getStringExtra(getString(R.string.windName)));
        windSpeed.setText(windSpeedString);
        windDirection.setText(i.getStringExtra(getString(R.string.windDirectionName)));
        humidityValue.setText(i.getStringExtra(getString(R.string.humidityVal)));
        pressureVal.setText(i.getStringExtra(getString(R.string.pressureVal)));
        cloudsVal.setText(i.getStringExtra(getString(R.string.cloudsVal)));
        cloudsAll.setText(i.getStringExtra(getString(R.string.cloudsAll)));


        Button backButton=(Button) findViewById(R.id.back_button);
        backButton.setTextSize(18);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String iconCode=i.getStringExtra(getString(R.string.backgroundCode));
        secondScreeen.setBackgroundResource(getApplicationContext().getResources().getIdentifier("drawable/b" + iconCode, null, getApplicationContext().getPackageName()));
    }
}
