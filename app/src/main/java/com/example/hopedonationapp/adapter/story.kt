import android.graphics.Bitmap

data class Story(
    val fileName: String,
    val fileUrl: String,
    val thumbnailBitmap: Bitmap? = null // Add this property
)
