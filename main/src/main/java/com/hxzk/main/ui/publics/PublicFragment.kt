package com.hxzk.main.ui.publics


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.databinding.FragmentPublicBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.generated.callback.OnClickListener
import com.hxzk.main.ui.adapter.NavigationItemAdapter
import com.hxzk.main.ui.adapter.PublicLeftAdapter
import com.hxzk.main.ui.search.SearchActivity
import com.hxzk.network.model.Children
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.base.extension.actionBundle
import com.hxzk.main.ui.adapter.PublicRightAdapter
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.main.util.RVMoveToMiddle
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX

class PublicFragment : Fragment(),View.OnClickListener{

    private val publicViewModel by viewModels<PublicViewModel> { getViewModelFactory()  }
    private lateinit var binding : FragmentPublicBinding
    lateinit var activity: MainActivity

    private lateinit var listAdapter: PublicLeftAdapter
    private lateinit var listRightAdapter: PublicRightAdapter

    private  val indexMap = mutableMapOf<Int,Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPublicBinding.inflate(inflater, container, false).apply {
            viewModel = publicViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        binding.lifecycleOwner = this
        setupListAdapter()
        initEvent()
        setupListAdapter()
    }

    private fun initEvent() {
        binding.ibScan.setOnClickListener(this)
        binding.searchView.setOnClickListener(this)
        publicViewModel.leftItemClick.observe(viewLifecycleOwner){
            //改变左侧item的选中状态及颜色
            listAdapter.itemSelectSytle(it)
            //点击的左侧RV的item移动到中间位置
            val pos = publicViewModel.leftItems.value?.indexOf(it)
            if (pos != null) {
                RVMoveToMiddle.moveToModdle(binding.leftRV,pos)
                //右侧Rv也移动到对应的位置
                //scrollToPositionWithOffset(),移动到指定下标的位置,并且位于可见屏幕的最上面
                // 参数，第一个是索引下标,也就是左侧的pos对应右侧大标题的下标()
                // 第二个参数item移动到指定位置后跟RecyclerView上边界或下边界之间的距离,默认0
                (binding.rightRV.layoutManager as LinearLayoutManager).scrollToPositionWithOffset((pos * 3),0)
            }
        }
        publicViewModel.leftItems.observe(viewLifecycleOwner){
            //将需要的数据通过map()提取出来
            val parentId : List<Int> = it.map { children -> children.id }
            publicViewModel.rightData(parentId)
        }
        publicViewModel.rightItemClick.observe(viewLifecycleOwner){
            val bean= CommonItemModel(it.id,it.link,it.title)
            val mBundle =Bundle()
            mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,bean)
            activity.actionBundle<X5MainActivity>(activity,mBundle)
        }
        publicViewModel.rightItems.observe(viewLifecycleOwner){
         //将数据进行再处理
            val datas = ArrayList<DataX>()
            it.forEachIndexed { index, articleListModel ->
                //将原list中每个公众号取出两个Item存储,算上标题是3个item
                //将第一个item的ItemType为1其余默认为0
                if(articleListModel.datas.size >= 2){
                    var titleItem = articleListModel.datas[2]
                    titleItem.itemType = 1
                    datas.add(titleItem)
                    datas.add(articleListModel.datas[0])
                    datas.add(articleListModel.datas[1])
                }else{
                    //如果有的公众号没有数据
                }
            }
            //刷新Adatpter
            if(datas.size>0){
                listRightAdapter.submitList(datas)
            }
        }
    }



    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        if (publicViewModel != null) {
            listAdapter = PublicLeftAdapter(publicViewModel)
            listRightAdapter = PublicRightAdapter(publicViewModel)
            binding.leftRV.adapter = listAdapter
            binding.rightRV.adapter = listRightAdapter

            binding.rightRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    //获取右侧列表第一个可见的Item的pos
                    val firstItemPos = ( binding.rightRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    //如果对3求余为0,那么说明是对应左侧的大类pos
                    if(firstItemPos % 3 == 0){
                        RVMoveToMiddle.moveToModdle(binding.leftRV,firstItemPos / 3)
                        publicViewModel.leftItems.value?.get(firstItemPos / 3)?.let { listAdapter.itemSelectSytle(it) }
                    }
                }
            })

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ib_scan ->   getString(R.string.common_tips_wating).sToast()
            R.id.searchView ->  startActivity(
                    Intent(activity, SearchActivity::class.java))
        }
    }

}