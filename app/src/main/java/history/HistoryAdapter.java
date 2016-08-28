package history;

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
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<History> result = new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView history_url, history_title;
        CardView cv;
        public ImageView history_icon;

        MyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.history_card_view);
            history_title = (TextView)itemView.findViewById(R.id.history_title);
            history_url = (TextView)itemView.findViewById(R.id.history_url);
            history_icon = (ImageView)itemView.findViewById(R.id.history_icon);
        }
    }

    public void setHistoryList(ArrayList<History> result) {
        this.result = result;

    }

    public HistoryAdapter(ArrayList<History> result, Context context) {
        this.context = context;
        this.result = result;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final History history = result.get(position);
        System.out.println(holder.history_title + " Null " + history.getImage());
        holder.history_title.setText(history.getTitle());
        holder.history_url.setText(history.getUrl());
        byte[] imgByte = history.getImage();
        if(imgByte != null)
            holder.history_icon.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setData(Uri.parse(history.getUrl()));
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
