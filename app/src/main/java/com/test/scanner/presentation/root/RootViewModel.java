package com.test.scanner.presentation.root;

import androidx.lifecycle.ViewModel;

import com.test.scanner.navigation.Command;
import com.test.scanner.navigation.Router;
import com.test.scanner.presentation.main.MainScreen;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RootViewModel extends ViewModel {

    private final Router router;

    @Inject
    public RootViewModel(Router router) {
        this.router = router;
    }

    public void openMainScreen() {
        router.execute(new Command.Navigate(new MainScreen()));
    }

}
