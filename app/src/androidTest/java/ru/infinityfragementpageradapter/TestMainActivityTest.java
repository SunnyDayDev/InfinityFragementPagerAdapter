package ru.infinityfragementpageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import ru.infinityfragementpageradapter.ui.PagerFragment;
import ru.infinityfragementpageradapter.ui.TestMainActivity;


/**
 * Created  by sashka on 10.03.15.
 */
public class TestMainActivityTest extends ActivityInstrumentationTestCase2<TestMainActivity> {
    private TestMainActivity mActivity;
    private InfinityFragmentPagerAdapter mAdapter;
    private FragmentManager mFM;
    private ViewPager mPager;
    private Solo mSolo;

    public TestMainActivityTest() {
        super(TestMainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mPager = (ViewPager) mActivity.findViewById(R.id.pager);
        mFM = mActivity.getSupportFragmentManager();

        mSolo = new Solo(getInstrumentation(), mActivity);
    }

    public void testPreCondition() {
        assertNotNull(mActivity);
        assertNotNull(mPager);
        assertNotNull(mFM);
    }

    public void testBounds() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = createAdapter(mFM, mPager, 10);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertNotNull(mAdapter);
        assertEquals(mAdapter.getRealCount(), 10);
        assertEquals(mPager.getCurrentItem(), 1);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAdapter.setCurrentItem(-1);
                } catch (Exception e) {
                    assertTrue(e instanceof IndexOutOfBoundsException);
                }
                try {
                    mAdapter.setCurrentItem(10);
                } catch (Exception e) {
                    assertTrue(e instanceof IndexOutOfBoundsException);
                }
            }
        });//end of Runnable
    }

    public void testBoundedShift() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = createAdapter(mFM, mPager, 10);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(1, mPager.getCurrentItem());
        //Scroll to 0 (but in test OnScrollChange Listener not triggered)
        mSolo.scrollViewToSide(mPager, Solo.LEFT, 0.8f);
        //Scroll one more time and OnScrollChangeListener triggered twice
        //So first scroll to 0 set current 4 and next 3
        mSolo.scrollViewToSide(mPager, Solo.LEFT, 0.8f);
        assertEquals(3, mPager.getCurrentItem());
        //Twice shift to left from 0 is 0->9->8
        assertEquals(8, mAdapter.getCurrentItem());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setCurrentItem(9);
            }
        });
        getInstrumentation().waitForIdleSync();
        //Position 9 in adapter is 2 in ViewPager
        assertEquals(2, mPager.getCurrentItem());
        //Swipe to right in adapter cyclyc 9->0, mPager.currentItem 2->3
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        assertEquals(3, mPager.getCurrentItem());
        assertEquals(0, mAdapter.getCurrentItem());
        //Swipe to right in adapter cyclyc 0->1, mPager.currentItem 3->4
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        assertEquals(4, mPager.getCurrentItem());
        assertEquals(1, mAdapter.getCurrentItem());
        //Swipe twice because in test sipe to last incorrect, but swipe more and correct both
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        //So first scroll to 5 set current 1 and next 2
        //Adapter position 1->2->3
        assertEquals(2, mPager.getCurrentItem());
        assertEquals(3, mAdapter.getCurrentItem());
    }

    public void testInfinityShift() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = createAdapter(mFM, mPager, InfinityFragmentPagerAdapter.COUNT_INFINITY_SHIFT);
                mAdapter.setCurrentItem(-8);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(1, mPager.getCurrentItem());
        //Scroll to 0 (but in test OnScrollChange Listener not triggered)
        mSolo.scrollViewToSide(mPager, Solo.LEFT, 0.8f);
        //Scroll one more time and OnScrollChangeListener triggered twice
        //So first scroll to 0 set current 4 and next 3
        mSolo.scrollViewToSide(mPager, Solo.LEFT, 0.8f);
        assertEquals(3, mPager.getCurrentItem());
        //Twice shift to left from -8 is -8->-9->-10
        assertEquals(-10, mAdapter.getCurrentItem());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setCurrentItem(9);
            }
        });
        getInstrumentation().waitForIdleSync();
        //Position 9 in adapter is 2 in ViewPager
        assertEquals(2, mPager.getCurrentItem());
        //Swipe to right in adapter cyclyc 9->10, mPager.currentItem 2->3
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        assertEquals(3, mPager.getCurrentItem());
        assertEquals(10, mAdapter.getCurrentItem());
        //Swipe to right in adapter 10->11, mPager.currentItem 3->4
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        assertEquals(4, mPager.getCurrentItem());
        assertEquals(11, mAdapter.getCurrentItem());
        //Swipe twice because in test sipe to last incorrect, but swipe more and correct both
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        mSolo.scrollViewToSide(mPager, Solo.RIGHT, 0.8f);
        //So first scroll to 5 set current 1 and next 2
        //Adapter position 11->12->13
        assertEquals(2, mPager.getCurrentItem());
        assertEquals(13, mAdapter.getCurrentItem());
    }

    public void testInfinity() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = createAdapter(mFM, mPager, InfinityFragmentPagerAdapter.COUNT_INFINITY_SHIFT);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertNotNull(mAdapter);
        assertEquals(mAdapter.getRealCount(), InfinityFragmentPagerAdapter.COUNT_INFINITY_SHIFT);
        assertEquals(mPager.getCurrentItem(), 1);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setCurrentItem(-100000000);
            }
        });//end of Runnable
        getInstrumentation().waitForIdleSync();
        assertEquals(-100000000, mAdapter.getCurrentItem());

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setCurrentItem(100000000);
            }
        });//end of Runnable
        getInstrumentation().waitForIdleSync();
        assertEquals(100000000, mAdapter.getCurrentItem());
    }

    private InfinityFragmentPagerAdapter createAdapter(FragmentManager fm, ViewPager p, final int count) {
        return new InfinityFragmentPagerAdapter(fm, p) {
            @Override
            public Fragment getRealItem(int shiftAtStart) {
                return PagerFragment.newInstace(shiftAtStart);
            }

            @Override
            public int getRealCount() {
                return count;
            }
        };
    }
}
