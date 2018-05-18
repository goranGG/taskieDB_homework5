package ada.osc.taskie.ui.task;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskCategory;
import ada.osc.taskie.ui.category.CategoryActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements TaskDialogFragment.OnTaskChangeListener {

    public static final String CREATE_TASK_TAG = "createTaskDialog";


    private int mSortPriorityType;
    private int mFilterStatusType = 0;

    private DatabaseHelper databaseHelper = null;

    @BindView(R.id.spinner_task_priority)
    Spinner mSortType;
    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    private Dao<Task, String> taskDao;
    private Dao<Category, String> categoryDao;
    private Dao<TaskCategory, String> taskCategoryDao;
    TaskClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setUpTaskClickListener();
        setUpTaskRecyclerView();
        setupPrioritySpinner();
        setupFloatingButton();
        try {
            databaseHelper = new DatabaseHelper(this);
            taskDao = databaseHelper.getTaskDao();
            categoryDao = databaseHelper.getCategoryDao();
            taskCategoryDao = databaseHelper.getTaskCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.refreshData(getTasks());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    //Setup floating button to showTaskDialogFragment(create action)
    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(Consts.CREATE_ACTION, null);
            }
        });
    }

    private void setUpTaskRecyclerView() {
        mAdapter = new TaskAdapter(mListener,this);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpTaskClickListener() {
        mListener = new TaskClickListener() {
            @Override
            public void onClick(Task task) {
                showTaskDialogFragment(Consts.UPDATE_ACTION, task.getId());
            }

            @Override
            public boolean onLongClick(Task task) {
                TaskHelper.deleteTask(task, TaskActivity.this, taskDao);
                mAdapter.refreshData(getTasks());
                return true;
            }

            @Override
            public void onPriorityChangeClick(Task task) {
                TaskHelper.updatePriorityOnClick(task,taskDao);
                mAdapter.refreshData(getTasks());
            }

            @Override
            public void onStatusSwitchChange(Task task) {
                TaskHelper.updateStatusOnSwitchChange(task,taskDao);
            }

        };
    }

    //Setup priority spinner to show strings from R.array.priority_sort_type_list
    public void setupPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prirority_sort_type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortType.setAdapter(adapter);
        mSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSortPriorityType = mSortType.getSelectedItemPosition();
                mAdapter.refreshData(getTasks());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Show task dialog
    private void showTaskDialogFragment(int action, String taskId) {
        DialogFragment createTaskDialogFragment = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            args.putString("taskId", taskId);
        }
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    //Toolbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_intent_to_categories:
                startActivity(new Intent(this, CategoryActivity.class));
                break;
            case R.id.action_settings:
                return true;
            case R.id.toolbar_task_status:
                switch (item.getTitle().toString()) {
                    case "All tasks":
                        item.setTitle(R.string.done);
                        mFilterStatusType = 1;
                        mAdapter.refreshData(getTasks());
                        break;
                    case "Done":
                        item.setTitle(R.string.not_done);
                        mFilterStatusType = 2;
                        mAdapter.refreshData(getTasks());
                        break;
                    case "Not done":
                        item.setTitle(R.string.all_tasks);
                        mFilterStatusType = 0;
                        mAdapter.refreshData(getTasks());
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Called when TaskDialogFragment is close with positive button
    @Override
    public void onTaskChange(int action) {
        mAdapter.refreshData(getTasks());
    }

    public List<Task> getTasks(){
        return TaskHelper.getTasksFromDb(mFilterStatusType,mSortPriorityType,taskDao);
    }

}
