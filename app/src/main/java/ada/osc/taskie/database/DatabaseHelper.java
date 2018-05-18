package ada.osc.taskie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskCategory;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "taskie.sqlite";
    public static final int DATABASE_VERSION = 5;

    private Dao<Task, String> taskDao = null;
    private Dao<Category, String> categoryDao = null;
    private Dao<TaskCategory, String> taskCategoryDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Task.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, TaskCategory.class);
        } catch (SQLException e) {
            Log.e("Taskie", "Unable to create database",e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, TaskCategory.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Task, String> getTaskDao() throws SQLException {
        if (taskDao == null) {
            taskDao = getDao(Task.class);
        }
        return taskDao;
    }
    public Dao<Category, String> getCategoryDao() throws SQLException {
        if (categoryDao == null) {
            categoryDao = getDao(Category.class);
        }
        return categoryDao;
    }
    public Dao<TaskCategory, String> getTaskCategoryDao() throws SQLException {
        if (taskCategoryDao == null) {
            taskCategoryDao = getDao(TaskCategory.class);
        }
        return taskCategoryDao;
    }

}
