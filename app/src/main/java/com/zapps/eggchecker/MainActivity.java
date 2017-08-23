package com.zapps.eggchecker;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    EditText inputEggcode;
    Button searchButton;

    TextView resultText;

    LinearLayout resultView;

    ArrayList<String> infos;
    Map<String, String> results;

    String resultKey;
    String resultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        infos = new ArrayList<>();
        resultView = (LinearLayout) findViewById(R.id.view_result);

        inputEggcode = (EditText) findViewById(R.id.input_eggcode);
        searchButton = (Button) findViewById(R.id.btn_search);

        results = new HashMap<>();

        webView.getSettings().setJavaScriptEnabled(true);

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
                Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT)
                        .show();
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

    }

    void getEggInfo() {



        for (int i=0;i<EggInfo.KEY_EGG_CODE+1;i++) {

            ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_result,null);
            final TextView title = (TextView)layout.findViewById(R.id.item_title);
            final TextView info = (TextView)layout.findViewById(R.id.item_value);
            webView.evaluateJavascript("javascript: document.getElementsByTagName('th')["
                    + i
                    + "].innerText", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d("names", value);
                    title.setText(value.replace("\"", ""));
                }
            });
            webView.evaluateJavascript("javascript: document.getElementsByTagName('td')["
                    + i
                    + "].innerText", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d("names", value);
                    info.setText(value.replace("\"", ""));
                }
            });

            resultView.addView(layout);

        }




    }
}


