package com.framgia.wsm.screen.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.framgia.wsm.data.source.NotificationRepository;
import com.framgia.wsm.data.source.remote.NotificationRemoteDataSource;
import com.framgia.wsm.utils.dagger.FragmentScope;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.utils.rx.BaseSchedulerProvider;
import com.framgia.wsm.widget.dialog.DialogManager;
import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to
 * the {@link NotificationPresenter}.
 */
@Module
public class NotificationModule {

    private Fragment mFragment;

    public NotificationModule(@NonNull Fragment fragment) {
        mFragment = fragment;
    }

    @FragmentScope
    @Provides
    public NotificationContract.ViewModel provideViewModel(Context context,
            NotificationContract.Presenter presenter, NotificationAdapter notificationAdapter,
            Navigator navigator, DialogManager dialogManager) {
        return new NotificationViewModel(context, presenter, notificationAdapter, navigator,
                dialogManager);
    }

    @FragmentScope
    @Provides
    public NotificationContract.Presenter providePresenter(
            NotificationRepository notificationRepository,
            BaseSchedulerProvider baseSchedulerProvider) {
        return new NotificationPresenter(notificationRepository, baseSchedulerProvider);
    }

    @FragmentScope
    @Provides
    public NotificationAdapter provideNotificationAdapter() {
        return new NotificationAdapter(mFragment.getActivity());
    }

    @FragmentScope
    @Provides
    NotificationRepository provideNotificationRepository(
            NotificationRemoteDataSource remoteDataSource) {
        return new NotificationRepository(remoteDataSource);
    }

    @FragmentScope
    @Provides
    Navigator provideNavigator() {
        return new Navigator(mFragment.getActivity());
    }
}
