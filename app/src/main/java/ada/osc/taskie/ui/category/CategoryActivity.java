package ada.osc.taskie.ui.category;

import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.TaskCategory;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity implements CategoryDialogFragment.onCategoryChangeListener {
    public static final String CREATE_CATEGORY_TAG = "createCategoryDialog";

    @BindView(R.id.recycler_view_categories)
    RecyclerView mRecyclerView;

    private DatabaseHelper databaseHelper = null;

    private CategoryAdapter mAdapter;
    private Dao<Category, String> categoryDao;
    private Dao<TaskCategory, String> taskCategoryDao;
    CategoryClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.categories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setupFloatingButton();
        getDaos();
        setUpCategoryClickListener();
        setUpRecyclerView();
    }

    private void getDaos() {
        try {
            databaseHelper = new DatabaseHelper(this);
            categoryDao = databaseHelper.getCategoryDao();
            taskCategoryDao = databaseHelper.getTaskCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUpCategoryClickListener() {
        mListener = new CategoryClickListener() {
            @Override
            public void onClick(Category category) {
                showTaskDialogFragment(Consts.UPDATE_ACTION, category.getId());
            }

            @Override
            public boolean onLongClick(Category category) {
                QueryBuilder<TaskCategory, String> queryBuilder = taskCategoryDao.queryBuilder();
                try {
                    if (queryBuilder.where().eq(TaskCategory.TASK_CATEGORY_CATEGORY_ID, category.getId()).query().size() > 0) {
                        Toast.makeText(getApplicationContext(),
                                "Unable to delete " + category.getName()
                                + " there are pending tasks", Toast.LENGTH_LONG).show();
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                CategoryHelper.deleteCategory(category, CategoryActivity.this, categoryDao, mAdapter);
                return false;
            }
        };
    }

    private void setUpRecyclerView() {
        mAdapter = new CategoryAdapter(mListener, this);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.refreshData(getCategories());
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(Consts.CREATE_ACTION, null);
            }
        });
    }

    //Show task dialog
    private void showTaskDialogFragment(int action, String categoryId) {
        DialogFragment createCategoryDialogFragment = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            args.putString("categoryId", categoryId);
        }
        createCategoryDialogFragment.setArguments(args);
        createCategoryDialogFragment.show(this.getFragmentManager(), CREATE_CATEGORY_TAG);
    }

    public List<Category> getCategories() {
        return CategoryHelper.getCategories(categoryDao);
    }

    @Override
    public void onCategoryChange(int action) {
        mAdapter.refreshData(getCategories());
    }

}
