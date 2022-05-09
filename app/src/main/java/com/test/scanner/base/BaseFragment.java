package com.test.scanner.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class BaseFragment<VM extends ViewModel, VB extends ViewBinding> extends Fragment {

    private VB binding;
    private VM viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        try {
            binding = createBinding(inflater, container);
            return binding.getRoot();
        } catch (ClassCastException | NullPointerException | ReflectiveOperationException e) {
            e.printStackTrace();
            return new View(getContext());
        }
    }

    @SuppressWarnings("unchecked")
    private VB createBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) throws ClassCastException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            NullPointerException {
        Class<VB> bindingClass = getGenericSuperclass(1);
        Method inflateMethod = bindingClass.getDeclaredMethod(
                "inflate",
                LayoutInflater.class,
                ViewGroup.class,
                boolean.class
        );
        Object bindingInstance = inflateMethod.invoke(null, inflater, container, false);
        String actualClassName = Objects.requireNonNull(bindingInstance).getClass()
                .getCanonicalName();
        if (Objects.equals(actualClassName, bindingClass.getCanonicalName())) {
            return (VB) bindingInstance;
        } else {
            throw new ClassCastException("Unable to cast " + actualClassName + " to "
                    + bindingClass.getCanonicalName());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getGenericSuperclass(int position) {
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalStateException("Wrong generic superclass argument");
        }
        type = ((ParameterizedType) type).getActualTypeArguments()[position];
        try {
            return (Class<T>) type;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Generic argument at " + position + " is " + type);
        }
    }

    protected VM getViewModel() {
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        return viewModel;
    }

    private VM createViewModel() {
        Class<VM> viewModelClass = getGenericSuperclass(0);
        return new ViewModelProvider(this).get(viewModelClass);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    protected VB getBinding() {
        return binding;
    }

}
