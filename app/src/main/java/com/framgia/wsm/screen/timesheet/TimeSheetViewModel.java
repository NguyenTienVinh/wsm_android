package com.framgia.wsm.screen.timesheet;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.wsm.data.model.TimeSheetDate;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.model.UserTimeSheet;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.requestleave.RequestLeaveActivity;
import com.framgia.wsm.screen.requestoff.RequestOffActivity;
import com.framgia.wsm.screen.requestovertime.RequestOvertimeActivity;
import com.framgia.wsm.utils.ActionType;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.framgia.wsm.utils.Constant.TimeConst.DAY_25_OF_MONTH;
import static com.framgia.wsm.utils.Constant.TimeConst.ONE_MONTH;

/**
 * Exposes the data to be used in the TimeSheet screen.
 */

public class TimeSheetViewModel extends BaseObservable implements TimeSheetContract.ViewModel {
    private static final String TAG = "TimeSheetViewModel";

    private Context mContext;
    private TimeSheetContract.Presenter mPresenter;
    private List<TimeSheetDate> mTimeSheetDates;
    private DialogManager mDialogManager;
    private int mMonth;
    private int mYear;
    private boolean isShowInformation;
    private TimeSheetDate mTimeSheetDate;
    private UserTimeSheet mUserTimeSheet;
    private Navigator mNavigator;
    private boolean isVisibleFloatingActionMenu;
    private boolean mIsLoading;
    private boolean mIsRefreshEnable;
    private boolean mIsEmployeeFullTime;
    private boolean mIsShowOvertime;
    private int mCutOffDate;

    public TimeSheetViewModel(Context context, TimeSheetContract.Presenter presenter,
            Navigator navigator, DialogManager dialogManager) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mTimeSheetDates = new ArrayList<>();
        mTimeSheetDate = new TimeSheetDate();
        mUserTimeSheet = new UserTimeSheet();
        mPresenter.getUser();
        mDialogManager = dialogManager;
        mNavigator = navigator;
        mDialogManager.showIndeterminateProgressDialog();
        setMonth();
        setYear();
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
    public void onGetTimeSheetError(BaseException throwable) {
        //todo show error message
        mDialogManager.dismissProgressDialog();
        setLoading(false);
    }

    @Override
    public void onGetTimeSheetSuccess(UserTimeSheet userTimeSheet) {
        setTimeSheetDates(userTimeSheet.getTimeSheetDates());
        setUserTimeSheet(userTimeSheet);
        mDialogManager.dismissProgressDialog();
        setLoading(false);
    }

    @Override
    public void onGetUserSuccess(User user) {
        if (user == null || user.getCompany() == null) {
            return;
        }
        setCutOffDate(user.getCompany().getCutOffDate());
        mIsEmployeeFullTime = user.isFullTime();
        notifyPropertyChanged(BR.employeeFullTime);
    }

    @Override
    public void onGetUserError(BaseException error) {
        Log.e(TAG, "onGetUserError: ", error);
    }

    @Override
    public void onReloadData() {
        mPresenter.getTimeSheet(mMonth + ONE_MONTH, mYear);
    }

    @Bindable
    public TimeSheetDate getTimeSheetDate() {
        return mTimeSheetDate;
    }

    private void setTimeSheetDate(TimeSheetDate timeSheetDate) {
        mTimeSheetDate = timeSheetDate;
        notifyPropertyChanged(BR.timeSheetDate);
        if (timeSheetDate.getNumberOfOvertime() > 0) {
            setShowOvertime(true);
        } else {
            setShowOvertime(false);
        }
    }

    @Bindable
    public List<TimeSheetDate> getTimeSheetDates() {
        return mTimeSheetDates;
    }

    private void setTimeSheetDates(List<TimeSheetDate> timeSheetDates) {
        if (timeSheetDates == null) {
            return;
        }
        mTimeSheetDates.clear();
        mTimeSheetDates.addAll(timeSheetDates);
        notifyPropertyChanged(BR.timeSheetDates);
    }

    @Bindable
    public int getMonth() {
        return mMonth;
    }

