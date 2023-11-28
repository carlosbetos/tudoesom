package br.com.tudoesom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class LangPopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_lang);
    }

    public void doInEnglish(View v) {

        CuttingBoard.getInstance().selectEnglishLanguage();
        finish();

    }

    public void doInEspanish(View v) {

        CuttingBoard.getInstance().selectSpanishLanguage();
        finish();
    }

    public void doInPortugues(View v) {

        CuttingBoard.getInstance().selectPortuguesLanguage();
        finish();
    }

    public void doInFrench(View v) {

        CuttingBoard.getInstance().selectFranchLanguage();
        finish();
    }


}
