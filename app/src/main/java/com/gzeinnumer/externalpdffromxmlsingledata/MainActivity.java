package com.gzeinnumer.externalpdffromxmlsingledata;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gzeinnumer.externalpdffromxmlsingledata.pdfSingle.PDFSAppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PDFSAppUtils.PDFSCallBack {

    private static final String TAG = "MainActivity_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(TAG);

        HashMap<String, String> dataToPDF = new HashMap<>();

        dataToPDF.put("title", "Title ini");

        PDFSAppUtils.initPDFSingleData(getApplicationContext(), this, dataToPDF);
    }

    @Override
    public void callBackPath(String path) {
        Log.d(TAG, "callBackPath: " + path);
        sharePdf(path);
    }

    private void sharePdf(String fileName) {
        //kode ini penting ungutuk memaksa agar aplikasi luar bsa mengakses data yang kita butuh kan
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ArrayList<Uri> uris = new ArrayList<Uri>();
        Uri u = Uri.fromFile(new File(fileName));
        uris.add(u);

        final Intent sendToIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendToIntent.setType("application/pdf");
        sendToIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        sendToIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        sendToIntent.putExtra(Intent.EXTRA_SUBJECT, "ini subject");
        sendToIntent.putExtra(Intent.EXTRA_TEXT, "ini message");
        sendToIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {
            startActivity(Intent.createChooser(sendToIntent, "Send mail..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}