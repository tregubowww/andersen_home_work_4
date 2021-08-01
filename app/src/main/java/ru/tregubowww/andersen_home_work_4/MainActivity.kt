package ru.tregubowww.andersen_home_work_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var watchView: WatchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        watchView = findViewById(R.id.watch)
        watchView.startTime()
    }
}