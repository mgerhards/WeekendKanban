package com.weekendkanban.ui;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.service.TaskService;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public class TaskPanel extends Panel {

    @SpringBean
    private TaskService taskService;

    private final SerializableConsumer<AjaxRequestTarget> refreshCallback;

    public TaskPanel(String id, IModel<Task> taskModel, SerializableConsumer<AjaxRequestTarget> refreshCallback) {
        super(id, taskModel);
        this.refreshCallback = refreshCallback;

        WebMarkupContainer viewPanel = createView(taskModel, refreshCallback);

        WebMarkupContainer container = new WebMarkupContainer("editContainer");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisible(false);

        viewPanel.add(new AjaxEventBehavior("click") {
            @Override
            public void onEvent(AjaxRequestTarget target) {
                get("viewContainer").setVisible(false);
                get("editContainer").setVisible(true);
                target.add(get("viewContainer"));
                target.add(get("editContainer"));
            }
        });


        var editForm = new Form<Task>("editForm", taskModel);

        editForm.add(new AjaxButton("saveChanges", editForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Task task = taskModel.getObject();
                taskService.save(task);
                container.setVisible(false);
                viewPanel.setVisible(true);
                target.add(container);
                target.add(viewPanel);
                refreshCallback.accept(target);
            }
        });

        editForm.add(new TextField("editTitle", new PropertyModel(taskModel, "title")));
        editForm.add(new TextArea("editDescription", new PropertyModel(taskModel, "description")));
        editForm.add(new TextField("editAssignee", new PropertyModel(taskModel, "assignee")));

        editForm.add(new AjaxLink<Void>("cancelEdit") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.setVisible(false);
                viewPanel.setVisible(true);
                target.add(container);
                target.add(viewPanel);
            }
        });

        add(container);
        container.add(editForm);
        add(viewPanel);
    }

    private WebMarkupContainer createView(IModel<Task> taskModel, SerializableConsumer<AjaxRequestTarget> refreshCallback) {
        WebMarkupContainer container = new WebMarkupContainer("viewContainer");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);

        container.add(new Label("title", taskModel.map(Task::getTitle)));
        container.add(new Label("description", taskModel.map(Task::getDescription)));
        container.add(new Label("assignee", taskModel.map(Task::getAssignee)));

        // Move left button
        container.add(new AjaxLink<Void>("moveLeft") {
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
        container.add(new AjaxLink<Void>("moveRight") {
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
        container.add(new AjaxLink<Void>("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                taskService.delete(taskModel.getObject().getId());
                refreshCallback.accept(target);
            }
        });


        return container;
    }

    private TaskStatus getNextStatus(TaskStatus current) {
        return switch (current) {
            case TODO -> TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> TaskStatus.IN_REVIEW;
            case IN_REVIEW-> TaskStatus.DONE;
            case DONE -> null;
        };
    }

    private TaskStatus getPreviousStatus(TaskStatus current) {
        return switch (current) {
            case TODO -> null;
            case IN_PROGRESS -> TaskStatus.TODO;
            case IN_REVIEW -> TaskStatus.IN_PROGRESS;
            case DONE -> TaskStatus.IN_REVIEW;
        };
    }

    private void refreshAllColumns(AjaxRequestTarget target) {
        // Find the HomePage and refresh all column panels
        getPage().visitChildren(ColumnPanel.class, (component, visit) -> {
            ((ColumnPanel) component).refreshTasks(target);
        });
    }
}