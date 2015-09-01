package com.example.uppersky_movil.instagram_api;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uppersky_movil.instagram_api.interfaces.OAuthAuthenticationListener;
import com.example.uppersky_movil.instagram_api.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    // UI
    private ListView listViewImages;



    private Button btnConnect;
    private TextView tvSummary;
    private InstagramApp mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSummary = (TextView) findViewById(R.id.tvSummary);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "ONSUCCESS");
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
      // showInstagramDialog();

          mApp = new InstagramApp(this,
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
                Constants.CALLBACKURL);
        mApp.setListener(listener);

        btnConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mApp.hasAccessToken()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setMessage("Disconnect from Instagram?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            mApp.resetAccessToken();
                                            btnConnect.setText("Connect");
                                            tvSummary.setText("Not connected");
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    mApp.authorize();
                }
            }
        });

        if (mApp.hasAccessToken()) {
            tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
