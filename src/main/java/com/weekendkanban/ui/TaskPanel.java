package com.weekendkanban.ui;

import com.weekendkanban.domain.Task;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

public class TaskPanel extends Panel {

    public TaskPanel(String id, IModel<Task> taskModel) {
        super(id, taskModel);

        add(new Label("title", taskModel.map(Task::getTitle)));
        add(new Label("description", taskModel.map(Task::getDescription)));
    }
}