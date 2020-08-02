package com.gzeinnumer.externalpdffromxmlsingledata.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzeinnumer.externalpdffromxmlsingledata.R;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.util.HashMap;

public class FunctionGlobalPDFSingle {

    private static PDFCallBack pdfCallBack;

    public interface PDFCallBack{
        //pakai call back ini untuk membalikan path yang bsa dipakai untuk dishare
        void callBackPath(String path);
    }

    private static final String TAG = "FunctionGlobalPDFSingle_";

    public static void initPDFSingleData(final Context context, final Activity activity, final HashMap<String, String> dataToPDF) {

        pdfCallBack = (PDFCallBack) activity;

        final AbstractViewRenderer page = new AbstractViewRenderer(context, R.layout.template_pdf) {

            @Override
            protected void initView(View view) {
                TextView tv_hello = view.findViewById(R.id.tv);
                tv_hello.setText("Kirim Text Kesini " + dataToPDF.get("title"));
            }
        };

        PdfDocument doc = new PdfDocument(context);
        doc.addPage(page);
        //emang sudah default, value ini sama dengan ukuran pada xml nya
        doc.setRenderWidth(1500);
        doc.setRenderHeight(2115);
        doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
        doc.setProgressTitle(R.string.pdf_loading_title);
        doc.setProgressMessage(R.string.pdf_laoding_message);
        doc.setFileName("PDF File Name");
        doc.setSaveDirectory(new File(FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder));
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete "+file.toString());
                pdfCallBack.callBackPath(file.toString());
            }

            @Override
            public void onError(Exception e) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error "+e.getMessage());
            }
        });
        doc.createPdf(activity);
    }
}
