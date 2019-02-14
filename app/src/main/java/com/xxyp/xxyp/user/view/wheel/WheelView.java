package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.AbstractWheelTextAdapter;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.IWheelAdapter;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.WheelAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Description : Numeric wheel view.
 */
public class WheelView extends View {

    /**
     * Top and bottom shadows colors
     */
    private int[] SHADOWS_COLORS = new int[]{
            0xefF2F2F4, 0xcfF2F2F4, 0x3fF2F2F4
    };

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF777E8C;

    /**
     * Top and bottom items offset (to hide that)
     */
    private static final int ITEM_OFFSET_PERCENT = 0;

    /**
     * Left and right padding value
     */
    private static final int PADDING = 10;

    /**
     * Default count of visible items
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Wheel Values
    private int currentItem = 0;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    // Item height
    private int itemHeight = 0;

    // Shadows drawables
    private GradientDrawable topShadow;

    private GradientDrawable bottomShadow;

    // Draw Shadows
    private boolean drawShadows = true;

    // Scrolling
    private WheelScroller scroller;

    private boolean isScrollingPerformed;

    private int scrollingOffset;

    // Cyclic
    boolean isCyclic = false;

    // Label & background
    private String label;

    /**
     * Text size (dp)
     */
    private int labelSize = 12;

    private int labelWidth = 0;

    private TextPaint labelPaint;

    private StaticLayout labelLayout;

    private Paint measurePaint;

    /**
     * Label offset
     */
    private static final int LABEL_OFFSET = 10;

    // Items layout
    private WheelItemLayout itemsLayout;

    // The number of first item in layout
    private int firstItem;

    // View adapter
    private IWheelAdapter adapter;

    // Recycle
    private WheelRecycle recycle = new WheelRecycle(this);

    // Listeners
    private List<OnWheelChangedListener> changingListeners = new LinkedList<>();

    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<>();

    //用于设置颜色时，区别年 与 月日； 年为 0 ，月 为1 ，日为 2
    private int extra = -1;

    private String yearStr, monthStr, dayStr;

    private int centerStrColor, aliginCenterColor, otherStrColor;

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * Initializes class data
     *
     * @param context the context
     */
    private void initData(Context context) {
        scroller = new WheelScroller(getContext(), scrollingListener);
        yearStr = context.getString(R.string.view_date_year);
        monthStr = context.getString(R.string.view_date_month);
        dayStr = context.getString(R.string.view_date_day);
        centerStrColor = context.getResources().getColor(R.color.c2);
        aliginCenterColor = context.getResources().getColor(R.color.c7);
        otherStrColor = context.getResources().getColor(R.color.c5);
    }

    public void setExtra(int value) {
        extra = value;
    }

