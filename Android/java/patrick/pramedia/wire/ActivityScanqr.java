package patrick.pramedia.wire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by munil on 5/2/2019.
 */

public class ActivityScanqr extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String kode_qr = rawResult.getText();
        Log.d("KODEQR", kode_qr);

        Intent i = new Intent(ActivityScanqr.this, Activitysetdevice.class);
        i.putExtra("KODEQR", kode_qr);
        startActivity(i);

        finish();

//        Log.v("TAG", rawResult.getText()); // Prints scan results
//        Log.v("TAG", rawResult.getBarcodeFormat().toString());

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setMessage(rawResult.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();
//
//        mScannerView.resumeCameraPreview(this);
    }
}
