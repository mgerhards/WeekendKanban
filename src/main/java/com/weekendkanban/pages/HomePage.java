package com.weekendkanban.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.ui.ColumnPanel;
import com.weekendkanban.ui.InfoPanel;
import org.apache.wicket.model.Model;

@WicketHomePage
public class HomePage extends BasePage {

    public HomePage() {
        add(new InfoPanel("infoPanel"));

        add(new ColumnPanel("todoColumn",  Model.of(TaskStatus.TODO),        target -> target.add(get("infoPanel"))));
        add(new ColumnPanel("doingColumn", Model.of(TaskStatus.IN_PROGRESS), target -> target.add(get("infoPanel"))));
        add(new ColumnPanel("reviewColumn", Model.of(TaskStatus.IN_REVIEW), target -> target.add(get("infoPanel"))));
        add(new ColumnPanel("doneColumn",  Model.of(TaskStatus.DONE),        target -> target.add(get("infoPanel"))));
    }
}