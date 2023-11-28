package br.com.tudoesom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

public class MainActivityLogo extends Activity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainlogo);


        Handler handleTab = new Handler();
        handleTab.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(MainActivityLogo.this, LangActivity.class));
            }
        }, 2000);


    }


    public void doProximaTela(View v) {

        startActivity(new Intent(MainActivityLogo.this, LangActivity.class));
    }
}
