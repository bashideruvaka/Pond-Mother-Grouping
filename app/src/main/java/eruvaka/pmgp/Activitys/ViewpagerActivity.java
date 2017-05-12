package eruvaka.pmgp.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

import eruvaka.pmgp.R;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.fragments.FeedEditFragment;
import eruvaka.pmgp.fragments.HistoryFragment;
import eruvaka.pmgp.fragments.SettingsFragment;

/**
 * Created by eruvaka on 28-02-2017.
 */

public class ViewpagerActivity extends ActionBarActivity {
    android.support.v7.app.ActionBar bar;
    ImageButton leftNav,rightNav;
    ArrayList<HashMap<String, String>> schedule_list = new ArrayList<HashMap<String, String>>();
    AlertDialog.Builder build;
    String feederSno,hex_id;
    UserSession session;
    private Utils util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
        bar=getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setIcon(android.R.color.transparent);
        session=new UserSession(getApplicationContext());
        util = new Utils(getApplicationContext());
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if(position==0){
                    bar.setTitle("Add Schedule");
                    leftNav.setVisibility(View.INVISIBLE);
                    rightNav.setVisibility(View.VISIBLE);
                }else if(position==1){
                    bar.setTitle("History");
                    leftNav.setVisibility(View.VISIBLE);
                    rightNav.setVisibility(View.VISIBLE);
                }else if(position==2){
                    bar.setTitle("Settings");
                    leftNav.setVisibility(View.VISIBLE);
                    rightNav.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub


            }
        });
        leftNav = (ImageButton)findViewById(R.id.left_nav);
        rightNav = (ImageButton)findViewById(R.id.right_nav);
        leftNav.setVisibility(View.INVISIBLE);

        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = pager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    pager.setCurrentItem(tab);
                    //leftNav.setVisibility(0);
                } else if (tab == 0) {
                    pager.setCurrentItem(tab);
                    //leftNav.setVisibility(8);
                }
            }
        });

        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = pager.getCurrentItem();
                // leftNav.setVisibility(0);
                tab++;
                pager.setCurrentItem(tab);


            }
        });
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    FeedEditFragment tab1 = new FeedEditFragment();
                    // tab1.setArguments(bundle);
                    System.out.println("feed"+pos);
                    return tab1;
                case 1:
                    HistoryFragment tab2 = new HistoryFragment();
                    // tab2.setArguments(bundle);
                    System.out.println("history"+pos);
                    return tab2;
                case 2:
                    SettingsFragment tab3 = new SettingsFragment();
                    // tab3.setArguments(bundle);
                    System.out.println("settings"+pos);
                    return tab3;
                    default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    // Returns the page title for the top indicator
    public CharSequence getPageTitle(int pos) {
        switch(pos){
            case 0: bar.setTitle("Add Schedule");
            case 1: bar.setTitle("History");
            case 2: bar.setTitle("Settings");
        }
        return "Page " + pos;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        try{
            MenuItem menuItem = menu.findItem(R.id.login_in1);
            String user_name = session.get("firstname");
            menuItem.setTitle("User : "+user_name);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_settings1:
                try{
                    util.showAlertDialog(this,"Logout","Would you like to logout?",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
                            finishAffinity();
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.login_in1:
                Intent user_intent = new Intent(ViewpagerActivity.this,UserProfileActivity.class);
                user_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user_intent);
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }





}
