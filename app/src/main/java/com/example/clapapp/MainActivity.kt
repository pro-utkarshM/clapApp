package com.example.clapapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    //null point exception will not occour by using safe call operator
    private lateinit var seekBar:SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar=findViewById(R.id.seekBar)
        handler= Handler(Looper.getMainLooper())

        val btnPlay=findViewById<FloatingActionButton>(R.id.fabPlay)
        btnPlay.setOnClickListener{
            if(mediaPlayer==null){
                mediaPlayer=MediaPlayer.create(this,R.raw.upbeat)
                initalizeSeekBar()
            }
            mediaPlayer?.start()
        }
        val btnPause=findViewById<FloatingActionButton>(R.id.fabPause)
        btnPause.setOnClickListener{
            mediaPlayer?.pause()
        }
        val btnStop=findViewById<FloatingActionButton>(R.id.fabStop)
        btnStop.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer=null
            handler.removeCallbacks(runnable)
            seekBar.progress=0
        }
    }
    private fun initalizeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if(fromUser)
                   mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        val tvPlayed=findViewById<TextView>(R.id.tvPlayed)
        val tvDue=findViewById<TextView>(R.id.tvDue)
        seekBar.max=mediaPlayer!!.duration
        //not null assertion operator !!.
        runnable= Runnable {
            seekBar.progress=mediaPlayer!!.currentPosition
            val playedTime=mediaPlayer!!.currentPosition/1000
            tvPlayed.text="$playedTime sec"
            val duration=mediaPlayer!!.duration/1000
            val dueTime=duration-playedTime

            tvDue.text="$dueTime sec"
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }
}