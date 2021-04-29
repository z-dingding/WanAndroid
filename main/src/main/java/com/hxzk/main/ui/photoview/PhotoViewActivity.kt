package com.hxzk.main.ui.photoview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.hxzk.main.R
import com.hxzk.main.common.Const
import kotlinx.android.synthetic.main.activity_photo_view.*


class PhotoViewActivity : AppCompatActivity() {

      var currentUrl : String? = null
      var imgsUrl : ArrayList<String>? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
      intent?.let{
          currentUrl =  it.getStringExtra(Const.PhotoView.KEY_CURRENT_RUL)
          imgsUrl =   it.getStringArrayListExtra(Const.PhotoView.KEY_IMGS_RUL)
      }
        viewPager.adapter = ViewPagerAdapter()
    }

   inner class  ViewPagerAdapter : PagerAdapter(){
       override fun getCount(): Int {
       return imgsUrl?.size ?: 0
       }

       override fun isViewFromObject(view: View, `object`: Any): Boolean {
           return view === `object`
       }

       override fun instantiateItem(container: ViewGroup, position: Int): Any {
           val photoView = PhotoView(container.context)
           Glide.with(container.context).load(imgsUrl?.get(position)).into(photoView)
            container.addView(
               photoView,
               ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.MATCH_PARENT
           )
           return photoView
       }

       override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
           container.removeView(`object` as View)
       }
   }

}