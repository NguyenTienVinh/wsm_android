package com.framgia.wsm.screen.requestoff;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.wsm.MainApplication;
import com.framgia.wsm.R;
import com.framgia.wsm.data.event.UnauthorizedEvent;
import com.framgia.wsm.data.event.UpdateRemainingDayOff;
import com.framgia.wsm.databinding.ActivityRequestOffBinding;
import com.framgia.wsm.screen.BaseActivity;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import javax.inject.Inject;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * OffRequest Screen.
 */
public class RequestOffActivity extends BaseActivity {

    @Inject
    RequestOffContract.ViewModel mViewModel;
    @Inject
    DialogManager mDialogManager;
    @Inject
    Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerRequestOffComponent.builder()
                .appComponent(((MainApplication) getApplication()).getAppComponent())
                .requestOffModule(new RequestOffModule(this))
                .build()
                .inject(this);

        ActivityRequestOffBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_request_off);
        binding.setViewModel((RequestOffViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mNavigator.finishActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RequestCode.REQUEST_OFF && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnauthorizedEvent event) {
        mDialogManager.showDialogUnauthorized();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRemainingDayOffEvent(UpdateRemainingDayOff event) {
        ((RequestOffViewModel) mViewModel).upDateRemainingDayOff(event.getUser());
    }
}
