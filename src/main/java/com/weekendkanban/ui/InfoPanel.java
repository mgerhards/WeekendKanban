package com.weekendkanban.ui;

import com.weekendkanban.service.TaskService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class InfoPanel extends Panel {

    @SpringBean
    private TaskService taskService;

    public InfoPanel(String id) {
        super(id);

        add(new Label("count_open",
                LoadableDetachableModel.of(() -> taskService.countOpenTasks())));

        add(new Label("count_closed",
                LoadableDetachableModel.of(() -> taskService.countClosedTasks())));

        add(new Label("count_all",
                LoadableDetachableModel.of(() -> taskService.countAllTasks())));
    }
}
