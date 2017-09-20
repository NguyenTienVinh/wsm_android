package com.framgia.wsm.screen.notification;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.Log;
import com.framgia.wsm.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.Notification;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.data.source.remote.api.request.NotificationRequest;
import com.framgia.wsm.screen.BaseRecyclerViewAdapter;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import java.util.List;

/**
 * Exposes the data to be used in the Notification screen.
 */

public class NotificationViewModel extends BaseObservable implements NotificationContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Notification> {
    private static final String TAG = "NotificationViewModel";
    private NotificationContract.Presenter mPresenter;
    private NotificationAdapter mNotificationAdapter;
    private int mPage;
    private boolean mIsShowProgress;
    private NotificationDialogFragment.UpdateNotificationListener mListener;
    private Notification mNotification;
    private boolean mIsSetReadAll;
    private Navigator mNavigator;
    private DialogManager mDialogManager;
    private Context mContext;
    private boolean mIsLoadMoreRecyclerView;

    NotificationViewModel(Context context, NotificationContract.Presenter presenter,
            NotificationAdapter notificationAdapter, Navigator navigator,
            DialogManager dialogManager) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mNotificationAdapter = notificationAdapter;
        mNotificationAdapter.setItemClickListener(this);
        mPage = 1;
        mPresenter.getNotification(mPage);
        mNavigator = navigator;
        mDialogManager = dialogManager;
        mIsLoadMoreRecyclerView = true;
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
    public void onItemRecyclerViewClick(Notification item) {
        if (item.getRead()) {
            if (checkUnauthorizedToAccess(item)) {
                return;
            }
            mListener.onClickNotification(item.getTrackableType(), item.getPermission(),
                    item.getTrachableStatus());
            mNavigator.dismissDialogFragment(NotificationDialogFragment.TAG);
            return;
        }
        mIsSetReadAll = false;
        mPresenter.setRead(new NotificationRequest(item.getId(), true, null));
        mNotification = item;
    }

    public NotificationAdapter getNotificationAdapter() {
        return mNotificationAdapter;
    }

    @Override
    public void onGetNotificationSuccess(List<Notification> list) {
        if (list == null) {
            return;
        }
        setShowProgress(false);
        mPage++;
        mNotificationAdapter.updateData(list);
        if (list.isEmpty()) {
            mIsLoadMoreRecyclerView = false;
        }
    }

    @Override
    public void onGetNotificationError(BaseException e) {
        setShowProgress(false);
        Log.e(TAG, "NotificationViewModel", e);
    }

    @Override
    public void onLoadMoreNotification() {
        if (mIsLoadMoreRecyclerView) {
            setShowProgress(true);
            mPresenter.getNotification(mPage);
        }
    }

    @Override
    public void onSetReadSuccess() {
        if (mIsSetReadAll) {
            mNotificationAdapter.setReadAll();
            mListener.onUpdateNotificationReadAll();
            return;
        }
        mNotificationAdapter.setReadOne(mNotification);
        if (checkUnauthorizedToAccess(mNotification)) {
            return;
        }
        mListener.onClickNotification(mNotification.getTrackableType(),
                mNotification.getPermission(), mNotification.getTrachableStatus());
        mNavigator.dismissDialogFragment(NotificationDialogFragment.TAG);
    }

    private boolean checkUnauthorizedToAccess(Notification notification) {
        if (notification.getTrackableType().equals(TrackableType.GROUP)
                || notification.getTrackableType().equals(TrackableType.USER_SPECIAL_TYPE)
                || notification.getTrackableType().equals(TrackableType.WORKSPACE)) {
            mDialogManager.dialogError(mContext.getString(R.string.you_are_unauthorized_to_access));
            return true;
        }
        return false;
    }

    @Override
    public void onSetReadError(BaseException e) {
        Log.e(TAG, "NotificationViewModel", e);
    }

    @Bindable
    public boolean isShowProgress() {
        return mIsShowProgress;
    }

    private void setShowProgress(boolean showProgress) {
        mIsShowProgress = showProgress;
        notifyPropertyChanged(BR.showProgress);
    }

    public void onClickReadAll() {
        mIsSetReadAll = true;
        mPresenter.setRead(new NotificationRequest(null, null, true));
    }

    @Override
    public void setUpdateNotificationListener(
            NotificationDialogFragment.UpdateNotificationListener listener) {
        mListener = listener;
    }

    /**
     * trackable type.
     */
    @StringDef({
            TrackableType.GROUP, TrackableType.LOCK_TIME_SHEET, TrackableType.REQUEST_LEAVE,
            TrackableType.REQUEST_OFF, TrackableType.REQUEST_OT, TrackableType.USER,
            TrackableType.USER_SPECIAL_TYPE, TrackableType.WORKSPACE
    })
    public @interface TrackableType {
        String GROUP = "Group";
        String LOCK_TIME_SHEET = "LockTimesheet";
        String REQUEST_LEAVE = "RequestLeave";
        String REQUEST_OFF = "RequestOff";
        String REQUEST_OT = "RequestOt";
        String USER = "User";
        String USER_SPECIAL_TYPE = "UserSpecialType";
        String WORKSPACE = "Workspace";
    }

    /**
     * Permission type.
     */
    @IntDef({
            PermissionType.USER, PermissionType.MANAGER
    })
    public @interface PermissionType {
        int USER = 0;
        int MANAGER = 1;
    }
}
