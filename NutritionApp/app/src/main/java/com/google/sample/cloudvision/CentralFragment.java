package com.google.sample.cloudvision;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
/**
 * Created by SuKim on 15/08/2017.
 */

public class CentralFragment extends Fragment {

    ViewPager pager;
    MainActivity mCenterActivity;
    Button floatingButton;
    MainActivity mainActivity = new MainActivity();

   // private TextView mImageDetails;
    private ImageView mMainImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_central, container, false);

        pager = (ViewPager) container;
        mCenterActivity = (MainActivity) getActivity();



        return fragmentView;

        }
}

