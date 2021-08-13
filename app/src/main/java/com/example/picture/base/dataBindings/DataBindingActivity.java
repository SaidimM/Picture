package com.example.picture.base.dataBindings;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.baidu.aip.asrwakeup3.core.mini.ActivityMiniUnit;
import com.baidu.aip.asrwakeup3.core.mini.ActivityMiniWakeUp;
import com.example.picture.R;

public abstract class DataBindingActivity extends ActivityMiniWakeUp {
    private ViewDataBinding mBinding;
    private TextView mTvStrictModeTip;

    public DataBindingActivity() {
    }

    protected abstract void initViewModel();

    protected abstract DataBindingConfig getDataBindingConfig();

    protected ViewDataBinding getBinding() {
        if (this.isDebug() && this.mBinding != null && this.mTvStrictModeTip == null) {
            this.mTvStrictModeTip = new TextView(this.getApplicationContext());
            this.mTvStrictModeTip.setAlpha(0.4F);
            this.mTvStrictModeTip.setTextSize(14.0F);
            this.mTvStrictModeTip.setBackgroundColor(-1);
            this.mTvStrictModeTip.setText(R.string.debug_activity_databinding_warning);
            ((ViewGroup)this.mBinding.getRoot()).addView(this.mTvStrictModeTip);
        }

        return this.mBinding;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initViewModel();
        DataBindingConfig dataBindingConfig = this.getDataBindingConfig();
        ViewDataBinding binding = DataBindingUtil.setContentView(this, dataBindingConfig.getLayout());
        binding.setLifecycleOwner(this);
        binding.setVariable(dataBindingConfig.getVmVariableId(), dataBindingConfig.getStateViewModel());
        SparseArray bindingParams = dataBindingConfig.getBindingParams();
        int i = 0;

        for(int length = bindingParams.size(); i < length; ++i) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }

        this.mBinding = binding;
    }

    public boolean isDebug() {
        return this.getApplicationContext().getApplicationInfo() != null && (this.getApplicationContext().getApplicationInfo().flags & 2) != 0;
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mBinding.unbind();
        this.mBinding = null;
    }
}
