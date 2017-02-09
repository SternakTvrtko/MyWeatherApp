package com.myweather.tvrtkosternak.myweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Tvrtko on 5.2.2017..
 */

public class SecondScreenActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_secondscreen);

        initGui();
    }

    private void initGui(){
        RelativeLayout secondScreeen=(RelativeLayout) findViewById(R.id.activity_second);
        TextView time=(TextView) findViewById(R.id.TimeSecond);
        TextView tempVal=(TextView) findViewById(R.id.temperatureVal);
        TextView tempMin=(TextView) findViewById(R.id.temperatureMin);
        TextView tempMax=(TextView) findViewById(R.id.temperatureMax);
        TextView precType=(TextView) findViewById(R.id.precipitationType);
        TextView precValue=(TextView) findViewById(R.id.precipitationValue);
        TextView windName=(TextView) findViewById(R.id.windName);
        TextView windSpeed=(TextView) findViewById(R.id.windSpeed);
        TextView windDirection=(TextView) findViewById(R.id.windDirectionName);
        TextView humidityValue=(TextView) findViewById(R.id.humidityValue);
        TextView pressureVal=(TextView) findViewById(R.id.pressureValue);
        TextView cloudsVal=(TextView) findViewById(R.id.cloudsValue);
        TextView cloudsAll=(TextView) findViewById(R.id.cloudsAll);

        Intent i=getIntent();

        String hourFrom=i.getStringExtra("timeFrom");
        hourFrom=hourFrom.substring(hourFrom.indexOf("T")+1,hourFrom.indexOf(":"));

        String hourTo=i.getStringExtra("timeTo");
        hourTo=hourTo.substring(hourTo.indexOf("T")+1,hourTo.indexOf(":"));

        time.setText(hourFrom+"-"+hourTo+"h");
        tempVal.setText(i.getStringExtra("tempValue"));
        tempMin.setText("Min: "+i.getStringExtra("tempMin"));
        tempMax.setText("Max: "+i.getStringExtra("tempMax"));

        if(i.getStringExtra("precipType")==null){
            precType.setText("None");
            precValue.setText("-");
        } else {
            precType.setText(i.getStringExtra("precipType"));
            precValue.setText(i.getStringExtra("precipVal")+"mm");
        }


        windName.setText(i.getStringExtra("windName"));
        windSpeed.setText(i.getStringExtra("windVal")+" mph");
        windDirection.setText(i.getStringExtra("windDirectionName"));
        humidityValue.setText(i.getStringExtra("humidityVal"));
        pressureVal.setText(i.getStringExtra("pressureVal"));
        cloudsVal.setText(i.getStringExtra("cloudsValue"));
        cloudsAll.setText(i.getStringExtra("cloudsAll"));


        Button backButton=(Button) findViewById(R.id.back_button);
        backButton.setTextSize(18);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String iconCode=i.getStringExtra("backgroundCode");
        secondScreeen.setBackgroundResource(getApplicationContext().getResources().getIdentifier("drawable/b" + iconCode, null, getApplicationContext().getPackageName()));
    }
}
