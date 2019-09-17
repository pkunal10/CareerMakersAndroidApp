package com.jk.admin.careermakers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.jk.admin.careermakers.Fragment.AdmissionFormFragment;
import com.jk.admin.careermakers.Fragment.AdmissionFragment;
import com.jk.admin.careermakers.Fragment.HomeFragment;
import com.jk.admin.careermakers.Fragment.JobOfferFragment;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        tableLayout = (TabLayout) findViewById(R.id.TabLayout);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);

        list.add(0, "Home");
        list.add(1, "Admission");
        list.add(2, "Job Offer");
        list.add(3, "Admission Form");


        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), list));
        tableLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.MenuFB)
        {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Carrier-Makers-Consultancy-1866385860339067/?modal=admin_todo_tour"));
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.MenuINSTA)
        {
            Toast.makeText(this, "No Instagram account yet.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        List<String> list;

        public PagerAdapter(FragmentManager supportFragmentManager, List<String> list) {
            super(supportFragmentManager);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();

            if (position == 0) {
               return new HomeFragment();

            } else if (position == 1) {
                return new AdmissionFragment();
            } else if (position == 2) {
                return new JobOfferFragment();
            } else if (position == 3) {
                return new AdmissionFormFragment();
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
    }
}
