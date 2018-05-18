package ada.osc.taskie.ui.TaskCategory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.TaskCategory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskCategoryAdapter extends RecyclerView.Adapter<TaskCategoryAdapter.TaskCategoryViewHolder> {
    private List<Category> mCategories;
    private List<Category> mCategoriesOnTask;
    private TaskCategoryClickListener mListener;
    private DatabaseHelper databaseHelper = null;
    private String mTaskId;
    private Context mContext;

    private Dao<TaskCategory, String> taskCategoryDao;

    public TaskCategoryAdapter(TaskCategoryClickListener listener, String taskId, Context context) {
        mListener = listener;
        mTaskId = taskId;
        mCategories = new ArrayList<>();
        mCategoriesOnTask = new ArrayList<>();
        mContext = context;
        try {
            databaseHelper = new DatabaseHelper(context);
            taskCategoryDao = databaseHelper.getTaskCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TaskCategoryAdapter(TaskCategoryClickListener listener, Context context) {
        mListener = listener;
        mCategories = new ArrayList<>();
        mCategoriesOnTask = new ArrayList<>();
        mContext = context;
        try {
            databaseHelper = new DatabaseHelper(context);
            taskCategoryDao = databaseHelper.getTaskCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshData(List<Category> categories, List<Category> categoriesOnTask) {
        mCategories.clear();
        mCategories.addAll(categories);
        mCategoriesOnTask.clear();
        mCategoriesOnTask.addAll(categoriesOnTask);
        notifyDataSetChanged();
    }

    public void refreshData(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final TaskCategoryViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mName.setText(c.getName());
        changeColorOnCategory(holder, c);
        if (mTaskId != null) {
            QueryBuilder<TaskCategory, String> queryBuilder = taskCategoryDao.queryBuilder();
            try {
                if (queryBuilder.where().eq(TaskCategory.TASK_CATEGORY_TASK_ID, mTaskId).and().eq(TaskCategory.TASK_CATEGORY_CATEGORY_ID, c.getId()).query().size() == 1) {
                    holder.mCategorySelected.setVisibility(View.VISIBLE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public TaskCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new TaskCategoryViewHolder(v, mListener);
    }

    public class TaskCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_name)
        TextView mName;
        @BindView(R.id.imageview_category_selected)
        ImageView mCategorySelected;

        public TaskCategoryViewHolder(View view, TaskCategoryClickListener taskCategoryClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mCategorySelected.getLayoutParams().height = 55;
        }

        @OnClick
        public void onCategoryClick() {
            if (mCategorySelected.getVisibility() == View.GONE) {
                mCategorySelected.setVisibility(View.VISIBLE);
            } else {
                mCategorySelected.setVisibility(View.GONE);
            }
            mListener.onClick(mCategories.get(getAdapterPosition()), mCategorySelected.getVisibility());
        }

    }

    private void changeColorOnCategory(TaskCategoryViewHolder holder, Category c) {
        switch (c.getColor()) {
            case Consts.COLOR_RED:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryRed));
                break;
            case Consts.COLOR_GREEN:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryGreen));
                break;
            case Consts.COLOR_BLUE:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryBlue));
                break;
        }
    }

}