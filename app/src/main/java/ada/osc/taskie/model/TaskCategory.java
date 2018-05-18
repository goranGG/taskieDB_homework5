package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = TaskCategory.TASK_CATEGORY_TABLE)
public class TaskCategory {

    public static final String TASK_CATEGORY_TABLE = "task_category";
    public static final String TASK_CATEGORY_ID = "id";
    public static final String TASK_CATEGORY_TASK_ID = "task_id";
    public static final String TASK_CATEGORY_CATEGORY_ID = "category_id";
    @DatabaseField(id = true, columnName = TASK_CATEGORY_ID)
    private String mId;
    @DatabaseField(columnName = TASK_CATEGORY_TASK_ID, foreign = true)
    private Task mTask;
    @DatabaseField(columnName = TASK_CATEGORY_CATEGORY_ID, foreign = true)
    private Category mCategory;

    public TaskCategory() {
        mId = UUID.randomUUID().toString();
    }

    public TaskCategory(Task mTask, Category mCategory) {
        mId = UUID.randomUUID().toString();
        this.mTask = mTask;
        this.mCategory = mCategory;
    }

    public Task getTask() {
        return mTask;
    }

    public void setTask(Task mTask) {
        this.mTask = mTask;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category mCategory) {
        this.mCategory = mCategory;
    }
}
