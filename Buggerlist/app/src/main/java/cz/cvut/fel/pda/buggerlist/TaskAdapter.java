package cz.cvut.fel.pda.buggerlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
/**
 * Created by Matej on 25.04.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements TaskDetailDialogFragment.Listener {

    private static final String TAG = "TaskAdapter";

    private List<Task> mDataSet;
    private int mImageResourceIdOne = R.drawable.ic_priority_one;
    private int mImageResourceIdTwo = R.drawable.ic_priority_two;
    private int mImageResourceIdTree = R.drawable.ic_priority_3;

    Context mContext;
    private int newposition;
    Database db;
    View mSnackView;

    public TaskAdapter(Context context, View snackView,List<Task> dataset){
        mDataSet = dataset;
        mContext = context;
        mSnackView = snackView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView taskName;
        private final TextView start;
        private final TextView duration;
        private ImageView priority;

        public ViewHolder(View v) {
            super(v);
            taskName = (TextView) v.findViewById(R.id.task_name);
            start = (TextView) v.findViewById(R.id.start_text_view);
            duration = (TextView) v.findViewById(R.id.duration_text_view);
            priority = (ImageView) v.findViewById(R.id.priority_image);
        }

        public TextView getStart() {
            return start;
        }

        public TextView getDuration() {
            return duration;
        }

        public ImageView getPriority() {
            return priority;
        }

        public TextView getTaskName() {
            return taskName;
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);

        return new ViewHolder(v);
    }

    /**
     * Nastaveni zobrazovanych parametru pro jednotlive ukoly
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.getTaskName().setText(mDataSet.get(position).getName());

        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String date = formatter.format(mDataSet.get(position).getDate().getTime());
        viewHolder.getStart().setText(date);

        Integer dur = mDataSet.get(position).getDuration();
        viewHolder.getDuration().setText(dur.toString());
        viewHolder.getPriority().setImageResource(getPriorityId(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click position = " + viewHolder.getAdapterPosition());
                newposition = viewHolder.getAdapterPosition();
                showDetailTaskDialog();
            }
        });

    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public void showDetailTaskDialog(){
        FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
        TaskDetailDialogFragment dialog = TaskDetailDialogFragment.newInstance(mDataSet.get(newposition).getId());
        dialog.setListener(TaskAdapter.this);

//        dialog.getDialog().requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        dialog.getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.show(fm, "TaskDetailDialog");

    }

    @Override
    public void returnData(int result) {

        db = new Database(mContext);
        if(result == 0){
            String name = mDataSet.get(newposition).getName();
            db.deleteTask(mDataSet.get(newposition).getId());
            mDataSet.remove(newposition);
            notifyDataSetChanged();
            notifyItemRemoved(newposition);

            Snackbar snackbar = Snackbar.make(mSnackView,"Task: " + name + " deleted.",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(result == 1){
            String name = mDataSet.get(newposition).getName();
            mDataSet.get(newposition).setDone(1);
            Task task = mDataSet.get(newposition);
            db.updateTask(task);
            mDataSet.remove(newposition);
            notifyDataSetChanged();
            notifyItemRemoved(newposition);

            Snackbar snackbar = Snackbar.make(mSnackView,"Task: " + name + " done.",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(result == 2){
            Task task = mDataSet.get(newposition);
            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.activity_new_task_form);
            dialog.setTitle("Edit " + mDataSet.get(newposition).getName());
            //nastaveni aktualnich dat
            TextView taskname = (TextView) dialog.findViewById(R.id.new_task);
            taskname.setText("Edit " + mDataSet.get(newposition).getName());


            dialog.show();
            //ulozeni upraveneho tasku
            String name = ((EditText)dialog.findViewById(R.id.new_task_name)).getText().toString();
            task.setName(name);

            db.updateTask(task);
            notifyDataSetChanged();
            notifyItemChanged(newposition);

            Snackbar snackbar = Snackbar.make(mSnackView,"Task: " + name + " updated.",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        db.close();
    }

    private int getPriorityId (int position){
        int taskPriority = mDataSet.get(position).getPriority();

        if(taskPriority == 1){
            return mImageResourceIdOne;
        }
        else if(taskPriority == 2){
            return mImageResourceIdTwo;
        }
        else{
            return mImageResourceIdTree;
        }
    }
}
