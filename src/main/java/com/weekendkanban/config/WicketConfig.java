package com.weekendkanban.config;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class WicketConfig implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        // Disable asynchronous page storage for simpler development
        webApplication.getStoreSettings().setAsynchronous(false);
        
        // Optional: Limit memory per session (in bytes)
        webApplication.getStoreSettings()
            .setMaxSizePerSession(org.apache.wicket.util.lang.Bytes.megabytes(10));
    }
}
