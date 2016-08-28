package mainactivity;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.aturag.shoppingbrowser.HomepageActivity;
import com.example.aturag.shoppingbrowser.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ConnectToServer.ServletAsyncTask;
import Crawler.GetFirstLinkFromGoogle;
import Database.Operations;
import Product.ProductAdapter;
import Product.ProductInfo;
import bookmarks.Bookmark;
import bookmarks.BookmarkActivity;
import history.History;
import history.HistoryActivity;

public class MainActivity extends FragmentActivity {

    //for Search View
    private SearchView searchView = null;
    private HashMap<Integer, Integer> queryMap = new HashMap<>();
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

    private Operations operations;

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
        init();
        Intent intent = getIntent();
        if(intent != null) {
            OpenUrl(intent.getData().toString());
        }
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        operations = new Operations(this);

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
        mWebView.resumeTimers();
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        mWebView.setWebViewClient(new mWebViewClient());
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setDownloadListener(new mDownloadListener());
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
        //OpenUrl(Parse_Uri("http://www.amazon.in/Sony-Bravia-KDL-32W700C-inches-Smart/dp/B015WEL8Q8"));
        //OpenUrl("http://www.flipkart.com/samsung-galaxy-j7-6-new-2016-edition/p/itmegmrnggh56u22?pid=MOBEG4XWDK4WBGNU&lid=LSTMOBEG4XWDK4WBGNUD7TNFK");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
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
                queryNumber++;
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
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            operations.insertHistory(operations, mWebView.getUrl(), mWebView.getTitle(), mWebView.getFavicon());
            callForComparePriceList(mWebView.getUrl());
            count++;
            imageStat = 1;
            final String Url = mWebView.getOriginalUrl();
            if(searchView != null)
                searchView.setQuery(Url, false);
            if(refreshMenuItem != null) {
                refreshMenuItem.setIcon(R.drawable.icon_refresh);
            }
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private void callForComparePriceList(final String url) {
        final String Url = url;
        productInfoList.clear();
        productAdapter.notifyDataSetChanged();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + url + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        new GetFirstLinkFromGoogle().getAllEcommerceUrl(url, queryNumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //new ServletAsyncTask(queryNumber).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Pair<Context, String>(this, url));
        } catch(Exception e) {
            System.out.println("Error in Fetching data from Server" + e.getMessage());
        }
    }

    private String Parse_Uri(final String Url) { // Input Can be http/https :// google.com or www.google.com or google
        if(URLUtil.isHttpsUrl(Url) || URLUtil.isHttpUrl(Url)) {
            return Url;
        }
        else {
            final String TrimmedUrl = Url.trim().replaceAll(" +", "+");
            return ("https://www.google.com/search?q=" + TrimmedUrl);
        }
    }

    public void OpenUrl(String url) {
        mWebView.loadUrl(url);
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
            searchView.clearFocus();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return selectOptionInMenu(item);
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

    private boolean selectOptionInMenu(MenuItem item) {
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

            case R.id.action_go:
                if(searchView != null)
                    OpenUrl(Parse_Uri((new SpannableStringBuilder(searchView.getQuery()).toString())));
                return true;

            case R.id.menu_forward:
                if(mWebView.canGoForward()) {
                    mWebView.goForward();
                    //callForComparePriceList(mWebView.getUrl());
                }
                return true;

            case R.id.menu_back:
                if(mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                return true;

            case R.id.menu_bookmark:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                showBookMark(operations);
                return true;

            case R.id.menu_add_bookmark:
                operations.insertBookmark(operations, mWebView.getUrl(), mWebView.getTitle(), mWebView.getFavicon());
                Toast.makeText(this, "Bookmark added", Toast.LENGTH_SHORT);
                return true;

            case R.id.menu_share:
                shareTextUrl(mWebView.getUrl(), mWebView.getTitle());
                return true;

            case R.id.menu_history:
                showHistory(operations);
                return true;

            case R.id.menu_home:
                Intent intent = new Intent(this, HomepageActivity.class);
                startActivity(intent);
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
                //HomepageActivity.this.startActivity(intent);
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


    public void showHistory(Operations historyOperations) {
        Cursor result = historyOperations.getHistory(historyOperations);
        List<History> listOfHistory = new ArrayList<>();
        result.moveToFirst();
        System.out.println(result.toString() + " " + result.getCount());
        if(result.getCount() >  0) {
            do {
                History history = new History();
                history.setUrl(result.getString(result.getColumnIndex(Operations.PAGE_URL)));
                history.setTitle(result.getString(result.getColumnIndex(Operations.PAGE_TITLE)));
                byte[] imgByte = result.getBlob(result.getColumnIndex(Operations.PAGE_IMAGE));
                history.setImage(imgByte);
                System.out.println("get history Image " + BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                listOfHistory.add(history);
            } while (result.moveToNext());
        }

        System.out.println("Histories !!!!!!!!!!!" + listOfHistory);

        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putParcelableArrayListExtra("listOfHistory", (ArrayList<? extends Parcelable>) listOfHistory);
        startActivity(intent);
    }

    public void showBookMark(Operations bookmarkOperations) {
        Cursor result = bookmarkOperations.getBookmark(bookmarkOperations);
        List<Bookmark> listOfBookMark = new ArrayList<>();
        result.moveToFirst();
        System.out.println(result.toString() + " " + result.getCount());
        if(result.getCount() >  0) {
            do {
                Bookmark bookmark = new Bookmark();
                bookmark.setUrl(result.getString(result.getColumnIndex(Operations.PAGE_URL)));
                bookmark.setTitle(result.getString(result.getColumnIndex(Operations.PAGE_TITLE)));
                byte[] imgByte = result.getBlob(result.getColumnIndex(Operations.PAGE_IMAGE));
                bookmark.setImage(imgByte);
                System.out.println("get bookmark Image " + BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                listOfBookMark.add(bookmark);
            } while (result.moveToNext());
        }

        System.out.println("Bookmarks !!!!!!!!!!!" + listOfBookMark);

        Intent intent = new Intent(this, BookmarkActivity.class);
        intent.putParcelableArrayListExtra("listOfBookmark", (ArrayList<? extends Parcelable>) listOfBookMark);
        startActivity(intent);
    }

    private void shareTextUrl(String url, String title) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
    }

}
