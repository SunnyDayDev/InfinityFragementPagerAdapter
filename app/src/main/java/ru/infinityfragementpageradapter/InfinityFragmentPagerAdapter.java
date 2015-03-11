package ru.infinityfragementpageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * This is endless adapter. It can be two type:
 * - Cyclic. It create pages by cycle. if count 3 than cycle is like 0-1-2-0-1-2-0-1-2...
 * - Infinity shift. Create page by shift at start position. It can be like (-3)-(-2)-(-1)-0(start)-1-2-3
 */
public abstract class InfinityFragmentPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int PAGES_COUNT = 6;
    private static final int FIRST = 0;
    private static final int LAST = 5;
    private static final int REACHABLE_FIRST = 1;
    private static final int REACHABLE_LAST = 4;
    private static final int ACTIVE_PAGES_COUNT = PAGES_COUNT - 2;
    public static final int COUNT_INFINITY_SHIFT = -1;

    private ViewPager mPager;
    //Delegate OnPageChangeListener
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    //Shift at start (cycles)
    private int mShift;
    private int mRealCount;
    //Need for testing
    private boolean mIsInitiated = false;

    public InfinityFragmentPagerAdapter(FragmentManager fm, ViewPager pager) {
        super(fm);
        setPager(pager);
        mRealCount = getRealCount();
    }

    /**
     * Init pager and set.
     * OffScreenPagerLimit = 0. Because adapter work correct only if visible only nearest neighbor.
     *
     * @param pager - pager what need to be endless.
     */
    private void setPager(ViewPager pager) {
        mIsInitiated = false;
        mPager = pager;
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(this);
        pager.setOnPageChangeListener(this);
        mIsInitiated = true;
        pager.setCurrentItem(1);
    }

    /**
     * Get Fragment by position in adapter. Recalculate position to real position (with mShift)
     * and call getRealItem.
     *
     * @param position - position in adapter.
     * @return Fragment at real position
     */
    @Override
    public Fragment getItem(int position) {
        return getRealItem(getPosition(position));
    }

    /**
     * Get Fragment by real position.
     * Need to override and set create Fragment logic.
     *
     * @param position - real position.
     * @return - Fragment at real position.
     */
    public abstract Fragment getRealItem(int position);

    /**
     * Calculate real position from position in adapter and shift.
     *
     * @param position - position in adapter.
     * @return real position
     */
    private int getPosition(int position) {
        if (!mIsInitiated) return 0;
        position = position - 1 + mShift * ACTIVE_PAGES_COUNT;
        if (mRealCount != COUNT_INFINITY_SHIFT) {
            position = position % mRealCount;
            if (position < 0) {
                position = mRealCount + position;
            }
        }
        return position;
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

    public abstract int getRealCount();

    /**
     * **********************************
     * OnPageChangeListener implementation set to ViewPager.
     * Each method delegate action (with calculated real position) to self mOnPageChangeListener.
     * ***********************************
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(getPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(getPosition(position));
        }
    }

    /**
     * Recalculate position when scroll ended and reset current item if need.
     * if first or last, than need it.
     * if first than need to be neighbor of last, same if last.
     *
     * @param state - scrolling state (catch idle)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int currentPosition = mPager.getCurrentItem();
            if (currentPosition == LAST) {
                mShift++;
                mPager.setCurrentItem(REACHABLE_FIRST, false);
            } else if (currentPosition == FIRST) {
                mShift--;
                mPager.setCurrentItem(REACHABLE_LAST, false);
            }
        }
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * ********************
     * Delegate block
     * *********************
     */

    public void setCurrentItem(int position) {
        if (mRealCount != COUNT_INFINITY_SHIFT && (position < 0 || position >= mRealCount)) {
            throw new IndexOutOfBoundsException("Adapter have " + mRealCount +
                    " items, current index is " + position + "\n");
        }
        int pos;
        mShift = position / ACTIVE_PAGES_COUNT;
        pos = position % ACTIVE_PAGES_COUNT;
        if (pos < 0) {
            mShift--;
            pos = ACTIVE_PAGES_COUNT + pos;
        }
        mPager.setAdapter(null);
        mPager.setAdapter(this);
        mPager.setCurrentItem(pos + 1);
    }

    public int getCurrentItem() {
        return getPosition(mPager.getCurrentItem());
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }
}
