package ru.infinityfragementpageradapter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.infinityfragementpageradapter.R;


/**
 * Created  by sashka on 02.03.15.
 */
public class PagerFragment extends Fragment {
    private static final String ARGS_TEST = "test";

    public static PagerFragment newInstace(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_TEST, page);
        PagerFragment f = new PagerFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_test, container, false);
        ((TextView) root.findViewById(R.id.test)).setText(String.valueOf(getArguments().getInt(ARGS_TEST)));
        return root;
    }
}
