package ada.osc.taskie.ui.task;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskCategory;
import ada.osc.taskie.ui.TaskCategory.CategoryOnTaskAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mTasks;
    private TaskClickListener mListener;

    private Context mContext;

    private CategoryOnTaskAdapter mAdapter;

    private DatabaseHelper databaseHelper = null;
    private Dao<Category, String> categoryDao;
    private Dao<TaskCategory, String> taskCategoryDao;

    public TaskAdapter(TaskClickListener listener,Context context) {
        mListener = listener;
        mTasks = new ArrayList<>();
        mContext = context;
        getAllDaos();
    }

    public void refreshData(List<Task> tasks) {
        mTasks.clear();
        mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        Task t = mTasks.get(position);
        holder.mTitle.setText(t.getTitle());
        holder.mDescription.setText(t.getDescription());
        switch (t.getPriority()) {
            case 0:
                holder.mPrioritiy.setImageResource(R.drawable.shape_low_priority);
                break;
            case 1:
                holder.mPrioritiy.setImageResource(R.drawable.shape_medium_priority);
                break;
            case 2:
                holder.mPrioritiy.setImageResource(R.drawable.shape_high_priority);
        }
        holder.mDate.setText(formatDate(t.getDate()));
        holder.mStatus.setChecked(t.isDone());
        setUpCategoryOnTaskRecyclerView(holder, t);
    }

    private void setUpCategoryOnTaskRecyclerView(TaskViewHolder holder, Task t) {
        try {
            mAdapter = new CategoryOnTaskAdapter(lookupCategoriesForTask(t),mContext);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager llm = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.mRecyclerView.setLayoutManager(llm);
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v, mListener);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_task_title)
        TextView mTitle;
        @BindView(R.id.textview_task_description)
        TextView mDescription;
        @BindView(R.id.textview_task_date)
        TextView mDate;
        @BindView(R.id.imageview_task_priority)
        ImageView mPrioritiy;
        @BindView(R.id.switch_task_status)
        Switch mStatus;
        @BindView(R.id.recycler_view_task_item_category)
        RecyclerView mRecyclerView;

        public TaskViewHolder(View view, TaskClickListener taskClickListener) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick
        public void onTaskClick(){
            mListener.onClick(mTasks.get(getAdapterPosition()));
        }
        @OnLongClick
        public boolean onLongClick(){
            return mListener.onLongClick(mTasks.get(getAdapterPosition()));
        }

        @OnClick(R.id.imageview_task_priority)
        public void changePriorityOnClick() {
            mListener.onPriorityChangeClick(mTasks.get(getAdapterPosition()));
        }

        @OnClick(R.id.switch_task_status)
        public void changeStatusOnSwitch(){
            mListener.onStatusSwitchChange(mTasks.get(getAdapterPosition()));
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("dd.\nMMM");
        return sd.format(date);
    }

    private PreparedQuery<Category> categoriesForPostQuery = null;

    private List<Category> lookupCategoriesForTask(Task task) throws SQLException {
        if (categoriesForPostQuery == null) {
            categoriesForPostQuery = makeCategoriesForTaskQuery();
        }
        categoriesForPostQuery.setArgumentHolderValue(0, task);
        return categoryDao.query(categoriesForPostQuery);
    }

    private PreparedQuery<Category> makeCategoriesForTaskQuery() throws SQLException {
        QueryBuilder<TaskCategory, String> taskCategoryQb = taskCategoryDao.queryBuilder();
        taskCategoryQb.selectColumns(TaskCategory.TASK_CATEGORY_CATEGORY_ID);
        SelectArg postSelectArg = new SelectArg();
        taskCategoryQb.where().eq(TaskCategory.TASK_CATEGORY_TASK_ID, postSelectArg);

        QueryBuilder<Category, String> categoryQb = categoryDao.queryBuilder();
        categoryQb.where().in(Category.CATEGORY_ID, taskCategoryQb);
        return categoryQb.prepare();
    }

    private void getAllDaos() {
        try {
            databaseHelper = new DatabaseHelper(mContext);
            categoryDao = databaseHelper.getCategoryDao();
            taskCategoryDao = databaseHelper.getTaskCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}