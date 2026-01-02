package com.weekendkanban.ui;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.service.TaskService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ColumnPanel extends Panel {

    @SpringBean
    private TaskService taskService;

    public ColumnPanel(String id, IModel<TaskStatus> statusModel) {
        super(id, statusModel);

        add(new Label("columnTitle", statusModel.map(TaskStatus::name)));

        var tasksModel = LoadableDetachableModel.of(() -> 
            taskService.findByStatus(statusModel.getObject())
        );

        add(new ListView<>("tasks", tasksModel) {
            @Override
            protected void populateItem(ListItem<Task> item) {
                item.add(new TaskPanel("task", item.getModel()));
            }
        });
    }
}