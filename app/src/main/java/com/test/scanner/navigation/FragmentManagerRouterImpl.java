package com.test.scanner.navigation;

import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FragmentManagerRouterImpl extends Router {

    private FragmentManager fragmentManager;
    private int containerId;

    @Inject
    public FragmentManagerRouterImpl() {
    }

    @Override
    public void attach(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    @Override
    public void execute(Command command) {
        command.accept(fragmentManager, containerId);
    }

}
