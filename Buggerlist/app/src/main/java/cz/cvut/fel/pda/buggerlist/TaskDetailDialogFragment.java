package cz.cvut.fel.pda.buggerlist;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Matej on 14.05.2017.
 */

public class TaskDetailDialogFragment extends DialogFragment {

    private long mTaskID;
    Database db;
    private Listener mListener;

    TextView taskName;
    TextView projectName;
    TextView taskDate;
    TextView taskDuration;

    int buttClick = -1;


    public TaskDetailDialogFragment(){}

    public static TaskDetailDialogFragment newInstance(long taskID){
        TaskDetailDialogFragment frag = new TaskDetailDialogFragment();
        Bundle args = new Bundle();
        args.putLong("taskID",taskID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_detail, container, false);

        mTaskID = getArguments().getLong("taskID");
        db = new Database(getActivity());
        Task actualTask = db.getTask(mTaskID);
        Project actualProject = db.getProject(actualTask.getProjectId());

        taskName = (TextView) view.findViewById(R.id.task_detail_name);
        projectName = (TextView) view.findViewById(R.id.task_detail_project);
        taskDate = (TextView) view.findViewById(R.id.task_detail_date);
        taskDuration = (TextView) view.findViewById(R.id.task_detail_time);

        Calendar cal = actualTask.getDate();
        taskName.setText(actualTask.getName());
        projectName.setText(actualProject.getName());
        DateFormat form = new SimpleDateFormat("EEEE, MMMM d");
        taskDate.setText(form.format(cal.getTime()));
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String startTime = formatter.format(cal.getTime());

        cal.add(Calendar.MINUTE, actualTask.getDuration());
        String endTime = formatter.format(cal.getTime());
        String timeInfo = startTime + " -- " + endTime +" ("+ actualTask.getDuration() +")";
        taskDuration.setText(timeInfo);

        ImageView delete = (ImageView) view.findViewById(R.id.button_delete);
        ImageView done = (ImageView) view.findViewById(R.id.button_done);
        ImageView edit = (ImageView) view.findViewById(R.id.button_edit);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttClick = 0;
                if (mListener != null) {
                    mListener.returnData(buttClick);
                }

                dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttClick = 1;
                if (mListener != null) {
                    mListener.returnData(buttClick);
                }

                dismiss();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttClick = 2;
                if (mListener != null) {
                    mListener.returnData(buttClick);
                }
                dismiss();
            }
        });

        db.close();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    static interface Listener {
        void returnData(int result);
    }

}
