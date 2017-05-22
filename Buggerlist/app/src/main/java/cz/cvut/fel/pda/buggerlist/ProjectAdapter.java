package cz.cvut.fel.pda.buggerlist;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;



/**
 * Created by Matej on 22.04.2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private static final String TAG = "ProjectAdapter";

    private List<Project> mDataSet;
    private List<Integer> mImageResourceId = Arrays.asList(
            R.drawable.ic_project_inbox,
            R.drawable.ic_project_school,
            R.drawable.ic_project_work,
            R.drawable.ic_project_mobile,
            R.drawable.ic_project_movie);
    //private int mColorCircle;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private ImageView imageView;
//        private GradientDrawable shape;
//        private LayerDrawable bgDrawable;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.project_label);
            imageView = (ImageView) v.findViewById(R.id.project_image);

//            LayerDrawable bgDrawable = (LayerDrawable)v.getBackground();
//            GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.project_circle);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {  return imageView; }

        //public GradientDrawable getShape(){ return shape;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ProjectAdapter(List<Project> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_project, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
//        Log.e(TAG, "pozice "+position);
        viewHolder.getTextView().setText(mDataSet.get(position).getName());
        viewHolder.getImageView().setImageResource(mImageResourceId.get(position));
        //viewHolder.getShape().setColor(mColorCircle);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "click position = " + viewHolder.getAdapterPosition());
                Intent TDetail = new Intent().setClass(v.getContext(), ProjectDetailActivity.class);
                TDetail.putExtra("project", mDataSet.get(position).getName());
                TDetail.putExtra("projectID",  mDataSet.get(position).getId());
                v.getContext().startActivity(TDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "velikost "+mDataSet.size());
        return 5;
        //testovani jinak nutno mazat databazi
        //return mDataSet.size();
    }
}
