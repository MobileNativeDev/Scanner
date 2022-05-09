package com.test.scanner.presentation.root;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.test.scanner.R;
import com.test.scanner.databinding.ActivityMainBinding;
import com.test.scanner.navigation.Router;
import com.test.scanner.utils.android.Permissions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    public Router router;
    @Inject
    public Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RootViewModel viewModel = new ViewModelProvider(this).get(RootViewModel.class);
        router.attach(getSupportFragmentManager(), R.id.container_main);
        permissions.attach(this);
        viewModel.openMainScreen();
        System.out.println("router___ main: " + router.toString());
    }

}