package com.weekendkanban.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.ui.ColumnPanel;
import org.apache.wicket.model.Model;

@WicketHomePage
public class HomePage extends BasePage {

    public HomePage() {    
        add(new ColumnPanel("todoColumn", Model.of(TaskStatus.TODO)));
        add(new ColumnPanel("doingColumn", Model.of(TaskStatus.IN_PROGRESS)));
        add(new ColumnPanel("doneColumn", Model.of(TaskStatus.DONE)));
    }
}