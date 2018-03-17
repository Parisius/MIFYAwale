package com.mifyai.mifyawale;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class OpponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent);

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
