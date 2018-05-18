package ada.osc.taskie.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskCategory;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static final Class<?>[] classes = new Class[]{Task.class, Category.class, TaskCategory.class};

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt", classes);
    }
}
