package com.weekendkanban.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.ui.ColumnPanel;
import org.apache.wicket.model.Model;

@WicketHomePage
public class HomePage extends WebPage {

    public HomePage() {    
        add(new ColumnPanel("todoColumn", Model.of(TaskStatus.TODO)));
        add(new ColumnPanel("inProgressColumn", Model.of(TaskStatus.IN_PROGRESS)));
        add(new ColumnPanel("doneColumn", Model.of(TaskStatus.DONE)));
    }
}