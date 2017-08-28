package com.framgia.wsm.screen.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.framgia.wsm.data.source.TokenRepository;
import com.framgia.wsm.data.source.UserRepository;
import com.framgia.wsm.data.source.local.TokenLocalDataSource;
import com.framgia.wsm.data.source.local.UserLocalDataSource;
import com.framgia.wsm.data.source.remote.UserRemoteDataSource;
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
 * the {@link LoginPresenter}.
 */
@Module
public class LoginModule {

    private Activity mActivity;

    public LoginModule(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    public LoginContract.ViewModel provideViewModel(Context context,
            LoginContract.Presenter presenter, Navigator navigator, DialogManager dialogManager) {
        Bundle bundle = mActivity.getIntent().getExtras();
        return new LoginViewModel(context, presenter, navigator, dialogManager, bundle);
    }

    @ActivityScope
    @Provides
    public LoginContract.Presenter providePresenter(UserRepository userRepository,
            TokenRepository tokenRepository, Validator validator,
            BaseSchedulerProvider baseSchedulerProvider) {
        return new LoginPresenter(userRepository, tokenRepository, validator,
                baseSchedulerProvider);
    }

    @ActivityScope
    @Provides
    public TokenRepository provideTokenRepository(TokenLocalDataSource tokenLocalDataSourc) {
        return new TokenRepository(tokenLocalDataSourc);
    }

    @ActivityScope
    @Provides
    public UserRepository provideUserRepository(UserLocalDataSource userLocalDataSource,
            UserRemoteDataSource remoteDataSource) {
        return new UserRepository(userLocalDataSource, remoteDataSource);
    }

    @ActivityScope
    @Provides
    public Navigator provideNavigator() {
        return new Navigator(mActivity);
    }

    @ActivityScope
    @Provides
    public Validator provideValidator() {
        return new Validator(mActivity.getApplicationContext(), LoginViewModel.class);
    }

    @ActivityScope
    @Provides
    public DialogManager provideDialogManager() {
        return new DialogManagerImpl(mActivity);
    }
}
