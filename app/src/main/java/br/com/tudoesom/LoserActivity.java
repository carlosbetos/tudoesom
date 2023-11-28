package br.com.tudoesom;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;

public class LoserActivity extends Activity {

    private final MediaPlayer mPlayer = new MediaPlayer();
    private final MediaPlayer mPlayerRandomico = new MediaPlayer();
    private final MediaPlayer somSorteAzar = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_loser);

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public void doRepeat(View view) {

        if (mPlayer.isPlaying()) {
            return;
        }

        if (mPlayerRandomico.isPlaying()) {
            return;
        }

        if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 385 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

        } else if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 46 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("LIT D'ENFANT");
        } else {

            ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + getIntent().getStringExtra("theRead"), "string", getPackageName())));
        }

        mPlayer.seekTo(0);
        mPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            mPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + CuttingBoard.getInstance().getLanguageSigla() + getIntent().getStringExtra("theRead")));
            mPlayer.prepare();

            mPlayerRandomico.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + CuttingBoard.getInstance().getLanguageSigla() + getIntent().getStringExtra("valorMonkei")));
            mPlayerRandomico.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 385 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

        } else if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 46 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("LIT D'ENFANT");
        } else {

            ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + getIntent().getStringExtra("theRead"), "string", getPackageName())));
        }

    }

    public void doNextGame(View v) {

        if (!somSorteAzar.isPlaying()) {
            finish();
        }
    }

    @Override
    protected void onPause() {

        finish();
        super.onPause();
    }

    public void doRandomico(View view) {

        if (mPlayer.isPlaying()) {
            return;
        }

        if (mPlayerRandomico.isPlaying()) {
            return;
        }


        if (Integer.parseInt(getIntent().getStringExtra("valorMonkei")) == 385 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

        } else if (Integer.parseInt(getIntent().getStringExtra("valorMonkei")) == 46 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("LIT D'ENFANT");
        } else {

            ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + Integer.valueOf(getIntent().getStringExtra("valorMonkei")), "string", getPackageName())));
        }

        mPlayerRandomico.seekTo(0);
        mPlayerRandomico.start();

    }


}