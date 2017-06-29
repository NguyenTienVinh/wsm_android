package com.framgia.wsm.screen.updateprofile;

import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.BasePresenter;
import com.framgia.wsm.screen.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface UpdateProfileContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel {

        void onInputEmployeeNameError(String employeeName);

        void onInputBirthdayError(String birthday);

        void onUpdateProfileSuccess(User user);

        void onUpdateProfileError(BaseException exception);

        void setAvatarUser(String avatar);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter<ViewModel> {

        boolean validateData(User user);

        void updateProfile(User user);
    }
}
