package com.framgia.wsm.data.source.remote.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by levutantuan on 8/10/17.
 */

public class ResetPasswordRequest extends BaseRequest {
    @Expose
    @SerializedName("user")
    private ResetPassword mResetPassword;

    public ResetPasswordRequest(String resetPasswordToken, String password,
            String confirmPassowrd) {
        mResetPassword = new ResetPassword(resetPasswordToken, password, confirmPassowrd);
    }

    public ResetPassword getResetPassword() {
        return mResetPassword;
    }

    public void setResetPassword(ResetPassword resetPassword) {
        mResetPassword = resetPassword;
    }

    /**
     * ResetPassword
     */
    private static final class ResetPassword {
        @Expose
        @SerializedName("reset_password_token")
        private String mResetPasswordToken;
        @Expose
        @SerializedName("password")
        private String mPassword;
        @Expose
        @SerializedName("password_confirmation")
        private String mComfirmPassword;

        ResetPassword(String resetPasswordToken, String password, String comfirmPassword) {
            mResetPasswordToken = resetPasswordToken;
            mPassword = password;
            mComfirmPassword = comfirmPassword;
        }

        public String getResetPasswordToken() {
            return mResetPasswordToken;
        }

        public void setResetPasswordToken(String resetPasswordToken) {
            mResetPasswordToken = resetPasswordToken;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public String getComfirmPassword() {
            return mComfirmPassword;
        }

        public void setComfirmPassword(String comfirmPassword) {
            mComfirmPassword = comfirmPassword;
        }
    }
}
