package com.framgia.wsm.screen.managelistrequests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.framgia.wsm.data.source.RequestRepository;
import com.framgia.wsm.data.source.UserRepository;
import com.framgia.wsm.data.source.local.UserLocalDataSource;
import com.framgia.wsm.data.source.remote.RequestRemoteDataSource;
import com.framgia.wsm.data.source.remote.UserRemoteDataSource;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.dagger.FragmentScope;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.utils.rx.BaseSchedulerProvider;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.framgia.wsm.widget.dialog.DialogManagerImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

/**
 * This is a Dagger module. We use this to pass in the View dependency to
 * the {@link ManageListRequestsPresenter}.
 */
@Module
public class ManageListRequestsModule {

    private Fragment mFragment;

    public ManageListRequestsModule(@NonNull Fragment fragment) {
        mFragment = fragment;
    }

    @FragmentScope
    @Provides
    public ManageListRequestsContract.ViewModel provideViewModel(Context context,
            ManageListRequestsContract.Presenter presenter,
            @Named("ManageListRequest") DialogManager dialogManager,
            ManageListRequestsAdapter manageListRequestsAdapter, Navigator navigator) {
        ManageListRequestsViewModel viewModel =
                new ManageListRequestsViewModel(context, presenter, dialogManager,
                        manageListRequestsAdapter, navigator);
        viewModel.setRequestType(mFragment.getArguments().getInt(Constant.EXTRA_REQUEST_TYPE));
        return viewModel;
    }

    @FragmentScope
    @Provides
    @Named("ManageListRequest")
    public DialogManager provideDialogManager() {
        return new DialogManagerImpl(mFragment.getActivity());
    }

    @FragmentScope
    @Provides
    public ManageListRequestsContract.Presenter providePresenter(
            RequestRepository requestRepository, BaseSchedulerProvider baseSchedulerProvider,
            UserRepository userRepository) {
        return new ManageListRequestsPresenter(requestRepository, baseSchedulerProvider,
                userRepository);
    }

    @FragmentScope
    @Provides
    public ManageListRequestsAdapter provideManageListRequestAdapter() {
        int mRequestType = mFragment.getArguments().getInt(Constant.EXTRA_REQUEST_TYPE);
        return new ManageListRequestsAdapter(mFragment.getActivity(), mRequestType);
    }

    @FragmentScope
    @Provides
    public Navigator provideNavigator() {
        return new Navigator(mFragment);
    }

    @FragmentScope
    @Provides
    public RequestRepository provideRequestRepository(
            RequestRemoteDataSource requestRemoteDataSource) {
        return new RequestRepository(requestRemoteDataSource);
    }

    @FragmentScope
    @Provides
    public UserRepository provideUserRepository(UserLocalDataSource userLocalDataSource,
            UserRemoteDataSource remoteDataSource) {
        return new UserRepository(userLocalDataSource, remoteDataSource);
    }
}
