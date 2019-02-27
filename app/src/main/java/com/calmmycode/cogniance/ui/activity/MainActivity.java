package com.calmmycode.cogniance.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.calmmycode.cogniance.Constants;
import com.calmmycode.cogniance.R;
import com.calmmycode.cogniance.ui.fragment.ResultsFragment;
import com.calmmycode.cogniance.ui.fragment.SearchFragment;
import com.calmmycode.cogniance.ui.interfaces.DataFetchListener;
import com.calmmycode.cogniance.ui.util.FragmentUtil;
import com.calmmycode.cogniance.ui.util.ViewUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataFetchListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fragmentContainer) FrameLayout fragmentContainer;
    private List<String> fragmentIds = Arrays.asList(SearchFragment.TAG, ResultsFragment.TAG);

    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(savedInstanceState == null){
            FragmentUtil.loadNewFragment(SearchFragment.TAG, getSupportFragmentManager(), fragmentIds, fragmentContainer);
        }
    }

    @Override
    public void onBackPressed() {
        if(!FragmentUtil.performFragmentPopBackStack(getSupportFragmentManager())){
            if (mBackPressed + Constants.BACK_PRESSURE_TIME_INTERVAL > System.currentTimeMillis()) {
                finish();
                return;
            } else {
                ViewUtil.showExitToast(this);
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public void dataFetched() {
        FragmentUtil.loadNewFragment(ResultsFragment.TAG, getSupportFragmentManager(), fragmentIds, fragmentContainer);
    }
}
