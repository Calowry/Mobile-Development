package com.project.calowry_app.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.calowry_app.R
import com.project.calowry_app.database.local.FoodDailyEntity
import com.project.calowry_app.ml.Model
import com.project.calowry_app.ui.MainActivity
import com.project.calowry_app.ui.base.CalowryApplication
import com.project.calowry_app.ui.customview.NumberEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {

    var camera: Button? = null
    var gallery: Button? = null
    var potraitMode: TextView? = null
    var save: ImageButton? = null
    var retry: ImageButton? = null
    var saveDesc: TextView? = null
    var retryDesc: TextView? = null
    var consumed: NumberEditText? = null
    var imageView: ImageView? = null
    var result: TextView? = null
    var imageSize = 150
    var calories: TextView? = null
    var carbs: TextView? = null
    var sugar: TextView? = null
    var protein: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        camera = findViewById(R.id.camera_btn)
        gallery = findViewById(R.id.gallery_btn)
        potraitMode = findViewById(R.id.potrait_mode)
        save = findViewById(R.id.save_btn)
        retry = findViewById(R.id.retry_btn)
        saveDesc = findViewById(R.id.save_desc)
        retryDesc = findViewById(R.id.retry_desc)
        consumed = findViewById(R.id.total_grams_consumed)
        result = findViewById(R.id.food_name)
        imageView = findViewById(R.id.food_photo_result)
        calories = findViewById(R.id.calories_value)
        carbs = findViewById(R.id.carbs_value)
        sugar = findViewById(R.id.sugar_value)
        protein = findViewById(R.id.protein_value)
        camera?.let { btn ->
            btn.setOnClickListener(View.OnClickListener {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 3)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
            })
        }

        gallery?.let { btn ->
            btn.setOnClickListener(View.OnClickListener {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
            })
        }

        save?.visibility = View.GONE
        saveDesc?.visibility = View.GONE
        retry?.visibility = View.GONE
        retryDesc?.visibility = View.GONE
        consumed?.visibility = View.GONE
    }

    private fun classifyImage(image: Bitmap?) {
        try {
            val model: Model = Model.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "Anggur",
                "Apel",
                "Ayam Goreng",
                "Brokoli",
                "Cap Cay",
                "Ikan Bakar",
                "Jamur Crispy",
                "Kentang",
                "Mie Goreng",
                "Nasi Putih",
                "Nasi Goreng",
                "Pempek Kapal Selam",
                "Pisang Goreng",
                "Rawon",
                "Rendang",
                "Roti",
                "Sate Ayam",
                "Sate Usus",
                "Sosis",
                "Soto Ayam",
                "Telur Mata Sapi",
                "Wortel"
            )
            val foodLabel = classes[maxPos]

            result!!.text = foodLabel

            // Read the CSV file
            val csvFile = applicationContext.assets.open("Data.csv")
            val csvReader = BufferedReader(InputStreamReader(csvFile))
            var line: String?
            var headers: List<String>? = null
            var foodData: Map<String, String>? = null

            // Find the matching row in the CSV for the detected food label
            while (csvReader.readLine().also { line = it } != null) {
                val row = line!!.split(", ")
                if (headers == null) {
                    headers = row
                } else {
                    val label = row[0]
                    if (label == foodLabel) {
                        foodData = headers.zip(row).toMap()
                        break
                    }
                }
            }
            csvReader.close()

            // Extract the nutrition values from the matched row
            if (foodData != null) {
                val caloriesValue = foodData["Calories"] + " Cal"
                val carbsValue = foodData["Carbohydrates"] + " gram"
                val sugarValue = foodData["Sugar"] + " gram"
                val proteinValue = foodData["Protein"] + " gram"

                // Set the nutrition values in the corresponding views
                calories?.text = caloriesValue
                carbs?.text = carbsValue
                sugar?.text = sugarValue
                protein?.text = proteinValue

                // Hide camera, gallery, and potraitMode
                camera?.visibility = View.GONE
                gallery?.visibility = View.GONE
                potraitMode?.visibility = View.GONE

                // Show save and retry buttons
                save?.visibility = View.VISIBLE
                retry?.visibility = View.VISIBLE
                saveDesc?.visibility = View.VISIBLE
                retryDesc?.visibility = View.VISIBLE
                consumed?.visibility = View.VISIBLE

                // Set click listeners for save and retry buttons
                save?.setOnClickListener {
                    val consumedValue = consumed?.text.toString()
                    val foodLabel = result?.text?.toString()
                    val caloriesValue = foodData["Calories"].toString().toFloat()
                    val carbsValue = foodData["Carbohydrates"].toString().toFloat()
                    val sugarValue = foodData["Sugar"].toString().toFloat()
                    val proteinValue = foodData["Protein"].toString().toFloat()

                    // Perform validation
                    when {
                        consumedValue.isBlank() -> {
                            consumed?.requestFocus()
                            consumed?.error = getString(R.string.not_filled)
                            return@setOnClickListener
                        }
                    }

                    // Calculate final nutrition values
                    val caloriesValueFinal = (consumedValue.toFloat() / 100) * caloriesValue
                    val carbsValueFinal = (consumedValue.toFloat() / 100) * carbsValue
                    val sugarValueFinal = (consumedValue.toFloat() / 100) * sugarValue
                    val proteinValueFinal = (consumedValue.toFloat() / 100) * proteinValue

                    // Get the current date and format it as "YYYY-MM-DD"
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    // Create a FoodDailyEntity object with the current date and calculated nutrition values
                    val foodDailyEntity = FoodDailyEntity(
                        scanDate = currentDate.toString(),
                        foodLabel = foodLabel.orEmpty(),
                        caloriesValue = caloriesValueFinal.toString(),
                        carbsValue = carbsValueFinal.toString(),
                        sugarValue = sugarValueFinal.toString(),
                        proteinValue = proteinValueFinal.toString()
                    )

                    // Save the FoodDailyEntity object to the database
                    GlobalScope.launch(Dispatchers.IO) {
                        CalowryApplication.database.foodDailyDao().insertFood(foodDailyEntity)
                    }

                    val intent = Intent(this@CameraActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                retry?.setOnClickListener {
                    // Show camera, gallery, and potraitMode
                    camera?.visibility = View.VISIBLE
                    gallery?.visibility = View.VISIBLE
                    potraitMode?.visibility = View.VISIBLE

                    // Hide save and retry buttons
                    save?.visibility = View.GONE
                    retry?.visibility = View.GONE
                    saveDesc?.visibility = View.GONE
                    retryDesc?.visibility = View.GONE
                }

            } else {
                // If no matching row is found, display an error or default values
                calories?.text = "N/A"
                carbs?.text = "N/A"
                sugar?.text = "N/A"
                protein?.text = "N/A"
            }

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                var image = data!!.extras!!["data"] as Bitmap?
                val dimension = min(image!!.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                imageView!!.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(image)
            } else {
                val dat = data!!.data
                var image: Bitmap? = null
                try {
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, dat)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                imageView!!.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
                classifyImage(image)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}