package com.weekendkanban.ui;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.service.TaskService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class TaskPanel extends Panel {

    @SpringBean
    private TaskService taskService;

    private final SerializableConsumer<AjaxRequestTarget> refreshCallback;

    public TaskPanel(String id, IModel<Task> taskModel, SerializableConsumer<AjaxRequestTarget> refreshCallback) {
        super(id, taskModel);
        this.refreshCallback = refreshCallback;

        add(new Label("title", taskModel.map(Task::getTitle)));
        add(new Label("description", taskModel.map(Task::getDescription)));

        // Move left button
        add(new AjaxLink<Void>("moveLeft") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Task task = taskModel.getObject();
                TaskStatus prev = getPreviousStatus(task.getStatus());
                if (prev != null) {
                    task.setStatus(prev);
                    taskService.save(task);
                    refreshAllColumns(target);
                }
            }

            @Override
            public boolean isVisible() {
                return taskModel.getObject().getStatus() != TaskStatus.TODO;
            }
        });

        // Move right button
        add(new AjaxLink<Void>("moveRight") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Task task = taskModel.getObject();
                TaskStatus next = getNextStatus(task.getStatus());
                if (next != null) {
                    task.setStatus(next);
                    taskService.save(task);
                    refreshAllColumns(target);
                }
            }

            @Override
            public boolean isVisible() {
                return taskModel.getObject().getStatus() != TaskStatus.DONE;
            }
        });

        // Delete button
        add(new AjaxLink<Void>("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                taskService.delete(taskModel.getObject().getId());
                refreshCallback.accept(target);
            }
        });
    }

    private TaskStatus getNextStatus(TaskStatus current) {
        return switch (current) {
            case TODO -> TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> TaskStatus.DONE;
            case DONE -> null;
        };
    }

    private TaskStatus getPreviousStatus(TaskStatus current) {
        return switch (current) {
            case TODO -> null;
            case IN_PROGRESS -> TaskStatus.TODO;
            case DONE -> TaskStatus.IN_PROGRESS;
        };
    }

    private void refreshAllColumns(AjaxRequestTarget target) {
        // Find the HomePage and refresh all column panels
        getPage().visitChildren(ColumnPanel.class, (component, visit) -> {
            ((ColumnPanel) component).refreshTasks(target);
        });
    }
}