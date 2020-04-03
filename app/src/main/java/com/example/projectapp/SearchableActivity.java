package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //   doMySearch(query);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchSuggestionContentProvider.AUTHORITY, SearchSuggestionContentProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            /* to navigationComplete the suggestions -- we use it in the itemSelected for example and we use alert dialog for confirm .
              SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                      HelloSuggestionProvider.AUTHORITY, HelloSuggestionProvider.MODE);
             suggestions.clearHistory();
             */
        }


    }
}
