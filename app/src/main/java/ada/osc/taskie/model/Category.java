package ada.osc.taskie.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = Category.CATEGORY_TABLE)
public class Category {

    public static final String CATEGORY_TABLE = "categories";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_COLOR = "color";

    @DatabaseField(id = true, columnName = CATEGORY_ID)
    private String mId;
    @DatabaseField(columnName = CATEGORY_NAME)
    private String mName;
    @DatabaseField(columnName = CATEGORY_COLOR)
    private String mColor;

    public Category() {
        mId = UUID.randomUUID().toString();
    }

    public Category(String mName,String mColor) {
        mId = UUID.randomUUID().toString();
        this.mName = mName;
        this.mColor = mColor;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
    }

    @Override
    public String toString() {
        return getName();
    }
}
