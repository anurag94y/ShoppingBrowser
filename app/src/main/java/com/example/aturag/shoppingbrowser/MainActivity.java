package com.example.aturag.shoppingbrowser;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Product.DatabaseTable;
import Product.ExtractDetailFromUrl;
import Product.ProductInfo;
import ConnectToServer.*;

public class MainActivity extends FragmentActivity {

    //for Search View
    private ListView urlListView;
    private SearchView searchView = null;
    private MenuItem searchMenuItem;
    private SearchAdapter searchAdapter;
    private ArrayList<String> friendList;
    DatabaseTable db = new DatabaseTable(this);
    boolean searchStatus = true;


    private HashMap<Integer, Integer> queryMap = new HashMap<>();
    private CollectionPagerAdapter mDemoCollectionPagerAdapter;
    private  ViewPager mViewPager;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    public static final int MIN_HTML_ALLOWED_LENGTH = 40;
    public static boolean _isFullScreenBanner;
    public static Handler _handler;
    private static RecyclerView mRecyclerView;
    private int imageStat;
    private int count = 0;
    public ProductAdapter productAdapter;
    public static int queryNumber = 1;
    private MenuItem refreshMenuItem = null;
    public static ArrayList<ProductInfo> productInfoList = new ArrayList<>();

    public MainActivity() {
        //this._isFullScreenBanner = false;
        //this._handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println(" in handler " + productInfoList);
                productAdapter.setProductInfoList(productInfoList);
                productAdapter.notifyDataSetChanged();
            }
        };
        setContentView(R.layout.main_activity);
        enableCookies();
        count = 0;
        /*mDemoCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);*/
        init();
        new ServletAsyncTask().execute(new Pair<Context, String>(this, "https://www.google.com/search?q=anurag+yadav"));
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new ProductAdapter(productInfoList,mWebView);
        mRecyclerView.setAdapter(productAdapter);
        imageStat = 1;
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                webViewStatus(progress);
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
        //webSettings.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
        mWebView.getSettings().setLightTouchEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.requestFocus();
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        //OpenUrl(("http://www.amazon.in/Canon-EOS-1300D-Digital-18-55mm/dp/B01D4EYNUG"));
        OpenUrl("http://www.snapdeal.com/product/micromax-32b4500mhd-81-cm-32/640439490139");
        //OpenUrl("http://www.flipkart.com/samsung-galaxy-j7-6-new-2016-edition/p/itmegmrnggh56u22?pid=MOBEG4XWDK4WBGNU&lid=LSTMOBEG4XWDK4WBGNUD7TNFK");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(searchView != null)
            searchView.requestFocus();
        return super.onPrepareOptionsMenu(menu);
    }

    public void webViewStatus(int progress) {
        if(progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE){
            if(!queryMap.containsKey(queryNumber)) {
                queryMap.put(queryNumber, 1);
                productInfoList = new ArrayList<>();
                productAdapter.setProductInfoList(productInfoList);
                productAdapter.notifyDataSetChanged();
            }
            imageStat = 2;
            if(refreshMenuItem != null) {
                refreshMenuItem.setIcon(R.drawable.icon_cancel);
            }
            final String Url = mWebView.getOriginalUrl();
            if(searchView != null)
                searchView.setQuery(Url, false);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
        mProgressBar.setProgress(progress);
        if(progress == 100) {
            System.out.println("cout >>> " + count);
            callForComparePriceList(mWebView.getUrl());
            count++;
            imageStat = 1;
            final String Url = mWebView.getOriginalUrl();
            if(searchView != null)
                searchView.setQuery(Url, false);
            if(count >= 1) {
                /*try {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ExtractDetailFromUrl extractDetailFromUrl = new ExtractDetailFromUrl();
                            if (extractDetailFromUrl.isProductUrl(Url)) {
                                extractDetailFromUrl.isValidProduct(Url, queryNumber);
                                System.out.println("!!!! Yes It is product Url My Bro How U identify that !!!!");
                            } else {
                                System.out.println("No it is not product Page");
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            //productAdapter.setProductInfoList(productInfoList);
                            productAdapter.notifyDataSetChanged();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    System.out.println("Error !!! " + e.getMessage());
                }*/
            }
            if(refreshMenuItem != null) {
                refreshMenuItem.setIcon(R.drawable.icon_refresh);
            }
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }



    private void refreshButtonLisener() {
        if(imageStat == 1) {
            OpenUrl(String.valueOf(searchView.getQuery()));
        }
        else {
            mWebView.stopLoading();
            imageStat = 1;
            if(refreshMenuItem != null) {
                refreshMenuItem.setIcon(R.drawable.icon_refresh);
            }
        }
    }

    public class mWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //callForComparePriceList(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private void callForComparePriceList(String url) {
        final String Url = url;
        productInfoList.clear();
        productAdapter.notifyDataSetChanged();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + url + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        ExtractDetailFromUrl extractDetailFromUrl = new ExtractDetailFromUrl();
                        if (extractDetailFromUrl.isProductUrl(Url)) {
                            extractDetailFromUrl.isValidProduct(Url, queryNumber);
                            System.out.println("!!!! Yes It is product Url My Bro How U identify that !!!!");
                        } else {
                            System.out.println("No it is not product Page");
                        }
                    } catch (Exception e) {
                        //System.out.println(e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //productAdapter.setProductInfoList(productInfoList);
                    productAdapter.notifyDataSetChanged();
                    queryNumber++;

                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            System.out.println("Error !!! " + e.getMessage());
        }
    }

    private String Parse_Uri(final String Url) { // Input Can be http/https :// google.com or www.google.com or google
        String regex = "^(?:[a-z]+:)?//";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Url);
        if(matcher.matches()) {
            return Url;
        }
        else {
            final String TrimmedUrl = Url.trim().replaceAll(" +", "+");
            /*final String queryUrl= "https://www.google.com/search?q=" + TrimmedUrl;
            final GetFirstLinkFromGoogle crawler = new GetFirstLinkFromGoogle();
            new AsyncTask<Void, Void, Void>() {
                String var = "";
                @Override
                protected Void doInBackground(Void... params) {
                    crawler.getAllEcommerceUrl(queryUrl);
                    return null;
                }
            }.execute();*/
            return ("https://www.google.com/search?q=" + TrimmedUrl);
        }
    }

    public void OpenUrl(String url) {
        mWebView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
            //callForComparePriceList(mWebView.);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);

        //searchMenuItem = menu.findItem(R.id.action_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            //searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView = new SearchView(getActionBar().getThemedContext());
            int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView magImage = (ImageView) searchView.findViewById(magId);
            magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            searchView.setIconifiedByDefault(false);
            getActionBar().setCustomView(searchView);
            getActionBar().setDisplayShowCustomEnabled(true);
            //ComponentName cn = new ComponentName(this, SearchAdapter.class);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            getActionBar().setCustomView(searchView);
            getActionBar().setDisplayShowCustomEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    OpenUrl(Parse_Uri(query));
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //initUrlList();
                    //Cursor c = db.getWordMatches(newText, null);
                    //searchAdapter.getFilter().filter(newText);
                    return true;
                }
            });
          //  searchView.setIconifiedByDefault(false);
        }

        refreshMenuItem = menu.findItem(R.id.action_refresh);
        //refreshButtonLisener();
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_refresh:
                refreshButtonLisener();
                return true;

            /*case R.id.action_search:
                System.out.println("!!!!!!!!! submit button start working !!!!!!");
                onSearchRequested();
                return true;*/

            case R.id.menu_forward:
                if(mWebView.canGoForward()) {
                    mWebView.goForward();
                    //callForComparePriceList(mWebView.getUrl());
                }
                return true;

            case R.id.menu_bookmark:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(MainActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_save:
                Toast.makeText(MainActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_share:
                Toast.makeText(MainActivity.this, "Share is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_delete:
                Toast.makeText(MainActivity.this, "Delete is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_preferences:
                Toast.makeText(MainActivity.this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public static void datachanged(ArrayList<ProductInfo> productInfolist, int query) {
        System.out.println("!!!!!!!! query" + query + " " + queryNumber);
        if(query == queryNumber) {
            System.out.println("Data Change Call " + productInfolist);
            productInfoList = productInfolist;
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
        try {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        catch (Exception e) {
            System.out.println("Error in Destroy Webview " + e.getMessage());
        }
    }

    private void initUrlList() {
        friendList = new ArrayList<>();
        //intialize friendList
        urlListView = (ListView) findViewById(R.id.search_listView);
        searchAdapter = new SearchAdapter(this, friendList);

        // add header and footer for list
        urlListView.setAdapter(searchAdapter);
        urlListView.setTextFilterEnabled(false);

        // use to enable search view popup text
        //friendListView.setTextFilterEnabled(true);

        // set up click listener
        urlListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0 && position <= friendList.size()) {
                    handelListItemClick((String)searchAdapter.getItem(position - 1));
                }
            }
        });
    }


    private void handelListItemClick(String url) {
        // close search view if its visible
        System.out.println("Item Click Running");
        if (searchView!= null && searchMenuItem != null) {
            searchMenuItem.collapseActionView();
            searchView.setQuery(url, false);
            OpenUrl(url);
        }

    }

}
