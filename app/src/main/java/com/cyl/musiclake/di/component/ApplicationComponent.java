package com.cyl.musiclake.di.component;

import android.content.Context;

import com.cyl.musiclake.di.module.ApplicationModule;
import com.cyl.musiclake.di.scope.ContextLife;
import com.cyl.musiclake.di.scope.PerApp;
import dagger.Component;


/**
 * Created by lw on 2017/1/19.
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();
}