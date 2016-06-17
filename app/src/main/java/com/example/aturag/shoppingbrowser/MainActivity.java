package com.example.aturag.shoppingbrowser;

import android.app.Activity;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText mEdittext;
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdittext = (EditText) findViewById(R.id.editText);

        OpenUrl("https://www.facebook.com/");
        mEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdittext.getWindowToken(), 0);
                    OpenUrl(Parse_Uri(String.valueOf(mEdittext.getText())));
                    return true;
                }
                return false;
            }
        });
    }

    private String Parse_Uri(String Url) { // Input Can be http/https :// google.com or www.google.com or google

        int http_idx = Url.indexOf("http:");
        int https_idx = Url.indexOf("https:");
        String Prefix_path = "";
        String Suffix_path = "";

        if(http_idx == -1 && https_idx == -1) {
            Prefix_path += "http://";
        }

        int www_idx = Url.indexOf("www.");
        if(www_idx == -1) {
            int dot_idx = Url.indexOf('.');
            if(dot_idx == -1) {
                Suffix_path = ".com";
            }
        } else {
            int cnt_of_dots = 0;
            for(int i = www_idx; i < Url.length(); ++i) {
                if(Url.charAt(i) == '.') {
                    cnt_of_dots += 1;
                }
            }
            if(cnt_of_dots <= 1) {
                Suffix_path = ".com";
            }
        }

        return Prefix_path + Url + Suffix_path; // Output Can be http/https :// www.google.com or google.com
    }

    private void OpenUrl(String url) {
        System.out.println(">>>>" + url);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);

        String newUA = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

        mWebView.getSettings().setUserAgentString(newUA);
        mWebView.loadUrl(url);
    }

}
