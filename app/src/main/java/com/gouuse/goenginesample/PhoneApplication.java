package com.gouuse.goenginesample;

/**
 * Created by reiserx on 2018/3/29.
 * desc :
 */


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.gouuse.goengine.http.GoHttp;
import com.gouuse.goengine.http.core.ApiCookie;
import com.gouuse.goenginesample.net.convert.GsonConverterFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by reiserx on 2017/5/23.
 * PhoneApplication处理程序初始化
 */
public class PhoneApplication extends Application {
    private static Context mApplicationContext;

    /**
     * 维护Activity 的list
     */
    private static final List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());

    /**
     * 维护Activity 的list
     */
    private static final List<Activity> imActivities = Collections
            .synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        initNet();
    }

    /**
     * 初始化网络
     */
    private void initNet() {

        GoHttp.init(this);
        GoHttp.CONFIG()
                //配置请求主机地址
                .baseUrl("")
                //配置全局请求头
//                .globalHeaders(globalHeaders)
                //配置全局请求参数
//                .globalParams(globalParams)
                //配置读取超时时间，单位秒
                .readTimeout(30)
                //配置写入超时时间，单位秒
                .writeTimeout(30)
                //配置连接超时时间，单位秒
                .connectTimeout(30)
                //配置请求失败重试次数
                .retryCount(3)
                //配置请求失败重试间隔时间，单位毫秒
                .retryDelayMillis(1000)
                //配置是否使用cookie
                .setCookie(true)
                //配置自定义cookie
                .apiCookie(new ApiCookie(this))
                //配置是否使用OkHttp的默认缓存
                .setHttpCache(true)
                .converterFactory(GsonConverterFactory.create())
//                //配置OkHttp缓存路径
//                .setHttpCacheDirectory(new File(GoHttp.getContext().getCacheDir(), GoConfig.CACHE_HTTP_DIR))
//                //配置自定义OkHttp缓存
//                .httpCache(new Cache(new File(GoHttp.getContext().getCacheDir(), GoConfig.CACHE_HTTP_DIR), GoConfig.CACHE_MAX_SIZE))
//                //配置自定义离线缓存
//                .cacheOffline(new Cache(new File(GoHttp.getContext().getCacheDir(), GoConfig.CACHE_HTTP_DIR), GoConfig.CACHE_MAX_SIZE))
//                //配置自定义在线缓存
//                .cacheOnline(new Cache(new File(GoHttp.getContext().getCacheDir(), GoConfig.CACHE_HTTP_DIR), GoConfig.CACHE_MAX_SIZE))
//                //配置开启Gzip请求方式，需要服务器支持
//                .postGzipInterceptor()
//                //配置应用级拦截器
//                .interceptor(new HttpLogInterceptor()
//                        .setLevel(HttpLogInterceptor.Level.BODY))
//                //配置网络拦截器
//                .networkInterceptor(new NoCacheInterceptor())
//                //配置主机证书验证
//                .hostnameVerifier(new SSLUtil.UnSafeHostnameVerifier("http://192.168.1.100/"))
//                //配置SSL证书验证
//                .SSLSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
        ;


    }


    public static Context getInstances() {
        return mApplicationContext;
    }

    public static void setInstances(Context context) {
        mApplicationContext = context;
    }


    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }


    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public static void pushActivity(Activity activity) {
        if (mActivitys == null) {
            return;
        }
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public static void popActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        mActivitys.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        return mActivitys.get(mActivitys.size() - 1);
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }
}
