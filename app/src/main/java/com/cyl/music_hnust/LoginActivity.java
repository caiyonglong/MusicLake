package com.cyl.music_hnust;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.http.HttpByGet;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ImageView mImg_bg;
    private View mProgressView;
    private View mLoginFormView;
    Button mEmailSignInButton;
    private ImageButton back;
    private static ProgressDialog loadingDialog;
    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingDialog = new ProgressDialog(LoginActivity.this);
        loadingDialog.setTitle("登录中...");
        loadingDialog.setCancelable(false);
        mContext = getApplicationContext();
    }


//        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.username);
//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    mEmailSignInButton.setText("登录");
//                    //mEmailSignInButton.setText("登录中...");
//                    attemptLogin();
//                    return true;
//                }
//                mEmailSignInButton.setText("登录");
//                return false;
//            }
//        });
//
//        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mEmailSignInButton.setText("登录中...");
//                attemptLogin();
//            }
//        });
//
//        back = (ImageButton) findViewById(R.id.backImageButton);
//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mLoginFormView = findViewById(R.id.login_form);
//
//        mImg_bg = (ImageView) findViewById(R.id.Img_bg);

//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;


            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;

            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mEmailSignInButton.setText("登录");
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }


    String json=null;
    private boolean isEmailValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 9;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }




    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private String login_status;
        private String userinfo;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            login_status = "网络请求失败";
            userinfo = HttpByGet.LoginByGet(mUsername, mPassword, 1, null);
            Log.e("result ", userinfo + "");
            if (userinfo != null) {
                String status = null;
                try {
                    final JSONObject jsonObject = new JSONObject(userinfo);
                    status = jsonObject.getString("status");

                    Log.e("status", status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ("success".equals(status)) {
                    login_status = "登录成功";

                    User userinfo3 = JsonParsing.Userinfo(userinfo);
                    String resultcode = HttpByGet.LoginByGet(mUsername, mPassword, 2, userinfo3);

                    Log.e("resultcode", resultcode);
                } else if ("fail".equals(status)) {
                    login_status = "帐号不存在或密码错误";
                }

            }
            if ("登录成功".equals(login_status)) {
                userinfo = HttpByGet.LoginByGet(mUsername, mPassword, 3, null);
                Log.e("useringofasdf======", userinfo);
                User userInfo = JsonParsing.getUserinfo(userinfo);
                Log.e("useringofasdf======", userInfo.getUser_name()+
                        userInfo.getUser_id());
                UserStatus.savaUserInfo(getApplicationContext(), userInfo);
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                if ("登录成功".equals(login_status)) {
                    loadingDialog.dismiss();
                    finish();
                } else {
                    loadingDialog.dismiss();
                    mEmailSignInButton.setText("登录");
                    Toast.makeText(getApplicationContext(), login_status, Toast.LENGTH_SHORT).show();
                }
            } else {
                loadingDialog.dismiss();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }


}
