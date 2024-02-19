package com.projecte3.provesprojecte

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class CaptureActivity : AppCompatActivity() {
    private var cameraPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        val previewFrame = findViewById<FrameLayout>(R.id.cameraPreview)
        cameraPreview = CameraPreview(this)
        previewFrame.addView(cameraPreview)
    }

    override fun onPause() {
        super.onPause()
        cameraPreview?.surfaceDestroyed(cameraPreview!!.holder)
    }

    override fun onResume() {
        super.onResume()
        cameraPreview?.surfaceCreated(cameraPreview!!.holder)
    }
}