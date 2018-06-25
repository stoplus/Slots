package com.example.slots.component;

import com.example.slots.ui.MainActivity;
import com.example.slots.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface DataBaseComponent {
    void inject(MainActivity mainActivity);
}