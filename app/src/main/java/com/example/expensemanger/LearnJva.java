package com.example.expensemanger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class LearnJva extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView =findViewById(R.id.pdfView);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        String URL = "https://firebasestorage.googleapis.com/v0/b/expensemanager-191aa.appspot.com/o/JVM.pdf?alt=media&token=74cc6e03-052a-47cd-a636-64eb5a42dcd5";
        String FinalURL="https://drive.google.com/viewerng/viewer?embedded=true&url="+URL;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebChromeClient(new WebChromeClient()
                                   {
                                       @Override
                                       public void onProgressChanged(WebView view, int newProgress) {
                                           super.onProgressChanged(view, newProgress);
                                           getSupportActionBar().setTitle("Loading");
                                           if(newProgress == 100)
                                           {
                                               progressBar.setVisibility(View.GONE);
                                               getSupportActionBar().setTitle("CORE JAVA");

                                           }
                                       }
                                   }
        );
        webView.loadUrl(FinalURL);

    }
}
