package com.zapps.eggchecker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    Button button;
    Button button2;
    String resultHTMLRaw;

    Document document;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);


        webView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("weblog", consoleMessage.message() + '\n' + consoleMessage.messageLevel() +
                        '\n' + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });


        webView.loadUrl("http://www.foodsafetykorea.go.kr/portal/fooddanger/eggHazardList.do?menu_grp=MENU_NEW02&menu_no=3497");


        webView.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("javascript:"
                                + "document.getElementById(\"search_keyword\").value=\"08LSH\"",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.d("onrecieve", value);
                            }
                        });

                webView.evaluateJavascript("javascript: searchList()", null);


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<13;i++) {
                    webView.evaluateJavascript("javascript: document.getElementsByTagName('td')["
                            + i
                            + "].innerText", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.d("names", value);
                        }
                    });
                }
            }
        });


    }

}


