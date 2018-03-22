package com.example.rmorawski.letsmakesomefun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String IP = "192.168.0.1";
    private final int PORT =  9876;

    @BindViews({R.id.turn_on, R.id.turn_off, R.id.vga, R.id.hdmi, R.id.volume_minus, R.id.volume_plus})
    List<Button> actionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Bind click listener to buttons
        for(Button button : actionButtons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();

        if(tag != null && !tag.isEmpty()) {
            sendCommand(tag);
            Toast.makeText(this, "" + tag, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendCommand(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(IP, PORT);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.print(msg);

                    pw.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
