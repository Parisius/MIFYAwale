package com.mifyai.mifyawale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import view.ViewTools;

public class PlayActivity extends Activity {

    private static final int    INTENT_ID_KEY             = 0;
    /** End of the game dialog **/
    public static final int     END_OF_GAME_DIALOG        = 0;
    /** The adversary is starving dialog **/
    public static final int     IS_STARVING_DIALOG        = 1;
    /** The adversary starves dialog **/
    public static final int     WILL_STARVE_DIALOG        = 2;
    /** The computer has not found any choice dialog **/
    public static final int     COMPUTER_NO_CHOICE_DIALOG = 3;

    private static final String KEY_SIZE                  = "size";
    private static final String KEY_TERRITORY_0           = "territory_0";
    private static final String KEY_TERRITORY_1           = "territory_1";
    private static final String KEY_POINTS                = "points";
    private static final String KEY_CURRENT_SIDE          = "current_side";

    private AwaleView           awaleView;
    /** current awale */
    public Awale                awale;
    private GameManager         gameManager;
    private MediaPlayer winMediaPlayer;
    private MediaPlayer         loseMediaPlayer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       // ReportSender.install(getApplicationContext());
        //cancelPendingItent();
        short size = 6;
        this.awale = new Awale((short) size);
        int nIA=Integer.parseInt(getIntent().getStringExtra("ia"));
        this.gameManager = new GameManager(this, this.awale,nIA);
        View inflateView = ViewTools.inflateView(this, R.layout.awale_layout);
        this.awaleView = (AwaleView) inflateView.findViewById(R.id.territory);
        this.awaleView.setAwale(new Awale(this.awale));

        if(nIA==1)
        {
            this.awaleView.setIAName("OBF (IA) :");
        }

        if(nIA==2)
        {
            this.awaleView.setIAName("Mlvelocity (IA) :");
        }

        if(nIA==3)
        {
            this.awaleView.setIAName("SLH (IA) :");
        }


        setContentView(inflateView);
        restoreState(savedInstanceState);
        this.winMediaPlayer = MediaPlayer.create(this, R.raw.win);
        this.loseMediaPlayer = MediaPlayer.create(this, R.raw.lose);

        super.onCreate(savedInstanceState);
    }

    private void cancelPendingItent() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(INTENT_ID_KEY);
    }

    private String getPartyReport() {
        String text = new String();
        if (this.awale.points[0] > this.awale.points[1]) {
            if (this.winMediaPlayer != null) {
                this.winMediaPlayer.start();
            }
            text += getString(R.string.you_win);
        } else if (this.awale.points[0] < this.awale.points[1]) {
            if (this.loseMediaPlayer != null) {
                this.loseMediaPlayer.start();
            }
            text += getString(R.string.you_lose);
        } else {
            text += getString(R.string.it_is_a_draw);
        }
        text += "\n\n";
        text += getString(R.string.your_score) + " " + this.awale.points[0]
                + " " + getString(R.string.seeds) + "\n";
        text += getString(R.string.computer_score) + " " + this.awale.points[1]
                + " " + getString(R.string.seeds) + "\n";

        return text;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = null;
        Button button = null;
        String text = null;
        View inflateView = null;
        switch (id) {
            case END_OF_GAME_DIALOG:
                inflateView = ViewTools.inflateView(this, R.layout.dialog_layout);
                textView = (TextView) inflateView
                        .findViewById(R.id.textView_dialog);
                text = getPartyReport();
                textView.setText(text);

                button = (Button) inflateView.findViewById(R.id.button_dialog);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        awaleView.surfaceDestroyed(awaleView.getHolder());
                        PlayActivity.this.finish();
//                        startActivity(new Intent(PlayActivity.this, MainActivity.class));
                    }
                });

                builder.setTitle(getString(R.string.game_over))
                        .setCancelable(false).setView(inflateView);
                break;
            case IS_STARVING_DIALOG:
                inflateView = ViewTools.inflateView(this, R.layout.dialog_layout);
                textView = (TextView) inflateView
                        .findViewById(R.id.textView_dialog);
                button = (Button) inflateView.findViewById(R.id.button_dialog);
                textView.setText(getString(R.string.is_starving_the_opponent));
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismissDialog(IS_STARVING_DIALOG);
                    }
                });

                builder.setTitle(getString(R.string.game_over))
                        .setCancelable(false).setView(inflateView);
                break;
            case WILL_STARVE_DIALOG:
                inflateView = ViewTools.inflateView(this, R.layout.dialog_layout);
                textView = (TextView) inflateView
                        .findViewById(R.id.textView_dialog);
                button = (Button) inflateView.findViewById(R.id.button_dialog);
                textView.setText(getString(R.string.will_starve_the_opponent));
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismissDialog(WILL_STARVE_DIALOG);
                    }
                });
                builder.setCancelable(false).setView(inflateView);
                break;
            case COMPUTER_NO_CHOICE_DIALOG:
                textView = new TextView(this);
                text = getString(R.string.i_am_lost) + "\n";
                text += getPartyReport();
                textView.setText(text);
                builder.setView(textView).setCancelable(false).setPositiveButton(
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                break;
        }
        return builder.create();
    }

    @Override
    protected void onStop() {
        if (this.winMediaPlayer != null) {
            this.winMediaPlayer.release();
        }
        //notifyGame();
        awaleView.surfaceDestroyed(awaleView.getHolder());
        finish();
        super.onStop();
    }

    protected void onPause(){
        if (this.winMediaPlayer != null) {
            this.winMediaPlayer.release();
        }
        awaleView.surfaceDestroyed(awaleView.getHolder());
        finish();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle map) {

        if (map != null) {
            map.putShort(KEY_SIZE, this.awale.size);
            map.putShortArray(KEY_TERRITORY_0, this.awale.territory[0]);
            map.putShortArray(KEY_TERRITORY_1, this.awale.territory[1]);
            map.putShort(KEY_CURRENT_SIDE, this.awale.currentSide);
            map.putShortArray(KEY_POINTS, this.awale.points);
        }

        super.onSaveInstanceState(map);
    }

    @Override
    protected void onRestoreInstanceState(Bundle map) {
        restoreState(map);
        super.onRestoreInstanceState(map);
    }

    private void restoreState(Bundle map) {
        if (map != null) {
            this.awale.size = map.getShort(KEY_SIZE);
            this.awale.territory[0] = map.getShortArray(KEY_TERRITORY_0);
            this.awale.territory[1] = map.getShortArray(KEY_TERRITORY_1);
            this.awale.currentSide = map.getShort(KEY_CURRENT_SIDE);
            this.awale.points = map.getShortArray(KEY_POINTS);
        }
    }

    /**
     * Play on the given side and the given position.
     */
    public void play(short side, short position) {
        this.gameManager.play(side, position);
    }

    /**
     * Ends the game.
     */
    public void gameOver() {
        this.gameManager.gameOver();
    }

    @Override
    protected void onDestroy() {
        awaleView.surfaceDestroyed(awaleView.getHolder());
        cancelPendingItent();
        if (this.winMediaPlayer != null) {
            this.winMediaPlayer.release();
        }
        awaleView.surfaceDestroyed(awaleView.getHolder());
        super.onDestroy();
    }
}
