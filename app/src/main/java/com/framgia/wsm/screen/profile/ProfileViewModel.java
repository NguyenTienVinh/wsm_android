package com.framgia.wsm.screen.profile;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.framgia.wsm.BR;
import com.framgia.wsm.BuildConfig;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.changepassword.ChangePasswordActivity;
import com.framgia.wsm.screen.profile.branch.BranchAdapter;
import com.framgia.wsm.screen.profile.group.GroupAdapter;
import com.framgia.wsm.screen.updateprofile.UpdateProfileActivity;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.common.DateTimeUtils;
import com.framgia.wsm.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Profile screen.
 */

public class ProfileViewModel extends BaseObservable implements ProfileContract.ViewModel {
    private static final String TAG = "ProfileViewModel";

    private Context mContext;
    private ProfileContract.Presenter mPresenter;
    private Navigator mNavigator;
    private BranchAdapter mBranchAdapter;
    private GroupAdapter mGroupAdapter;
    private User mUser;
    private boolean mIsLoadDataFirstTime;

    private String mAvatar;
    private String mBirthday;
    private String mContractDate;
    private String mStartProbationDate;
    private String mEndProbationDate;

    ProfileViewModel(Context context, ProfileContract.Presenter presenter,
            Navigator navigator, BranchAdapter branchAdapter, GroupAdapter groupAdapter) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mNavigator = navigator;
        mBranchAdapter = branchAdapter;
        mGroupAdapter = groupAdapter;
        mIsLoadDataFirstTime = true;
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
        mBranchAdapter.updateDataBranch(mUser.getBranches());
        mGroupAdapter.updateDataGroup(mUser.getGroups());
        mIsLoadDataFirstTime = false;

        setAvatar(mUser.getAvatar());
        setBirthday(mUser.getBirthday());
        setContractDate(mUser.getContractDate());
        setEndProbationDate(mUser.getEndProbationDate());
        setStartProbationDate(mUser.getStartProbationDate());
    }

    @Override
    public void onGetUserError(BaseException exception) {
        Log.e(TAG, "onGetUserError", exception);
    }

    @Override
    public void reloadData() {
        if (mIsLoadDataFirstTime) {
            mPresenter.getUser();
        }
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = BuildConfig.BASE_URL + avatar;
        mUser.setAvatar(mAvatar);
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        mBirthday =
                DateTimeUtils.convertUiFormatToDataFormat(birthday, DateTimeUtils.INPUT_TIME_FORMAT,
                        mContext.getString(R.string.format_date_mm_dd_yyyy));
        mUser.setBirthday(mBirthday);
        notifyPropertyChanged(BR.birthday);
    }

    @Bindable
    public String getContractDate() {
        return mContractDate;
    }

    private void setContractDate(String contractDate) {
        mContractDate = formatDate(contractDate);
        mUser.setContractDate(mContractDate);
        notifyPropertyChanged(BR.contractDate);
    }

    @Bindable
    public String getStartProbationDate() {
        return mStartProbationDate;
    }

    private void setStartProbationDate(String startProbationDate) {
        mStartProbationDate = formatDate(startProbationDate);
        mUser.setStartProbationDate(mStartProbationDate);
        notifyPropertyChanged(BR.startProbationDate);
    }

    @Bindable
    public String getEndProbationDate() {
        return mEndProbationDate;
    }

    private void setEndProbationDate(String endProbationDate) {
        mEndProbationDate = formatDate(endProbationDate);
        mUser.setEndProbationDate(mEndProbationDate);
        notifyPropertyChanged(BR.endProbationDate);
    }

    private String formatDate(String dataInput) {
        return DateTimeUtils.convertUiFormatToDataFormat(dataInput,
                DateTimeUtils.DATE_FORMAT_YYYY_MM_DD_2,
                mContext.getString(R.string.format_date_mm_dd_yyyy));
    }

    public BranchAdapter getBranchAdapter() {
        return mBranchAdapter;
    }

    public GroupAdapter getGroupAdapter() {
        return mGroupAdapter;
    }

    public void onClickEditProfile(View view) {
        mIsLoadDataFirstTime = true;
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_USER, mUser);
        mNavigator.startActivityForResultFromFragment(UpdateProfileActivity.class, bundle,
                Constant.RequestCode.PROFILE_USER);
    }

    public void onClickChangePassword(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_USER, mUser);
        mNavigator.startActivity(ChangePasswordActivity.class, bundle);
    }
}
