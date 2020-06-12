package com.example.projectapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.appcompat.app.AppCompatActivity

class SearchableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                /**
                 * The QUERY string is always included with the ACTION_SEARCH intent.
                 * In this example, the query is retrieved and passed to a local doMySearch() method where the actual search operation is done.
                 */
                //doMySearch(query)
            }
        }

       // handleIntent(intent)

//        // Get the intent, verify the action and get the query
//        val intent = intent
//        if (Intent.ACTION_SEARCH == intent.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            //   doMySearch(query);
//            val suggestions = SearchRecentSuggestions(this,
//                    SearchSuggestionContentProvider.AUTHORITY, SearchSuggestionContentProvider.MODE)
//            suggestions.saveRecentQuery(query, null)
//
//            /* to navigationComplete the suggestions -- we use it in the itemSelected for example and we use alert dialog for confirm .
//              SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                      HelloSuggestionProvider.AUTHORITY, HelloSuggestionProvider.MODE);
//             suggestions.clearHistory();
//             */
//        }
    }

    override fun onNewIntent(intent: Intent?) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent?){
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
        }
    }
}