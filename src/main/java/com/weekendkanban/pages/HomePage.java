package com.weekendkanban.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

@WicketHomePage
public class HomePage extends WebPage {

    public HomePage() {
        add(new Label("title", "WeekendKanban"));
    }
}