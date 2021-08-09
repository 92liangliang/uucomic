package com.re.ng.uu.comic.http;

import android.text.TextUtils;

import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.config.ApiConstant;
import com.re.ng.uu.comic.util.LogUtil;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yyj on 2018/07/31. email: 2209011667@qq.com
 */

public class UUClient {
    public static final String TAG = "UUClient";

    public static ApiService getDefault() {
        return bind(ApiConstant.UU_API_URL).create(ApiService.class);
    }

    public static ApiService getDefault2() {
        return bind(ApiConstant.UU_API_URL2).create(ApiService.class);
    }

    public static <T> void sub(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static Retrofit bind(String BASE_URL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient(true))
                .build();
        return retrofit;
    }

    private static OkHttpClient getClient(boolean needToken) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间

        if (needToken) {
            builder.addNetworkInterceptor(getGetParameterIntercepter());
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        OkHttpClient mOkHttpClient = builder.build();
        return mOkHttpClient;
    }

    private static OkHttpClient getRechargeClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        OkHttpClient mOkHttpClient = builder
                .followRedirects(false)
                .followSslRedirects(false)
//                .addInterceptor(new RedirectInterceptor())
                .build();
        return mOkHttpClient;
    }

    public static Observable<String> recharge(String money, int pay_type) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String time = getTime();
                String token = UUClient.md5("hahmh" + time);
                OkHttpClient client = getRechargeClient();
                FormBody formBody = new FormBody.Builder()
                        .add("money", money)
                        .add("pay_type", pay_type + "")
                        .add("time", time)
                        .add("token", token)
                        .add("utoken", APP.getInstance().getUToken())
                        .build();
                Request request = new Request.Builder()
                        .url(ApiConstant.UU_API_URL + "Finance/charge")
                        .post(formBody).build();
                Response response = client.newCall(request).execute();
                String location = response.headers().get("Location");
                LogUtil.d("OkHttp", "location = " + location);
                if (response.isSuccessful()) {
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } else if (response.code() == 302) {
                    emitter.onNext(location);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("网络错误"));
                }
            }
        });
    }

    public static String getTime() {
        return String.valueOf(new Date().getTime() / 1000L);
    }

    public static String getToken(String time){
        return UUClient.md5("hahmh" + time);
    }

    private static Interceptor getGetParameterIntercepter() {
        final String METHOD_GET = "GET";
        final String METHOD_POST = "POST";
        final String HEADER_KEY_USER_AGENT = "User-Agent";

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                HttpUrl.Builder urlBuilder = request.url().newBuilder();
                HttpUrl url = request.url();
                if (TextUtils.isEmpty(url.queryParameter("time"))) {
                    String time = getTime();
                    String token = md5("hahmh" + time);
                    urlBuilder.addEncodedQueryParameter("time", time);
                    urlBuilder.addEncodedQueryParameter("token", token);
                    LogUtil.d(TAG, "url = " + url.url());
                    LogUtil.d(TAG, "time = " + time);
                    LogUtil.d(TAG, "token = " + token);
                }
                // 将最终的url填充到request中
                requestBuilder.url(urlBuilder.build());
                // 这里我们可以添加header
//                requestBuilder.addHeader(HEADER_KEY_USER_AGENT, getUserAgent()); // 举例，调用自己业务的getUserAgent方法
                return chain.proceed(requestBuilder.build());

            }
        };
    }

    public static String md5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
