package bookmarks;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aturag.shoppingbrowser.HomepageActivity;
import com.example.aturag.shoppingbrowser.R;

import java.util.ArrayList;

import mainactivity.MainActivity;

/**
 * Created by anurag.yadav on 8/7/16.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Bookmark> result = new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bookmark_url, bookmark_title;
        CardView cv;
        public ImageView bookmark_icon;

        MyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.bookmark_card_view);
            bookmark_title = (TextView)itemView.findViewById(R.id.bookmark_title);
            bookmark_url = (TextView)itemView.findViewById(R.id.bookmark_url);
            bookmark_icon = (ImageView)itemView.findViewById(R.id.bookmark_icon);
        }
    }

    public void setBookmarkList(ArrayList<Bookmark> result) {
        this.result = result;

    }

    public BookmarkAdapter(ArrayList<Bookmark> result, Context context) {
        this.context = context;
        this.result = result;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_list_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Bookmark bookmark = result.get(position);
        System.out.println(holder.bookmark_title + " Null " + bookmark.getImage());
        holder.bookmark_title.setText(bookmark.getTitle());
        holder.bookmark_url.setText(bookmark.getUrl());
        byte[] imgByte = bookmark.getImage();
        if(imgByte != null)
            holder.bookmark_icon.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setData(Uri.parse(bookmark.getUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(result == null)
            return 0;
        return result.size();
    }
}
