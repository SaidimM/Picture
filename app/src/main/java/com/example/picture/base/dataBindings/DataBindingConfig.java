package com.example.picture.base.dataBindings;

import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class DataBindingConfig {
    private final int layout;
    private final int vmVariableId;
    private final ViewModel stateViewModel;
    private SparseArray bindingParams = new SparseArray();

    public DataBindingConfig(@NonNull Integer layout, @NonNull Integer vmVariableId, @NonNull ViewModel stateViewModel) {
        this.layout = layout;
        this.vmVariableId = vmVariableId;
        this.stateViewModel = stateViewModel;
    }

    public int getLayout() {
        return this.layout;
    }

    public int getVmVariableId() {
        return this.vmVariableId;
    }

    public ViewModel getStateViewModel() {
        return this.stateViewModel;
    }

    public SparseArray getBindingParams() {
        return this.bindingParams;
    }

    public DataBindingConfig addBindingParam(@NonNull Integer variableId, @NonNull Object object) {
        if (this.bindingParams.get(variableId) == null) {
            this.bindingParams.put(variableId, object);
        }

        return this;
    }
}
