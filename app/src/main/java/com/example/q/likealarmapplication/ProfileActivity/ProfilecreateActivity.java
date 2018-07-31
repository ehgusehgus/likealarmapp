package com.example.q.likealarmapplication.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.q.likealarmapplication.R;

import java.util.ArrayList;

public class ProfilecreateActivity extends AppCompatActivity {

    Context mContext;
    Boolean is_first = false;
    MultiViewTypeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

//        Intent i = getIntent();
//        Bundle extras = i.getExtras();
//        String keyword_got = extras.getString("keyword");
//        String ingredient_got = extras.getString("ingredient");
//        String category_got = extras.getString("category");
//        String category_got2 = extras.getString("category2");
//        String tag_got = extras.getString("tag");
//        String creater_got = extras.getString("creater");
//        String updated_got = extras.getString("updated_at");
//        ArrayList<String> recipes_got = extras.getStringArrayList("recipes");

        ArrayList<Model> list = new ArrayList();
        list.add(new Model(Model.EDIT_NAME_TYPE, "이름", "", null, null));
        list.add(new Model(Model.EDIT_SEX_TYPE, "성별", "", null, null));
        list.add(new Model(Model.EDIT_AGE_TYPE, "나이", "", null, null));
        list.add(new Model(Model.EDIT_HEIGHT_TYPE, "키", "", null, null));
        list.add(new Model(Model.EDIT_CHAR_TYPE, "성격", "", null, null));
        list.add(new Model(Model.EDIT_ALCOHOL_TYPE, "음주", "", null, null));

        mAdapter = new MultiViewTypeAdapter(list, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mAdapter.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        MenuItem edit = menu.add(Menu.NONE, R.id.done, 10, "완료");
        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        edit.setIcon(R.drawable.cloud);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.done:
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                //((MultiViewTypeAdapter)recyclerView.getAdapter()).finishClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
