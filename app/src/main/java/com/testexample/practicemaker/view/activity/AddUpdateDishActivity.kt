package com.testexample.practicemaker.view.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.testexample.practicemaker.R
import com.testexample.practicemaker.application.FavDishapplication
import com.testexample.practicemaker.databinding.ActivityAddUpdateDishBinding
import com.testexample.practicemaker.databinding.DialogCustomImageSelectionBinding
import com.testexample.practicemaker.databinding.DialogCustomListBinding
import com.testexample.practicemaker.model.entities.FavDish
import com.testexample.practicemaker.utils.Constants
import com.testexample.practicemaker.view.adapter.CustomItemListAdapter
import com.testexample.practicemaker.viewmodel.FavDishViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityAddUpdateDishBinding
    companion object{
       private const val SAVE_IMAGE_DIRECTORY = "PracticeMakerImage"
    }
    private var mStringImagePath :String?= ""
    lateinit var dialogCamera :Dialog
    lateinit var dialogCustomList :Dialog
    private  var favDish : FavDish? =null
    private val mFavDishViewModel : FavDishViewModel by viewModels(){
        FavDishViewModel.FavDishViewModelFactory((application as FavDishapplication).respository)
    }
    private var title:String=""
    private var type:String=""
    private var category:String=""
    private var ingredient:String=""
    private var coockTime:String=""
    private var directionTocook:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        favDish = intent.getParcelableExtra("favdish")
        setContentView(mBinding.root)
        setUpActionBar()
        mBinding.icAddPhoto.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTimeInMinutes.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)
        if(favDish!=null) {
            mBinding.etTitle.setText(favDish?.title)
            mBinding.etType.setText(favDish?.type)
            mBinding.etCategory.setText(favDish?.category)
            mBinding.etIngredients.setText(favDish?.ingredients)
            mBinding.etCookingTimeInMinutes.setText(favDish?.cookingTime)
            mBinding.etDirectionToCook.setText(favDish?.directionToCook)
            Glide.with(this)
                    .load(favDish?.image)
                    .centerCrop()
                    .into(mBinding.imageMainView)
            mStringImagePath = favDish?.image
            mBinding.icAddPhoto.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))

        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setHomeButtonEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

     private fun storeImageInStorage(bitmap: Bitmap) :String{
         val wrapper = ContextWrapper(applicationContext)

         var file = wrapper.getDir(SAVE_IMAGE_DIRECTORY,Context.MODE_PRIVATE)
         file = File(file,"${UUID.randomUUID()}.JPG")

         try{
             val outStream : OutputStream = FileOutputStream(file)
             bitmap.compress(Bitmap.CompressFormat.JPEG,100,outStream)
             outStream.flush()
             outStream.close()
         }catch (e : IOException){
             e.printStackTrace()
         }
         return file.absolutePath
     }

    private fun customItemdialog(title : String, itemList : List<String>, selection : String){
        dialogCustomList = Dialog(this)
        val customDialogBinding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        dialogCustomList.setContentView(customDialogBinding.root)
        customDialogBinding.tvTitle.text = title
        customDialogBinding.rcvDialogList.layoutManager = LinearLayoutManager(this)
        val customListAdapter = CustomItemListAdapter(this, itemList, selection)
        customDialogBinding.rcvDialogList.adapter =  customListAdapter

        dialogCustomList.show()
    }

    val someActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                dialogCamera.dismiss()
                // There are no request codes
                val data: Intent? = result.data
                data?.extras.let {
                    val thumbnail : Bitmap = data?.extras!!.get("data") as Bitmap
                    //mBinding.imageMainView.setImageBitmap(thumbnail)
                    Glide.with(this)
                            .load(thumbnail)
                            .centerCrop()
                            .into(mBinding.imageMainView)
                    mStringImagePath = storeImageInStorage(thumbnail)
                    mBinding.icAddPhoto.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                }
            }
        }
    val galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                dialogCamera.dismiss()
                // There are no request codes
                val selectedPhotoUri: Uri? = result.data?.data
                selectedPhotoUri?.let {
                    Glide.with(this)
                            .load(it)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable>{
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    Log.e("TAG","Error Loading Image",e)

                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    resource?.let {
                                        val bitmap :Bitmap = resource.toBitmap()
                                        mStringImagePath =  storeImageInStorage(bitmap)
                                    }
                                    return false
                                }
                            })
                            .into(mBinding.imageMainView)
                    //mBinding.imageMainView.setImageURI(selectedPhotoUri)
                    mBinding.icAddPhoto.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                }
            }else if(result?.resultCode == Activity.RESULT_CANCELED){
                Log.e("Cancelled","User Canclled Image Selection")
            }
        }

    private fun customImageSelectionDialog(){
        dialogCamera = Dialog(this)
        val dialogBinding : DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialogCamera.setContentView(dialogBinding.root)

        dialogBinding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()){
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            someActivityResultLauncher.launch(cameraIntent)
                        }
                    }
                    if(report!!.areAllPermissionsGranted()){
                        Toast.makeText(this@AddUpdateDishActivity, "You Have Camera Permission now", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                   showRationalDialogForPermission()
                }


            }).onSameThread().check()
        }
        dialogBinding.tvGallery.setOnClickListener {
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryActivityResultLauncher.launch(galleryIntent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    Toast.makeText(this@AddUpdateDishActivity, "You have gallery permission denied to select the image", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                  showRationalDialogForPermission()
                }


            }).onSameThread().check()
        }
        dialogCamera.show()
    }

    private fun showRationalDialogForPermission(){
       AlertDialog.Builder(this).setMessage("It's Look Like you have turned off permissions "
               +  "required for thi feature it can be enable under Applications Setting")
           .setPositiveButton("GO TO SETTINGS"){
               _,_ ->
               try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri  =  Uri.fromParts("package",packageName,null)
                   intent.data = uri
                   startActivity(intent)
               }catch (e : ActivityNotFoundException){
                   e.printStackTrace()
               }
           }
           .setNegativeButton("Cancel"){ dialog, _ ->
               dialog.dismiss()
           }.show()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ic_add_photo ->{
                customImageSelectionDialog()
                return
            }
            R.id.et_type ->{
                customItemdialog(resources.getString(R.string.title_select_dish_type),
                        Constants.dishType(),
                        Constants.DISH_TYPE)
            }
            R.id.et_category ->{
                customItemdialog(resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategories(),
                        Constants.DISH_CATEGORY)
            }
            R.id.et_cooking_time_in_minutes ->{
                customItemdialog(resources.getString(R.string.title_select_cooking_time_in_minutes),
                        Constants.dishCookeTime(),
                        Constants.DISH_COOKING_TIME)
            }
            R.id.btn_add_dish ->{
                title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                type= mBinding.etType.text.toString().trim { it <= ' ' }
                category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                ingredient = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                coockTime = mBinding.etCookingTimeInMinutes.text.toString().trim { it <= ' ' }
                directionTocook = mBinding.etDirectionToCook.text.toString().trim { it <= ' ' }
                
                when{
                    TextUtils.isEmpty(mStringImagePath) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_image_empty),
                                Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_title_empty),
                                Toast.LENGTH_SHORT).show()
                    }

                    TextUtils.isEmpty(type) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_type_empty),
                                Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(category) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_category_empty),
                                Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(ingredient) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_ingredients_empty),
                                Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(coockTime) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_cooking_time_empty),
                                Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(directionTocook) -> {
                        Toast.makeText(this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_direction_empty),
                                Toast.LENGTH_SHORT).show()
                    }else ->{
                    val favDishDetails = FavDish(
                            mStringImagePath, Constants.DISH_IMAGE_SOURCE_LOCAL, title, type, category, ingredient,
                            coockTime, directionTocook, false)
                    if(favDish==null) {
                        mFavDishViewModel.insert(favDishDetails)
                        Log.e("isUpdate","No")
                    }else {
                        Log.e("isUpdate","yes")
                        Log.e("favDishDetails", favDishDetails.toString())
                        mFavDishViewModel.update(favDishDetails)
                    }
                    Toast.makeText(this@AddUpdateDishActivity, "You Successfully added your favorite dish details", Toast.LENGTH_SHORT).show()
                    finish()
                }
                }
            }
        }
    }

    fun selectedCustomItem(item : String, selection : String){
        when(selection){
              Constants.DISH_TYPE -> {
                  dialogCustomList.dismiss()
                  mBinding.etType.setText(item)
              }
            Constants.DISH_CATEGORY -> {
                dialogCustomList.dismiss()
                mBinding.etCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME -> {
            dialogCustomList.dismiss()
            mBinding.etCookingTimeInMinutes.setText(item)
        }
        }
    }

}



