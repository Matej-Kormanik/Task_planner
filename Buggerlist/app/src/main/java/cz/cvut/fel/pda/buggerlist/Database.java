package cz.cvut.fel.pda.buggerlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Dominik on 12.05.2017.
 */

public class Database extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "Database";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "buggerlist";
    // Table Names
    private static final String TABLE_PROJECT = "projects";
    private static final String TABLE_TASK = "tasks";

    // Common column names
    private static final String KEY_ID = "id";

    // Projects Table - column names
    private static final String KEY_PROJECT_NAME = "project_name";

    // Tasks Table - column names
    private static final String KEY_PROJECT_ID = "project_id";
    private static final String KEY_TASK_NAME = "task_name";
    private static final String KEY_TASK_DATE = "task_date";
    private static final String KEY_TASK_DURATION = "task_duration";
    private static final String KEY_TASK_PRIORITY = "task_priority";
    private static final String KEY_TASK_DESCRIPTION = "task_description";
    private static final String KEY_TASK_DONE = "task_done";



    // Table Create Statements
    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE "
            + TABLE_PROJECT + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_PROJECT_NAME + " TEXT"
            + ");";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK
            + "( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_PROJECT_ID + " INTEGER, "
            + KEY_TASK_NAME + " TEXT,"
            + KEY_TASK_DATE +" NUMERIC_TYPE,"
            + KEY_TASK_DURATION + " INTEGER,"
            + KEY_TASK_PRIORITY + " INTEGER,"
            + KEY_TASK_DESCRIPTION + " TEXT,"
            + KEY_TASK_DONE + " INTEGER,"
            + "FOREIGN KEY(" + KEY_PROJECT_ID + ") REFERENCES " + TABLE_PROJECT+"(" + KEY_ID + ")"
            + " );";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_TASK);

        Log.i(LOG, CREATE_TABLE_PROJECT);
        Log.i(LOG, CREATE_TABLE_TASK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // create new tables
        onCreate(db);
    }


    // ------------------------ task table methods ----------------//

    public long createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROJECT_ID, task.getProjectId());
        values.put(KEY_TASK_NAME, task.getName());
        values.put(KEY_TASK_DATE, task.getDate().getTimeInMillis());
        values.put(KEY_TASK_DURATION, task.getDuration());
        values.put(KEY_TASK_PRIORITY, task.getPriority());
        values.put(KEY_TASK_DESCRIPTION, task.getDescription());
        values.put(KEY_TASK_DONE, task.getDone());

        // insert row
        long task_id = db.insert(TABLE_TASK, null, values);

        return task_id;
    }

    /*
	 * get single task
	 */
    public Task getTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE "
                + KEY_ID + " = " + task_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Task task = new Task();
        task.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        task.setProjectId(c.getLong(c.getColumnIndex(KEY_PROJECT_ID)));
        task.setName(c.getString(c.getColumnIndex(KEY_TASK_NAME)));
        task.setDate(c.getLong(c.getColumnIndex(KEY_TASK_DATE)));
        task.setDuration(c.getInt(c.getColumnIndex(KEY_TASK_DURATION)));
        task.setPriority(c.getInt(c.getColumnIndex(KEY_TASK_PRIORITY)));
        task.setDescription(c.getString(c.getColumnIndex(KEY_TASK_DESCRIPTION)));
        task.setDone(c.getInt(c.getColumnIndex(KEY_TASK_DONE)));

        return task;
    }

    /**
     * getting all task
     * */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                task.setProjectId(c.getLong(c.getColumnIndex(KEY_PROJECT_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK_NAME)));
                task.setDate(c.getLong(c.getColumnIndex(KEY_TASK_DATE)));
                task.setDuration(c.getInt(c.getColumnIndex(KEY_TASK_DURATION)));
                task.setPriority(c.getInt(c.getColumnIndex(KEY_TASK_PRIORITY)));
                task.setDescription(c.getString(c.getColumnIndex(KEY_TASK_DESCRIPTION)));
                task.setDone(c.getInt(c.getColumnIndex(KEY_TASK_DONE)));

                tasks.add(task);
            } while (c.moveToNext());
        }

        return tasks;
    }

    public List<Task> getProjectTasks(long project_id) {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE "
                + KEY_PROJECT_ID + " = " +project_id;;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                task.setProjectId(c.getLong(c.getColumnIndex(KEY_PROJECT_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK_NAME)));
//                task.setDate(setDateTime(c.getString(c.getColumnIndex(KEY_TASK_DATE))));
                task.setDate(c.getLong(c.getColumnIndex(KEY_TASK_DATE)));
                task.setDuration(c.getInt(c.getColumnIndex(KEY_TASK_DURATION)));
                task.setPriority(c.getInt(c.getColumnIndex(KEY_TASK_PRIORITY)));
                task.setDescription(c.getString(c.getColumnIndex(KEY_TASK_DESCRIPTION)));
                task.setDone(c.getInt(c.getColumnIndex(KEY_TASK_DONE)));

                tasks.add(task);
            } while (c.moveToNext());
        }

        return tasks;
    }

    /*
         * Updating a task
         */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROJECT_ID, task.getProjectId());
        values.put(KEY_TASK_NAME, task.getName());
        values.put(KEY_TASK_DATE, task.getDate().toString());
        values.put(KEY_TASK_DURATION, task.getDuration());
        values.put(KEY_TASK_PRIORITY, task.getPriority());
        values.put(KEY_TASK_DESCRIPTION, task.getDescription());
        values.put(KEY_TASK_DONE, task.getDone());

        // updating row
        return db.update(TABLE_TASK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    /*
     * Deleting a task
     */
    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, KEY_ID + " = ?",
                new String[] { String.valueOf(task_id) });
        Log.e(LOG, "delete: " + String.valueOf(task_id));
    }

    //get today unfinish task
    public List<Task> getUnfinishedTasks(int done) {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE "
                + KEY_TASK_DONE + " = " + done;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                task.setProjectId(c.getLong(c.getColumnIndex(KEY_PROJECT_ID)));
                task.setName(c.getString(c.getColumnIndex(KEY_TASK_NAME)));
                task.setDate(c.getLong(c.getColumnIndex(KEY_TASK_DATE)));
                task.setDuration(c.getInt(c.getColumnIndex(KEY_TASK_DURATION)));
                task.setPriority(c.getInt(c.getColumnIndex(KEY_TASK_PRIORITY)));
                task.setDescription(c.getString(c.getColumnIndex(KEY_TASK_DESCRIPTION)));
                task.setDone(c.getInt(c.getColumnIndex(KEY_TASK_DONE)));

                tasks.add(task);
            } while (c.moveToNext());
        }

        return tasks;
    }

    // ------------------------ project table methods ----------------//

    //create project
    public long createProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROJECT_NAME, project.getName());

        // insert row
        long project_id = db.insert(TABLE_PROJECT, null, values);

        return project_id;
    }

    //get project
    public Project getProject(long project_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT + " WHERE "
                + KEY_ID + " = " + project_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Project project = new Project();

        project.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        project.setName(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));

        return project;
    }

    /**
     * getting all projects
     * */

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<Project>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Project project = new Project();

                project.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                project.setName(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));

                projects.add(project);
            } while (c.moveToNext());
        }

        return projects;
    }



    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
//set datetime
    private Date setDateTime(String time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date datetime = null;
        try {
            datetime = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datetime;
    }


}
