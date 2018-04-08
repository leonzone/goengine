package com.gouuse.goenginesample.view.login;

import android.view.View;
import android.widget.EditText;

import com.gouuse.goenginesample.R;
import com.gouuse.goenginesample.base.MvpActivity;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView {

    private EditText mEtName;
    private EditText mEtPass;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        mEtName = findViewById(R.id.editText);
        mEtPass = findViewById(R.id.editText2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpPresenter.login(mEtName.getText().toString(), mEtPass.getText().toString());
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void loginSuccess(String loginName) {

    }

    @Override
    public void loginFail(long code, String str) {

    }

}
