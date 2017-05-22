package cz.cvut.fel.pda.buggerlist;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Database db;

    private TabLayout tabLayout;
    Toolbar toolbar;
    public TextView mToolbarTitle;
    private SectionsPageAdapter mSectionsPageAdapter;
    //public List<Project> mDataset = new ArrayList<Project>();

    //private CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
    private ViewPager mViewPager;

    private int[] tabIcons = {
          R.drawable.ic_organize,
          R.drawable.ic_add_task,
          R.drawable.ic_buggerlist
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(getApplicationContext());
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle("new title");

        setContentView(R.layout.activity_main);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mSectionsPageAdapter.getCount();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setCurrentItem(1);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
       // mToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        initDataset();
        db.close();
    }

        private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
       // mToolbarTitle.setText(title);
//        getSupportActionBar().setTitle(title);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFragmentOrganize(), getResources().getString(R.string.tab_organize));
        adapter.addFragment(new TabFragmentAddTask(), getResources().getString(R.string.tab_task));
        adapter.addFragment(new TabFragmentBuggerList(), getResources().getString(R.string.tab_buggerList));
        viewPager.setAdapter(adapter);
    }

    public void initDataset(){

        Project projectInbox = new Project("Inbox");
        Project School = new Project("School");
        Project Work = new Project("Work");
        Project Develop = new Project("Develop app");
        Project Movies = new Project("Movies to watch");

        long projectInbox_id = db.createProject(projectInbox);
        long School_id = db.createProject(School);
        long Work_id = db.createProject(Work);
        long Develop_id = db.createProject(Develop);
        long Movies_id = db.createProject(Movies);

        Task tasktest = new Task("Do homework", Calendar.getInstance(), 30, School_id);
        Task tasktest1 = new Task("Write reserch paper", Calendar.getInstance(), 30, School_id);
        Task tasktest2 = new Task("new task 1", Calendar.getInstance(), 30, projectInbox_id);
        Task tasktest3 = new Task("new task 2", Calendar.getInstance(), 30, projectInbox_id);
        Task tasktest4 = new Task("Talk to boss", Calendar.getInstance(), 30, Work_id);
        Task tasktest5 = new Task("Ask for promotion", Calendar.getInstance(), 30, Work_id);
        Task tasktest6 = new Task("Connect to database", Calendar.getInstance(), 30, Develop_id);
        Task tasktest7 = new Task("Make new activity", Calendar.getInstance(), 30, Develop_id);
        Task tasktest8 = new Task("Help to teacher", Calendar.getInstance(), 30, School_id);
        Task tasktest9 = new Task("Guardians of the galaxy", Calendar.getInstance(), 90, Movies_id);
        Task tasktest10 = new Task("Beauty and the beast", Calendar.getInstance(), 90,Movies_id);

        tasktest1.setPriority(2);
        tasktest9.setPriority(3);
        tasktest10.setPriority(3);

        long tasktest_id = db.createTask(tasktest);
        long tasktest_id1 = db.createTask(tasktest1);
        long tasktest_id2 = db.createTask(tasktest2);
        long tasktest_id3 = db.createTask(tasktest3);
        long tasktest_id4 = db.createTask(tasktest4);
        long tasktest_id5 = db.createTask(tasktest5);
        long tasktest_id6 = db.createTask(tasktest6);
        long tasktest_id7 = db.createTask(tasktest7);
        long tasktest_id8 = db.createTask(tasktest8);
        long tasktest_id9 = db.createTask(tasktest9);
        long tasktest_id10 = db.createTask(tasktest10);

        tasktest.setId(tasktest_id);
        tasktest1.setId(tasktest_id1);
        tasktest2.setId(tasktest_id2);
        tasktest3.setId(tasktest_id3);
        tasktest4.setId(tasktest_id4);
        tasktest5.setId(tasktest_id5);
        tasktest6.setId(tasktest_id6);
        tasktest7.setId(tasktest_id7);
        tasktest8.setId(tasktest_id8);
        tasktest9.setId(tasktest_id9);
        tasktest10.setId(tasktest_id10);

        db.updateTask(tasktest);
        db.updateTask(tasktest1);
        db.updateTask(tasktest2);
        db.updateTask(tasktest3);
        db.updateTask(tasktest4);
        db.updateTask(tasktest5);
        db.updateTask(tasktest6);
        db.updateTask(tasktest7);
        db.updateTask(tasktest8);
        db.updateTask(tasktest9);
        db.updateTask(tasktest10);

    }

}
