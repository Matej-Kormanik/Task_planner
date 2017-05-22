package cz.cvut.fel.pda.buggerlist;

import android.app.DatePickerDialog;
import android.app.Dialog;;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dominik on 10.05.2017.
 */

public class NewTaskDialogFragment extends DialogFragment {



//    private TextView selection;
    private TextView saveTask;
    private Listener mListener;
    private Database db;
    private List<Project> mProject = new ArrayList<Project>();
    private  List<String> mProjectName = new ArrayList<String>();
    private Spinner spin;


    //new task atributes
    private RadioGroup priority;
    private String mSelectedProjectName;
    private long mSelectedProjectId;
    private String mInfo;
    private static TextView mDate;
    private static TextView mTime;
    private static Calendar mDateTask;
    private DialogFragment mDatePickerDialog;
    private DialogFragment mTimePickerDialog;
    private SimpleDateFormat mDateFormatter;




    public NewTaskDialogFragment(){}

//    public static NewTaskDialogFragment newInstance(){
//        NewTaskDialogFragment fragment = new NewTaskDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("lala", "lalatext");
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_new_task_form, container,false);

        db = new Database(getActivity());

      //vyber projekt
        mProject = db.getAllProjects();
        getProjectsNames();
        spin = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mProjectName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);

        //zvol datum
        mDateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        mDate= (TextView) view.findViewById(R.id.new_task_date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatePickerDialog = new DatePickerFragment();
                mDatePickerDialog.show(getFragmentManager(), "datapicker");
            }
        });


        //zvol cas
        mTime = (TextView) view.findViewById(R.id.new_task_time);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog = new TimePickerFragment();
                mTimePickerDialog.show(getFragmentManager(), "timepicker");
            }
        });


        // priorita defaultne normal
        priority = (RadioGroup) view.findViewById(R.id.new_task_priority);
        priority.check(R.id.pri2);

        //aditional info

        //zobrazit cast s duration az v momente kdy uzivatel vyplni datum

        //pokud nevyplni cas volat Task(String name, long projectId) kde projectId je projekt Inbox

        //ulozi ukol a pokud uzivatel vyplnil datum presune do kalendare
        saveTask = (TextView) view.findViewById(R.id.new_task_save);
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vyplnit podle dat uzivatele;
                mSelectedProjectName = spin.getSelectedItem().toString();
                mSelectedProjectId = getProjectId(mSelectedProjectName);
                mInfo = ((EditText) view.findViewById(R.id.new_task_info)).getText().toString();
                Task tasktest = new Task("Completly new task", Calendar.getInstance(), 90, mSelectedProjectId);

                tasktest.setPriority(getPriorityValue(priority.getCheckedRadioButtonId()));

                if(tasktest.getDate() != null){
                    startCalendar(tasktest);
                }

                //ulozit do databaze;
                long tasktest_id = db.createTask(tasktest);
                tasktest.setId(tasktest_id);
                db.updateTask(tasktest);

                if (mListener != null) {
                    mListener.returnData(tasktest.getId());
                }

                dismiss();
            }
        });

        db.close();
        return view;
    }


    //spousti predvyplneny kalendar
    private void startCalendar(Task task) {
        Calendar beginTime = task.getDate();
        Calendar endTime = task.getDate();
        endTime.add(Calendar.MINUTE, task.getDuration());

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, task.getName())
                .putExtra(CalendarContract.Events.DESCRIPTION, task.getDescription())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    private void getProjectsNames(){
        for(int i=0; i< mProject.size(); i++){
            mProjectName.add(mProject.get(i).getName());
        }
    }
    public int getPriorityValue(int id){
        int pr;
        if(id == R.id.pri1) pr = 1;
        else if(id == R.id.pri2) pr = 2;
        else pr =3;

        return pr;
    }

    private long getProjectId(String name){
        long id =0;
        for (int i=0; i < mProject.size(); i++){
            if(name == mProject.get(i).getName()) id = mProject.get(i).getId();
        }
        return id;
    }

    public void setDate(String date){
        mDate.setText(date);
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            mDate.setText(sdf.format(c.getTime()));
            mDateTask.set(year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            mTime.setText(hourOfDay + ":"	+ minute);
            mDateTask.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mDateTask.set(Calendar.MINUTE, minute);
        }
    }

//    private void setDateTimeField() {
//        mDate.setOnClickListener(getActivity());
//
//        Calendar newCalendar = Calendar.getInstance();
//        mDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
//
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                mDate.setText(mDateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//    }


    public void setListener(Listener listener) {
        mListener = listener;
    }



    static interface Listener {
        void returnData(long taskId);
    }
}

