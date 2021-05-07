package com.hxzk.main.ui.mine


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hxzk.base.extension.action
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.logDebug
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.FragmentMineBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.integral.IntegralActivity
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.rank.RankActivity
import com.hxzk.main.util.CropCircleTransformation
import com.hxzk.network.model.UserInfoModel
import kotlinx.android.synthetic.main.fragment_mine.*
import java.nio.charset.StandardCharsets

class MineFragment : BaseFragment() , View.OnClickListener {
    val viewModel by viewModels<MineViewModel> { getViewModelFactory()}
    lateinit var databindding: FragmentMineBinding
    lateinit var activity: MainActivity

    /**
     * 获取本地的用户图片
     */
    val localUserPhoto
    get() = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databindding = FragmentMineBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
        }
        databindding.lifecycleOwner = this.viewLifecycleOwner
        return super.onCreateView(databindding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = requireActivity() as MainActivity
        //改变状态栏颜色
        activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.colorPrimary)
        initData()
        initEvent()
    }

    private fun initEvent() {
        iv_userPhoto.setOnClickListener(this)
        stv_integral.setOnClickListener(this)
        iv_integral.setOnClickListener(this)
    }

    private fun initData() {
        if(localUserPhoto.isNotEmpty()){
            Glide.with(this)
                    .load(localUserPhoto)
                    .apply(RequestOptions().transform(CropCircleTransformation(activity)))
                    .placeholder(R.drawable.loading_bg_circle)
                    .error(R.drawable.avatar_default)
                    .into(iv_userPhoto)
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.stv_integral ->
                if(viewModel.userInfo.value != null){
                activity.actionBundle<IntegralActivity>(activity,Bundle().also {
                    it.putInt(Const.IntegralList.KEY_COINCOUNT, viewModel.userInfo.value!!.coinCount)
                })
            }else{
                    getString(R.string.common_tips_pleasewaiting).sToast()
                }

            R.id.iv_userPhoto -> checkPromission()

            R.id.iv_integral -> activity.action<RankActivity>(activity)
        }
    }

    /**
     * 检查是否有访问图片的权限
     */
    private fun checkPromission() {

    }

}