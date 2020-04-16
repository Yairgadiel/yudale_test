package yair.test;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by yair on 08/02/19.
 */

public class RegistrationDialog extends Dialog implements View.OnClickListener {

    // region Members

    private IOnDialogClosedListener _listener;
    private User _user;

    // endregion

    // region UI Components

    private EditText _emailAddress;
    private EditText _password;

    private Button _okBtn;
    private Button _discardBtn;

    // endregion

    public RegistrationDialog(@NonNull Context context, User user) {
        super(context);

        _user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_layout);

        _emailAddress = findViewById(R.id.email_address);
        _password = findViewById(R.id.password);
        _okBtn = findViewById(R.id.ok_btn);
        _discardBtn = findViewById(R.id.discard_btn);

        _okBtn.setOnClickListener(this);
        _discardBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                _user.setEmailAddress(_emailAddress.getText().toString());
                _user.setPassword(_password.getText().toString());

                _listener.onDialogClosed(true);

                break;
            case R.id.discard_btn:
                _listener.onDialogClosed(false);

                dismiss();

                break;
        }
    }

    public void setDialogClosedListener(IOnDialogClosedListener listener) {
        _listener = listener;
    }
}
