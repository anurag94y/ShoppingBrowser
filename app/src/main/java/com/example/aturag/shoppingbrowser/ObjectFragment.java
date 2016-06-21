package com.example.aturag.shoppingbrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ObjectFragment extends Fragment {

    private WebView mWebView;
    private Context context;
    ProgressBar mProgressBar;
    public static final String ARG_OBJECT = "object";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.object_fragment, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        final EditText mEdittext = (EditText) rootView.findViewById(R.id.editText);
        OpenUrl("http://www.google.com");
        context = rootView.getContext();
        mEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
                    /*InputMethodManager imm = (InputMethodManager)rootView.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdittext.getWindowToken(), 0);*/
                    Uri url = Uri.parse(String.valueOf(mEdittext.getText()));
                    System.out.println(">>>> url "+ url);
                    OpenUrl(Parse_Uri(String.valueOf(mEdittext.getText())));
                    return true;
                }
                    // If it wasn't the Back key or there's no web page history, bubble up to the default
                    // system behavior (probably exit the activity)
                return false;
            }
        });

        return rootView;
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
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            if(progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE){
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
            }
            System.out.println(">>>> " + progress);
            mProgressBar.setProgress(progress);
            if(progress == 100) {
                mProgressBar.setVisibility(ProgressBar.GONE);
            }
        }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBuiltInZoomControls(true);
   //     mWebView.addJavascriptInterface(new MyJavaScriptInterface(context), "Android");
        mWebView.loadUrl(url);
    }





    /*private void OpenUrl(String url) {
        System.out.println(">>>>" + url);
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
    }*/
}



