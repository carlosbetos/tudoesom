package br.com.tudoesom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrActivity extends  Activity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(zXingScannerView= new ZXingScannerView(this));
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        zXingScannerView.stopCamera();
        super.onPause();
        finish();

    }

    @Override
    public void handleResult(Result result) {
        zXingScannerView.stopCamera();
        Intent data = new Intent();
        data.putExtra("theRead", result.getText().toString() );
        setResult( Activity.RESULT_OK, data );
        this.finish();
    }


}
