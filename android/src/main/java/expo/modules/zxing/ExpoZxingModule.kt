package expo.modules.zxing

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.zxing.BarcodeFormat
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
import java.io.ByteArrayInputStream


class ExpoZxingModule : Module() {
  private var reader: MultiFormatReader? = null
  private var mReader: MultipleBarcodeReader? = null

  override fun definition() = ModuleDefinition {
    Name("ExpoZxing")

    OnCreate {
      reader = MultiFormatReader()
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
        // 文字列の配列に変換
        return@Function results?.map<Result?, String?> { it?.text }?.toTypedArray<String?>()
      } catch (e: Exception) {
        e.printStackTrace()
        return@Function null
      }
    }
  }


  private fun getBarcodeFormat(type: String): BarcodeFormat? {
    when (type) {
      "Aztec" -> return BarcodeFormat.AZTEC
      "Codabar" -> return BarcodeFormat.CODABAR
      "Code39" -> return BarcodeFormat.CODE_39
      "Code93" -> return BarcodeFormat.CODE_93
      "Code128" -> return BarcodeFormat.CODE_128
      "DataMatrix" -> return BarcodeFormat.DATA_MATRIX
      "Ean8" -> return BarcodeFormat.EAN_8
      "Ean13" -> return BarcodeFormat.EAN_13
      "ITF" -> return BarcodeFormat.ITF
      "MaxiCode" -> return BarcodeFormat.MAXICODE
      "PDF417" -> return BarcodeFormat.PDF_417
      "QRCode" -> return BarcodeFormat.QR_CODE
      "RSS14" -> return BarcodeFormat.RSS_14
      "RSSExpanded" -> return BarcodeFormat.RSS_EXPANDED
      "UPCA" -> return BarcodeFormat.UPC_A
      "UPCE" -> return BarcodeFormat.UPC_E
      "UPCEANExtension" -> return BarcodeFormat.UPC_EAN_EXTENSION
    }
    return null
  }
}
