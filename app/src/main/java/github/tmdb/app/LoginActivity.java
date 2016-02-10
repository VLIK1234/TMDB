package github.tmdb.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import github.tmdb.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE_VK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button noAuthButton = (Button) findViewById(R.id.bt_no_auth);
        noAuthButton.setOnClickListener(this);
        Button vkAuthButton = (Button) findViewById(R.id.bt_vk_auth);
        vkAuthButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_no_auth:
                startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                break;
            case R.id.bt_vk_auth:
                Intent intentVkAuth = new Intent(this, VkLoginActivity.class);
                startActivityForResult(intentVkAuth, REQUEST_CODE_VK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VK && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

}