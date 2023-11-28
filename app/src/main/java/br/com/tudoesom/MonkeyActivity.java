package br.com.tudoesom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MonkeyActivity extends Activity {

    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerTime;
    boolean podeSair;
    boolean inicio;
    String valorbole;
    boolean estaTocandoAinda;

    String valorRandomico=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_monkey);
        estaTocandoAinda = false;
        podeSair = false;
        valorbole = getIntent().getStringExtra("veiodaSelecao");
        inicio = Boolean.parseBoolean(valorbole);

        findViewById(R.id.imageView4).setVisibility(View.GONE);
        findViewById(R.id.imageView5).setVisibility(View.GONE);
        findViewById(R.id.textViewTheRead).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        findViewById(R.id.imageView4).setVisibility(View.GONE);
        findViewById(R.id.imageView5).setVisibility(View.GONE);
        findViewById(R.id.textViewTheRead).setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();


        if (CuttingBoard.getInstance().isTabuleiro()) {
            ((ImageView) findViewById(R.id.imageViewFundoMonkey)).setImageResource(R.drawable.green_toolbar_bg_tabuleiro);
            ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.play);
        } else if (CuttingBoard.getInstance().isBaralho()) {
            ((ImageView) findViewById(R.id.imageViewFundoMonkey)).setImageResource(R.drawable.green_toolbar_bg_baralho);
            ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.play);
        }

        if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.PORTUGUES) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.portugues);
        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.ENGLISH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.ingles);
        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.SPANISH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.espanhol);
        } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.FRENCH) {
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.frances);
        }
        if (inicio && valorRandomico!=null) {
            ImageView vd = (ImageView) findViewById(R.id.imageView1);
            doMonkey(vd);
            inicio=false;
       }


    }

    public void doCancel(View v) {

        CuttingBoard.getInstance().doRandom();
        ((ImageView) MonkeyActivity.this.findViewById(R.id.imageView4)).setVisibility(View.GONE);
        findViewById(R.id.textViewTheRead).setVisibility(View.GONE);
        v.setVisibility(View.GONE);

    }

    public void doBack(View v) throws IOException {
        if (CuttingBoard.getInstance().isBaralho()) {
            checkExit();
        } else {
            onBackPressed();
        }


    }

    public void doQr(View v) {
        inicio = false;
        startActivityForResult(new Intent(MonkeyActivity.this, QrActivity.class), 1);
    }

    public void doMonkey(View v) {

        valorRandomico = CuttingBoard.getInstance().getNumero().toString();

        if (estaTocandoAinda) {
            return;
        }
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.playpre);

        estaTocandoAinda = true;
        MonkeyActivity.this.findViewById(R.id.imageView4).setVisibility(View.GONE);
        MonkeyActivity.this.findViewById(R.id.imageView5).setVisibility(View.GONE);
        findViewById(R.id.textViewTheRead).setVisibility(View.VISIBLE);

        if (CuttingBoard.getInstance().length() > 0) {
            ((ImageView) MonkeyActivity.this.findViewById(R.id.imageView4)).setVisibility(View.VISIBLE);
            if (CuttingBoard.getInstance().isBaralho()) {
                MonkeyActivity.this.findViewById(R.id.imageView5).setVisibility(View.VISIBLE);
            }
        }

        try {

            if (Integer.parseInt(valorRandomico) == 385 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                ((TextView) findViewById(R.id.textViewTheRead)).setText("FOURGON D'INCENDIE");

            } else if (Integer.parseInt(valorRandomico) == 46 && CuttingBoard.getInstance().getLanguageSigla().equalsIgnoreCase("fr")) {
                ((TextView) findViewById(R.id.textViewTheRead)).setText("LIT D'ENFANT");
            } else {

                ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + CuttingBoard.getInstance().getNumero().intValue(), "string", getPackageName())));
            }

            playAudioOnly(CuttingBoard.getInstance().getLanguageSigla() + CuttingBoard.getInstance().getNumero());
            podeTocar(CuttingBoard.getInstance().getLanguageSigla() + CuttingBoard.getInstance().getNumero(), obterTempo(CuttingBoard.getInstance().getLanguageSigla() + CuttingBoard.getInstance().getNumero().toString()));
            ajustarFonte(getString(getResources().getIdentifier(CuttingBoard.getInstance().getLanguageSigla() + CuttingBoard.getInstance().getNumero().intValue(), "string", getPackageName())));

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(MonkeyActivity.this, "Ops... ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        Log.i("TudoÉsom", "onActivityResult");

        if (resultCode == RESULT_OK) {

            try {
                try {

                    String qrcodeLido = data.getStringExtra("theRead");

                    if (!CuttingBoard.getInstance().temNaCartaOuTabuleiroOuEtiqueta(qrcodeLido)) {

                        playAudioOnly("codigoinvalido");
                        ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(R.string.code_not_found));
                        ajustarFonte(getString(R.string.code_not_found));
                        findViewById(R.id.textViewTheRead).setVisibility(View.VISIBLE);


                    } else {
                        int tempo = obterTempo(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"));
                        playAudioOnly(CuttingBoard.getInstance().getLanguageSigla() + data.getStringExtra("theRead"));
                        if (data.getStringExtra("theRead").equals(CuttingBoard.getInstance().getNumero().toString())) {

                            CuttingBoard.getInstance().removeNumero();

                            if (CuttingBoard.getInstance().isBaralho()) {
                                podeSair = true;
                            }

                            Intent intent = new Intent(MonkeyActivity.this, WinnerActivity.class);

                            try {
                                if (CuttingBoard.getInstance().comsorteouazar(Integer.parseInt(valorRandomico))) {
                                    playAudioComTimer("ganhouespecial", tempo);
                                    //    podeTocar("ganhouespecial", obterTempo("ganhouespecial"));
                                    intent.putExtra("booleaPuletrescasas", "true");

                                } else {
                                    playAudioComTimer("ganhou", tempo);
                                    intent.putExtra("booleaPuletrescasas", "false");
                                    //  podeTocar("ganhou", obterTempo("ganhouespecial"));
                                }

                                intent.putExtra("theRead", data.getStringExtra("theRead"));
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            CuttingBoard.getInstance().doRandom();
                            playAudioComTimer("perdeu", tempo);
                            //  podeTocar("perdeu", obterTempo("perdeu"));

                            try {

                                Intent intent = new Intent(MonkeyActivity.this, LoserActivity.class);
                                intent.putExtra("theRead", data.getStringExtra("theRead"));
                                intent.putExtra("valorMonkei", valorRandomico);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (NumberFormatException e) {

                    playAudioOnly("codigoinvalido");
                    ((TextView) findViewById(R.id.textViewTheRead)).setText(getString(R.string.code_not_found));
                    findViewById(R.id.textViewTheRead).setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void checkExit() throws IOException {


        if (CuttingBoard.getInstance().isBaralho() && podeSair && CuttingBoard.getInstance().length() != 47) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.PORTUGUES) {
                builder.setTitle("SAÍDA");
            } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.ENGLISH) {
                builder.setTitle("EXIT");
            } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.SPANISH) {
                builder.setTitle("SALIDA");
            } else if (CuttingBoard.getInstance().getLanguage() == CuttingBoard.Language.FRENCH) {
                builder.setTitle("SORTIE");
            }
            builder.setMessage("Deseja Sair do Jogo das Cartas?")
                    .setCancelable(false)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();

                        }
                    })
                    .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        try {
            checkExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public int obterTempo(String nomeAudio) throws IOException {

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
        mediaPlayer.prepare();
        return mediaPlayer.getDuration() + 25;


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


    public void podeTocar(String nomeAudio, int time) throws IOException {
        Handler handleCarta = new Handler();

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://br.com.tudoesom/raw/" + nomeAudio));
        mediaPlayer.prepare();

        handleCarta.postDelayed(new Runnable() {

            @Override
            public void run() {
                estaTocandoAinda = false;
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.play);
            }
        }, time);
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

        Intent intent = new Intent(MonkeyActivity.this, LangPopUpActivity.class);
        inicio = true;
        startActivity(intent);

    }


}