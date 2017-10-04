package com.framgia.wsm.screen.updateprofile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.UserRepository;
import com.framgia.wsm.data.source.local.UserLocalDataSource;
import com.framgia.wsm.data.source.remote.UserRemoteDataSource;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.RequestPermissionManager;
import com.framgia.wsm.utils.dagger.ActivityScope;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.utils.rx.BaseSchedulerProvider;
import com.framgia.wsm.utils.validator.Validator;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.framgia.wsm.widget.dialog.DialogManagerImpl;
import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to
 * the {@link UpdateProfilePresenter}.
 */
@Module
public class UpdateProfileModule {

    private AppCompatActivity mActivity;

    public UpdateProfileModule(@NonNull AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    public UpdateProfileContract.ViewModel provideViewModel(Context context, Navigator navigator,
            DialogManager dialogManager, UpdateProfileContract.Presenter presenter,
            RequestPermissionManager requestPermissionManager,
            UpdateProfileAdapter updateProfileAdapter) {
        User user = mActivity.getIntent().getParcelableExtra(Constant.EXTRA_USER);
        UpdateProfileViewModel updateProfileViewModel =
                new UpdateProfileViewModel(context, user, navigator, dialogManager, presenter,
                        requestPermissionManager);
        updateProfileViewModel.setUpdateProfileAdapter(updateProfileAdapter);
        return updateProfileViewModel;
    }

    @ActivityScope
    @Provides
    public UpdateProfileContract.Presenter providePresenter(UserRepository userRepository,
            BaseSchedulerProvider baseSchedulerProvider, Validator validator) {
        return new UpdateProfilePresenter(userRepository, baseSchedulerProvider, validator);
    }

    @ActivityScope
    @Provides
    Navigator provideNavigator() {
        return new Navigator(mActivity);
    }

    @ActivityScope
    @Provides
    DialogManager provideDialogManager() {
        return new DialogManagerImpl(mActivity);
    }

    @ActivityScope
    @Provides
    Validator provideValidator() {
        return new Validator(mActivity.getApplicationContext(), User.class);
    }

    @ActivityScope
    @Provides
    UserRepository provideUserRepository(UserLocalDataSource userLocalDataSource,
            UserRemoteDataSource remoteDataSource) {
        return new UserRepository(userLocalDataSource, remoteDataSource);
    }

    @ActivityScope
    @Provides
    RequestPermissionManager provideRequestPermissionManager() {
        return new RequestPermissionManager(mActivity);
    }

    @ActivityScope
    @Provides
    UpdateProfileAdapter provideUpdateProfileAdapter() {
        return new UpdateProfileAdapter(mActivity, mActivity.getSupportFragmentManager());
    }
}
