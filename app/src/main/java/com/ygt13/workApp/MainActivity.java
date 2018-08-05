package com.ygt13.workApp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button baslatButton;
    Button setButton;
    EditText mola;
    EditText calisma;
    EditText set;
    TextView kronometre;
    int calismaSure;
    int molaSure;
    int setSayisi;
    int kalanSure;
    int kalanSet;
    boolean setAyarlandi=false;
    boolean counterIsPause=false;
    boolean counterIsActive=false;
    boolean breakIsActive;
    TextView setTextView;
    CountDownTimer countDownTimer;
    TextView bilgi;
    MediaPlayer mediaplayer;
    AudioManager audioManager;

    public void workOrBreak(){
        if(kalanSet>0){
            if(breakIsActive){
                kalanSet--;
                //set sayisini degistirmen ve view ini degistirmen gerek
                breakIsActive=false;
                bilgi.setText("Let's go!");
                set.setText(Integer.toString(kalanSet));
                mediaplayer=MediaPlayer.create(this,R.raw.calis);
                mediaplayer.seekTo(10000);
                mediaplayer.start();
                runTime(calismaSure);

            }
            else if(!breakIsActive){
                breakIsActive=true;
                bilgi.setText("Break Time!");
                mediaplayer=MediaPlayer.create(this,R.raw.mola);
                mediaplayer.start();
                runTime(molaSure);
            }

        }
        else {
            bilgi.setText("It's done. You are Great!");
            setTextView.setText("Set");
            baslatButton.setText("Baslat");
            baslatButton.setVisibility(View.INVISIBLE);
            set.setText(Integer.toString(setSayisi));
            mediaplayer=MediaPlayer.create(this,R.raw.bitis);
            mediaplayer.start();
        }
    }

    public void runTime(int sure){
        counterIsActive=true;
        countDownTimer= new CountDownTimer(sure * 1000 + 100, 1000) { //+100 ekleme sebebimiz her saniye gecer 0.1 saniye oteliyor otelemesini istemiyoruz

            @Override
            public void onTick(long millisUntilFinished) {
                kalanSure=(int) millisUntilFinished/1000;
                updateTimer((int) millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                kronometre.setText("00:00");
                workOrBreak();


            }
        }.start();

    }
    public void cancelTimer(){
        if (counterIsActive){
        countDownTimer.cancel();
        }
    }
    public void controlTimer (View view){

        if(!counterIsActive) {
            counterIsPause=false;
            baslatButton.setText("Pause");
            bilgi.setText("Work!");
            bilgi.setVisibility(View.VISIBLE);
            setTextView.setText("Remaining Set");
            set.setText(Integer.toString(kalanSet));
            runTime(calismaSure);
        }
        else{
            if(!counterIsPause) {
                baslatButton.setText("Resume");
                counterIsPause=true;
                cancelTimer();
            }
            else{
                baslatButton.setText("Pause");
                counterIsPause=false;
                runTime(kalanSure);
            }

        }

    }

    public void setAyarla(View view){

        if (!setAyarlandi){
            if(!TextUtils.isEmpty(calisma.getText().toString())){
                calismaSure=Integer.parseInt(calisma.getText().toString())*60;

                if(!TextUtils.isEmpty(mola.getText().toString())){

                    molaSure=Integer.parseInt(mola.getText().toString())*60;
                    if(!TextUtils.isEmpty(set.getText().toString())){

                        setSayisi=Integer.parseInt( set.getText().toString());
                        kalanSet=setSayisi-1;
                        updateTimer(calismaSure);
                        setButton.setText("Stop");
                        setAyarlandi=true;
                        calisma.setEnabled(false);
                        mola.setEnabled(false);
                        set.setEnabled(false);
                        baslatButton.setVisibility(View.VISIBLE);

                    }
                    else{
                        Toast.makeText(this, "Set field required.", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(this, "Break field required.", Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(this,"Study field required"  , Toast.LENGTH_LONG).show();
            }
        }
        else{
            setAyarlandi=false;
            calisma.setEnabled(true);
            mola.setEnabled(true);
            set.setEnabled(true);
            setButton.setText("Set");
            baslatButton.setText("Start");
            cancelTimer();
            updateTimer(calismaSure);
            baslatButton.setVisibility(View.INVISIBLE);
            breakIsActive=false;
            bilgi.setVisibility(View.INVISIBLE);
            setTextView.setText("Set Clear");
            set.setText(Integer.toString(setSayisi));
        }
        counterIsActive=false;

    }

    public void updateTimer(int sure){
        int minute=(int) sure/60;
        int second=sure-minute*60;
        if (second>9){
            kronometre.setText(Integer.toString(minute)+":"+Integer.toString(second));
        }
        else {
            kronometre.setText(Integer.toString(minute)+":0"+Integer.toString(second));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baslatButton=(Button) findViewById(R.id.startButton);
        setButton=(Button) findViewById(R.id.setAyarla);
        baslatButton.setVisibility(View.INVISIBLE);
        calisma=(EditText)findViewById(R.id.calismaText);
        mola=(EditText)findViewById(R.id.molaText);
        set=(EditText)findViewById(R.id.setText);
        kronometre=(TextView)findViewById(R.id.kronometre);
        bilgi= (TextView) findViewById(R.id.bilgiTextView);
        bilgi.setVisibility(View.INVISIBLE);
        setTextView=(TextView)findViewById(R.id.setTane);
        breakIsActive=false;
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,0);


    }
}
