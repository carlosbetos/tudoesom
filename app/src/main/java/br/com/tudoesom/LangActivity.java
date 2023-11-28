package br.com.tudoesom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class LangActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_lang);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CuttingBoard.getInstance().reset();
    }

    public void doInEnglish(View v) {

        CuttingBoard.getInstance().selectEnglishLanguage();

        Intent intent = new Intent(LangActivity.this, SelectionActivity.class);
        startActivity(intent);
    }

    public void doInEspanish(View v) {

        CuttingBoard.getInstance().selectSpanishLanguage();

        Intent intent = new Intent(LangActivity.this, SelectionActivity.class);
        startActivity(intent);
    }

    public void doInPortugues(View v) {

        CuttingBoard.getInstance().selectPortuguesLanguage();

        Intent intent = new Intent(LangActivity.this, SelectionActivity.class);
        startActivity(intent);
    }

    public void doInFrench(View v) {

        CuttingBoard.getInstance().selectFranchLanguage();

        Intent intent = new Intent(LangActivity.this, SelectionActivity.class);
        startActivity(intent);
    }


}
