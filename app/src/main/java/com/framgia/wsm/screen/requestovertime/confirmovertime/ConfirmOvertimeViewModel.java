package com.framgia.wsm.screen.requestovertime.confirmovertime;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.framgia.wsm.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.Branch;
import com.framgia.wsm.data.model.RequestOverTime;
import com.framgia.wsm.data.model.Shifts;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.requestovertime.RequestOvertimeActivity;
import com.framgia.wsm.utils.ActionType;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.RequestType;
import com.framgia.wsm.utils.StatusCode;
import com.framgia.wsm.utils.TypeToast;
import com.framgia.wsm.utils.common.DateTimeUtils;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.fstyle.library.DialogAction;
import com.fstyle.library.MaterialDialog;
import java.util.Locale;

import static com.framgia.wsm.utils.common.DateTimeUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM;
import static com.framgia.wsm.utils.common.DateTimeUtils.INPUT_TIME_FORMAT;

/**
 * Exposes the data to be used in the ConfirmOvertime screen.
 */

public class ConfirmOvertimeViewModel extends BaseObservable
        implements ConfirmOvertimeContract.ViewModel {

    private static final String TAG = ConfirmOvertimeViewModel.class.getSimpleName();

    private Context mContext;
    private ConfirmOvertimeContract.Presenter mPresenter;
    private RequestOverTime mRequestOverTime;
    private Navigator mNavigator;
    private DialogManager mDialogManager;
    private User mUser;
    private int mActionType;

    ConfirmOvertimeViewModel(Context context, ConfirmOvertimeContract.Presenter presenter,
            RequestOverTime requestOverTime, Navigator navigator, DialogManager dialogManager) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mRequestOverTime = requestOverTime;
        mNavigator = navigator;
        mDialogManager = dialogManager;
        mPresenter.getUser();
    }

    private void setTimeRequestOverTIme(RequestOverTime requestOverTime) {
        requestOverTime.setFromTime(
                DateTimeUtils.convertUiFormatToDataFormat(requestOverTime.getFromTime(),
                        INPUT_TIME_FORMAT, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM));
        requestOverTime.setToTime(
                DateTimeUtils.convertUiFormatToDataFormat(requestOverTime.getToTime(),
                        INPUT_TIME_FORMAT, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM));
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void onGetUserSuccess(User user) {
        if (user == null) {
            return;
        }
        mUser = user;
        notifyPropertyChanged(BR.user);
        notifyPropertyChanged(BR.numberHour);
    }

    @Override
    public void onGetUserError(BaseException e) {
        Log.e(TAG, "ConfirmOvertimeViewModel", e);
    }

    @Override
    public void onDismissProgressDialog() {
        mDialogManager.dismissProgressDialog();
    }

    @Override
    public void onShowIndeterminateProgressDialog() {
        mDialogManager.showIndeterminateProgressDialog();
    }

    @Override
    public void onCreateFormOverTimeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_REQUEST_TYPE_CODE, RequestType.REQUEST_OVERTIME);
        mNavigator.finishActivityWithResult(bundle, Activity.RESULT_OK);
        mNavigator.showToastCustom(TypeToast.SUCCESS,
                mContext.getString(R.string.create_form_success));
    }

    @Override
    public void onCreateFormOverTimeError(BaseException exception) {
        mDialogManager.dialogError(exception, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog,
                    @NonNull DialogAction dialogAction) {
                mNavigator.finishActivity();
            }
        });
    }

    @Override
    public void onEditFormOverTimeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_REQUEST_TYPE_CODE, RequestType.REQUEST_OVERTIME);
        mNavigator.finishActivityWithResult(bundle, Activity.RESULT_OK);
        mNavigator.showToastCustom(TypeToast.SUCCESS,
                mContext.getString(R.string.edit_form_success));
    }

    @Override
    public void onEditFormOverTimeError(BaseException exception) {
        mDialogManager.dialogError(exception, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog,
                    @NonNull DialogAction dialogAction) {
                mNavigator.finishActivity();
            }
        });
    }

    @Override
    public void onDeleteFormOverTimeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_REQUEST_TYPE_CODE, RequestType.REQUEST_OVERTIME);
        mNavigator.finishActivityWithResult(bundle, Activity.RESULT_OK);
        mNavigator.showToastCustom(TypeToast.SUCCESS,
                mContext.getString(R.string.delete_form_success));
    }

    @Override
    public void onDeleteFormOverTimeError(BaseException exception) {
        mDialogManager.dialogError(exception);
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    @Bindable
    public String getNumberHour() {
        if (mUser == null) {
            return "";
        }
        Shifts shifts = new Shifts();
        for (Branch branch : mUser.getBranches()) {
            if (mRequestOverTime.getBranch().getBranchName().equals(branch.getBranchName())) {
                shifts = branch.getShifts().get(0);
            }
        }
        float numberHourOvertime = DateTimeUtils.getHourBetweenTwoDate(mRequestOverTime.getToTime(),
                mRequestOverTime.getFromTime(), DateTimeUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM);
        if (isContainLunchBreak(shifts)) {
            return String.format(Locale.getDefault(), DateTimeUtils.HOUR_FORMAT_2F,
                    numberHourOvertime - shifts.getNumberHourLunch());
        }
        return String.format(Locale.getDefault(), DateTimeUtils.HOUR_FORMAT_2F, numberHourOvertime);
    }

    private boolean isContainLunchBreak(Shifts shifts) {
        shifts.setTimeLunch(DateTimeUtils.convertUiFormatToDataFormat(shifts.getTimeLunch(),
                DateTimeUtils.INPUT_TIME_FORMAT, DateTimeUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM));
        shifts.setTimeAfternoon(DateTimeUtils.convertUiFormatToDataFormat(shifts.getTimeAfternoon(),
                DateTimeUtils.INPUT_TIME_FORMAT, DateTimeUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM));
        int hourOfTimeAfternoon = DateTimeUtils.getHourOfDay(shifts.getTimeAfternoon());
        int minuteOfTimeAfternoon = DateTimeUtils.getMinute(shifts.getTimeAfternoon());
        int hourOfTimeLunch = DateTimeUtils.getHourOfDay(shifts.getTimeLunch());
        int minuteOfTimeLunch = DateTimeUtils.getMinute(shifts.getTimeLunch());
        return DateTimeUtils.checkHourOfDateLessThanOrEqual(mRequestOverTime.getFromTime(),
                hourOfTimeLunch, minuteOfTimeLunch)
                && !DateTimeUtils.checkHourOfDateLessThan(mRequestOverTime.getToTime(),
                hourOfTimeAfternoon, minuteOfTimeAfternoon)
                && DateTimeUtils.getHourBetweenTwoDate(mRequestOverTime.getToTime(),
                mRequestOverTime.getFromTime(), DateTimeUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM)
                > Constant.TimeConst.FOUR_HOUR;
    }

    public RequestOverTime getRequestOverTime() {
        return mRequestOverTime;
    }

    public String getTitleToolbar() {
        if (mActionType == ActionType.ACTION_CREATE) {
            return mContext.getString(R.string.confirm_overtime_request);
        }
        if (mActionType == ActionType.ACTION_DETAIL) {
            return mContext.getString(R.string.detail_overtime_request);
        }
        return mContext.getString(R.string.confirm_edit_overtime_request);
    }

    public boolean isDetail() {
        return mActionType == ActionType.ACTION_DETAIL;
    }

    public boolean isAcceptStatus() {
        return StatusCode.ACCEPT_CODE.equals(mRequestOverTime.getStatus());
    }

    public boolean isPendingStatus() {
        return StatusCode.PENDING_CODE.equals(mRequestOverTime.getStatus());
    }

    public boolean isRejectStatus() {
        return StatusCode.REJECT_CODE.equals(mRequestOverTime.getStatus());
    }

    public boolean isForwardedStatus() {
        return StatusCode.FORWARD_CODE.equals(mRequestOverTime.getStatus());
    }

    public boolean isCancelStatus() {
        return StatusCode.CANCELED_CODE.equals(mRequestOverTime.getStatus());
    }

    public void setActionType(int actionType) {
        mActionType = actionType;
        if (ActionType.ACTION_DETAIL == mActionType) {
            setTimeRequestOverTIme(mRequestOverTime);
        }
    }

    public boolean isVisibleButtonSubmit() {
        return StatusCode.PENDING_CODE.equals(mRequestOverTime.getStatus())
                || mActionType == ActionType.ACTION_CREATE;
    }

    public void onClickArrowBack(View view) {
        mNavigator.finishActivity();
    }

    public void onClickSubmit(View view) {
        if (mRequestOverTime == null) {
            return;
        }
        if (mActionType == ActionType.ACTION_CREATE) {
            mPresenter.createFormRequestOverTime(mRequestOverTime);
            return;
        }
        mPresenter.editFormRequestOvertime(mRequestOverTime);
    }

    public void onClickDelete(View view) {
        if (mRequestOverTime == null) {
            return;
        }
        mDialogManager.dialogBasic(mContext.getString(R.string.confirm_delete),
                mContext.getString(R.string.do_you_want_delete_this_request),
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                            @NonNull DialogAction dialogAction) {
                        mPresenter.deleteFormRequestOvertime(mRequestOverTime.getId());
                    }
                });
    }

    public void onClickEdit(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_ACTION_TYPE, ActionType.ACTION_EDIT);
        bundle.putParcelable(Constant.EXTRA_REQUEST_OVERTIME, mRequestOverTime);
        mNavigator.startActivityForResult(RequestOvertimeActivity.class, bundle,
                Constant.RequestCode.REQUEST_OVERTIME);
    }
}
