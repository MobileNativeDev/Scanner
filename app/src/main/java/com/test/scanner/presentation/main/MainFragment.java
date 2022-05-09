package com.test.scanner.presentation.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.scanner.base.BaseFragment;
import com.test.scanner.databinding.FragmentMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends BaseFragment<MainViewModel, FragmentMainBinding> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().buttonScan.setOnClickListener(
                button -> getViewModel().openScanScreen());
        getBinding().buttonCopy.setOnClickListener(button -> getViewModel().copy());
        getViewModel().getResult().observe(
                getViewLifecycleOwner(),
                result -> getBinding().textMeterReading.setText(result)
        );
    }

}
