package com.hxzk.main.ui.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2

import com.hxzk.main.databinding.FragmentSystemBinding
import com.hxzk.main.event.TransparentStatusBarEvent
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.ViewPagerFragmentAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.system.child_nav.ChildNavigationFragment
import com.hxzk.main.ui.system.child_sys.ChildSystemFragment
import org.greenrobot.eventbus.EventBus
import java.util.*

class SystemFragment : BaseFragment(),View.OnClickListener{

    private  val sysViewModle by viewModels<SystemViewModel> { getViewModelFactory() }
    private lateinit var binding : FragmentSystemBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSystemBinding.inflate(inflater, container, false).also {
            it.viewModel = sysViewModle
        }
        return super.onCreateView(binding.root)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvSystemTile.setOnClickListener(this)
        binding.tvNavigationTitle.setOnClickListener(this)
        initVp()
    }



    private fun initVp() {
        val frags = LinkedList<Fragment>()
        frags.add(getInstance(ChildSystemFragment::class.java, null)!!)
        frags.add(getInstance(ChildNavigationFragment::class.java, null)!!)
        val vpAdapter =  ViewPagerFragmentAdapter(requireActivity(), frags)
        binding.vp.apply {
            adapter = vpAdapter
            //是否禁止左右滑动,true允许左右滑动
            isUserInputEnabled = true
            //如果允许滑动则默认不进行预加载
            //vp.offscreenPageLimit = 1
            //默认加载第一个Fragment,并隐藏中间页
            setCurrentItem(0,false)
        }
        //监听滑动
        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeTVColor(position)
            }

        })
    }

   private  fun changeTVColor(index : Int){
       binding.tvSystemTile.isSelected = false
       binding.tvNavigationTitle.isSelected = false
       when(index){
         0 ->  binding.tvSystemTile.isSelected = true
         1 ->  binding.tvNavigationTitle.isSelected = true
       }
   }
    private fun exchangeFrag(index : Int){
        binding.vp.setCurrentItem(index,false)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.tvSystemTile ->  {changeTVColor(0)  ;exchangeFrag(0)}
            binding.tvNavigationTitle  -> {changeTVColor(1) ;exchangeFrag(1)}
        }
    }



}