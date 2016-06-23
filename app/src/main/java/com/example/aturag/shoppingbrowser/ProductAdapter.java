package com.example.aturag.shoppingbrowser;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Product.ProductInfo;

/**
 * Created by Aturag on 22-Jun-16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private WebView mWebView;
    private int[] ecommerceIcon = {R.drawable.amazon_icon, R.drawable.flipkat_icon, R.drawable.snapdeal_icon, R.drawable.ebay_icon};
    private ArrayList<ProductInfo> productInfoList = new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_Name, product_Price, genre;
        CardView cv;
        public ImageView ecommerce_Icon;

        MyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            product_Name = (TextView)itemView.findViewById(R.id.product_name);
            product_Price = (TextView)itemView.findViewById(R.id.product_price);
            ecommerce_Icon = (ImageView)itemView.findViewById(R.id.product_icon);
        }
    }

    public void setProductInfoList(ArrayList<ProductInfo> productInfoList) {
        this.productInfoList = productInfoList;
    }

    public ProductAdapter(ArrayList<ProductInfo> productInfoList, WebView mWebView) {
        this.productInfoList = productInfoList;
        this.mWebView = mWebView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ProductInfo productInfo = productInfoList.get(position);
        holder.product_Name.setText(productInfo.getName());
        holder.product_Price.setText("Price:" + productInfo.getPrice());
        holder.ecommerce_Icon.setImageResource(ecommerceIcon[productInfo.ecommerceIcon]);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(productInfo.Url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfoList.size();
    }
}