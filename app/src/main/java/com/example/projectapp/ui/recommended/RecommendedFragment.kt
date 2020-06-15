package com.example.projectapp.ui.recommended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projectapp.R
import com.example.projectapp.sharedViewModel

class RecommendedFragment : Fragment() {
    private var slideshowViewModel: RecommendedViewModel? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        slideshowViewModel = ViewModelProviders.of(this).get(RecommendedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recommended, container, false)
        val textView = root.findViewById<TextView>(R.id.text_slideshow)
        /* slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("recommended");
            }
        });*/
        sharedViewModel.setActiveIntroStarted(false)
        return root
    }
}