    private void setMonth() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) > DAY_25_OF_MONTH) {
            mMonth = calendar.get(Calendar.MONTH) + 1;
            return;
        }
        mMonth = calendar.get(Calendar.MONTH);
    }

    @Bindable
    public int getYear() {
        return mYear;
    }

    private void setYear() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
    }

    @Bindable
    public String getTotalDayOff() {
        return String.valueOf(mUserTimeSheet.getTotalDayOff());
    }

    @Bindable
    public String getTotalFining() {
        return String.valueOf(mUserTimeSheet.getTotalFining());
    }

    @Bindable
    public String getNumberDayOffHaveSalary() {
        return String.valueOf(mUserTimeSheet.getNumberDayOffHaveSalary());
    }

    @Bindable
    public String getNumberDayOffNoSalary() {
        return String.valueOf(mUserTimeSheet.getNumberDayOffNoSalary());
    }

    @Bindable
    public String getNumberOverTime() {
        return String.valueOf(mUserTimeSheet.getNumberOverTime());
    }

    private void setUserTimeSheet(UserTimeSheet userTimeSheet) {
        mUserTimeSheet = userTimeSheet;
        notifyPropertyChanged(BR.totalDayOff);
        notifyPropertyChanged(BR.totalFining);
        notifyPropertyChanged(BR.numberDayOffHaveSalary);
        notifyPropertyChanged(BR.numberDayOffNoSalary);
        notifyPropertyChanged(BR.numberOverTime);
    }

    @Bindable
    public boolean isShowInformation() {
        return isShowInformation;
    }

    public void setShowInformation(boolean showInformation) {
        isShowInformation = showInformation;
        notifyPropertyChanged(BR.showInformation);
    }

    @Bindable
    public boolean isVisibleFloatingActionMenu() {
        return isVisibleFloatingActionMenu;
    }

    public void setVisibleFloatingActionMenu(boolean visibleFloatingActionMenu) {
        isVisibleFloatingActionMenu = visibleFloatingActionMenu;
        notifyPropertyChanged(BR.visibleFloatingActionMenu);
    }

    @Bindable
    public boolean isShowOvertime() {
        return mIsShowOvertime;
    }

    public void setShowOvertime(boolean showOvertime) {
        mIsShowOvertime = showOvertime;
        notifyPropertyChanged(BR.showOvertime);
    }

    public void onDayClicked(TimeSheetDate timeSheetDate) {
        setShowInformation(true);
        setTimeSheetDate(timeSheetDate);
    }

    public void onClickRequestLeave(View view) {
        setVisibleFloatingActionMenu(false);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_ACTION_TYPE, ActionType.ACTION_CREATE);
        mNavigator.startActivityForResult(RequestLeaveActivity.class, bundle,
                Constant.RequestCode.REQUEST_LEAVE);
    }

    public void onClickRequestOvertime(View view) {
        setVisibleFloatingActionMenu(false);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_ACTION_TYPE, ActionType.ACTION_CREATE);
        mNavigator.startActivityForResult(RequestOvertimeActivity.class, bundle,
                Constant.RequestCode.REQUEST_OVERTIME);
    }

    public void onClickRequestOff(View view) {
        setVisibleFloatingActionMenu(false);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_ACTION_TYPE, ActionType.ACTION_CREATE);
        mNavigator.startActivityForResult(RequestOffActivity.class, bundle,
                Constant.RequestCode.REQUEST_OFF);
    }

    public void onNextMonth() {
        if (mMonth == 11) {
            mMonth = 0;
            mYear++;
        } else {
            mMonth++;
        }
        mDialogManager.showIndeterminateProgressDialog();
        notifyPropertyChanged(BR.month);
        notifyPropertyChanged(BR.year);
        mPresenter.getTimeSheet(mMonth + ONE_MONTH, mYear);
    }

    public void onPreviousMonth() {
        if (mMonth == 0) {
            mMonth = 11;
            mYear--;
        } else {
            mMonth--;
        }
        mDialogManager.showIndeterminateProgressDialog();
        notifyPropertyChanged(BR.month);
        notifyPropertyChanged(BR.year);
        mPresenter.getTimeSheet(mMonth + ONE_MONTH, mYear);
    }

    @Bindable
    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoading(true);
                mDialogManager.showIndeterminateProgressDialog();
                mPresenter.getTimeSheet(mMonth + ONE_MONTH, mYear);
            }
        };
    }

    public AppBarLayout.OnOffsetChangedListener getOnOffsetChangedListener() {
        return new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                setRefreshEnable(verticalOffset == 0);
            }
        };
    }

    @Bindable
    public boolean isRefreshEnable() {
        return mIsRefreshEnable;
    }

    private void setRefreshEnable(boolean refreshEnable) {
        mIsRefreshEnable = refreshEnable;
        notifyPropertyChanged(BR.refreshEnable);
    }

    @Bindable
    public boolean isEmployeeFullTime() {
        return mIsEmployeeFullTime;
    }

    @Bindable
    public int getCutOffDate() {
        return mCutOffDate;
    }

    private void setCutOffDate(int cutOffDate) {
        mCutOffDate = cutOffDate;
        notifyPropertyChanged(BR.cutOffDate);
    }
}
