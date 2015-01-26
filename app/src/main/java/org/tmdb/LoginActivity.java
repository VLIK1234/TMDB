package org.tmdb;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import org.tmdb.vlik1234.R;


public class LoginActivity extends ActionBarActivity {
    public static final int REQUEST_CODE_VK = 0;
    public static final int REQUEST_CODE_RV = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onNoAuthClick(View view) {
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RV);
    }

    public void onVkAuthClick(View view) {
        Intent intent = new Intent(this, VkLoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_VK);
    }

    public void onAlphaClick(View view) {
        Intent intent = new Intent(this, AlphaActivity.class);
        startActivityForResult(intent, REQUEST_CODE_VK);
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
