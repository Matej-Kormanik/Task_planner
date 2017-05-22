package cz.cvut.fel.pda.buggerlist;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Matej on 22.04.2017.
 */

public class Task {

    private long id;
    private long projectId;
    private String name;
    private String description;
//    private Date date;
    private Calendar date;
    private int duration;
//    private Priority priority;
    private int priority;
    private int done;

    private static int idCounter = 0;

//    public Task() {
//        setId(idCounter++);
//    }

    public Task(){
    }

    public Task(String name, Calendar date, int duration, long projectId) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.projectId = projectId;
        this.done = 0;
        //priorita se defalutne nastavi pri
        //this.priority = Priority.VERYHIGH;
        this.priority = 1;
    }

    public Task(String name, long projectId){
        this.date = null;
        this.name = name;
        this.priority = 1;
        this.projectId = projectId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getProjectId() {
        return projectId;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(long date){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);

        this.date = c;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration ) {
        this.duration = duration;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    //    public Priority getPriority() {
//        return priority;
//    }
//
//    public void setPriority(Priority priority) {
//        this.priority = priority;
//    }


    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (name != task.name) return false;
        if (description != null ? !description.equals(task.description) : task.description != null)
            return false;
        return priority == task.priority;

    }

//    @Override
//    public int hashCode() {
//        int result = id;
//        result = 31 * result + (description != null ? description.hashCode() : 0);
//        result = 31 * result + (priority != null ? priority.hashCode() : 0);
//        return result;
//    }
}
