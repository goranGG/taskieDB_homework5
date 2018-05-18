package ada.osc.taskie.ui.task;

import ada.osc.taskie.model.Task;

public interface TaskClickListener {
    void onClick(Task task);
    boolean onLongClick(Task task);
    void onPriorityChangeClick(Task task);
    void onStatusSwitchChange(Task task);
}
