package github.tmdb.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import github.tmdb.utils.AuthUtils;


public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AuthUtils.isLogged()) {
            startMainActivity();
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            startMainActivity();
        } else {
            finish();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainScreenActivity.class));
        finish();
    }
}
