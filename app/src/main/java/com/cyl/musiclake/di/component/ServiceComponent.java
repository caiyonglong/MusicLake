package com.cyl.musiclake.di.component;

import android.content.Context;


import com.cyl.musiclake.di.module.ServiceModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerService;

import dagger.Component;


/**
 * Created by lw on 2017/1/19.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
