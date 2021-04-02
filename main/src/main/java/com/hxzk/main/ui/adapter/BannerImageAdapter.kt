package com.hxzk.main.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hxzk.network.model.HomeBanner
import com.youth.banner.adapter.BannerAdapter


/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class BannerImageAdapter(banners: List<HomeBanner> ,val mContext : Context) : BannerAdapter<HomeBanner, BannerImageAdapter.BannerViewHolder>(
    banners
){

    override fun onCreateHolder(
        parent: ViewGroup?,
        viewType: Int
    ): BannerViewHolder {
        val imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: HomeBanner?,
        position: Int,
        size: Int
    ) {
        Glide.with(mContext)
            .load(data?.imagePath)
            .into(holder?.imageView!!)
    }

    class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
         var imageView: ImageView = view
    }
}