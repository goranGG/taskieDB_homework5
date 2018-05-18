package ada.osc.taskie.ui.category;

import ada.osc.taskie.model.Category;

public interface CategoryClickListener {
    void onClick(Category category);
    boolean onLongClick(Category category);
}
