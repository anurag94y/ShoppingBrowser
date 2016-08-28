package bookmarks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.aturag.shoppingbrowser.R;

import java.util.ArrayList;
import java.util.List;

import Database.Operations;

/**
 * Created by anurag.yadav on 8/7/16.
 */
public class BookmarkActivity extends FragmentActivity {

    private List<Bookmark> listOfBookmark = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Button mButton;
    private Operations operations;
    private BookmarkAdapter BookmarkAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mButton = (Button) findViewById(R.id.clear_bookmark);
        operations = new Operations(this);
        Intent intent = getIntent();
        if(intent != null) {
            listOfBookmark = intent.getParcelableArrayListExtra("listOfBookmark");
            System.out.println("Bookmark !!!!!!!!!!!!!!" + listOfBookmark);
        }
        BookmarkAdapter = new BookmarkAdapter((ArrayList<Bookmark>) listOfBookmark, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(BookmarkAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operations.deleteBookmark(operations);
                BookmarkAdapter.setBookmarkList(new ArrayList<Bookmark>());
                BookmarkAdapter.notifyDataSetChanged();
            }
        });
    }


}
