package patrick.pramedia.wire;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Setting");
    }

    @Override
    public void onClick(View view) {

    }
}
