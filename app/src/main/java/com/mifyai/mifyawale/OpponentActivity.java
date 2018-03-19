package com.mifyai.mifyawale;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent);

        Typeface outOfAfricaTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/out_of_africa.ttf");

        TextView awaleTitleTextView = (TextView) findViewById(R.id.awale_title);
        awaleTitleTextView.setTypeface(outOfAfricaTypeFace);

        Typeface africanTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/african.ttf");

        Button ObfGameButton = (Button) findViewById(R.id.obf_button);
        ObfGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(getApplicationContext(), PlayActivity.class);
              intent.putExtra("ia","1");
                startActivity(intent);
            }
        });

        Button SlhGameButton = (Button) findViewById(R.id.slh_button);
        SlhGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("ia","3");
                startActivity(intent);
            }
        });

        Button MlvelocityGameButton = (Button) findViewById(R.id.MlVelocity_button);
        MlvelocityGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("ia","2");
                startActivity(intent);
            }
        });
    }

}
