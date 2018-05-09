package com.gouuse.goengine.http.request;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.gouuse.goengine.common.GoConfig;
import com.gouuse.goengine.http.GoHttp;
import com.gouuse.goengine.http.core.ApiManager;
import com.gouuse.goengine.http.func.ApiRetryFunc;
import com.gouuse.goengine.http.mode.CacheResult;
import com.gouuse.goengine.http.mode.DownProgress;
import com.gouuse.goengine.http.callback.NetCallback;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;



/**
 * Created by reiserx on 2018/3/29.
 * desc :下载请求
 */
public class DownloadRequest extends BaseHttpRequest<DownloadRequest> {

    private String rootName;
    private String dirName = GoConfig.DEFAULT_DOWNLOAD_DIR;
    private String fileName = GoConfig.DEFAULT_DOWNLOAD_FILE_NAME;

    public DownloadRequest(String suffixUrl) {
        super(suffixUrl);
        rootName = getDiskCachePath(GoHttp.getContext());
    }

    /**
     * 设置根目录，默认App缓存目录，有外置卡则默认外置卡缓存目录
     *
     * @param rootName
     * @return
     */
    public DownloadRequest setRootName(String rootName) {
        if (!TextUtils.isEmpty(rootName)) {
            this.rootName = rootName;
        }
        return this;
    }

    /**
     * 设置文件夹路径
     *
     * @param dirName
     * @return
     */
    public DownloadRequest setDirName(String dirName) {
        if (!TextUtils.isEmpty(dirName)) {
            this.dirName = dirName;
        }
        return this;
    }

    /**
     * 设置文件名称
     *
     * @param fileName
     * @return
     */
    public DownloadRequest setFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            this.fileName = fileName;
        }
        return this;
    }

    @Override
    protected <T> Observable<T> execute(Type type) {
        return (Observable<T>) apiService
                .downFile(suffixUrl, params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap(new Function<ResponseBody, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(final ResponseBody responseBody) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<DownProgress>() {
                            @Override
                            public void subscribe(FlowableEmitter<DownProgress> subscriber) throws Exception {
                                File dir = getDiskCacheDir(rootName, dirName);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                File file = new File(dir.getPath() + File.separator + fileName);
                                saveFile(subscriber, file, responseBody);
                            }
                        }, BackpressureStrategy.LATEST);
                    }
                })
                .sample(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .retryWhen(new ApiRetryFunc(retryCount, retryDelayMillis));
    }

    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return null;
    }

    @Override
    protected  void execute(NetCallback callback) {
        if (super.tag != null) {
            ApiManager.get().add(super.tag, callback);
        }
        this.execute(getType(callback)).subscribe(callback);
    }

    private void saveFile(FlowableEmitter<? super DownProgress> sub, File saveFile, ResponseBody resp) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            try {
                int readLen;
                int downloadSize = 0;
                byte[] buffer = new byte[8192];

                DownProgress downProgress = new DownProgress();
                inputStream = resp.byteStream();
                outputStream = new FileOutputStream(saveFile);

                long contentLength = resp.contentLength();
                downProgress.setTotalSize(contentLength);

                while ((readLen = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readLen);
                    downloadSize += readLen;
                    downProgress.setDownloadSize(downloadSize);
                    sub.onNext(downProgress);
                }
                outputStream.flush();
                sub.onComplete();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (resp != null) {
                    resp.close();
                }
            }
        } catch (IOException e) {
            sub.onError(e);
        }
    }

    private File getDiskCacheDir(String rootName, String dirName) {
        return new File(rootName + File.separator + dirName);
    }

    private String getDiskCachePath(Context context) {
        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
