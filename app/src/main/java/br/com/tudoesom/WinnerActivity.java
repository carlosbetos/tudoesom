package br.com.tudoesom;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class WinnerActivity extends Activity {

    private final MediaPlayer mPlayer = new MediaPlayer();
    private final MediaPlayer somSorteAzar = new MediaPlayer();
    private final MediaPlayer somCartasZeradas = new MediaPlayer();
    private final MediaPlayer somDeReiniciarCartas = new MediaPlayer();
    private ImageView imageView;
    boolean sorte;
    MediaPlayer sompinguin = new MediaPlayer();
    MediaPlayer mediaPlayerTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_winner);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        imageView = (ImageView) findViewById(R.id.imageView1);
        animaPinguin(imageView);

        if (CuttingBoard.getInstance().isBaralho() && CuttingBoard.getInstance().length() == 0) {
            ((ImageView) findViewById(R.id.ImageView01)).setImageResource(R.drawable.baralho);

            try {
                somDeReiniciarCartas.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/cartasreiniciando"));
                somDeReiniciarCartas.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                playAudioComTimer("cartaszeradasfimdojogo", 2400);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        try {

            sompinguin.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/pinguinfeliz"));
            sompinguin.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }

        Handler handleTab = new Handler();
        handleTab.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    String valorbole = getIntent().getStringExtra("booleaPuletrescasas");
                    sorte = Boolean.parseBoolean(valorbole);
                    if (sorte) {

                        somSorteAzar.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/puleduascasas"));
                        somSorteAzar.prepare();
                        somSorteAzar.start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 2400);

    }

    public void doRepeat(View view) {

        if (mPlayer.isPlaying()) {
            return;
        }
        if (somSorteAzar.isPlaying()) {
            return;
        }
        if (sompinguin.isPlaying()) {
            return;
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


        } catch (IOException e) {
            e.printStackTrace();
        }


        if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 385 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

        } else if (Integer.parseInt(getIntent().getStringExtra("theRead")) == 46 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
            ((TextView) findViewById(R.id.textViewTheRead)).setText("  LIT D 'ENFANT");
        } else {

             ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + getIntent().getStringExtra("theRead"), "string", getPackageName())));
        }

    }

    public void doNextGame(View v) {
        if (mPlayer.isPlaying()) {
            return;
        }
        if (somSorteAzar.isPlaying()) {
            return;
        }
        if (sompinguin.isPlaying()) {
            return;
        }


        if (!somSorteAzar.isPlaying()) {
            if (CuttingBoard.getInstance().length() == 0) {

                somDeReiniciarCartas.start();
                CuttingBoard.getInstance().reset();
                CuttingBoard.getInstance().doRandom();
            }

            if (sompinguin.isPlaying()) {

                return;
            }

            finish();
        }


    }


    @Override
    protected void onPause() {


        if (CuttingBoard.getInstance().isBaralho() && CuttingBoard.getInstance().length() == 0) {

            somDeReiniciarCartas.start();
            CuttingBoard.getInstance().reset();
        }
        finish();
        super.onPause();
    }

    protected void animaPinguin(View view) {

        view.setBackgroundResource(R.drawable.pinguinfeliz);
        AnimationDrawable anima = (AnimationDrawable) view.getBackground();
        anima.start();

        Handler handleTabAnima = new Handler();
        handleTabAnima.postDelayed(new Runnable() {

            @Override
            public void run() {
                AnimationDrawable animaStop = (AnimationDrawable) imageView.getBackground();
                animaStop.stop();

            }
        }, 2000);


    }


    public void tocarNoPingui(View view) throws IOException {

        if (sompinguin.isPlaying()) {
            return;
        }
        if(sorte){
            playAudioComTimer("puleduascasas",2050);
        }
        animaPinguin((ImageView) findViewById(R.id.imageView1));
        sompinguin.seekTo(0);
        sompinguin.start();
    }

    public void playAudioComTimer(final String nomeAudio, int time) throws IOException {
        Handler handleCarta = new Handler();

        handleCarta.postDelayed(new Runnable() {

            @Override
            public void run() {
                mediaPlayerTime = MediaPlayer.create(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
                mediaPlayerTime.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mp = null;
                    }
                });
                mediaPlayerTime.start();
            }
        }, time);
    }


}