    // Scrolling listener
    WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);

            int height = getHeight();
            if (scrollingOffset > height) {
                scrollingOffset = height;
                scroller.stopScrolling();
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height;
                scroller.stopScrolling();
            }
        }

        @Override
        public void onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersEnd();
                isScrollingPerformed = false;
            }

            scrollingOffset = 0;
            invalidate();
        }

        @Override
        public void onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller.scroll(scrollingOffset, 0);
            }
            notifyScrollingListenersJustify();
        }
    };

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.setInterpolator(interpolator);
    }

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * Sets the desired count of visible items. Actual amount of visible items
     * depends on wheel layout parameters. To apply changes and rebuild view
     * call measure().
     *
     * @param count the desired count for visible items
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
    }

    /**
     * Gets view adapter
     *
     * @return the view adapter
     */
    public IWheelAdapter getAdapter() {
        return adapter;
    }

    // Adapter listener
    private DataSetObserver dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidateWheel(false);
        }

        @Override
        public void onInvalidated() {
            invalidateWheel(true);
        }
    };

    /**
     * Sets view adapter. Usually new adapters contain different views, so it
     * needs to rebuild view by calling measure().
     *
     * @param adapter the view adapter
     */
    public void setAdapter(IWheelAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataObserver);
        }
        this.adapter = adapter;
        if (this.adapter != null) {
            this.adapter.registerDataSetObserver(dataObserver);
        }

        invalidateWheel(true);
    }

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * Removes wheel changing listener
     *
     * @param listener the listener
     */
    // public void removeChangingListener(OnWheelChangedListener listener) {
    // changingListeners.remove(listener);
    // }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    // public void removeScrollingListener(OnWheelScrollListener listener) {
    // scrollingListeners.remove(listener);
    // }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * Notifies listeners about end scrolling
     */
    protected void notifyScrollingListenersJustify() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingJustify(this);
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    public void setCurrentItem(int index, boolean animated) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return; // throw?
        }

        int itemCount = adapter.getItemsCount();
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount;
                }
                index %= itemCount;
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {

            if (animated) {
                int itemsToScroll = index - currentItem;
                if (isCyclic) {
                    int scroll = itemCount + Math.min(index, currentItem)
                            - Math.max(index, currentItem);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
            } else {
                scrollingOffset = 0;
                int old = currentItem;
                currentItem = index;

                notifyChangingListeners(old, currentItem);

                invalidate();
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown
     * the last one
     *
     * @return true if wheel is cyclic
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidateWheel(false);
    }

    /**
     * Determine whether shadows are drawn
     *
     * @return true is shadows are drawn
     */
    public boolean drawShadows() {
        return drawShadows;
    }

    /**
     * Set whether shadows should be drawn
     *
     * @param drawShadows flag as true or false
     */
    public void setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
    }

    /**
     * Invalidates wheel
     *
     * @param clearCaches if true then cached views will be clear
     */
    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            recycle.clearAll();
            if (itemsLayout != null) {
                itemsLayout.removeAllViews();
            }
            scrollingOffset = 0;
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        }

        invalidate();
    }

    /**
     * Initializes resources
     */
    private void initResourcesIfNecessary() {
        if (labelPaint == null) {
            labelPaint = new TextPaint(
                    Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            labelPaint.setTextSize(labelSize * ScreenUtils.density);
            labelPaint.setFakeBoldText(false);
            labelPaint.setColor(DEFAULT_TEXT_COLOR);
            labelPaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        }

        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }

        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }

    }

    /**
     * Calculates desired height for layout
     *
     * @param layout the source layout
     * @return the desired layout height
     */
    private int getDesiredHeight(LinearLayout layout) {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }

        int desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50;

        return Math.max(desired, getSuggestedMinimumHeight());
    }

    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        }

        if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
            itemHeight = itemsLayout.getChildAt(0).getHeight();
            return itemHeight;
        }

        return getHeight() / visibleItems;
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();
        int width;
        itemsLayout.setLayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        labelWidth = 0;
        if (label != null && label.length() > 0) {
            labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, labelPaint));
        }
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = labelWidth + 2 * PADDING;
            if (labelWidth > 0) {
                width += LABEL_OFFSET;
            }
            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        buildViewForMeasuring();

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    /**
     * Sets layouts width and height
     *
     * @param width  the layout width
     * @param height the layout height
     */
    private void layout(int width, int height) {
        int itemsWidth = width - 2 * PADDING;

        itemsLayout.layout(0, 0, itemsWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (adapter != null && adapter.getItemsCount() > 0) {
            updateView();
            drawItems(canvas);
        }
        drawCenterRect(canvas);

        if (drawShadows) {
            drawShadows(canvas);
        }
    }

    /**
     * Draws shadows on top and bottom of control
     *
     * @param canvas the canvas for drawing
     */
    private void drawShadows(Canvas canvas) {
        int height = getVisibleItems() / 2 * getItemHeight();
        // */
        topShadow.setBounds(0, 0, getWidth(), height);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * Draws items
     *
     * @param canvas the canvas for drawing
     */
    private void drawItems(Canvas canvas) {
        canvas.save();
        // view画布移动，而layout布局未移动，而且中心点有差异-deltaY
        int top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2;
        int deltaY = -top + scrollingOffset;
        canvas.translate(PADDING, deltaY);

        itemsLayout.draw(canvas, -deltaY, getMeasuredHeight() / 2);

        canvas.restore();

        // draw label
        if (labelLayout != null) {
            if (adapter instanceof AbstractWheelTextAdapter) {
                canvas.save();
                AbstractWheelTextAdapter tempAdapter = (AbstractWheelTextAdapter) adapter;
                CharSequence currentValue = tempAdapter.getItemText(currentItem);
                String currentText = "";
                if (!TextUtils.isEmpty(currentValue)) {
                    currentText = currentValue.toString();
                }
                // 测量文字长度
                float size = tempAdapter.getTextSize();
                if (measurePaint == null) {
                    measurePaint = new Paint();
                }
                // 这里要还原为像素，AbstractWheelTextAdapter 中转为
                measurePaint.setTextSize(size * ScreenUtils.density);
                if (currentText.length() == 1) {
                    currentText = "0" + currentText;
                }
                float width = measurePaint.measureText(currentText);
                canvas.translate(
                        (itemsLayout.getMeasuredWidth() + width) / 2 + PADDING + LABEL_OFFSET,
                        (getMeasuredHeight() - labelSize * ScreenUtils.density) / 2);
                labelLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    /**
     * Draws rect for current value
     *
     * @param canvas the canvas for drawing
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        // 使用自己的画线，而不是描边
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.view_divider_color));
        // 设置线宽
        paint.setStrokeWidth((float) 1.2);
        // 绘制上边直线
        canvas.drawLine(0, center - offset, getWidth(), center - offset, paint);
        // 绘制下边直线
        canvas.drawLine(0, center + offset, getWidth(), center + offset, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getAdapter() == null) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            default:
                break;
        }

        return scroller.onTouchEvent(event);
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;

        int itemHeight = getItemHeight();
        int count = scrollingOffset / itemHeight;
        int pos = currentItem - count;
        int itemCount = adapter.getItemsCount();
        int fixPos = scrollingOffset % itemHeight;
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0;
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else {
            //
            if (pos < 0) {
                count = currentItem;
                pos = 0;
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1;
                pos = itemCount - 1;
            } else if (pos > 0 && fixPos > 0) {
                pos--;
                count++;
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++;
                count--;
            }
        }

        int offset = scrollingOffset;
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        // update offset
        scrollingOffset = offset - count * itemHeight;
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    /**
     * Scroll the wheel
     *
     * @param itemsToScroll items to scroll
     * @param time          scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        int distance = itemsToScroll * getItemHeight() - scrollingOffset;
        scroller.scroll(distance, time);
    }

    /**
     * Calculates range for wheel items
     *
     * @return the items range
     */
    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }

        int first = currentItem;
        int count = 1;

        while (count * getItemHeight() < getHeight()) {
            first--;
            count += 2; // top + bottom items
        }

        if (scrollingOffset != 0) {
            if (scrollingOffset > 0) {
                first--;
            }
            count++;

            // process empty items above the first or below the second
            int emptyItems = scrollingOffset / getItemHeight();
            first -= emptyItems;
            count += Math.asin(emptyItems);
        }
        return new ItemsRange(first, count);
    }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private boolean rebuildItems() {
        boolean updated;
        ItemsRange range = getItemsRange();
        if (itemsLayout != null) {
            int first = recycle.recycleItems(itemsLayout, firstItem, range);
            updated = firstItem != first;
            firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }

        if (range != null) {
            if (!updated) {
                updated = firstItem != range.getFirst()
                        || itemsLayout.getChildCount() != range.getCount();
            }

            if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
                for (int i = firstItem - 1; i >= range.getFirst(); i--) {
                    if (!addViewItem(i, true)) {
                        break;
                    }
                    firstItem = i;
                }
            } else {
                firstItem = range.getFirst();
            }
        }

        int first = firstItem;
        if (range != null) {
            for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
                if (!addViewItem(firstItem + i, false) && itemsLayout.getChildCount() == 0) {
                    first++;
                }
            }
        }
        firstItem = first;

        setShowColor();
        return updated;
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items
     * sizes.
     */
    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            layout(getWidth(), getHeight());
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private void createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = new WheelItemLayout(getContext());
            itemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
        if (labelWidth > 0) {
            if (labelLayout == null || labelLayout.getWidth() > labelWidth) {
                labelLayout = new StaticLayout(label, labelPaint, labelWidth,
                        Layout.Alignment.ALIGN_NORMAL, 1.5f, 0, false);
            } else {
                labelLayout.increaseWidthTo(labelWidth);
            }
        }
    }

    /**
     * Builds view for measuring
     */
    private void buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }
        if (labelWidth > 0) {
            if (labelLayout == null || labelLayout.getWidth() > labelWidth) {
                labelLayout = new StaticLayout(label, labelPaint, labelWidth,
                        Layout.Alignment.ALIGN_CENTER, 1.5f, 0, false);
            } else {
                labelLayout.increaseWidthTo(labelWidth);
            }
        }
        // add views
        int addItems = visibleItems / 2;
        for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                firstItem = i;
            }
        }
        setShowColor();
    }

    /**
     * 设置文本展示颜色
     */
    private void setShowColor() {
        if (itemsLayout == null) {
            return;
        }
        int childCount = itemsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = itemsLayout.getChildAt(i);
            if (!(v instanceof TextView)) {
                continue;
            }

            TextView textView = (TextView) v;
            String str = ((TextView) v).getText().toString();
            int compare = currentItem;

            //通过extra字段去除字符串中的非数字部分
            if (extra == 0) {
                str = str.substring(0, str.indexOf(yearStr));
            } else if (extra == 1) {
                str = str.substring(0, str.indexOf(monthStr));
                ++compare;
            } else if (extra == 2) {
                str = str.substring(0, str.indexOf(dayStr));
                ++compare;
            }

            int number = -1;
            try {
                number = Math.abs(Integer.parseInt(str) - compare);
            } catch (NumberFormatException e) {
                Log.d("WheelView", "error");
            }

            //头尾交替时做相关处理，以免无法设置相关颜色值
            if (number + 3 >= adapter.getItemsCount()) {
                number = adapter.getItemsCount() % number;
            }

            if (number == 0) {
                textView.setTextColor(centerStrColor);
            } else if (number == 1 || number == 2) {
                textView.setTextColor(aliginCenterColor);
            } else {
                textView.setTextColor(otherStrColor);
            }
        }
    }

    /**
     * Adds view for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            if (first) {
                itemsLayout.addView(view, 0);
            } else {
                itemsLayout.addView(view);
            }

            return true;
        }

        return false;
    }

    /**
     * Checks whether intem index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private boolean isValidItemIndex(int index) {
        return adapter != null && adapter.getItemsCount() > 0
                && (isCyclic || index >= 0 && index < adapter.getItemsCount());
    }

    /**
     * Returns view for specified item
     *
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private View getItemView(int index) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return null;
        }
        int count = adapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return adapter.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        index %= count;
        return adapter.getItem(index, recycle.getItem(), itemsLayout);
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        scroller.stopScrolling();
    }
}
