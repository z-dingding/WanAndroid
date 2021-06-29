package com.hxzk.main.ui.modifyuserinfo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.DrawableWrapper
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hxzk.base.extension.logWarn
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.GlobalUtil
import com.hxzk.base.util.Preference
import com.hxzk.main.R
import com.hxzk.main.album.AlbumActivity
import com.hxzk.main.callback.PermissionListener
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.ActivityModifyUserInfoBinding
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.util.*
import com.hxzk.main.util.ColorUtil.isBitmapDark
import com.hxzk.main.widget.cropper.CropImage
import com.hxzk.main.widget.cropper.CropImageView
import java.io.File
import java.io.IOException

class ModifyUserInfoActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityModifyUserInfoBinding

    /**
     * 用户名
     */
    private lateinit var userName: String

    /**
     * 用户头像本地路径,实际是Uri
     */
    private lateinit var localUserAvatarPath: String

    /**
     * 背景图本地路径,实际是Uri
     */
    private lateinit var localUserBgPath: String

    /**
     * 区分是用户头像(0)还是背景头像(1)
     */
    private var action: Int = 0

    /**
     * 临时存储拍照图片的uri
     */
    private var photoUri: Uri? = null

    /**
     * 裁剪过后保存在内部存储中的头像Uri
     */
    private var userAvatarUri: Uri? = null
    /**
     * 裁剪过后保存在内部存储中的背景Uri
     */
    private var userBgImageUri: Uri? = null

    /**
     * 判断用户是否修改了个人信息。
     * @return 修改了个人信息返回true，否则返回false。
     */
    private val isUserInfoChanged: Boolean
    get() = userAvatarUri != null  || userBgImageUri != null

    private var userBgLoadListener: RequestListener<Bitmap> = object : RequestListener<Bitmap> {


        override fun onResourceReady(bitmap: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            if (bitmap == null) {
                return false
            }
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height
            if (bitmapWidth <= 0 || bitmapHeight <= 0) {
                return false
            }
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    // 测量图片头部的颜色，以确定状态栏和导航栏的颜色
                    .setRegion(0, 0, bitmapWidth - 1, (bitmapHeight * 0.1).toInt())
                    .generate { palette ->
                        val isDark = isBitmapDark(palette, bitmap)
                        if (isDark) {
                            binding.save.setTextColor(ContextCompat.getColorStateList(this@ModifyUserInfoActivity, R.color.save_bg_light))
                            setToolbarAndStatusbarIconIntoLight()
                        } else {
                            binding.save.setTextColor(ContextCompat.getColorStateList(this@ModifyUserInfoActivity, R.color.save_bg_dark))
                            setToolbarAndStatusbarIconIntoDark()
                        }
                    }

            val left = (bitmapWidth * 0.2).toInt()
            val right = bitmapWidth - left
            val top = bitmapHeight / 2
            val bottom = bitmapHeight - 1
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .setRegion(left, top, right, bottom)
                    .generate { palette ->
                        val isDark = isBitmapDark(palette, bitmap)
                        val color: Int
                        color = if (isDark) {
                            ContextCompat.getColor(this@ModifyUserInfoActivity, R.color.text_white)
                        } else {
                            ContextCompat.getColor(this@ModifyUserInfoActivity, R.color.primary_text)
                        }
                        binding.userName.setTextColor(color)
                    }
            return false
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
            return false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_user_info)
        init()
    }

     private fun init() {
        intent?.let {
            localUserAvatarPath = it.getStringExtra(Const.ModifyUserInfo.KEY_USER_BG) ?: ""
            localUserBgPath = it.getStringExtra(Const.ModifyUserInfo.KEY_USER_AVATAR) ?: ""
            userName = it.getStringExtra(Const.ModifyUserInfo.KEY_USER_NAME) ?: ""
        }
        setupToolbar()
        title = ""
        transparentStatusBar()
        binding.userName.text = userName

        Glide.with(this)
                .load(localUserAvatarPath)
                //转化为圆型图
                .transform(CropCircleTransformation(this))
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.avatar_default)
                .into(binding.userAvatar)

        Glide.with(this)
                .asBitmap()
                .listener(userBgLoadListener)
                .load(localUserBgPath)
                .transform(BlurTransformation(this, 15))
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.bg_wall)
                .into(binding.userBg)

        binding.avatarCamera.setOnClickListener(this)
        binding.bgCamera.setOnClickListener(this)
        binding.save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.avatarCamera -> {
                action = TAKE_AVATAR_PICTURE
                showPictureDialog()
            }
            R.id.bgCamera -> {
                action = TAKE_BG_IMAGE_PICTURE
                showPictureDialog()
            }
            R.id.save -> {
               if(isUserInfoChanged){
                   if (userAvatarUri != null){
                       var saveUserAvatar by Preference(Const.ModifyUserInfo.KEY_USER_AVATAR,"")
                       saveUserAvatar = userAvatarUri.toString()
                   }
                   if (userBgImageUri!= null){
                       var saveUserBg by Preference(Const.ModifyUserInfo.KEY_USER_BG,"")
                       saveUserBg = userBgImageUri.toString()
                   }
                   //如果不是首次选择就一定会有图片保存到本地,localUserAvatarPath为空说明是首次进入操作
                  // if(localUserAvatarPath.isNotBlank()){
                       cleanExtrnalCacheFiles()
                       clearInnerCacheFiles()
                      finish()
                 //  }
               }else{
                   finish()
               }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                exit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * 通过点击返回键或者返回按钮退出时，检查用户信息是否有变更，如果有变更则弹出对话框询问用户是否确定放弃保存。
     */
    private fun exit() {
        if (isUserInfoChanged) {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
            builder.setMessage(GlobalUtil.getString(R.string.confirm_to_abort_userinfo_modification))
            builder.setPositiveButton(GlobalUtil.getString(R.string.abort)) { _, _ -> finish() }
            builder.setNegativeButton(GlobalUtil.getString(R.string.stay), null)
            builder.create().show()
        } else {
            finish()
        }
    }

    /**
     * 展示拍照或相册的Dialog
     */
    private fun showPictureDialog() {
        val items = arrayOf(resources.getString(R.string.take_photo), resources.getString(R.string.your_album))
        val builder = AlertDialog.Builder(this)
        when (action) {
            TAKE_AVATAR_PICTURE -> builder.setTitle(resources.getString(R.string.select_avatar))
            TAKE_BG_IMAGE_PICTURE -> builder.setTitle(resources.getString(R.string.select_bg_image))
        }
        builder.setItems(items) { _, which ->
            when (which) {
                0 -> refreshPermissionStatus(which)
                1 -> refreshPermissionStatus(which)
            }
        }
        builder.show()
    }

    /**
     * 检查权限
     */
    private fun refreshPermissionStatus(which: Int) {
        val requestPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        handlePermissions(requestPermission, object :
                PermissionListener {
            override fun onGranted() {
                if (which == 0) takePhoto() else chooseFromAlbum()
            }
            override fun onDenied(deniedPermissions: List<String>) {
                resources.getString(R.string.allow_storage_permission_please)
            }
        })
    }


    /**
     * 打开摄像头拍照。
     */
    private fun takePhoto() {
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            (GlobalUtil.getString(R.string.operation_failed_without_sdcard)).sToast()
            return
        }
        // 创建 File 对象，用于存储拍照后的图片/Android/data/你的应用包名/cache/
        val outputImage = File(externalCacheDir, AVATAR_PHOTO)
        try {
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
        } catch (e: IOException) {
            logWarn(TAG, e.message, e)
        }

        photoUri = if (AndroidVersion.hasNougat()) {
            // Android N 开始，将不允许在 App 间，使用 file:// 的方式，传递一个 File
            FileProvider.getUriForFile(this, "com.hxzk.app.fileprovider", outputImage)
        } else {
            Uri.fromFile(outputImage)
        }
        // 启动相机程序
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //MediaStore.EXTRA_OUTPUT参数就是转移保存地址的，对应Value中保存的URI就是指定的保存地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, TAKE_PHOTO)
    }

    /**
     * 从相册中选择图片。
     */
    private fun chooseFromAlbum() {
        val reqWidth = DeviceInfo.screenWidth
        var reqHeight = reqWidth
        if (action == TAKE_BG_IMAGE_PICTURE) {
            reqHeight = reqWidth * 5 / 7
        }
         AlbumActivity.actionStartForResult(this@ModifyUserInfoActivity, CHOOSE_FROM_ALBUM, reqWidth, reqHeight)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                cropPhoto(photoUri)
            }
            CHOOSE_FROM_ALBUM -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    showCroppedPhoto(data.getParcelableExtra(AlbumActivity.IMAGE_URI))
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
               (GlobalUtil.getString(R.string.crop_failed)).sToast()
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    showCroppedPhoto(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    (GlobalUtil.getString(R.string.crop_failed)).sToast()
                }
            }
        }
    }

    /**
     * 对指定图片进行裁剪。(不是压缩不改变图片大小(内存和硬盘))
     * 图片的uri地址。
     */
    private fun cropPhoto(uri: Uri?) {
        val reqWidth = DeviceInfo.screenWidth
        var reqHeight = reqWidth
        if (action == TAKE_BG_IMAGE_PICTURE) {
            reqHeight = reqWidth * 5 / 7
        }
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(reqWidth, reqHeight)
                .setActivityTitle(GlobalUtil.getString(R.string.crop))
                .setRequestedSize(reqWidth, reqHeight)
                .setCropMenuCropButtonIcon(R.drawable.ic_crop)
                .start(this)
    }

    /**
     * 裁剪完成后最终的战士图片的Uri
     */
    private fun showCroppedPhoto(imageUri: Uri?) {
        if (imageUri == null) return
        if (action == TAKE_AVATAR_PICTURE) {
            userAvatarUri = imageUri
            Glide.with(this)
                    .load(userAvatarUri)
                    .transform(CropCircleTransformation(this))
                    .error(R.drawable.avatar_default)
                    .into(binding.userAvatar)
            if (TextUtils.isEmpty(localUserBgPath) && userBgImageUri == null) {
                userBgImageUri = imageUri
                Glide.with(this)
                        .asBitmap()
                        .listener(userBgLoadListener)
                        .load(userAvatarUri)
                        .transform(BlurTransformation(this, 20))
                        .into(binding.userBg)
            }
        } else if (action == TAKE_BG_IMAGE_PICTURE) {
            userBgImageUri = imageUri
            Glide.with(this)
                    .asBitmap()
                    .listener(userBgLoadListener)
                    .load(userBgImageUri)
                    .transform(BlurTransformation(this, 20))
                    .into(binding.userBg)
        }
    }

    /**
     * 设置Toolbar和状态栏上的图标为深色。
     */
    private fun setToolbarAndStatusbarIconIntoDark() {
        ViewUtils.setLightStatusBar(window, binding.userBg)
        binding.toolbar?.let { ViewUtils.setToolbarIconColor(this, it, true) }
    }

    /**
     * 设置Toolbar和状态栏上的图标颜色为浅色。
     */
    private fun setToolbarAndStatusbarIconIntoLight() {
        ViewUtils.clearLightStatusBar(window, binding.userBg)
        binding.toolbar?.let { ViewUtils.setToolbarIconColor(this, it, false) }
    }


    /**
     * 清除拍照返回外部存储文件(作用仅仅是保存了原图)
     */
    private fun cleanExtrnalCacheFiles() {
        val outputImage = File(externalCacheDir, AVATAR_PHOTO)
        if (outputImage.exists()) {
            outputImage.delete()
        }
    }

    /**
     *如果最终没有选择图片则不需要操作
     * 清除除当前内部所有存储裁剪的文件(由于没有办法确定)
     */
   private fun clearInnerCacheFiles(){
       val croppedDir = File("$cacheDir/cropped")
       if (croppedDir.exists()) {
           val files = croppedDir.listFiles()
           if (files != null) {
               for (file in files) {
                   if(userBgImageUri !=null && userAvatarUri != null){
                       //如果替换头像和背景两张图片
                       if(file.toUri().toString() != userBgImageUri.toString() && file.toUri().toString() != userAvatarUri.toString()){
                           file.delete()
                       }
                   }else if (userBgImageUri !=null){
                       //如果替换了头像
                       if(file.toUri().toString() != userBgImageUri.toString() && file.toUri().toString() != localUserAvatarPath){
                           file.delete()
                       }
                   }else if(userAvatarUri != null){
                       //如果替换了背景
                       if(file.toUri().toString() != userAvatarUri.toString() && file.toUri().toString() != localUserBgPath){
                           file.delete()
                       }
                   }

               }
           }
       }
    }

    companion object {
        private const val TAG = "ModifyUserInfoActivity"

        const val TAKE_AVATAR_PICTURE = 0
        const val TAKE_BG_IMAGE_PICTURE = 1

        /**
         * 本地存储的用户头像的名称
         */
        const val AVATAR_PHOTO = "avatar_photo.jpg"
        /**
         * 本地存储的用户头像的名称
         */

        const val TAKE_PHOTO = 1000

        const val CHOOSE_FROM_ALBUM = 1001
    }
}