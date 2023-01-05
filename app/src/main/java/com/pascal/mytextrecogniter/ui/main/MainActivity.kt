package com.pascal.mytextrecogniter.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.otaliastudios.cameraview.Audio
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.pascal.mytextrecogniter.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = "image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Text Recogniter"

        initCameraView()
        initListeners()

        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 100)
        } else {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item?.itemId) {
            R.id.menu_item_camera -> {
                showCameraView()
                true
            }
            R.id.menu_item_upload_photo -> {
                showGalleryView()
                true
            }
            else -> {
                /* nothing to do in here */
                super.onOptionsItemSelected(item)
            }
        }

    private fun initListeners() {
        camera_view.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                camera_view.stop()
                CameraUtils.decodeBitmap(jpeg) { bitmap ->
                    image_view.scaleType = ImageView.ScaleType.FIT_XY
                    image_view.setImageBitmap(bitmap)

                    val image = InputImage.fromBitmap(bitmap, 0)
                    val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                    textRecognizer.process(image)
                        .addOnSuccessListener {
                            camera_view.visibility = View.GONE
                            image_view.visibility = View.VISIBLE
                            processTextRecognitionResult(it)
                        }
                        .addOnFailureListener {
                            showToast(it.localizedMessage)
                        }
                    super.onPictureTaken(jpeg)
                }
            }
        })
        button_take_picture.setOnClickListener {
            camera_view.captureSnapshot()
        }
    }

    private fun initCameraView() {
        camera_view.audio = Audio.OFF
        camera_view.playSounds = false
        camera_view.cropOutput = true
    }

    private fun showCameraView() {
        camera_view.start()
        camera_view.visibility = View.VISIBLE
        image_view.visibility = View.GONE
    }

    private fun showGalleryView() {
        if (camera_view.isStarted) {
            camera_view.stop()
        }
        camera_view.visibility = View.GONE
        image_view.visibility = View.GONE
        val intentGallery = Intent()
        intentGallery.type = "image/*"
        intentGallery.action = Intent.ACTION_GET_CONTENT
        val intentChooser = Intent.createChooser(intentGallery, "Pick Picture")
        startActivityForResult(intentChooser, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Activity.RESULT_OK && data != null) {

            val image_bitmap = selectFromGalleryResult(data)
            image_view.setImageBitmap(image_bitmap)

            val image = InputImage.fromBitmap(image_bitmap, 0)
            val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            textRecognizer.process(image)
                .addOnSuccessListener {
                    camera_view.visibility = View.GONE
                    image_view.visibility = View.VISIBLE
                    image_view.scaleType = ImageView.ScaleType.CENTER_CROP
                    processTextRecognitionResult(it)
                }
                .addOnFailureListener {
                    showToast(it.localizedMessage)
                }
        }
    }

    private fun selectFromGalleryResult(data: Intent?): Bitmap {
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm =
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bm!!
    }

    private fun processTextRecognitionResult(firebaseVisionText: Text) {
        // text_view_result.text = firebaseVisionText.text

        val intent = Intent(this@MainActivity, TranslateActivity::class.java)
        intent.putExtra("data", firebaseVisionText.text)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        if (camera_view.isStarted) {
            camera_view.stop()
        }
        super.onPause()
    }
}