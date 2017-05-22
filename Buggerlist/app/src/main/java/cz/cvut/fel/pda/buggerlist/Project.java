package cz.cvut.fel.pda.buggerlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matejkormanik on 4/29/17.
 */
public class Project  {

    private long id;
    private String name;
    private List<Task> tasks;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
        this.tasks =new ArrayList<>();
    }


    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int taskID) {
        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            if(it.next().getId() == taskID)
                it.remove();
        }
    }

    public Task getTask(int taskID) {
        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            Task t = it.next();
            if(t.getId() == taskID)
                return t;
        }
        return null;
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

    public int getNumberOfTasks() {return tasks.size();}
    public List<Task> getListTasks() {return tasks;}

}
