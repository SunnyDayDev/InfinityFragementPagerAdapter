package ru.infinityfragementpageradapter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.infinityfragementpageradapter.InfinityFragmentPagerAdapter;
import ru.infinityfragementpageradapter.R;

/**
 * Created  by sashka on 02.03.15.
 */
public class MainContentFragment extends Fragment {

    ViewPager mPager;
    InfinityFragmentPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calendar_week, container, false);
        mPager = (ViewPager) root.findViewById(R.id.pager);
        mAdapter = createBoundedAdapter(getFragmentManager(), mPager);
        return root;
    }

    private InfinityFragmentPagerAdapter createBoundedAdapter(FragmentManager fm, ViewPager p) {
        return new InfinityFragmentPagerAdapter(fm, p) {
            @Override
            public Fragment getRealItem(int shiftAtStart) {
                return PagerFragment.newInstace(shiftAtStart);
            }

            @Override
            public int getRealCount() {
                return 11;
            }
        };
    }

}
