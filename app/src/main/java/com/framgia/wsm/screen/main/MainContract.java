package com.framgia.wsm.screen.main;

import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.BasePresenter;
import com.framgia.wsm.screen.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MainContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel {
        boolean onBackPressed();

        void onGetUserSuccess(User user);

        void onGetUserError(BaseException exception);

        void goNextFragmentListRequestOff();

        void goNextFragmentListRequestOverTime();

        void goNextFragmentListRequestLeave();

        void goNextFragmentPersonalInformation();

        void onReloadDataUser();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<ViewModel> {
        void clearUser();

        void getUser();
    }
}
