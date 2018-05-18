package ada.osc.taskie.ui.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.Task;

public class TaskHelper {

    private static final int SORT_ASC = 1;
    private static final int SORT_DESC = 2;
    private static final int FILTER_DONE = 1;
    private static final int FILTER_NOT_DONE = 2;

    public static List<Task> getTasksFromDb(int filterType, int sortType, Dao<Task,String> taskDao) {
        QueryBuilder<Task, String> queryBuilder = taskDao.queryBuilder();
        queryBuilder = sortTasksByPriority(queryBuilder, sortType);
        return filterTasksByStatus(queryBuilder, filterType);
    }

    private static List<Task> filterTasksByStatus(QueryBuilder<Task, String> queryBuilder,int filterType) {
        try {
            switch (filterType) {
                case FILTER_DONE:
                    return queryBuilder.where().eq(Task.TASK_STATUS, true).query();
                case FILTER_NOT_DONE:
                    return queryBuilder.where().eq(Task.TASK_STATUS, false).query();
                default:
                    return queryBuilder.query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static QueryBuilder<Task, String> sortTasksByPriority(QueryBuilder<Task, String> queryBuilder, int sortType) {
        switch (sortType) {
            case SORT_ASC:
                return queryBuilder.orderBy(Task.TASK_PRIORITY, true);
            case SORT_DESC:
                return queryBuilder.orderBy(Task.TASK_PRIORITY, false);
            default:
                return queryBuilder;
        }
    }

    // Delete task from DB
    public static void deleteTask(final Task task, Context context, final Dao<Task,String> taskDao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete '" + task.getTitle() + "' task");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteBuilder<Task, String> deleteBuilder = taskDao.deleteBuilder();
                try {
                    deleteBuilder.where().eq(Task.TASK_ID, task.getId());
                    deleteBuilder.delete();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void updatePriorityOnClick(Task task, Dao<Task,String> taskDao) {
        try {
            UpdateBuilder<Task, String> updateBuilder = taskDao.updateBuilder();
            updateBuilder.where().eq(Task.TASK_ID, task.getId());
            switch (task.getPriority()) {
                case 0:
                    updateBuilder.updateColumnValue(Task.TASK_PRIORITY, 1);
                    break;
                case 1:
                    updateBuilder.updateColumnValue(Task.TASK_PRIORITY, 2);
                    break;
                case 2:
                    updateBuilder.updateColumnValue(Task.TASK_PRIORITY, 0);
                    break;
            }
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateStatusOnSwitchChange(Task task, Dao<Task,String> taskDao) {
        try {
            UpdateBuilder<Task, String> updateBuilder = taskDao.updateBuilder();
            updateBuilder.where().eq(Task.TASK_ID, task.getId());
            updateBuilder.updateColumnValue(Task.TASK_STATUS, !task.isDone());
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
