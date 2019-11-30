package com.example.shirin_pc.inputtime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroSliderActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout layoutDots;
    Button btnNext,btnSkip;
    SliderPageAdapter pageAdapter;
    private SliderPrefManager prefMan;
    int[]layoutIds={R.layout.into_slide1,R.layout.into_slide2,R.layout.into_slide3,R.layout.into_slide4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        changeStatusBarColor();
        prefMan=new SliderPrefManager(this);
        if (!prefMan.startSlider()){
            launchMainScreen();
            return;
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        pageAdapter = new SliderPageAdapter();
        showDots(viewPager.getCurrentItem());
        viewPager.setAdapter(pageAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                showDots(position);
                //
                if (position == viewPager.getAdapter().getCount()-1) {
                    btnSkip.setVisibility(View.GONE);
                    btnNext.setText(R.string.gotit);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage=viewPager.getCurrentItem();
                int lastPages=viewPager.getAdapter().getCount()-1;
                if (currentPage==lastPages){
                    prefMan.setStartSlider(false);
                    launchMainScreen();
                }else {
                    viewPager.setCurrentItem(currentPage+1);
                }
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainScreen();

            }
        });


    }

        private void showDots(int pageNumber){
            TextView[]dots=new TextView[layoutIds.length];
            layoutDots.removeAllViews();
            for (int i = 0; i <dots.length ; i++) {
                dots[i]=new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                dots[i].setTextColor(ContextCompat.getColor(this,(i==pageNumber ? R.color.dot_active:R.color.dot_incative)));
                layoutDots.addView(dots[i]);

            }
        }
    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }



    private void launchMainScreen() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public class SliderPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return layoutIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object ;
        }
        public Object instantiateItem(ViewGroup container,int position){
            View view=LayoutInflater.from(IntroSliderActivity.this)
            .inflate(layoutIds[position],container,false);
            container.addView(view);
            return view;
        }
        public void destroyItem(ViewGroup container,int position,Object object){
            View view=(View)object;
            container.removeView(view);
        }

    }
}
