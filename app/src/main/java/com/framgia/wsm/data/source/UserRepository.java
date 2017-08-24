package com.framgia.wsm.data.source;

import com.framgia.wsm.data.model.LeaveType;
import com.framgia.wsm.data.model.OffType;
import com.framgia.wsm.data.model.OffTypeDay;
import com.framgia.wsm.data.model.Setting;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.local.UserLocalDataSource;
import com.framgia.wsm.data.source.remote.UserRemoteDataSource;
import com.framgia.wsm.data.source.remote.api.request.ChangePasswordRequest;
import com.framgia.wsm.data.source.remote.api.request.UpdateProfileRequest;
import com.framgia.wsm.data.source.remote.api.response.BaseResponse;
import com.framgia.wsm.data.source.remote.api.response.SignInDataResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.List;

/**
 * Created by le.quang.dao on 10/03/2017.
 */
public class UserRepository {
    private UserDataSource.LocalDataSource mLocalDataSource;
    private UserDataSource.RemoteDataSource mRemoteDataSource;

    public UserRepository(UserLocalDataSource localDataSource,
            UserRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public Observable<SignInDataResponse> login(String userName, String passWord, String deviceId) {
        return mRemoteDataSource.login(userName, passWord, deviceId)
                .flatMap(
                        new Function<BaseResponse<SignInDataResponse>,
                                ObservableSource<SignInDataResponse>>() {
                            @Override
                            public ObservableSource<SignInDataResponse> apply(@NonNull
                                    BaseResponse<SignInDataResponse> signInDataResponseBaseResponse)
                                    throws Exception {
                                if (signInDataResponseBaseResponse != null) {
                                    return Observable.just(
                                            signInDataResponseBaseResponse.getData());
                                }
                                return Observable.error(new NullPointerException());
                            }
                        });
    }

    public Observable<Object> logout() {
        return mRemoteDataSource.logout();
    }

    public Observable<User> getUserProfile(int userId) {
        return mRemoteDataSource.getUserProfile(userId)
                .flatMap(new Function<BaseResponse<User>, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(
                            @NonNull BaseResponse<User> userBaseResponse) throws Exception {
                        if (userBaseResponse != null) {
                            return Observable.just(userBaseResponse.getData());
                        }
                        return Observable.error(new NullPointerException());
                    }
                });
    }

    public Observable<List<LeaveType>> getListLeaveType() {
        return mRemoteDataSource.getListLeaveType();
    }

    public Observable<List<OffType>> getListOffType() {
        return mRemoteDataSource.getListOffType();
    }

    public Observable<BaseResponse<OffTypeDay>> getRemainingDayOff() {
        return mRemoteDataSource.getRemainingDayOff();
    }

    public void saveUser(User user) {
        mLocalDataSource.saveUser(user);
    }

    public Observable<User> getUser() {
        return mLocalDataSource.getUser();
    }

    public User getUserCheckLogin() {
        return mLocalDataSource.getUserCheckLogin();
    }

    public void clearData() {
        mLocalDataSource.clearData();
    }

    public Observable<BaseResponse<User>> updateProfile(UpdateProfileRequest updateProfile) {
        return mRemoteDataSource.updateProfile(updateProfile);
    }

    public Observable<Object> changePassword(String currentPassword, String newPassword,
            String confirmPassword) {
        ChangePasswordRequest changePasswordRequest =
                new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);
        return mRemoteDataSource.changePassword(changePasswordRequest);
    }

    public Single<BaseResponse<Setting>> getSetting() {
        return mRemoteDataSource.getSetting();
    }

    public Single<Object> changeSetting(Setting setting) {
        return mRemoteDataSource.changeSetting(setting);
    }
}
