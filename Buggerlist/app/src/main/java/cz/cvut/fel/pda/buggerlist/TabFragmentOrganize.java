package cz.cvut.fel.pda.buggerlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Matej on 17.04.2017.
 */

public class TabFragmentOrganize extends Fragment {
    private static final String TAG = "tabOrganize";
    private static final int DATASET_COUNT = 60;
    Database db;

    private static final int SPAN_COUNT = 3;
    //https://developer.android.com/samples/RecyclerView/src/com.example.android.recyclerview/RecyclerViewFragment.html
//    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
//
    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected ProjectAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    //protected List<String> mDataset = new ArrayList<String>();
    protected List<Project> mProjects;// = new ArrayList<Project>();

    private FloatingActionButton newProjectButton;

//    public TabFragmentOrganize() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_organize));
        //initDataset();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.tab_organize));
        final View view = inflater.inflate(R.layout.tab_fragment_organize, container,false);
        view.setTag(TAG);

        db = new Database(getActivity());
       // initDataset();
        mProjects = db.getAllProjects();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.task_list_organize);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;//LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProjectAdapter(mProjects);
        mRecyclerView.setAdapter(mAdapter);

        newProjectButton = (FloatingActionButton) view.findViewById(R.id.add_project);
        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.new_project_dialog);
                dialog.setTitle(R.string.newProject);

                dialog.show();

                FloatingActionButton addProject = (FloatingActionButton) dialog.findViewById(R.id.add_project);

                addProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String projectName = ((EditText)dialog.findViewById(R.id.edit_project_name)).getText().toString();
                        db.createProject(new Project(projectName));
                        Snackbar snackbar = Snackbar.make(view,"New project: " + projectName + " created.",Snackbar.LENGTH_LONG);
                        snackbar.show();

                        dialog.dismiss();
                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });



        db.close();
        return view;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
       // mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;//LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

}
