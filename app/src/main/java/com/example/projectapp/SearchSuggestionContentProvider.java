package com.example.projectapp;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionContentProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.projectapp.SearchSuggestionContentProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
