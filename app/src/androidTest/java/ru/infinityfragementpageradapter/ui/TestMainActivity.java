package ru.infinityfragementpageradapter.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import ru.infinityfragementpageradapter.R;

/**
 * Created  by sashka on 02.03.15.
 */
public class TestMainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.main_content, createFragment())
                    .commit();
        }
    }

    private Fragment createFragment() {
        return new MainContentFragment();
    }
}
