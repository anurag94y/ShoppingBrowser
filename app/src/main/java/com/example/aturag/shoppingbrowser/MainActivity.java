package com.example.aturag.shoppingbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Crawler.GetFirstLinkFromGoogle;
import Product.ExtractDetailFromUrl;

public class MainActivity extends FragmentActivity {

    private CollectionPagerAdapter mDemoCollectionPagerAdapter;
    private  ViewPager mViewPager;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private EditText mEdittext;
    public static final int MIN_HTML_ALLOWED_LENGTH = 40;
    public static boolean _isFullScreenBanner;
    private Handler _handler;
    private ImageView refresh;
    private ImageView settings;
    private int imageStat;

    public MainActivity() {
        this._isFullScreenBanner = false;
        this._handler = new Handler();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_fragment);
        enableCookies();
        /*mDemoCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);*/

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mEdittext = (EditText) findViewById(R.id.editText);
        refresh = (ImageView) findViewById(R.id.refresh);
        settings = (ImageView) findViewById(R.id.settings);

        mEdittext.setSelectAllOnFocus(true);

        imageStat = 1;
        refresh.setImageResource(R.drawable.icon_refresh);
        settings.setImageResource(R.drawable.icon_setting);



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshButtonLisener();
            }
        });

        OpenUrl(Parse_Uri("Sony xperia l amazon"));
        Paras_function("http://www.amazon.in/Canon-EOS-1300D-Digital-18-55mm/dp/B01D4EYNUG");
        //OpenUrl("http://www.amazon.in/Canon-EOS-1300D-Digital-18-55mm/dp/B01D4EYNUG");
        mEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdittext.getWindowToken(), 0);
                    Uri url = Uri.parse(String.valueOf(mEdittext.getText()));
                    System.out.println(">>>> url " + url);
                    OpenUrl(Parse_Uri(String.valueOf(mEdittext.getText())));
                    return true;
                }
                // If it wasn't the Back key or there's no web page history, bubble up to the default
                // system behavior (probably exit the activity)
                return false;
            }
        });

        try {
            System.out.println("---------------------------andr aaya---------------------");
            ProductDetails pd = new ProductDetails("http://www.snapdeal.com/product/micromax-32b4500mhd-81-cm-32/640439490139", "Snapdeal");
            System.out.println("------------------------ Product name ---------------\n" + pd.getProductName());
            System.out.println("------------------------ Product price--------------\n" + pd.getProductPrice());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshButtonLisener() {
        if(imageStat == 1) {
            OpenUrl(String.valueOf(mEdittext.getText()));
        }
        else {
            mWebView.stopLoading();
            imageStat = 1;
            refresh.setImageResource(R.drawable.icon_refresh);
        }
    }

    private String Parse_Uri(String Url) { // Input Can be http/https :// google.com or www.google.com or google

        String regex = "^(?:[a-z]+:)?//";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Url);
        ExtractDetailFromUrl extractDetailFromUrl = new ExtractDetailFromUrl();
        try {
            if(extractDetailFromUrl.isProductUrl(Url)) {
                System.out.println("!!!! Yes It is product Url My Bro How U identify that !!!!");
            } else {
                System.out.println("No it is not product Page");
            }

        } catch (Exception e) {
            System.out.println("Error !!! " + e.getMessage());
        }
        if(matcher.matches()) {
            return Url;
        }
        else {
            final String TrimmedUrl = Url.trim().replaceAll(" +", "+");
            final String queryUrl= "https://www.google.com/search?q=" + TrimmedUrl;
            final GetFirstLinkFromGoogle crawler = new GetFirstLinkFromGoogle();
            new AsyncTask<Void, Void, Void>() {
                String var = "";
                @Override
                protected Void doInBackground(Void... params) {
                    crawler.getAllEcommerceUrl(queryUrl);
                    return null;
                }
            }.execute();
            Paras_function("https://www.google.com/search?q=sony+xperia+l+amazon");
            return ("https://www.google.com/search?q=" + TrimmedUrl);
        }
    }

    public void OpenUrl(String url) {
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE){
                    imageStat = 2;
                    refresh.setImageResource(R.drawable.icon_cancel);
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
                mProgressBar.setProgress(progress);
                if(progress == 100) {
                    imageStat = 1;
                    refresh.setImageResource(R.drawable.icon_refresh);
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        this.mWebView.resumeTimers();
        this.mWebView.addJavascriptInterface(new MyJavaScriptInterface(this), "Android");
        this.mWebView.setWebViewClient(new mWebViewClient());
        this.mWebView.setDownloadListener(new mDownloadListener());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        mWebView.getSettings().setLightTouchEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl(url);
        mWebView.requestFocus();
    }

    private void Paras_function(final String url) {

        new AsyncTask<Void,Void ,Void>() {
            Document doc;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    System.out.println("Bhodi kkk");
                    doc = Jsoup.connect(url).get();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("doc error " + url + " " +  e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                System.out.println("doc doc " + url + " " + doc);
                System.out.println("-----------------------Bhodi kkkkkkk -----------------");
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute();
    }



    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class mWebViewClient extends WebViewClient {
        mWebViewClient() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //_handleRedirect(url);
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mEdittext.setText(url);
            return false;
           // return _handleRedirect(url);
        }

        private boolean _handleRedirect(String url) {
            MainActivity.this._handler.removeCallbacksAndMessages(null);
            if (url == null) {
                return false;
            }
            boolean isHttpUrl = MainActivity._isHttpUrl(url);
            boolean isMarketUrl = MainActivity._isMarketUrl(url);
            if (isMarketUrl && isHttpUrl) {
                url = MainActivity._replaceHttpWithMarketUrl(url);
            }

            if (!isMarketUrl && isHttpUrl) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            if (deviceCanHandleIntent(MainActivity.this, intent)) {
                //MainActivity.this.startActivity(intent);
                //MainActivity.this.finish();
                return true;
            } else if (!MainActivity.this._isFullScreenBanner) {
                return false;
            } else {
                return false;
            }
        }
    }

    class mDownloadListener implements DownloadListener {
        mDownloadListener() {
        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            ResolveInfo ri = MainActivity.this.getPackageManager().resolveActivity(intent, 0);
            if (ri != null) {
               //MainActivity.this.startActivity(intent);
            }
        }
    }



    private void enableCookies() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
    }

    private static String _replaceHttpWithMarketUrl(String url) {
        if (!_isMarketUrl(url)) {
            return url;
        }
        return "market://details?" + Uri.parse(url).getEncodedQuery();
    }

    private static boolean _isMarketUrl(String url) {
        Uri uri = Uri.parse(url);
        String host = uri.getHost();

        return (uri.getScheme() != null && uri.getScheme().equals("market")) || (host != null && host.equals("play.google.com"));
    }

    private static boolean _isHttpUrl(String url) {
        String scheme = Uri.parse(url).getScheme();
        System.out.println(">>>>" + scheme + " " + url);
        if(scheme == null)
            return false;
        return scheme.equals("http") || scheme.equals("https");
    }

    public static boolean deviceCanHandleIntent(Context context, Intent intent) {
        try {
            if (context.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    protected void onResume() {
        super.onResume();
        this.mWebView.resumeTimers();
        CookieSyncManager.getInstance().startSync();
    }

    protected void onDestroy() {
        super.onDestroy();
       // webViewPlaceholder.removeView(mWebView);
        mWebView.removeAllViews();
        mWebView.destroy();
    }


}
