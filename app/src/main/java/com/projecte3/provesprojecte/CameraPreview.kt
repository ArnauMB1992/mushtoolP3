package com.projecte3.provesprojecte

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraPreview(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private var mCamera: Camera? = null
    private val mHolder: SurfaceHolder

    init {
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera = Camera.open()
        mCamera!!.setDisplayOrientation(90) // Añade esta línea para cambiar la orientación a vertical
        try {
            mCamera!!.setPreviewDisplay(holder)
            mCamera!!.startPreview()
        } catch (e: IOException) {
            Log.d("ERROR", "Error setting camera preview: " + e.message)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera!!.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        if (mHolder.surface == null) {
            return
        }
        try {
            mCamera!!.stopPreview()
        } catch (e: Exception) {
        }
        try {
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
        } catch (e: Exception) {
            Log.d("ERROR", "Error starting camera preview: " + e.message)
        }
    }
}