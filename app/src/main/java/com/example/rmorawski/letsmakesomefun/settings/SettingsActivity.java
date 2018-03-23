package com.example.rmorawski.letsmakesomefun.settings;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rmorawski.letsmakesomefun.R;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.address_ip)
    EditText addressIp;

    @BindView(R.id.port)
    EditText port;

    @BindView(R.id.save)
    Button save;

    @BindView(R.id.debug)
    Switch debug;

    Settings settings = Settings.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        addressIp.setText(settings.getIp());
        port.setText(String.valueOf(settings.getPort()));
        debug.setChecked(settings.isDebug());

        Log.d("SettingsActivity", ""+ settings.isDebug());

        save.setOnClickListener(v -> {
            saveSettings();

            Toast.makeText(this, "Operation completed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            settings.setLogged(false);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSettings() {
        settings.setIp(addressIp.getText().toString());
        settings.setPort(Integer.valueOf(port.getText().toString()));
        settings.setDebug(debug.isChecked());
    }
}
