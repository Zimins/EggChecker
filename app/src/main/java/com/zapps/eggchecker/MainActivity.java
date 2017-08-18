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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    EditText inputEggcode;
    Button searchButton;

    TextView resultText;

    Button button;
    Button button2;

    ArrayList<String> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        infos = new ArrayList<>();

        inputEggcode = (EditText) findViewById(R.id.input_eggcode);
        searchButton = (Button) findViewById(R.id.btn_search);
        resultText = (TextView) findViewById(R.id.tv_result);

        webView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eggcode = inputEggcode.getText().toString();
                searchByCode(eggcode);
            }
        });
    }

    void searchByCode(String eggCode) {
        webView.evaluateJavascript("javascript:"
                        + "document.getElementById(\"search_keyword\").value=\"" +
                        eggCode + "\"",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("onrecieve", value);


                    }
                });
        webView.evaluateJavascript("javascript: searchList()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getEggInfo();
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    void getEggInfo() {
        resultText.setText("");
        for (int i=0;i<13;i++) {
            webView.evaluateJavascript("javascript: document.getElementsByTagName('td')["
                    + i
                    + "].innerText", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d("names", value);
                    resultText.append(value + "\n");
                    infos.add(value);
                }
            });
        }
    }
}


