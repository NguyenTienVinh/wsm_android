package com.framgia.wsm.screen.changepassword;

import android.content.Context;
import android.util.Log;
import com.framgia.wsm.R;
import com.framgia.wsm.utils.common.StringUtils;
import com.framgia.wsm.utils.rx.BaseSchedulerProvider;
import com.framgia.wsm.utils.validator.Validator;

/**
 * Listens to user actions from the UI ({@link ChangePasswordActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ChangePasswordPresenter implements ChangePasswordContract.Presenter {
    private static final String TAG = ChangePasswordPresenter.class.getName();

    private Context mContext;
    private ChangePasswordContract.ViewModel mViewModel;
    private Validator mValidator;
    private BaseSchedulerProvider mBaseSchedulerProvider;

    ChangePasswordPresenter(Context context, Validator validator,
            BaseSchedulerProvider baseSchedulerProvider) {
        mContext = context;
        mValidator = validator;
        mBaseSchedulerProvider = baseSchedulerProvider;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void setViewModel(ChangePasswordContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        // TODO: change password
    }

    @Override
    public boolean validateDataInput(String currentPassword, String newPassword,
            String confirmPassword) {
        boolean isValid;
        validateCurrentPasswordInput(currentPassword);
        validateNewPasswordInput(newPassword);
        try {
            isValid =
                    mValidator.validateAll(mViewModel) && validateConfirmPasswordInput(newPassword,
                            confirmPassword);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException: ", e);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void validateCurrentPasswordInput(String currentPassword) {
        String errorMessage = mValidator.validateValueNonEmpty(currentPassword);
        if (!StringUtils.isBlank(errorMessage)) {
            mViewModel.onInputCurrentPasswordError(errorMessage);
        } else {
            mViewModel.onInputCurrentPasswordError("");
        }
    }

    @Override
    public void validateNewPasswordInput(String newPassword) {
        String errorMessage = mValidator.validateValueNonEmpty(newPassword);
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = mValidator.validateValueRangeMin6(newPassword);
            if (!StringUtils.isBlank(errorMessage)) {
                mViewModel.onInputNewPasswordError(errorMessage);
            } else {
                mViewModel.onInputNewPasswordError("");
            }
        } else {
            mViewModel.onInputNewPasswordError(errorMessage);
        }
    }

    @Override
    public boolean validateConfirmPasswordInput(String newPassword, String confirmPassword) {
        boolean isValid;
        if (!newPassword.equals(confirmPassword)) {
            mViewModel.onInputConfirmPasswordError(
                    mContext.getString(R.string.confirm_password_does_not_match));
            isValid = false;
        } else {
            mViewModel.onInputConfirmPasswordError(mContext.getString(R.string.none));
            isValid = true;
        }
        return isValid;
    }
}
