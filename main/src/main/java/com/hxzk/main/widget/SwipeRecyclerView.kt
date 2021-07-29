package com.hxzk.main.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.ui.adapter.ShareViewHolder

/**
 * @author: hxzk_zjt
 * @date: 2021/7/28
 * 描述:列表项左滑点击按钮删除
 */
class SwipeRecyclerView : RecyclerView {
    /**
     * 最小滑动触发滑动的距离
     */
    private  var mTouchSlop:Int = 0
    /**
     * 最大滑动距离
     */
    private var maxLength = 0

    /**
     * 按下时x和y的坐标点
     */
    private var xDown = 0
    private  var yDown:Int = 0

    /**
     * 移动时x和y的坐标点
     */
    private  var xMove:Int = 0
    private  var yMove:Int = 0

    /**
     * 当前选中的item索引（这个很重要）
     */
    private var curSelectPosition = 0

    /**
     * 是否是第一次touch
     */
    private var isFirst = true

    /**
     * 当前选中的布局
     */
    lateinit var mCurItemLayout: ConstraintLayout
    /**
     *上一个选中的布局(记录上一个mCurItemLayout)
     */
    lateinit  var mLastItemLayout:ConstraintLayout

    /**
     * 默认隐藏的删除布局（和mItemDelete一样）
     */
    lateinit var mLlHidden : ConstraintLayout

    /**
     * 默认隐藏的删除布局（和mLlHidden一样）
     */
    lateinit var mItemDelete:ConstraintLayout


    /**
     * 记录连续移动的长度
     */
    private var mMoveWidth = 0

    /**
     * 隐藏部分长度
     */
    private var mHiddenWidth = 0

    lateinit var mScroller: Scroller

    constructor(mContext : Context) : super(mContext)

    constructor(mContext : Context, attrs: AttributeSet?) : super(mContext,attrs){
        //滑动到最小距离
        //它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        //滑动的最大距离(180 * 2.75 + 0.5f) =495
        maxLength = (180 * context.resources.displayMetrics.density + 0.5f).toInt()
        //初始化Scroller
        //初始化Scroller
        mScroller = Scroller(context, LinearInterpolator(context, null))
    }
    constructor(mContext : Context, attrs: AttributeSet?, defStyleAttr: Int) : super(mContext,attrs,defStyleAttr)



    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x.toInt()
        val y = e.y.toInt()
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //记录当前按下的坐标
                xDown = x
                yDown = y
                //计算选中哪个Item
                //第一个可见的item的索引
                val firstPosition: Int = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val itemRect = Rect()
                //获取recyclerview中所有的item总数
                val count = childCount
                var i = 0
                while (i < count) {
                    val child = getChildAt(i)
                    if (child.visibility == View.VISIBLE) {
                        //方法用来找到控件占据的矩形区域的矩形坐标。 参数outRect表示控件占据的矩形区域
                        child.getHitRect(itemRect)
                        if (itemRect.contains(x, y)) {
                            curSelectPosition = firstPosition + i
                            break
                        }
                    }
                    i++
                }
                if (isFirst) { //第一次时，不用重置上一次的Item
                    isFirst = false
                } else {
                    //屏幕再次接收到点击时，恢复上一次Item的状态
                    if (mLastItemLayout != null && mMoveWidth > 0) {
                        //将Item右移，恢复原位
                        scrollRight(mLastItemLayout, 0 - mMoveWidth)
                        //清空变量
                        mHiddenWidth = 0
                        mMoveWidth = 0
                    }
                }

                //取到当前选中的Item，赋给mCurItemLayout，以便对其进行左移
                val item = getChildAt(curSelectPosition - firstPosition)
                if (item != null) {
                    //获取当前选中的Item
                    val viewholder  = getChildViewHolder(item) as ShareViewHolder
                    mCurItemLayout = viewholder.rootView
                    //找到具体元素
//                    mLlHidden =viewholder.delLayoutItem
//                    mItemDelete =viewholder.delLayoutItem
                    mItemDelete.setOnClickListener(OnClickListener {
                        mRightListener?.onRightClick(curSelectPosition, "")
                    })

                    //这里将删除按钮的宽度设为可以移动的距离
                    mHiddenWidth = mLlHidden.getWidth()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                xMove = x
                yMove = y
                val dx: Int = xMove - xDown //为负时：手指向左滑动；为正时：手指向右滑动。这与Android的屏幕坐标定义有关
                val dy: Int = yMove - yDown //

                //左滑
                //左滑小于0且滑动的x和y要大于最小滑动距离
                if (dx < 0 && Math.abs(dx) > mTouchSlop && Math.abs(dy) < mTouchSlop) {
                    var newScrollX = Math.abs(dx)
                    if (mMoveWidth >= mHiddenWidth) { //超过了，不能再移动了
                        newScrollX = 0
                    } else if (mMoveWidth + newScrollX > mHiddenWidth) { //这次要超了，
                        newScrollX = mHiddenWidth - mMoveWidth
                    }
                    //左滑，每次滑动手指移动的距离
                    scrollLeft(mCurItemLayout, newScrollX)
                    //对移动的距离叠加
                    mMoveWidth += newScrollX
                } else if (dx > 0) { //右滑
                    //执行右滑，这里没有做跟随，瞬间恢复
                    scrollRight(mCurItemLayout, 0 - mMoveWidth)
                    mMoveWidth = 0
                }
            }
            MotionEvent.ACTION_UP -> {
                //返回当前滑动View左边界的位置，其实获取的值就是这块幕布在窗口左边界时的x坐标
                val scrollX: Int = mCurItemLayout.getScrollX()
                if (mHiddenWidth > mMoveWidth) {
                    val toX: Int = mHiddenWidth - mMoveWidth
                    if (scrollX > mHiddenWidth / 2) { //超过一半长度时松开，则自动滑到左侧
                        scrollLeft(mCurItemLayout, toX)
                        mMoveWidth = mHiddenWidth
                    } else { //不到一半时松开，则恢复原状
                        scrollRight(mCurItemLayout, 0 - mMoveWidth)
                        mMoveWidth = 0
                    }
                }
                mLastItemLayout = mCurItemLayout
            }
        }
        return super.onTouchEvent(e)
    }


    //主要功能是计算拖动的位移量、更新背景、设置要显示的屏幕
    // scrollBy 通俗的说就是相对我们当前位置偏移。
    //scrollTo 直接滚动到
    override fun computeScroll() {
        //计算有没有终止，需要通过mScroller.computeScrollOffset()
        if (mScroller.computeScrollOffset()) {
            mCurItemLayout.scrollBy(mScroller.currX, 0)
            invalidate()
        }
    }

    /**
     * 向左滑动
     */
    private fun scrollLeft(item: View, scorllX: Int) {
        item.scrollBy(scorllX, 0)
    }

    /**
     * 向右滑动
     */
    private fun scrollRight(item: View, scorllX: Int) {
        item.scrollBy(scorllX, 0)
    }



    /**
     * 删除的监听事件
     */
    private var mRightListener: OnRightClickListener? = null
    fun setRightClickListener(listener: OnRightClickListener?) {
        mRightListener = listener
    }
    interface OnRightClickListener {
        fun onRightClick(position: Int, id: String?)
    }
}