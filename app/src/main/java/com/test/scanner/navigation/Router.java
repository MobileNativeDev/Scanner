package com.test.scanner.navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

public abstract class Router {

    public abstract void attach(FragmentManager fragmentManager, @IdRes int containerId);
    public abstract void execute(Command command);

    public void execute(Command... commands) {
        for (Command command : commands) {
            execute(command);
        }
    }

}
