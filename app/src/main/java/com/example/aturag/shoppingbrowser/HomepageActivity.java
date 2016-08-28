package com.example.aturag.shoppingbrowser;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import mainactivity.MainActivity;

/**
 * Created by anurag.yadav on 9/19/16.
 */
public class HomepageActivity extends FragmentActivity {

    private ImageView flipkart;
    private ImageView snapdeal;
    private ImageView ebay;
    private ImageView shopclues;
    private ImageView paytm;
    private ImageView amazon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        amazon = (ImageView) findViewById(R.id.hm_amazon);
        flipkart = (ImageView) findViewById(R.id.hm_flipkart);
        paytm = (ImageView) findViewById(R.id.hm_paytm);
        shopclues = (ImageView) findViewById(R.id.hm_shopclues);
        snapdeal = (ImageView) findViewById(R.id.hm_snapdeal);
        ebay = (ImageView) findViewById(R.id.hm_ebay);

        amazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.amazon.in/");
            }
        });

        flipkart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.flipkart.com/");
            }
        });

        shopclues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.shopclues.com/");
            }
        });

        snapdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.snapdeal.com/");
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://paytm.com/");
            }
        });

        ebay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.ebay.in/");
            }
        });


    }

    private void openUrl(String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


}
