package yair.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser _currUser;

    private TextView _header;
    private EditText _name;
    private Button _nameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_screen_layout);

        _header = findViewById(R.id.hello_header);
        _name = findViewById(R.id.enter_name);
        _nameBtn = findViewById(R.id.enter_name_btn);

        _nameBtn.setOnClickListener(this);

        _currUser = FirebaseAuth.getInstance().getCurrentUser();

        if (_currUser.getDisplayName() != null) {
            _header.setText(_currUser.getDisplayName());
        }
        else {

        }
    }

    @Override
    public void onClick(View v) {

    }
}
