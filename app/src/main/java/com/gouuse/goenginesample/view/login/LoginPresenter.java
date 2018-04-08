package com.gouuse.goenginesample.view.login;

import com.gouuse.goengine.http.GoHttp;
import com.gouuse.goenginesample.base.BasePresenter;
import com.gouuse.goenginesample.entity.User;
import com.gouuse.goenginesample.net.RequestCallBack;


/**
 * Created by reiserx on 2018/3/29.
 * desc :登录LoginPresenter
 */
public class LoginPresenter extends BasePresenter<LoginView> {


    LoginPresenter(LoginView view) {
        super(view);
    }

    public void login(String userName, String passWord) {
        GoHttp.POST("auth_center/v3/login")
                .tag("login")
                .addParam("account", userName)
                .addParam("password", passWord)
                .request(new RequestCallBack<User>() {
                    @Override
                    public void onSuccess(User user) {
                        mvpView.loginSuccess(user.toString());

                    }

                    @Override
                    protected void onFailed(int errCode, String errMsg) {
                        mvpView.loginFail(errCode, errMsg);
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        GoHttp.cancelTag("login");
    }
}
