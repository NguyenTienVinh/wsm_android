package com.framgia.wsm.data.source.remote.api.middleware;

import android.content.Context;
import com.framgia.wsm.R;
import com.framgia.wsm.data.event.InternetEvent;
import com.framgia.wsm.data.event.UnauthorizedEvent;
import com.framgia.wsm.data.source.TokenRepository;
import com.framgia.wsm.utils.InternetManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Sun on 3/18/2017.
 */

public class InterceptorImpl implements Interceptor {

    private static final String KEY_TOKEN = "WSM-AUTH-TOKEN";
    private static final String KEY_LOCATE = "WSM-LOCALE";
    private Context mContext;
    private TokenRepository mTokenRepository;
    private InternetManager mInternetManager;

    public InterceptorImpl(Context applicationContext, TokenRepository tokenRepository,
            InternetManager internetManager) {
        mContext = applicationContext;
        mTokenRepository = tokenRepository;
        mInternetManager = internetManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = initializeHeader(chain);
        Request request = builder.build();
        if (!mInternetManager.isConnection(mContext)) {
            EventBus.getDefault().post(new InternetEvent());
        }
        Response response = chain.proceed(request);
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            EventBus.getDefault().post(new UnauthorizedEvent());
        }
        return response;
    }

    private Request.Builder initializeHeader(Chain chain) {
        Request originRequest = chain.request();
        return originRequest.newBuilder()
                .header("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Cache-Control", "no-store")
                .addHeader(KEY_LOCATE, mContext.getString(R.string.locate))
                .addHeader(KEY_TOKEN, mTokenRepository.getToken())
                .method(originRequest.method(), originRequest.body());
    }
}
