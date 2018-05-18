package ada.osc.taskie.ui.category;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mCategories;
    private CategoryClickListener mListener;
    Context mContext;


    public CategoryAdapter(CategoryClickListener listener, Context context) {
        mListener = listener;
        mCategories = new ArrayList<>();
        mContext = context;
    }

    public void refreshData(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mName.setText(c.getName());
        switch (c.getColor()) {
            case Consts.COLOR_RED:
                holder.mName.setTextColor(ContextCompat.getColor(mContext,R.color.colorCategoryRed));
                break;
            case Consts.COLOR_GREEN:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryGreen));
                break;
            case Consts.COLOR_BLUE:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryBlue));
                break;
            default:
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorCategoryBlack));
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(v, mListener);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_name)
        TextView mName;

        public CategoryViewHolder(View view, CategoryClickListener categoryClickListener) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick
        public void onCategoryClick() {
            mListener.onClick(mCategories.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onLongClick() {
            return mListener.onLongClick(mCategories.get(getAdapterPosition()));
        }
    }

}