package ada.osc.taskie.ui.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.Category;

public class CategoryHelper {

    public static List<Category> getCategories(Dao<Category,String> categoryDao) {
        QueryBuilder<Category, String> queryBuilder = categoryDao.queryBuilder();
        try {
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // delete category from DB
    public static void deleteCategory(final Category category, Context context, final Dao<Category,String> categoryDao, final CategoryAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete '" + category + "' category");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteBuilder<Category, String> deleteBuilder = categoryDao.deleteBuilder();
                try {
                    deleteBuilder.where().eq(Category.CATEGORY_ID, category.getId());
                    deleteBuilder.delete();
                    adapter.refreshData(getCategories(categoryDao));
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

}
