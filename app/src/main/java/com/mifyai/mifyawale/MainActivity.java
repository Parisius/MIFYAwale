package com.mifyai.mifyawale;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import utils.ReportSender;
import view.ViewTools;

/**
 * This activity display the main menu.
 *
 * @author Marie-Parisius HOUESSOU
 */
public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //ReportSender.install(getApplicationContext());
        setContentView(R.layout.activity_main);

       ViewTools.inflateView(this, R.layout.awale_layout);

        Typeface outOfAfricaTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/out_of_africa.ttf");

        TextView awaleTitleTextView = (TextView) findViewById(R.id.awale_title);
        awaleTitleTextView.setTypeface(outOfAfricaTypeFace);

        Typeface africanTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/african.ttf");

//        Button newGameButton = (Button) findViewById(R.id.new_game_button);
//        newGameButton.setTypeface(africanTypeFace);
//        newGameButton.setOnClickListener(new View.OnClickListener() {
//            @Override                                                                 //C'était l'action originelle. J'ai dupliqué et modifié en bas.
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, PlayActivity.class));
//            }
//        });

        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setTypeface(africanTypeFace);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OpponentActivity.class));
            }
        });

        Button rulesButton = (Button) findViewById(R.id.rules_button);
        rulesButton.setTypeface(africanTypeFace);
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
            }
        });

        Button creditsButton = (Button) findViewById(R.id.credits_button);
        creditsButton.setTypeface(africanTypeFace);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,
                        CreditsActivity.class));
            }
        });

        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            TextView demoVersionButton = (TextView) findViewById(R.id.demo_version);
//            demoVersionButton.setText(getString(R.string.demo_version) + " " + pi.versionName);
            demoVersionButton.setText("Version 1.0");
        } catch (PackageManager.NameNotFoundException e) {
        }
        super.onCreate(savedInstanceState);
    }
}
