package com.example.aturag.shoppingbrowser;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aturag on 22-Jun-16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private int[] ecommerceIcon = {R.drawable.amazon_icon, R.drawable.flipkat_icon, R.drawable.snapdeal_icon, R.drawable.ebay_icon};
    private ArrayList<String> productName, productPrice;
    private ArrayList<Integer> ecommerceIconForProduct;
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


    public ProductAdapter(ArrayList<String> productName, ArrayList<String> productPrice, ArrayList<Integer> ecommerceIconForProduct) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.ecommerceIconForProduct = ecommerceIconForProduct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.product_Name.setText(productName.get(position));
        holder.product_Price.setText(productPrice.get(position));
        holder.ecommerce_Icon.setImageResource(ecommerceIcon[ecommerceIconForProduct.get(position)]);
    }

    @Override
    public int getItemCount() {
        return productPrice.size();
    }
}