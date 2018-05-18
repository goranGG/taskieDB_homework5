package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;


@DatabaseTable(tableName = Task.TASK_TABLE)
public class Task {

    public static final String TASK_TABLE = "tasks";
    public static final String TASK_ID = "id";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_DATE = "date";
    public static final String TASK_STATUS = "status";

    @DatabaseField(columnName = TASK_ID, id = true)
    private String mId;
    @DatabaseField(columnName = TASK_TITLE)
    private String mTitle;
    @DatabaseField(columnName = TASK_DESCRIPTION)
    private String mDescription;
    @DatabaseField(columnName = TASK_PRIORITY)
    private int mPriority;
    @DatabaseField(columnName = TASK_DATE)
    private Date mDate;
    @DatabaseField(columnName = TASK_STATUS)
    private boolean mDone;

    public Task() {
    }

    public Task(String mTitle, String mDescription, int mPriority, Date mDate) {
        mId = UUID.randomUUID().toString();
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mPriority = mPriority;
        this.mDate = mDate;
        mDone = false;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean Status) {
        this.mDone = Status;
    }

    public void updateTaskPriority(Task task) {
        switch (task.getPriority()) {
            case 0:
                task.setPriority(1);
                break;
            case 1:
                task.setPriority(2);
                break;
            case 2:
                task.setPriority(0);
                break;
        }
    }


}
