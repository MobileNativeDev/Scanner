package com.test.scanner.navigation;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

public abstract class Command {

    protected static final String BUNDLE_RESULT_KEY = "key_result";

    public abstract void accept(FragmentManager fragmentManager, @IdRes int containerId);

    public static class Navigate extends Command {

        private final Screen screen;

        public Navigate(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void accept(FragmentManager fragmentManager, int containerId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = screen.getFragment();
            fragment.setArguments(screen.getArguments());
            switch (screen.getTransactionMode()) {
                case ADD:
                    transaction.add(containerId, fragment, screen.getTag());
                    break;
                case REPLACE:
                    transaction.replace(containerId, fragment, screen.getTag());
                    break;
            }
            if (screen.isAddToBackStack()) {
                transaction.addToBackStack(screen.getTag());
            }
            transaction.commit();
        }
    }

    public static class Back extends Command {

        private static Back INSTANCE;

        private Back() {
        }

        public static Back getInstance() {
            if (INSTANCE == null) {
                synchronized (Back.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new Back();
                    }
                }
            }
            return INSTANCE;
        }

        @Override
        public void accept(FragmentManager fragmentManager, int containerId) {
            fragmentManager.popBackStack();
        }

    }

    public static class RegisterResultListener<T extends Parcelable> extends Command {

        private final String requestKey;
        private final ResultListener<T> resultListener;

        public RegisterResultListener(
                String requestKey,
                ResultListener<T> resultListener
        ) {
            this.requestKey = requestKey;
            this.resultListener = resultListener;
        }


        @Override
        public void accept(FragmentManager fragmentManager, int containerId) {
            Fragment fragment = fragmentManager.findFragmentById(containerId);
            if (fragment != null) {
                FragmentResultListener listener = (requestKey, result) -> {
                    T data = result.getParcelable(BUNDLE_RESULT_KEY);
                    resultListener.handle(data);
                };
                fragmentManager.setFragmentResultListener(
                        requestKey,
                        fragment,
                        listener
                );
            }
        }
    }

    public static class SetResult<T extends Parcelable> extends Command {

        private final String requestKey;
        private final T result;

        public SetResult(String requestKey, T result) {
            this.requestKey = requestKey;
            this.result = result;
        }

        @Override
        public void accept(FragmentManager fragmentManager, int containerId) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_RESULT_KEY, result);
            fragmentManager.setFragmentResult(requestKey, bundle);
        }

    }

    public interface ResultListener<T extends Parcelable> {

        void handle(T result);

    }

}
