package ada.osc.taskie.ui.TaskCategory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryOnTaskAdapter extends RecyclerView.Adapter<CategoryOnTaskAdapter.TaskCategoryViewHolder> {
    private List<Category> mCategories;
    Context mContext;


    public CategoryOnTaskAdapter(List<Category> categories, Context context) {
        mCategories = categories;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(final TaskCategoryViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mCategory.setText(c.getName());
        changeColorOnCategory(holder, c);
    }

    private void changeColorOnCategory(TaskCategoryViewHolder holder, Category c) {
        switch (c.getColor()) {
            case Consts.COLOR_RED:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryRed));
                break;
            case Consts.COLOR_GREEN:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryGreen));
                break;
            case Consts.COLOR_BLUE:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryBlue));
                break;
            default:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryBlack));
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public TaskCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_on_task, parent, false);
        return new TaskCategoryViewHolder(v);
    }

    public class TaskCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_on_task_name)
        TextView mCategory;


        public TaskCategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}