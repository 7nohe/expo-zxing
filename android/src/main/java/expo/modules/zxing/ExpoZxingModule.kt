package expo.modules.zxing

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.multi.GenericMultipleBarcodeReader
import com.google.zxing.multi.MultipleBarcodeReader
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlin.Array
import kotlin.Exception
import kotlin.IntArray
import kotlin.String
import android.util.Base64
import expo.modules.kotlin.Promise
import java.io.ByteArrayInputStream
import expo.modules.interfaces.permissions.Permissions
import expo.modules.kotlin.exception.Exceptions


class ExpoZxingModule : Module() {
  private var reader: MultiFormatReader? = null
  private var mReader: MultipleBarcodeReader? = null
  override fun definition() = ModuleDefinition {
    Name("ExpoZxing")

    OnCreate {
      reader = MultiFormatReader()
    }

    AsyncFunction("requestCameraPermissionsAsync") { promise: Promise ->
      Permissions.askForPermissionsWithPermissionsManager(
              permissionsManager,
              promise,
              Manifest.permission.CAMERA
      )
    }

    AsyncFunction("getCameraPermissionsAsync") { promise: Promise ->
      Permissions.getPermissionsWithPermissionsManager(
              permissionsManager,
              promise,
              Manifest.permission.CAMERA
      )
    }


    Function("decode") { base64String: String ->
      val decodedString = Base64.decode(base64String, Base64.DEFAULT)
      val decodedByte = ByteArrayInputStream(decodedString)
      val bitmap: Bitmap = BitmapFactory.decodeStream(decodedByte)
      mReader = GenericMultipleBarcodeReader(reader)
      val width = bitmap.width
      val height = bitmap.height
      val pixels = IntArray(width * height)
      bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
      var results: Array<Result?>? = null
      try {
        results = mReader?.decodeMultiple(BinaryBitmap(HybridBinarizer(RGBLuminanceSource(width, height, pixels))))
        return@Function results?.map<Result?, String?> { it?.text }?.toTypedArray<String?>()
      } catch (e: Exception) {
        e.printStackTrace()
        return@Function null
      }
    }

    View(ExpoZxingView::class) {
      Events("onScanned")
    }
  }

  private val permissionsManager: Permissions
    get() = appContext.permissions ?: throw Exceptions.PermissionsModuleNotFound()
}
