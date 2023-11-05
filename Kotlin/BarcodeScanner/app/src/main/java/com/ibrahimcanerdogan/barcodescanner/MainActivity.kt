package com.ibrahimcanerdogan.barcodescanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.ProcessingException
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.ibrahimcanerdogan.barcodescanner.databinding.ActivityMainBinding
import com.ibrahimcanerdogan.barcodescanner.databinding.InfoDialogBinding
import com.ibrahimcanerdogan.barcodescanner.model.BaseBarcodeModel
import com.ibrahimcanerdogan.barcodescanner.model.CalendarEvent
import com.ibrahimcanerdogan.barcodescanner.model.ContactInfo
import com.ibrahimcanerdogan.barcodescanner.model.DriverLicense
import com.ibrahimcanerdogan.barcodescanner.model.Email
import com.ibrahimcanerdogan.barcodescanner.model.Geo
import com.ibrahimcanerdogan.barcodescanner.model.Phone
import com.ibrahimcanerdogan.barcodescanner.model.Sms
import com.ibrahimcanerdogan.barcodescanner.model.Url
import com.ibrahimcanerdogan.barcodescanner.model.Wifi
import com.ibrahimcanerdogan.barcodescanner.utils.BarcodeData
import com.ibrahimcanerdogan.barcodescanner.utils.ViewUtils.getAspectRatio

@SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var camera : Camera
    private val cameraSelector by lazy {
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    }

    private val cameraAspectRatio by lazy {
        val metrics = DisplayMetrics().also { binding.previewView.display.getRealMetrics(it) }
        metrics.getAspectRatio()
    }

    private val executor by lazy { ContextCompat.getMainExecutor(this) }

    private var isStop = false

    init {
        lifecycleScope.launchWhenResumed {
            viewModel.cameraProvider.observe(this@MainActivity) {
                it?.let {
                    bindToLifecycleUseCaseGroup()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        clickButtons()
    }

    private fun clickButtons() {
        binding.apply {
            bottomAppBar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menuFlashLight -> {
                        it.isChecked = !it.isChecked
                        camera.cameraControl.enableTorch(it.isChecked)
                        if (it.isChecked) {
                            it.icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.icon_flashlight_off)
                        } else {
                            it.icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.icon_flashlight_on)
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }

            bottomAppBar.setNavigationOnClickListener {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }

            buttonControl.setOnClickListener {
                if (isStop) {
                    bindToLifecycleUseCaseGroup()
                    binding.buttonControl.setImageResource(R.drawable.icon_stop)
                } else {
                    viewModel.cameraProvider.value?.unbindAll()
                    binding.buttonControl.setImageResource(R.drawable.icon_play)
                }
                isStop = !isStop
            }
        }
    }

    private fun bindToLifecycleUseCaseGroup() {
        val barcodeScanner = BarcodeScanning.getClient()

        val previewUseCase = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .setTargetAspectRatio(cameraAspectRatio)
            .build()
            .also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

        val analysisUseCase = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .setTargetAspectRatio(cameraAspectRatio)
            .build()
            .also {
                it.setAnalyzer(executor) { imageProxy ->
                    processImageProxy(barcodeScanner, imageProxy)
                }
            }

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(previewUseCase)
            .addUseCase(analysisUseCase)
            .build()

        try {
            viewModel.cameraProvider.value?.bindToLifecycle(this, cameraSelector, useCaseGroup)?.let {
                camera = it
            }
        } catch (e: Exception) {
            Log.e("MainActivity", e.localizedMessage ?: "")
        }
    }

    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        imageProxy.image?.let {  image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            processBarcode(barcodeScanner, inputImage)
                .addOnCompleteListener{
                    imageProxy.close()
                }
        }
    }

    private fun processBarcode(
        barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(),
        inputImage: InputImage
    ) = barcodeScanner.process(inputImage)
        .addOnSuccessListener { barcodes ->
            if (!barcodes.isNullOrEmpty()) {
                viewModel.cameraProvider.value?.unbindAll()
                viewModel.getBarcodeData(barcodes) { model, type ->
                    openInfoDialog(model, type)
                }
            }
        }.addOnFailureListener {
            Log.e("MainActivity", it.localizedMessage ?: "")
        }

    private fun openInfoDialog(model: BaseBarcodeModel, type: Int) {
        val infoDialogBinding = InfoDialogBinding.inflate(layoutInflater)

        BottomSheetDialog(this).apply {
            setContentView(infoDialogBinding.root)
            setOnShowListener {
                (infoDialogBinding.root.parent as ViewGroup).background = ColorDrawable(Color.TRANSPARENT)
                viewModel.cameraProvider.value?.unbindAll()
            }
            setOnDismissListener {
                bindToLifecycleUseCaseGroup()
            }
            show()
        }

        infoDialogBinding.apply {
            textViewValueType.text = BarcodeData.instance()?.getTypeValue()
            textViewFormat.text = BarcodeData.instance()?.getFormatType()

            setInfoDialogBarcodeData(type, model)
        }
    }

    private fun InfoDialogBinding.setInfoDialogBarcodeData(type: Int, model: BaseBarcodeModel) {
          when(type) {
              Barcode.TYPE_CONTACT_INFO -> {
                  setInfo(textViewTitle, (model as ContactInfo).contactName?.formattedName)
                  setInfo(textViewSubTitle1, model.contactTitle)
                  setInfo(textViewSubTitle2, model.contactPhone?.get(0)?.number)
                  setInfo(textViewSubTitle3, model.contactMail?.get(0)?.address)
                  setInfo(textViewSubTitle4, model.contactAddress?.get(0)?.addressLines?.get(0).toString())
                  setInfo(textViewSubTitle5, model.contactUrl?.get(0).toString())
                  setInfo(textViewSubTitle6, model.contactOrganization)

                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_EMAIL -> {
                  setInfo(textViewTitle, (model as Email).emailAddress.toString())
                  Linkify.addLinks(textViewTitle, Linkify.EMAIL_ADDRESSES)
                  setInfo(textViewSubTitle1, model.emailSubject)
                  setInfo(textViewSubTitle2, model.emailBody)
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_PHONE -> {
                  setInfo(textViewTitle, (model as Phone).phoneNumber.toString())
                  Linkify.addLinks(textViewTitle, Linkify.PHONE_NUMBERS)
                  setInfo(textViewSubTitle1, model.phoneType.toString(), "Phone Type :")
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_SMS -> {
                  setInfo(textViewTitle, (model as Sms).smsPhoneNumber.toString())
                  Linkify.addLinks(textViewTitle, Linkify.PHONE_NUMBERS)
                  setInfo(textViewSubTitle1, model.smsMessage.toString())
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_URL -> {
                  setInfo(textViewTitle, (model as Url).urlLink.toString())
                  Linkify.addLinks(textViewTitle, Linkify.WEB_URLS)
                  setInfo(textViewSubTitle1, model.urlTitle.toString())
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_WIFI -> {
                  setInfo(textViewTitle, (model as Wifi).wifiPassword.toString())
                  setInfo(textViewSubTitle1, model.wifiSsid.toString(), "SSID :")
                  setInfo(textViewSubTitle2, model.wifiEncryptionType.toString())
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_GEO -> {
                  val lat = (model as Geo).geoLatitude.toString()
                  val long = model.geoLongitude.toString()
                  val latlng = "($lat , $long)"
                  setInfo(textViewTitle, latlng)
                  setInfo(textViewSubTitle1, "Click to go to location.")

                  textViewSubTitle1.setOnClickListener {
                      val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$long")
                      val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                      mapIntent.setPackage("com.google.android.apps.maps")
                      mapIntent.resolveActivity(packageManager)?.let {
                          startActivity(mapIntent)
                      }
                  }
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_CALENDAR_EVENT -> {
                  setInfo(textViewTitle, (model as CalendarEvent).calendarSummary.toString())
                  setInfo(textViewSubTitle1, model.calendarLocation.toString(), "Location :")
                  setInfo(textViewSubTitle2, model.calendarOrganizer.toString())
                  setInfo(textViewSubTitle3, model.calendarStart.toString(), "Start Time :")
                  setInfo(textViewSubTitle4, model.calendarEnd.toString(), "End Time :")
                  setInfo(textViewSubTitle5, model.calendarDescription.toString(), "Description :")

                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
              Barcode.TYPE_DRIVER_LICENSE -> {
                  setInfo(textViewTitle, (model as DriverLicense).driverLicenseNumber.toString())

                  val driverName = "${model.driverFirstName.toString()} ${model.driverMiddleName.toString()} ${model.driverLastName.toString()}"
                  setInfo(textViewSubTitle1, driverName, "Full Name :")
                  setInfo(textViewSubTitle2, model.driverGender.toString(), "Gender :")
                  setInfo(textViewSubTitle3, model.driverBirthDate.toString(), "Birth Date :")

                  val driverAddress = "${model.driverAddressStreet.toString()} ${model.driverAddressCity.toString()} ${model.driverAddressState.toString()} ${model.driverAddressZip.toString()}"
                  setInfo(textViewSubTitle4, driverAddress, "Address :")
                  setInfo(textViewSubTitle5, model.driverIssueDate.toString(), "Issue Date :")
                  setInfo(textViewSubTitle6, model.driverExpiryDate.toString(), "Expiry Date :")
                  setInfo(textViewSubTitle7, model.driverIssuingCountry.toString(), "Issuing Country :")

                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              } else -> {
                  setInfo(textViewTitle, model.rawValue.toString())
                  buttonCopy.setOnClickListener {
                      copy(model.toString())
                  }
              }
          }
    }

    private fun copy(copiedText: String) {
        val clipboard : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("data", copiedText)
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(this, "Barcode Data Copied!", Toast.LENGTH_SHORT).show()
    }

    private fun setInfo(textView: TextView, data: String?, dataType: String? = "", visibleStatus: Int = View.VISIBLE) {
        var visible = visibleStatus
        textView.text = "$dataType $data"
        if (data.isNullOrEmpty()) visible = View.GONE
        textView.visibility = visible
    }

    // Permission
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { permission ->
            if (permission.value && permission.key == Manifest.permission.CAMERA) {
                createCameraProvider()
            } else if (permission.value && permission.key == Manifest.permission.READ_EXTERNAL_STORAGE) {
                getImageFromGallery()
            }
        }
    }

    private fun getImageFromGallery() {
        pickImageGallery.launch("image/*")
    }

    private val pickImageGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        val image : InputImage
        uri?.let {
            try {
                image = InputImage.fromFilePath(this, it)
                processBarcode(inputImage = image)
            } catch (e: Exception) {
                Log.e("MainActivity", e.localizedMessage ?: "")
            }
        }
    }

    private fun createCameraProvider() {
        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)
        cameraProvideFuture.addListener(
            { viewModel.setCameraProvider(cameraProvideFuture.get())},
            executor
        )
    }
}