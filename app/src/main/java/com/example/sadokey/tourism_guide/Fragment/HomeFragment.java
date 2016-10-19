package com.example.sadokey.tourism_guide.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.example.sadokey.tourism_guide.R;
import com.example.sadokey.tourism_guide.adapter.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;


//this fragment is only used to build view pager and TabHost
public class HomeFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabHost tabHost;
    HorizontalScrollView horizontalScrollView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.activity_home_fragment,container,false);

        initViewPager();

        initTabHost();

        return view;
    }


    private void initViewPager() {

        viewPager=(ViewPager)view.findViewById(R.id.viewpager);

        List<Fragment> mylistOfFragments=new ArrayList<>();
        mylistOfFragments.add(new OffersFragment());
        mylistOfFragments.add(new PlacesFragment());
        mylistOfFragments.add(new SuggestionFragment());
        mylistOfFragments.add(new WhoToFollowFragment());


        MyFragmentPageAdapter adapter=new MyFragmentPageAdapter(getChildFragmentManager(),mylistOfFragments);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                int selectedItem = viewPager.getCurrentItem();
                tabHost.setCurrentTab(selectedItem);
            }
        });
    }


    private void initTabHost() {

        tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();

        String[] TabName = { "Offers", "Places" , "Suggestion" ,"who To follow"};


        for (int i = 0; i < TabName.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(TabName[i]);
            tabSpec.setIndicator(TabName[i]);
            tabSpec.setContent(new fakeContent(getContext()));
            tabHost.addTab(tabSpec);
        }


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            int selectedItem = tabHost.getCurrentTab();
            viewPager.setCurrentItem(selectedItem);

            horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.h_scrollView);
            View tabView = tabHost.getCurrentView();
            int x_postion = tabView.getLeft() - (horizontalScrollView.getWidth() - tabView.getWidth()) / 2;
            horizontalScrollView.smoothScrollBy(x_postion, 0);
            }
        });

    }

    class fakeContent implements TabHost.TabContentFactory {

        Context context;

        public fakeContent(Context context)
        {
            this.context=context;
        }

        @Override
        public View createTabContent(String tag)
        {
            View myview=new View(context);
            myview.setMinimumHeight(0);
            myview.setMinimumWidth(0);
            return myview;
        }
    }
}
