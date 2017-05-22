package cz.cvut.fel.pda.buggerlist;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matej on 27.04.2017.
 */

public class ProjectDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProjectDetailActivity";
    private ViewPager mViewPager;

    Database db;

    Intent intent;
    Toolbar toolbar;
    private String ProjectName;
    private long projectId;
    TextView labelName;
    protected RecyclerView mRecyclerView;
    protected TaskAdapter mAdapter;

    protected List<Task> mTasks = new ArrayList<Task>();

    Button Bdate, Bprior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        ProjectName = intent.getStringExtra("project");
        projectId = intent.getLongExtra("projectID", 0);

        db = new Database(getApplicationContext());
        mTasks = db.getProjectTasks(projectId);
        db.close();

        setContentView(R.layout.activity_project);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(ProjectName);

        mRecyclerView = (RecyclerView) findViewById(R.id.project_detail_list);
        //initDataset();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaskAdapter(this, findViewById(android.R.id.content),mTasks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//buttons filter

        Bdate = (Button) findViewById(R.id.button_datesort);
        Bprior =(Button) findViewById(R.id.button_prioritysort);

//        Bdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bdate.setPressed(true);
//                Bdate.setBackgroundTintList(R.color.projectCircle);
//            }
//        });

        Bdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bdate.setPressed(true);
                Bprior.setPressed(false);
                return true;
            }
        });

        Bprior.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bdate.setPressed(false);
                Bprior.setPressed(true);
                return true;
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
