package br.com.tudoesom;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SelectionActivity extends Activity {

    MediaPlayer mediaPlayer;
    String valorCapturado;
    boolean estaTocandoAinda;
    String qrcodeLido=null;
    boolean inicio;
    String valorbole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selection);
        findViewById(R.id.textViewTheRead).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);

        valorbole = getIntent().getStringExtra("veiodaSelecao");
        inicio = Boolean.parseBoolean(valorbole);


    }

    public void doRepeat(View view) throws IOException {

        if (estaTocandoAinda) {
            return;
        }

        estaTocandoAinda = true;

        if(inicio){
            findViewById(R.id.textViewTheRead).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView8).setVisibility(View.VISIBLE);

            if(valorCapturado.equalsIgnoreCase("codigoinvalido")) {
                playAudioOnly("codigoinvalido");
                podeTocar("codigoinvalido");
            }else {

                playAudioOnly(CuttingBoard.getInstance().getLanguageSigla() + qrcodeLido);
                podeTocar(valorCapturado);
                if (qrcodeLido.equalsIgnoreCase("385") && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                    ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");
                    ajustarFonte("FOURGON D'INCENDIE");

                } else if (qrcodeLido.equalsIgnoreCase("46") && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                    ((TextView) findViewById(R.id.textViewTheRead)).setText("LIT D 'ENFANT");
                    ajustarFonte("LIT D 'ENFANT");
                } else {

                    ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + qrcodeLido, "string", getPackageName())));
                    ajustarFonte(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + qrcodeLido, "string", getPackageName())));
                }
            }
        }else{
            if(valorCapturado.equalsIgnoreCase("codigoinvalido")){
                playAudioOnly("codigoinvalido");
                podeTocar("codigoinvalido");
            }else {
                playAudioOnly(CuttingBoard.getInstance().getLanguageSigla() + qrcodeLido);
                podeTocar(CuttingBoard.getInstance().getLanguageSigla() + qrcodeLido);
            }
        }



    }

    public void doQr(View v) {

        startActivityForResult(new Intent(SelectionActivity.this, QrActivity.class), 1);
    }

    public void doBaralho(View v) {

        CuttingBoard.getInstance().selectBaralho();
        CuttingBoard.getInstance().doRandom();

        reiniciarCartas();

        Intent intent = new Intent(SelectionActivity.this, MonkeyActivity.class);
        intent.putExtra("veiodaSelecao", "false");
        startActivity(intent);
    }

    public void doTabuleiro(View v) {

        CuttingBoard.getInstance().selectTabuleiro();
        CuttingBoard.getInstance().doRandom();
        Intent intent = new Intent(SelectionActivity.this, MonkeyActivity.class);

        intent.putExtra("veiodaSelecao", "false");
        startActivity(intent);
    }

    public void doBack(View v) {

        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("TudoÉsom", "onResume");

        if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.PORTUGUES) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.portugues);
            ((ImageView) findViewById(R.id.imageViewFundoBandeira)).setImageResource(R.drawable.blue_toolbar_bg_brasil);

        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.ENGLISH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.ingles);
            ((ImageView) findViewById(R.id.imageViewFundoBandeira)).setImageResource(R.drawable.blue_toolbar_bg_ingles);

        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.SPANISH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.espanhol);
            ((ImageView) findViewById(R.id.imageViewFundoBandeira)).setImageResource(R.drawable.blue_toolbar_bg_espanhol);

        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.FRENCH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.frances);
            ((ImageView) findViewById(R.id.imageViewFundoBandeira)).setImageResource(R.drawable.blue_toolbar_bg_frances);


        }

        if (inicio  && qrcodeLido!=null) {
            ImageView vd = (ImageView) findViewById(R.id.imageView1);
            try {
                doRepeat(vd);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inicio=false;
        }



    }

/*    @Override
    protected void onPause() {
        super.onPause();
        // estaTocandoAinda=true;
        findViewById(R.id.textViewTheRead).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("TudoÉsom", "onActivityResult");


        if (resultCode == RESULT_OK) {

            try {

                try {
                    qrcodeLido = data.getStringExtra("theRead");
                    if (!CuttingBoard.getInstance().temNaCartaOuTabuleiroOuEtiqueta(qrcodeLido)) {
                        valorCapturado = "codigoinvalido";
                        try {
                            playAudioOnly("codigoinvalido");
                            podeTocar("codigoinvalido");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        estaTocandoAinda = true;
                        ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(R.string.code_not_found));
                        ajustarFonte(getString(R.string.code_not_found));
                    }else {
                        valorCapturado= (CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"));
                        playAudioOnly(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"));
                        podeTocar(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"));
                        estaTocandoAinda = true;

                        if (qrcodeLido.equalsIgnoreCase("385") && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                            ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

                        } else if (qrcodeLido.equalsIgnoreCase("46") && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                            ((TextView) findViewById(R.id.textViewTheRead)).setText("  LIT D 'ENFANT");
                        } else {

                            ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"), "string", getPackageName())));
                        }
                        ajustarFonte(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"), "string", getPackageName())));
                    }

                } catch (NumberFormatException e) {
                    playAudioOnly("codigoinvalido");
                   ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(R.string.code_not_found));
                }

                findViewById(R.id.textViewTheRead).setVisibility(View.VISIBLE);
                findViewById(R.id.imageView8).setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SelectionActivity.this, "Ops... ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reiniciarCartas() {
        MediaPlayer somDeReiniciarCartas = new MediaPlayer();
        try {
            playAudioOnly("cartasreiniciando");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CuttingBoard.getInstance().reset();
    }


    public void playAudioOnly(String nomeAudio) throws IOException {


        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });
        mediaPlayer.start();
    }

    public void podeTocar(String nomeAudio) throws IOException {
        Handler handleCarta = new Handler();

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
        mediaPlayer.prepare();

        handleCarta.postDelayed(new Runnable() {

            @Override
            public void run() {
                estaTocandoAinda = false;
            }
        }, obterTempo(nomeAudio));
    }

    public int obterTempo(String nomeAudio) throws IOException {

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
        mediaPlayer.prepare();
        return mediaPlayer.getDuration() + 50;


    }

    public void ajustarFonte(String texto) {

        int tamanho = texto.length();

        if (tamanho < 6) {
            ((TextView) findViewById(R.id.textViewTheRead)).setTextSize(70);
        } else if (tamanho > 5 && tamanho < 11) {
            ((TextView) findViewById(R.id.textViewTheRead)).setTextSize(50);
        } else if (tamanho > 10 && tamanho < 17) {
            ((TextView) findViewById(R.id.textViewTheRead)).setTextSize(35);
        } else if (tamanho > 16 && tamanho < 21) {
            ((TextView) findViewById(R.id.textViewTheRead)).setTextSize(30);
        } else {
            ((TextView) findViewById(R.id.textViewTheRead)).setTextSize(22);
        }
    }

    public void alteraIdioma(View v) throws IOException {
        Intent intent = new Intent(SelectionActivity.this, LangPopUpActivity.class);
        inicio=true;
        startActivity(intent);
    }


}