package com.example.rmorawski.letsmakesomefun;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmorawski.letsmakesomefun.settings.LocalStorage;
import com.example.rmorawski.letsmakesomefun.settings.Settings;
import com.example.rmorawski.letsmakesomefun.settings.SettingsActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog aboutDialog;
    private AlertDialog loginDialog;
    private AlertDialog errorConnection;

    private Settings settings;

    // Here you have to add button id when adding button to application
    @BindViews({
            R.id.turn_on,
            R.id.turn_off,
            R.id.vga,
            R.id.hdmi,
            R.id.volume_minus,
            R.id.volume_plus
    })
    List<Button> actionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Bind click listener to buttons
        for (Button button : actionButtons) {
            button.setOnClickListener(this);
        }

        if(settings == null) {
            settings = Settings.getInstance();
            getSettings();
        }
        createDialogs();
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();

        if (tag != null && !tag.isEmpty()) {
            sendCommand(tag);

            if(settings.isDebug()) {
                Toast.makeText(this, "" + tag, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    public void sendCommand(final String msg) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(settings.getIp(), settings.getPort());
                PrintWriter pw = new PrintWriter(socket.getOutputStream());

                pw.print(msg);

                pw.close();
                socket.close();
            } catch (IOException e) {

                runOnUiThread(() -> errorConnection.show());
                e.printStackTrace();
            }
            catch(IllegalArgumentException e) {
                runOnUiThread(() -> errorConnection.show());
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                if(settings.isLogged()) {
                    startSettingsActivity();
                }
                else {
                    loginDialog.show();
                }
            }
                return true;
            case R.id.about:
                aboutDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSettings() {
        // Storage
        LocalStorage localStorage = new LocalStorage(this);

        if(localStorage.getValue("ip") != null) {
            settings.setIp(localStorage.getValue("ip"));
        }
        if(localStorage.getValue("port") != null) {
            settings.setPort(Integer.valueOf(localStorage.getValue("port")));
        }
        if(localStorage.getValue("debug") != null) {
            settings.setDebug(Boolean.valueOf(localStorage.getValue("debug")));
        }
    }

    private void saveSettings() {
        LocalStorage localStorage = new LocalStorage(this);
        localStorage.setValue("ip", settings.getIp());
        localStorage.setValue("port", String.valueOf(settings.getPort()));
        localStorage.setValue("debug", String.valueOf(settings.isDebug()));
    }

    private void createDialogs() {
        if (aboutDialog == null) {
            AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
            aboutBuilder.setMessage("This is test message")
                    .setTitle("About application")
                    .setPositiveButton("Close", (dialog, id) -> dialog.cancel());

            aboutDialog = aboutBuilder.create();
        }

        if (loginDialog == null) {
            View view = getLayoutInflater().inflate(R.layout.dialog, null);

            TextView userNameTv = view.findViewById(R.id.username);
            TextView passwordTv = view.findViewById(R.id.password);
            TextView badCredentialsTv = view.findViewById(R.id.bad_credentials);

            userNameTv.setOnClickListener(v -> badCredentialsTv.setVisibility(View.GONE));
            passwordTv.setOnClickListener(v -> badCredentialsTv.setVisibility(View.GONE));

            loginDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("Zaloguj", null)
                    .setNegativeButton("Anuluj", null)
                    .setCancelable(false)
                    .create();

            loginDialog.setOnShowListener(dialog -> {
                Button posBtn = loginDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posBtn.setOnClickListener(v -> {

                    String username = userNameTv.getText().toString();
                    String password = passwordTv.getText().toString();

                    Log.d("LoginDialog", "Username: " + username + " Password: " + password);

                    if (username.equals("test") && password.equals("test")) {
                        // Ok let's login
                        settings.setLogged(true);
                        Log.d("LoginDialog", "User logged");

                        // Hide and reset dialog
                        userNameTv.setText("");
                        passwordTv.setText("");
                        loginDialog.cancel();

                        // Start settings activity
                        startSettingsActivity();
                    } else {
                        badCredentialsTv.setVisibility(View.VISIBLE);
                    }
                });

                loginDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v) -> loginDialog.cancel());
            });
        }

        if(errorConnection == null) {
            errorConnection = new AlertDialog.Builder(this)
                    .setTitle("Connection error")
                    .setMessage("Check ip address and port in settings")
                    .create();
        }
    }

    private void startSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
