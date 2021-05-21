package com.hxzk.main.ui.mine


import android.net.Uri
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
import com.hxzk.base.util.Preference
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.common.Const.ModifyUserInfo.Companion.KEY_USER_AVATAR
import com.hxzk.main.common.Const.ModifyUserInfo.Companion.KEY_USER_BG
import com.hxzk.main.common.Const.ModifyUserInfo.Companion.KEY_USER_NAME
import com.hxzk.main.databinding.FragmentMineBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.integral.IntegralActivity
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.modifyuserinfo.ModifyUserInfoActivity
import com.hxzk.main.ui.rank.RankActivity
import com.hxzk.main.util.CropCircleTransformation
import com.hxzk.network.model.UserInfoModel
import kotlinx.android.synthetic.main.fragment_mine.*
import java.nio.charset.StandardCharsets

class MineFragment : BaseFragment() , View.OnClickListener {
    val viewModel by viewModels<MineViewModel> { getViewModelFactory()}
    lateinit var databindding: FragmentMineBinding
    lateinit var activity: MainActivity


    private var userAvatarUri: String? = null
    private var userBgImageUri: String? = null


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
        initEvent()
    }

    private fun initEvent() {
        iv_userPhoto.setOnClickListener(this)
        stv_integral.setOnClickListener(this)
        iv_integral.setOnClickListener(this)
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

            R.id.iv_userPhoto ->
                activity.actionBundle<ModifyUserInfoActivity>(activity,Bundle().apply {
                putString(KEY_USER_BG,userAvatarUri)
                putString(KEY_USER_AVATAR,userBgImageUri)
                putString(KEY_USER_NAME,viewModel.userInfo.value?.username)
            })

            R.id.iv_integral -> activity.action<RankActivity>(activity)
        }
    }

    override fun onStart() {
        super.onStart()
        var saveUserAvatar by Preference(Const.ModifyUserInfo.KEY_USER_AVATAR,"")
        userAvatarUri = saveUserAvatar
        var saveUserBg by Preference(Const.ModifyUserInfo.KEY_USER_BG,"")
        userBgImageUri = saveUserBg

        Glide.with(this)
                .load(userAvatarUri)
                .apply(RequestOptions().transform(CropCircleTransformation(activity)))
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.avatar_default)
                .into(iv_userPhoto)

        Glide.with(this)
                .load(userBgImageUri)
                .apply(RequestOptions().transform(CropCircleTransformation(activity)))
                .placeholder(R.drawable.bg_wall)
                .error(R.drawable.bg_wall)
                .into(iv_bgWall)
    }

}