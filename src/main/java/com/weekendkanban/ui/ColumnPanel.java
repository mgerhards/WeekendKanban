package com.weekendkanban.ui;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.service.TaskService;
import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
i
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ColumnPanel extends Panel {

    @SpringBean
    private TaskService taskService;

    private final WebMarkupContainer tasksContainer;
    private String newTaskTitle;

    public ColumnPanel(String id, IModel<TaskStatus> statusModel) {
        super(id, statusModel);

        add(new Label("columnTitle", statusModel.map(TaskStatus::name)));

        // Container for AJAX refresh
        tasksContainer = new WebMarkupContainer("tasksContainer");
        tasksContainer.setOutputMarkupId(true);
        add(tasksContainer);

        var tasksModel = LoadableDetachableModel.of(() -> 
            taskService.findByStatus(statusModel.getObject())
        );

        tasksContainer.add(new ListView<>("tasks", tasksModel) {
            @Override
            protected void populateItem(ListItem<Task> item) {
                item.add(new TaskPanel("task", item.getModel(), ColumnPanel.this::refreshTasks));
            }
        });

        // Add task form
        Form<Void> addForm = new Form<>("addForm");
        addForm.setOutputMarkupId(true);
        add(addForm);

        TextField<String> titleField = new TextField<>("newTaskTitle", new PropertyModel<>(this, "newTaskTitle"));
        titleField.setOutputMarkupId(true);
        addForm.add(titleField);

        addForm.add(new AjaxSubmitLink("addTask") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (newTaskTitle != null && !newTaskTitle.trim().isEmpty()) {
                    Task task = new Task(newTaskTitle.trim());
                    task.setStatus(statusModel.getObject());
                    taskService.save(task);
                    newTaskTitle = null;
                    target.add(tasksContainer, addForm);
                }
            }
        });
    }

    public void refreshTasks(AjaxRequestTarget target) {
        // Force the model to reload from database
        tasksContainer.modelChanged();
        target.add(tasksContainer);
    }
}