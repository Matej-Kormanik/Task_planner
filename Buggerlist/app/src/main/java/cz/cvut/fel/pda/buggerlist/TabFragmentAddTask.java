package cz.cvut.fel.pda.buggerlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 * Created by Matej on 17.04.2017.
 */

public class TabFragmentAddTask extends Fragment implements NewTaskDialogFragment.Listener{
    private static final String TAG = "AddTask";

    Database db;

    protected RecyclerView mRecyclerView;
    protected TaskAdapter mAdapter;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
        }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected List<Task> mTaskToday = new ArrayList<Task>();
    protected List<Task> mAllTasks = new ArrayList<Task>();

    private FloatingActionButton newTaskButton;
    private NewTaskDialogFragment newTaskDialogFragment;

    //newTask atributy
    String taskName;
    Integer taskDuration;
    String taskNote;
    Date taskDate= null;
    RadioGroup taskpriority;

    public TabFragmentAddTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_task));


    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_task));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("TASK");
        final View view = inflater.inflate(R.layout.tab_fragment_add_task, container,false);
        //setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_task));

        db = new Database(getActivity());
        mAllTasks = db.getUnfinishedTasks(0);
        initDataset();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.task_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new TaskAdapter(getActivity(), view, mTaskToday);
        mRecyclerView.setAdapter(mAdapter);

        //dialog pro novy ukol
        newTaskButton = (FloatingActionButton) view.findViewById(R.id.add_new_task_bt);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newTaskDialogFragment = new NewTaskDialogFragment();
//                newTaskDialogFragment.setTargetFragment(this, "request_code");
                newTaskDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                newTaskDialogFragment.show(fm, "new_task_dialog");
//                Window window = newTaskDialogFragment.getDialog().getWindow();

//                final Dialog dialog = new Dialog(getActivity());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.activity_new_task_form);
//                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                dialog.setTitle(R.string.newTask);

//
//
//                //addTask button
//                TextView addTask = (TextView) dialog.findViewById(R.id.new_task_save);
//                addTask.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v2) {
//                        //
//                        taskName = ((EditText)dialog.findViewById(R.id.new_task_name)).getText().toString();
//                        taskpriority.getCheckedRadioButtonId();
//
//                        if(taskDate == null){
//                            Task newTask = new Task(taskName, Calendar.getInstance(), 20, 0);
//                            db.createTask(newTask);
//
//                            mAdapter.notifyDataSetChanged();
//                            dialog.dismiss();
//                        }
//                        else {
//
//                        }
//                    }
//                });
//
//                //back button
//                ImageView backDialog = (ImageView) dialog.findViewById(R.id.new_task_back);
//                backDialog.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v2) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        dialog.dismiss();
//                    }
//                });
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void returnData(long taskId) {
        Task task = db.getTask(taskId);
        mTaskToday.add(task);
        mAdapter.notifyItemInserted(mTaskToday.size()-1 );
        mAdapter.notifyDataSetChanged();
    }

    private void initDataset() {

            //ukoly ze vsech projektu ktere maji dnesni datum
//            Log.d(TAG, "size " + tasks.size());
            for (int i = 0; i < mAllTasks.size(); i++){
                if(mAllTasks.get(i).getDate().DATE == Calendar.getInstance().DATE){
                    mTaskToday.add(mAllTasks.get(i));
                    //Log.d(TAG, "add  " + tasks.size());
                }
            }

    }

}
