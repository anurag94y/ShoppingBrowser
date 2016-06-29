package com.example.aturag.shoppingbrowser;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aturag on 26-Jun-16.
 */
public class SearchAdapter extends BaseAdapter implements Filterable{

    private MainActivity activity;
    private UrlFilter urlFilter;
    private Typeface typeface;
    private ArrayList<String> urlList;
    private ArrayList<String> filteredurlList;


    public SearchAdapter(MainActivity activity, ArrayList<String> UrlList) {
        this.activity = activity;
        this.urlList = UrlList;
        this.filteredurlList = UrlList;

        //typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/vegur_2.otf");
        getFilter();
    }

    /**
     * Get size of user list
     * @return userList size
     */
    @Override
    public int getCount() {
        return filteredurlList.size();
    }

    /**
     * Get specific item from user list
     * @param i item index
     * @return list item
     */
    @Override
    public String getItem(int i) {
        return filteredurlList.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final String url = getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.search_results, parent, false);
            holder = new ViewHolder();
            holder.url = (TextView) view.findViewById(R.id.txtQuery);
           //holder.url.setTypeface(typeface, Typeface.NORMAL);

            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
        holder.url.setText(url);
        return view;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if ( urlFilter == null) {
             urlFilter = new UrlFilter();
        }
        return  urlFilter;
    }

    static class ViewHolder {
        TextView url;
    }
    /**
     * Keep reference to children view to avoid unnecessary calls
     */

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class UrlFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<String> tempList = new ArrayList<>();

                // search content in friend list
                for (String url : urlList) {
                    if (url.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(url);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = urlList.size();
                filterResults.values = urlList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredurlList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

}
