package com.example.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

//    public void insertTask(ToDoModel task) {
//        ContentValues cv = new ContentValues();
//        cv.put(TASK, task.getTask());
//        cv.put(STATUS, 0);
//        db.insert(TODO_TABLE, null, cv);
//    }
//
//    public List<ToDoModel> getAllTasks() {
//        List<ToDoModel> taskList = new ArrayList<>();
//        Cursor cur = null;
//        db.beginTransaction();
//        try {
//            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
//            if (cur != null) {
//                if (cur.moveToFirst()) {
//                    do {
//                        ToDoModel task = new ToDoModel();
//                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
//                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
//                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
//                        taskList.add(task);
//                    } while (cur.moveToNext());
//                }
//            }
//        } finally {
//            db.endTransaction();
//            if (cur != null) {
//                cur.close();
//            }
//        }
//        return taskList;
//    }
public void insertTask(ToDoModel task) {
    ContentValues cv = new ContentValues();
    cv.put(TASK, task.getTask());
    cv.put(STATUS, 0);
    long result = db.insert(TODO_TABLE, null, cv);
    if (result == -1) {
        Log.e("DatabaseHandler", "Failed to insert task: " + task.getTask());
    } else {
        Log.i("DatabaseHandler", "Task inserted: " + task.getTask());
    }
}

    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                int idIndex = cur.getColumnIndex(ID);
                int taskIndex = cur.getColumnIndex(TASK);
                int statusIndex = cur.getColumnIndex(STATUS);
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cur.getInt(idIndex));
                    task.setTask(cur.getString(taskIndex));
                    task.setStatus(cur.getInt(statusIndex));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        Log.i("DatabaseHandler", "Retrieved tasks: " + taskList.size());
        return taskList;
    }


    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
    }
}
