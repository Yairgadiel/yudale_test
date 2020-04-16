package yair.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


/**
 * Created by yair on 08/02/19.
 */

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    // region Members

    private FirebaseAuth _firebaseAuth;
    private CallbackManager _callbackManager;

    // endregion

    // region UI Components

    private Button _login;
    private Button _register;
    private LoginButton _fbBtn;

    // endregion

    private class TestClass extends ITest.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    private class SecondTestClass extends ISecondTest.Stub {


        @Override
        public void secondTest() throws RemoteException {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        _callbackManager = CallbackManager.Factory.create();

        _firebaseAuth = FirebaseAuth.getInstance();

        _login = findViewById(R.id.login_btn);
        _register = findViewById(R.id.register_btn);
        _fbBtn = findViewById(R.id.fb_login_btn);

        _login.setOnClickListener(this);
        _register.setOnClickListener(this);

        _fbBtn.setReadPermissions(Arrays.asList("email"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        _fbBtn.registerCallback(_callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                System.out.println("FB Success");

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("FB Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("FB Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = _firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            System.out.println(currentUser.getDisplayName() + " is connected");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                final User newUser = new User();

                final RegistrationDialog dialog = new RegistrationDialog(this, newUser);

                dialog.setDialogClosedListener(new IOnDialogClosedListener() {
                    @Override
                    public void onDialogClosed(boolean result) {
                        if (result) {
                            _firebaseAuth.createUserWithEmailAndPassword(newUser.getEmailAddress(), newUser.getPassword())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(HomeScreen.this, "Authentication success!",
                                                        Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                            else {
                                                if (task.getException() != null) {
                                                    task.getException().printStackTrace();
                                                }

                                                Toast.makeText(HomeScreen.this, "Authentication failed!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

                dialog.show();

                break;
        case R.id.login_btn:
            final User user = new User();

            final RegistrationDialog loginDialog = new RegistrationDialog(this, user);

            loginDialog.setDialogClosedListener(new IOnDialogClosedListener() {
                @Override
                public void onDialogClosed(boolean result) {
                    if (result) {
                        _firebaseAuth.signInWithEmailAndPassword(user.getEmailAddress(), user.getPassword())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(HomeScreen.this, "Authentication success!",
                                                    Toast.LENGTH_LONG).show();
                                            loginDialog.dismiss();
                                        }
                                        else {
                                            if (task.getException() != null) {
                                                task.getException().printStackTrace();
                                            }

                                            Toast.makeText(HomeScreen.this, "Authentication failed! " + task.getException().getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
            });

            loginDialog.show();

            break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        _firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithCredential:success");

                            FirebaseUser user = _firebaseAuth.getCurrentUser();

                            Toast.makeText(HomeScreen.this, "Authentication successful! " + user.getDisplayName(),
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(HomeScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

