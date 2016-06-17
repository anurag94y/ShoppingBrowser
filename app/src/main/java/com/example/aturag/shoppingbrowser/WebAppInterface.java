package com.example.aturag.shoppingbrowser;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Aturag on 17-Jun-16.
 */
public class WebAppInterface {
    Context mContext;
    MainActivity mainActivity;

    /** Instantiate the interface and set the context */
    WebAppInterface(MainActivity c) {
        mContext = c.getApplicationContext();
        mainActivity = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
