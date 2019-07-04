package com.prototype.mistplaychallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    ListView gameListView;
    CustomAdapter mCustomAdapter;
    List<Game> allGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        gameListView = findViewById(R.id.gameListView);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);
        setSearch();

        deserializeAsList();

        mCustomAdapter = new CustomAdapter(this, allGames);
        gameListView.setAdapter(mCustomAdapter);
        gameListView.setTextFilterEnabled(true);

        gameListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println(firstVisibleItem+"---"+visibleItemCount+"---"+totalItemCount);
                //add more games to the list when there are two items remaining at the scroll end, then update listView
                if(firstVisibleItem+visibleItemCount >= totalItemCount-2){
                    mCustomAdapter.addGames(totalItemCount);
                    mCustomAdapter.notifyDataSetChanged();
                }
            }
        });

        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You choose " + position + " listItem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deserializeAsList() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            allGames = objectMapper.readValue(loadJSONFromAsset(), new TypeReference<List<Game>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getApplicationContext().getAssets().open("games.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void setSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCustomAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
