package expo.modules.zxing

import QrCodeAnalyzer
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.views.ExpoView
import expo.modules.kotlin.viewevent.EventDispatcher
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExpoZxingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    var camera: Camera? = null
    private val previewView = PreviewView(context)
    private val onScanned by EventDispatcher()
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private val currentActivity
        get() = appContext.currentActivity as? AppCompatActivity
                ?: throw Exceptions.MissingActivity()
    private val tag = "ExpoZxing"
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        previewView.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewRemoved(parent: View?, child: View?) = Unit
            override fun onChildViewAdded(parent: View?, child: View?) {
              parent?.measure(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
              )
              parent?.layout(0, 0, parent.measuredWidth, parent.measuredHeight)
            }
          })
        addView(previewView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))

        previewView.setBackgroundColor(Color.WHITE)

        createCamera()
    }

    private fun createCamera() {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener(
                {
                    val cameraProvider: ProcessCameraProvider = providerFuture.get()
                    val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

                    val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrResult ->
                                    Log.d(tag, "Barcode scanned: ${qrResult.text}")
                                    onScanned(mapOf("result" to qrResult.text));
                                })
                            }

                    try {
                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(currentActivity, cameraSelector, preview, imageAnalysis)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                ContextCompat.getMainExecutor(context)
        )
    }
}
