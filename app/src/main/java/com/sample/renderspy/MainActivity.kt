package com.sample.renderspy

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var contentView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = TextView(this)
        contentView.text = "Loading content..."
        setContentView(contentView)

        contentView.postDelayed({
            contentView.text = "Screen is usable"
            reportIsUsable()
        }, 1000)
    }
}
