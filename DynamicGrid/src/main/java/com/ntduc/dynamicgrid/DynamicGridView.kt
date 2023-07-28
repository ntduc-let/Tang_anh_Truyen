package com.ntduc.dynamicgrid

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ListAdapter
import com.ntduc.dynamicgrid.DynamicGridUtils.getViewX
import com.ntduc.dynamicgrid.DynamicGridUtils.getViewY
import com.ntduc.dynamicgrid.DynamicGridView
import com.ntduc.dynamicgrid.DynamicGridView.SwitchCellAnimator
import java.util.LinkedList
import java.util.Stack
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DynamicGridView : GridView {

    companion object {
        private const val INVALID_ID = -1
        private const val MOVE_DURATION = 300
        private const val SMOOTH_SCROLL_AMOUNT_AT_EDGE = 8

        val isPreLollipop: Boolean
            get() = false
    }

    private var mHoverCell: BitmapDrawable? = null
    private var mHoverCellCurrentBounds: Rect? = null
    private var mHoverCellOriginalBounds: Rect? = null

    private var mTotalOffsetY = 0
    private var mTotalOffsetX = 0

    private var mDownX = -1
    private var mDownY = -1
    private var mLastEventY = -1
    private var mLastEventX = -1

    //used to distinguish straight line and diagonal switching
    private var mOverlapIfSwitchStraightLine = 0

    private val idList: MutableList<Long> = ArrayList()

    private var mMobileItemId = INVALID_ID.toLong()

    private var mCellIsMobile = false
    private var mActivePointerId = INVALID_ID

    private var mIsMobileScrolling = false
    private var mSmoothScrollAmountAtEdge = 0
    private var mIsWaitingForScrollFinish = false
    private var mScrollState = OnScrollListener.SCROLL_STATE_IDLE

    var isEditMode = false
        private set

    private val mWobbleAnimators: MutableList<ObjectAnimator> = LinkedList()
    private var mHoverAnimation = false
    private var mReorderAnimation = false
    var isWobbleInEditMode = true
    var isEditModeEnabled = true

    private var mUserScrollListener: OnScrollListener? = null
    private var mDropListener: OnDropListener? = null
    private var mDragListener: OnDragListener? = null
    private var mEditModeChangeListener: OnEditModeChangeListener? = null

    private var mUserItemClickListener: OnItemClickListener? = null
    private val mLocalItemClickListener: OnItemClickListener = OnItemClickListener { parent, view, position, id ->
        if (!isEditMode && isEnabled && mUserItemClickListener != null) {
            mUserItemClickListener!!.onItemClick(parent, view, position, id)
        }
    }

    private var mUndoSupportEnabled = false
    private var mModificationStack: Stack<DynamicGridModification>? = null
    private var mCurrentModification: DynamicGridModification? = null

    private var mSelectedItemBitmapCreationListener: OnSelectedItemBitmapCreationListener? = null
    private var mMobileView: View? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    override fun setOnScrollListener(scrollListener: OnScrollListener) {
        mUserScrollListener = scrollListener
    }

    fun setOnDropListener(dropListener: OnDropListener?) {
        mDropListener = dropListener
    }

    fun setOnDragListener(dragListener: OnDragListener?) {
        mDragListener = dragListener
    }

    /**
     * Start edit mode with position. Useful for start edit mode in
     */
    fun startEditMode(position: Int = -1) {
        if (!isEditModeEnabled) return
        requestDisallowInterceptTouchEvent(true)
        if (isPostHoneycomb && isWobbleInEditMode) startWobbleAnimation()
        if (position != -1) {
            startDragAtPosition(position)
        }
        isEditMode = true
        if (mEditModeChangeListener != null) mEditModeChangeListener!!.onEditModeChanged(true)
    }

    fun stopEditMode() {
        isEditMode = false
        requestDisallowInterceptTouchEvent(false)
        if (isPostHoneycomb && isWobbleInEditMode) stopWobble(true)
        if (mEditModeChangeListener != null) mEditModeChangeListener!!.onEditModeChanged(false)
    }

    fun setOnEditModeChangeListener(editModeChangeListener: OnEditModeChangeListener?) {
        mEditModeChangeListener = editModeChangeListener
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        mUserItemClickListener = listener
        super.setOnItemClickListener(mLocalItemClickListener)
    }

    var isUndoSupportEnabled: Boolean
        get() = mUndoSupportEnabled
        set(undoSupportEnabled) {
            if (mUndoSupportEnabled != undoSupportEnabled) {
                mModificationStack = if (undoSupportEnabled) Stack() else null
            }
            mUndoSupportEnabled = undoSupportEnabled
        }

    fun undoLastModification() {
        if (mUndoSupportEnabled) {
            if (mModificationStack != null && !mModificationStack!!.isEmpty()) {
                val modification = mModificationStack!!.pop()
                undoModification(modification)
            }
        }
    }

    fun undoAllModifications() {
        if (mUndoSupportEnabled) {
            if (mModificationStack != null && !mModificationStack!!.isEmpty()) {
                while (!mModificationStack!!.isEmpty()) {
                    val modification = mModificationStack!!.pop()
                    undoModification(modification)
                }
            }
        }
    }

    fun hasModificationHistory(): Boolean {
        if (mUndoSupportEnabled) {
            if (mModificationStack != null && !mModificationStack!!.isEmpty()) {
                return true
            }
        }
        return false
    }

    fun clearModificationHistory() {
        mModificationStack!!.clear()
    }

    fun setOnSelectedItemBitmapCreationListener(selectedItemBitmapCreationListener: OnSelectedItemBitmapCreationListener?) {
        mSelectedItemBitmapCreationListener = selectedItemBitmapCreationListener
    }

    private fun undoModification(modification: DynamicGridModification) {
        for (transition in modification.getTransitions()) {
            reorderElements(transition!!.second, transition.first)
        }
    }

    private fun startWobbleAnimation() {
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v != null && java.lang.Boolean.TRUE != v.getTag(R.id.dgv_wobble_tag)) {
                if (i % 2 == 0) animateWobble(v) else animateWobbleInverse(v)
                v.setTag(R.id.dgv_wobble_tag, true)
            }
        }
    }

    private fun stopWobble(resetRotation: Boolean) {
        for (wobbleAnimator in mWobbleAnimators) {
            wobbleAnimator.cancel()
        }
        mWobbleAnimators.clear()
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v != null) {
                if (resetRotation) v.rotation = 0f
                v.setTag(R.id.dgv_wobble_tag, false)
            }
        }
    }

    private fun restartWobble() {
        stopWobble(false)
        startWobbleAnimation()
    }

    fun init(context: Context) {
        super.setOnScrollListener(mScrollListener)
        val metrics = context.resources.displayMetrics
        mSmoothScrollAmountAtEdge = (SMOOTH_SCROLL_AMOUNT_AT_EDGE * metrics.density + 0.5f).toInt()
        mOverlapIfSwitchStraightLine = resources.getDimensionPixelSize(R.dimen.dgv_overlap_if_switch_straight_line)
    }

    private fun animateWobble(v: View) {
        val animator = createBaseWobble(v)
        animator.setFloatValues(-2f, 2f)
        animator.start()
        mWobbleAnimators.add(animator)
    }

    private fun animateWobbleInverse(v: View) {
        val animator = createBaseWobble(v)
        animator.setFloatValues(2f, -2f)
        animator.start()
        mWobbleAnimators.add(animator)
    }

    private fun createBaseWobble(v: View): ObjectAnimator {
        if (!isPreLollipop) v.setLayerType(LAYER_TYPE_SOFTWARE, null)
        val animator = ObjectAnimator()
        animator.duration = 180
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator.setPropertyName("rotation")
        animator.target = v
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                v.setLayerType(LAYER_TYPE_NONE, null)
            }
        })
        return animator
    }

    private fun reorderElements(originalPosition: Int, targetPosition: Int) {
        if (mDragListener != null) mDragListener!!.onDragPositionsChanged(originalPosition, targetPosition)
        adapterInterface.reorderItems(originalPosition, targetPosition)
    }

    private val columnCount: Int
        get() = adapterInterface.columnCount
    private val adapterInterface: DynamicGridAdapterInterface
        get() = adapter as DynamicGridAdapterInterface

    /**
     * Creates the hover cell with the appropriate bitmap and of appropriate
     * size. The hover cell's BitmapDrawable is drawn on top of the bitmap every
     * single time an invalidate call is made.
     */
    private fun getAndAddHoverView(v: View): BitmapDrawable {
        val w = v.width
        val h = v.height
        val top = v.top
        val left = v.left
        val b = getBitmapFromView(v)
        val drawable = BitmapDrawable(resources, b)
        mHoverCellOriginalBounds = Rect(left, top, left + w, top + h)
        mHoverCellCurrentBounds = Rect(mHoverCellOriginalBounds)
        drawable.bounds = mHoverCellCurrentBounds!!
        return drawable
    }

    /**
     * Returns a bitmap showing a screenshot of the view passed in.
     */
    private fun getBitmapFromView(v: View): Bitmap {
        val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        return bitmap
    }

    private fun updateNeighborViewsForId(itemId: Long) {
        idList.clear()
        val draggedPos = getPositionForID(itemId)
        for (pos in firstVisiblePosition..lastVisiblePosition) {
            if (draggedPos != pos && adapterInterface.canReorder(pos)) {
                idList.add(getId(pos))
            }
        }
    }

    /**
     * Retrieves the position in the grid corresponding to `itemId`
     */
    fun getPositionForID(itemId: Long): Int {
        val v = getViewForId(itemId)
        return v?.let { getPositionForView(it) } ?: -1
    }

    fun getViewForId(itemId: Long): View? {
        val firstVisiblePosition = firstVisiblePosition
        val adapter = adapter
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            val position = firstVisiblePosition + i
            val id = adapter.getItemId(position)
            if (id == itemId) {
                return v
            }
        }
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x.toInt()
                mDownY = event.y.toInt()
                mActivePointerId = event.getPointerId(0)
                if (isEditMode && isEnabled) {
                    layoutChildren()
                    val position = pointToPosition(mDownX, mDownY)
                    startDragAtPosition(position)
                } else if (!isEnabled) {
                    return false
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_ID) return super.onTouchEvent(event)

                val pointerIndex = event.findPointerIndex(mActivePointerId)
                mLastEventY = event.getY(pointerIndex).toInt()
                mLastEventX = event.getX(pointerIndex).toInt()
                val deltaY = mLastEventY - mDownY
                val deltaX = mLastEventX - mDownX
                if (mCellIsMobile) {
                    mHoverCellCurrentBounds!!.offsetTo(
                        mHoverCellOriginalBounds!!.left + deltaX + mTotalOffsetX,
                        mHoverCellOriginalBounds!!.top + deltaY + mTotalOffsetY
                    )
                    mHoverCell!!.bounds = mHoverCellCurrentBounds!!
                    invalidate()
                    handleCellSwitch()
                    mIsMobileScrolling = false
                    handleMobileCellScroll()
                    return false
                }
            }

            MotionEvent.ACTION_UP -> {
                touchEventsEnded()
                if (mUndoSupportEnabled) {
                    if (mCurrentModification != null && mCurrentModification!!.getTransitions().isNotEmpty()) {
                        mModificationStack!!.push(mCurrentModification)
                        mCurrentModification = DynamicGridModification()
                    }
                }
                if (mHoverCell != null) {
                    if (mDropListener != null) {
                        mDropListener!!.onActionDrop()
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                touchEventsCancelled()
                if (mHoverCell != null) {
                    if (mDropListener != null) {
                        mDropListener!!.onActionDrop()
                    }
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                /* If a multitouch event took place and the original touch dictating
                 * the movement of the hover cell has ended, then the dragging event
                 * ends and the hover cell is animated to its corresponding position
                 * in the listview. */
                val pointerIndex = event.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr
                        MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    touchEventsEnded()
                }
            }

            else -> {}
        }
        return super.onTouchEvent(event)
    }

    private fun startDragAtPosition(position: Int) {
        mTotalOffsetY = 0
        mTotalOffsetX = 0
        val itemNum = position - firstVisiblePosition
        val selectedView = getChildAt(itemNum)
        if (selectedView != null) {
            mMobileItemId = adapter.getItemId(position)
            if (mSelectedItemBitmapCreationListener != null) mSelectedItemBitmapCreationListener!!.onPreSelectedItemBitmapCreation(selectedView, position, mMobileItemId)
            mHoverCell = getAndAddHoverView(selectedView)
            if (mSelectedItemBitmapCreationListener != null) mSelectedItemBitmapCreationListener!!.onPostSelectedItemBitmapCreation(selectedView, position, mMobileItemId)
            if (isPostHoneycomb) selectedView.visibility = INVISIBLE
            mCellIsMobile = true
            updateNeighborViewsForId(mMobileItemId)
            if (mDragListener != null) {
                mDragListener!!.onDragStarted(position)
            }
        }
    }

    private fun handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds)
    }

    fun handleMobileCellScroll(r: Rect?): Boolean {
        val offset = computeVerticalScrollOffset()
        val height = height
        val extent = computeVerticalScrollExtent()
        val range = computeVerticalScrollRange()
        val hoverViewTop = r!!.top
        val hoverHeight = r.height()
        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0)
            return true
        }
        if (hoverViewTop + hoverHeight >= height && offset + extent < range) {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0)
            return true
        }
        return false
    }

    override fun setAdapter(adapter: ListAdapter) {
        super.setAdapter(adapter)
    }

    private fun touchEventsEnded() {
        val mobileView = getViewForId(mMobileItemId)
        if (mobileView != null && (mCellIsMobile || mIsWaitingForScrollFinish)) {
            mCellIsMobile = false
            mIsWaitingForScrollFinish = false
            mIsMobileScrolling = false
            mActivePointerId = INVALID_ID

            // If the autoscroller has not completed scrolling, we need to wait for it to
            // finish in order to determine the final location of where the hover cell
            // should be animated to.
            if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
                mIsWaitingForScrollFinish = true
                return
            }
            mHoverCellCurrentBounds!!.offsetTo(mobileView.left, mobileView.top)
            animateBounds(mobileView)
        } else {
            touchEventsCancelled()
        }
    }

    private fun animateBounds(mobileView: View) {
        val sBoundEvaluator: TypeEvaluator<Rect> = object : TypeEvaluator<Rect> {
            override fun evaluate(fraction: Float, startValue: Rect, endValue: Rect): Rect {
                return Rect(
                    interpolate(startValue.left, endValue.left, fraction),
                    interpolate(startValue.top, endValue.top, fraction),
                    interpolate(startValue.right, endValue.right, fraction),
                    interpolate(startValue.bottom, endValue.bottom, fraction)
                )
            }

            fun interpolate(start: Int, end: Int, fraction: Float): Int {
                return (start + fraction * (end - start)).toInt()
            }
        }
        val hoverViewAnimator = ObjectAnimator.ofObject(
            mHoverCell, "bounds",
            sBoundEvaluator, mHoverCellCurrentBounds
        )
        hoverViewAnimator.addUpdateListener { invalidate() }
        hoverViewAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                mHoverAnimation = true
                updateEnableState()
            }

            override fun onAnimationEnd(animation: Animator) {
                mHoverAnimation = false
                updateEnableState()
                reset(mobileView)
            }
        })
        hoverViewAnimator.start()
    }

    private fun reset(mobileView: View?) {
        idList.clear()
        mMobileItemId = INVALID_ID.toLong()
        mobileView!!.visibility = VISIBLE
        mHoverCell = null
        if (isPostHoneycomb && isWobbleInEditMode) {
            if (isEditMode) {
                restartWobble()
            } else {
                stopWobble(true)
            }
        }
        //ugly fix for unclear disappearing items after reorder
        for (i in 0 until lastVisiblePosition - firstVisiblePosition) {
            val child = getChildAt(i)
            if (child != null) {
                child.visibility = VISIBLE
            }
        }
        invalidate()
    }

    private fun updateEnableState() {
        isEnabled = !mHoverAnimation && !mReorderAnimation
    }

    /**
     * Seems that GridView before HONEYCOMB not support stable id in proper way.
     * That cause bugs on view recycle if we will animate or change visibility state for items.
     *
     * @return
     */
    private val isPostHoneycomb: Boolean
        get() = true

    private fun touchEventsCancelled() {
        val mobileView = getViewForId(mMobileItemId)
        if (mCellIsMobile) {
            reset(mobileView)
        }
        mCellIsMobile = false
        mIsMobileScrolling = false
        mActivePointerId = INVALID_ID
    }

    private fun handleCellSwitch() {
        val deltaY = mLastEventY - mDownY
        val deltaX = mLastEventX - mDownX
        val deltaYTotal = mHoverCellOriginalBounds!!.centerY() + mTotalOffsetY + deltaY
        val deltaXTotal = mHoverCellOriginalBounds!!.centerX() + mTotalOffsetX + deltaX
        mMobileView = getViewForId(mMobileItemId)
        var targetView: View? = null
        var vX = 0f
        var vY = 0f
        val mobileColumnRowPair = getColumnAndRowForView(mMobileView)
        for (id in idList) {
            val view = getViewForId(id)
            if (view != null) {
                val targetColumnRowPair = getColumnAndRowForView(view)
                if (aboveRight(targetColumnRowPair, mobileColumnRowPair)
                    && deltaYTotal < view.bottom
                    && deltaXTotal > view.left
                    || aboveLeft(targetColumnRowPair, mobileColumnRowPair)
                    && deltaYTotal < view.bottom
                    && deltaXTotal < view.right
                    || belowRight(targetColumnRowPair, mobileColumnRowPair)
                    && deltaYTotal > view.top
                    && deltaXTotal > view.left
                    || belowLeft(targetColumnRowPair, mobileColumnRowPair)
                    && deltaYTotal > view.top
                    && deltaXTotal < view.right
                    || (above(targetColumnRowPair, mobileColumnRowPair)
                            && deltaYTotal < view.bottom - mOverlapIfSwitchStraightLine)
                    || (below(targetColumnRowPair, mobileColumnRowPair)
                            && deltaYTotal > view.top + mOverlapIfSwitchStraightLine)
                    || (right(targetColumnRowPair, mobileColumnRowPair)
                            && deltaXTotal > view.left + mOverlapIfSwitchStraightLine)
                    || (left(targetColumnRowPair, mobileColumnRowPair)
                            && deltaXTotal < view.right - mOverlapIfSwitchStraightLine)
                ) {
                    val xDiff = abs(getViewX(view) - getViewX(mMobileView!!))
                    val yDiff = abs(getViewY(view) - getViewY(mMobileView!!))
                    if (xDiff >= vX && yDiff >= vY) {
                        vX = xDiff
                        vY = yDiff
                        targetView = view
                    }
                }
            }
        }
        if (targetView != null) {
            val originalPosition = getPositionForView(mMobileView)
            val targetPosition = getPositionForView(targetView)
            val adapter = adapterInterface
            if (targetPosition == INVALID_POSITION
                || !adapter.canReorder(originalPosition)
                || !adapter.canReorder(targetPosition)
            ) {
                updateNeighborViewsForId(mMobileItemId)
                return
            }
            reorderElements(originalPosition, targetPosition)
            if (mUndoSupportEnabled) {
                mCurrentModification!!.addTransition(originalPosition, targetPosition)
            }
            mDownY = mLastEventY
            mDownX = mLastEventX
            val switchCellAnimator: SwitchCellAnimator = if (isPostHoneycomb && isPreLollipop) //Between Android 3.0 and Android L
                KitKatSwitchCellAnimator(deltaX, deltaY) else if (isPreLollipop) //Before Android 3.0
                PreHoneycombCellAnimator(deltaX, deltaY) else  //Android L
                LSwitchCellAnimator(deltaX, deltaY)
            updateNeighborViewsForId(mMobileItemId)
            switchCellAnimator.animateSwitchCell(originalPosition, targetPosition)
        }
    }

    private interface SwitchCellAnimator {
        fun animateSwitchCell(originalPosition: Int, targetPosition: Int)
    }

    private inner class PreHoneycombCellAnimator(private val mDeltaX: Int, private val mDeltaY: Int) : SwitchCellAnimator {
        override fun animateSwitchCell(originalPosition: Int, targetPosition: Int) {
            mTotalOffsetY += mDeltaY
            mTotalOffsetX += mDeltaX
        }
    }

    /**
     * A [org.askerov.dynamicgrid.DynamicGridView.SwitchCellAnimator] for versions KitKat and below.
     */
    private inner class KitKatSwitchCellAnimator(private val mDeltaX: Int, private val mDeltaY: Int) : SwitchCellAnimator {
        override fun animateSwitchCell(originalPosition: Int, targetPosition: Int) {
            assert(mMobileView != null)
            viewTreeObserver.addOnPreDrawListener(AnimateSwitchViewOnPreDrawListener(mMobileView!!, originalPosition, targetPosition))
            mMobileView = getViewForId(mMobileItemId)
        }

        private inner class AnimateSwitchViewOnPreDrawListener(private val mPreviousMobileView: View, private val mOriginalPosition: Int, private val mTargetPosition: Int) : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                mTotalOffsetY += mDeltaY
                mTotalOffsetX += mDeltaX
                animateReorder(mOriginalPosition, mTargetPosition)
                mPreviousMobileView.visibility = VISIBLE
                if (mMobileView != null) {
                    mMobileView!!.visibility = INVISIBLE
                }
                return true
            }
        }
    }

    /**
     * A [org.askerov.dynamicgrid.DynamicGridView.SwitchCellAnimator] for versions L and above.
     */
    private inner class LSwitchCellAnimator(private val mDeltaX: Int, private val mDeltaY: Int) : SwitchCellAnimator {
        override fun animateSwitchCell(originalPosition: Int, targetPosition: Int) {
            viewTreeObserver.addOnPreDrawListener(AnimateSwitchViewOnPreDrawListener(originalPosition, targetPosition))
        }

        private inner class AnimateSwitchViewOnPreDrawListener(private val mOriginalPosition: Int, private val mTargetPosition: Int) : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                mTotalOffsetY += mDeltaY
                mTotalOffsetX += mDeltaX
                animateReorder(mOriginalPosition, mTargetPosition)
                assert(mMobileView != null)
                mMobileView!!.visibility = VISIBLE
                mMobileView = getViewForId(mMobileItemId)
                assert(mMobileView != null)
                mMobileView!!.visibility = INVISIBLE
                return true
            }
        }
    }

    private fun belowLeft(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y > mobileColumnRowPair.y && targetColumnRowPair.x < mobileColumnRowPair.x
    }

    private fun belowRight(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y > mobileColumnRowPair.y && targetColumnRowPair.x > mobileColumnRowPair.x
    }

    private fun aboveLeft(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y < mobileColumnRowPair.y && targetColumnRowPair.x < mobileColumnRowPair.x
    }

    private fun aboveRight(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y < mobileColumnRowPair.y && targetColumnRowPair.x > mobileColumnRowPair.x
    }

    private fun above(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y < mobileColumnRowPair.y && targetColumnRowPair.x == mobileColumnRowPair.x
    }

    private fun below(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y > mobileColumnRowPair.y && targetColumnRowPair.x == mobileColumnRowPair.x
    }

    private fun right(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y == mobileColumnRowPair.y && targetColumnRowPair.x > mobileColumnRowPair.x
    }

    private fun left(targetColumnRowPair: Point, mobileColumnRowPair: Point): Boolean {
        return targetColumnRowPair.y == mobileColumnRowPair.y && targetColumnRowPair.x < mobileColumnRowPair.x
    }

    private fun getColumnAndRowForView(view: View?): Point {
        val pos = getPositionForView(view)
        val columns = columnCount
        val column = pos % columns
        val row = pos / columns
        return Point(column, row)
    }

    private fun getId(position: Int): Long {
        return adapter.getItemId(position)
    }

    private fun animateReorder(oldPosition: Int, newPosition: Int) {
        val isForward = newPosition > oldPosition
        val resultList: MutableList<Animator> = LinkedList()
        if (isForward) {
            for (pos in min(oldPosition, newPosition) until max(oldPosition, newPosition)) {
                val view = getViewForId(getId(pos))
                if ((pos + 1) % columnCount == 0) {
                    resultList.add(
                        createTranslationAnimations(
                            view, (-view!!.width * (columnCount - 1)).toFloat(), 0f,
                            view.height.toFloat(), 0f
                        )
                    )
                } else {
                    resultList.add(createTranslationAnimations(view, view!!.width.toFloat(), 0f, 0f, 0f))
                }
            }
        } else {
            for (pos in max(oldPosition, newPosition) downTo min(oldPosition, newPosition) + 1) {
                val view = getViewForId(getId(pos))
                if ((pos + columnCount) % columnCount == 0) {
                    resultList.add(
                        createTranslationAnimations(
                            view, (view!!.width * (columnCount - 1)).toFloat(), 0f,
                            -view.height.toFloat(), 0f
                        )
                    )
                } else {
                    resultList.add(createTranslationAnimations(view, -view!!.width.toFloat(), 0f, 0f, 0f))
                }
            }
        }
        val resultSet = AnimatorSet()
        resultSet.playTogether(resultList)
        resultSet.duration = MOVE_DURATION.toLong()
        resultSet.interpolator = AccelerateDecelerateInterpolator()
        resultSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                mReorderAnimation = true
                updateEnableState()
            }

            override fun onAnimationEnd(animation: Animator) {
                mReorderAnimation = false
                updateEnableState()
            }
        })
        resultSet.start()
    }

    private fun createTranslationAnimations(view: View?, startX: Float, endX: Float, startY: Float, endY: Float): AnimatorSet {
        val animX = ObjectAnimator.ofFloat(view, "translationX", startX, endX)
        val animY = ObjectAnimator.ofFloat(view, "translationY", startY, endY)
        val animSetXY = AnimatorSet()
        animSetXY.playTogether(animX, animY)
        return animSetXY
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mHoverCell != null) {
            mHoverCell!!.draw(canvas)
        }
    }

    interface OnDropListener {
        fun onActionDrop()
    }

    interface OnDragListener {
        fun onDragStarted(position: Int)
        fun onDragPositionsChanged(oldPosition: Int, newPosition: Int)
    }

    interface OnEditModeChangeListener {
        fun onEditModeChanged(inEditMode: Boolean)
    }

    interface OnSelectedItemBitmapCreationListener {
        fun onPreSelectedItemBitmapCreation(selectedView: View?, position: Int, itemId: Long)
        fun onPostSelectedItemBitmapCreation(selectedView: View?, position: Int, itemId: Long)
    }

    /**
     * This scroll listener is added to the gridview in order to handle cell swapping
     * when the cell is either at the top or bottom edge of the gridview. If the hover
     * cell is at either edge of the gridview, the gridview will begin scrolling. As
     * scrolling takes place, the gridview continuously checks if new cells became visible
     * and determines whether they are potential candidates for a cell swap.
     */
    private val mScrollListener: OnScrollListener = object : OnScrollListener {
        private var mPreviousFirstVisibleItem = -1
        private var mPreviousVisibleItemCount = -1
        private var mCurrentFirstVisibleItem = 0
        private var mCurrentVisibleItemCount = 0
        private var mCurrentScrollState = 0
        override fun onScroll(
            view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
            totalItemCount: Int
        ) {
            mCurrentFirstVisibleItem = firstVisibleItem
            mCurrentVisibleItemCount = visibleItemCount
            mPreviousFirstVisibleItem = if (mPreviousFirstVisibleItem == -1) mCurrentFirstVisibleItem else mPreviousFirstVisibleItem
            mPreviousVisibleItemCount = if (mPreviousVisibleItemCount == -1) mCurrentVisibleItemCount else mPreviousVisibleItemCount
            checkAndHandleFirstVisibleCellChange()
            checkAndHandleLastVisibleCellChange()
            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem
            mPreviousVisibleItemCount = mCurrentVisibleItemCount
            if (isPostHoneycomb && isWobbleInEditMode) {
                updateWobbleState(visibleItemCount)
            }
            if (mUserScrollListener != null) {
                mUserScrollListener!!.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
            }
        }

        private fun updateWobbleState(visibleItemCount: Int) {
            for (i in 0 until visibleItemCount) {
                val child = getChildAt(i)
                if (child != null) {
                    if (mMobileItemId != INVALID_ID.toLong() && java.lang.Boolean.TRUE != child.getTag(R.id.dgv_wobble_tag)) {
                        if (i % 2 == 0) animateWobble(child) else animateWobbleInverse(child)
                        child.setTag(R.id.dgv_wobble_tag, true)
                    } else if (mMobileItemId == INVALID_ID.toLong() && child.rotation != 0f) {
                        child.rotation = 0f
                        child.setTag(R.id.dgv_wobble_tag, false)
                    }
                }
            }
        }

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            mCurrentScrollState = scrollState
            mScrollState = scrollState
            isScrollCompleted
            if (mUserScrollListener != null) {
                mUserScrollListener!!.onScrollStateChanged(view, scrollState)
            }
        }

        /**
         * This method is in charge of invoking 1 of 2 actions. Firstly, if the gridview
         * is in a state of scrolling invoked by the hover cell being outside the bounds
         * of the gridview, then this scrolling event is continued. Secondly, if the hover
         * cell has already been released, this invokes the animation for the hover cell
         * to return to its correct position after the gridview has entered an idle scroll
         * state.
         */
        private val isScrollCompleted: Unit
            get() {
                if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (mCellIsMobile && mIsMobileScrolling) {
                        handleMobileCellScroll()
                    } else if (mIsWaitingForScrollFinish) {
                        touchEventsEnded()
                    }
                }
            }

        /**
         * Determines if the gridview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        fun checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID.toLong()) {
                    updateNeighborViewsForId(mMobileItemId)
                    handleCellSwitch()
                }
            }
        }

        /**
         * Determines if the gridview scrolled down enough to reveal a new cell at the
         * bottom of the list. If so, then the appropriate parameters are updated.
         */
        fun checkAndHandleLastVisibleCellChange() {
            val currentLastVisibleItem = mCurrentFirstVisibleItem + mCurrentVisibleItemCount
            val previousLastVisibleItem = mPreviousFirstVisibleItem + mPreviousVisibleItemCount
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID.toLong()) {
                    updateNeighborViewsForId(mMobileItemId)
                    handleCellSwitch()
                }
            }
        }
    }

    private class DynamicGridModification() {
        private val transitions: MutableList<Pair<Int, Int>?>

        init {
            transitions = Stack()
        }

        fun hasTransitions(): Boolean {
            return transitions.isNotEmpty()
        }

        fun addTransition(oldPosition: Int, newPosition: Int) {
            transitions.add(Pair(oldPosition, newPosition))
        }

        fun getTransitions(): List<Pair<Int, Int>?> {
            transitions.reverse()
            return transitions
        }
    }
}