package com.framgia.wsm.screen.requestoff;

import com.framgia.wsm.data.model.OffRequest;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.UserRepository;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.data.source.remote.api.error.Type;
import com.framgia.wsm.data.source.remote.api.response.ErrorResponse;
import com.framgia.wsm.utils.rx.ImmediateSchedulerProvider;
import com.framgia.wsm.utils.validator.Validator;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vinh on 25/08/2017.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateRequestOffPresenterTest {

    @InjectMocks
    RequestOffPresenter mRequestOffPresenter;
    @Mock
    RequestOffViewModel mRequestOffViewModel;
    @Mock
    UserRepository mUserRepository;
    @Mock
    Validator mValidator;
    @InjectMocks
    ImmediateSchedulerProvider mBaseSchedulerProvider;

    @Before
    public void setUp() throws Exception {
        mRequestOffPresenter.setViewModel(mRequestOffViewModel);
        mRequestOffPresenter.setSchedulerProvider(mBaseSchedulerProvider);
    }

    @Test
    public void getUser_shouldSuccess_whenGetUserFromLocal() throws Exception {
        // Give
        User user = new User();
        // When
        when(mUserRepository.getUser()).thenReturn(Observable.just(user));
        // Then
        mRequestOffPresenter.getUser();

        verify(mRequestOffViewModel, Mockito.never()).onGetUserError(null);
        verify(mRequestOffViewModel).onGetUserSuccess(user);
    }

    @Test(expected = BaseException.class)
    public void getUser_shouldReturnError_whenCanNotGetUser() throws Exception {
        //Give
        BaseException baseException = new BaseException(Type.HTTP, new ErrorResponse());
        //When
        when(mUserRepository.getUser()).thenThrow(baseException);
        //Then
        mRequestOffPresenter.getUser();
        //Actual
        verify(mRequestOffViewModel).onGetUserError(baseException);
    }

    @Test
    public void validateReasonInput_shouldError_whenInputValidDataError() throws Exception {
        // Give
        String expect = "Reason can not be blank!";
        OffRequest offRequest = new OffRequest();
        offRequest.setReason("");
        // When
        when(mValidator.validateValueNonEmpty("")).thenReturn(expect);
        // Then
        mRequestOffPresenter.validateData(offRequest);
        verify(mRequestOffViewModel).onInputReasonError(expect);
    }

    @Test
    public void validateReasonInput_shouldCorrect_whenInputValidData() throws Exception {
        // Give
        String expect = "";
        String reason = "Sick!";
        OffRequest offRequest = new OffRequest();
        offRequest.setReason(reason);
        // When
        when(mValidator.validateValueNonEmpty(reason)).thenReturn(expect);
        // Then
        mRequestOffPresenter.validateData(offRequest);
        verify(mRequestOffViewModel).onInputReasonError(expect);
    }
}
