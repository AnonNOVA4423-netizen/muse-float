package com.example.musefloat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Muse Float\n\n1) まず「他のアプリの上に表示」を許可してください。\n2) 次に「アバターを出す」を押してください。\n止めたいときは通知から、または「アバターを消す」で止められます。");
        tv.setTextSize(16);
        tv.setPadding(48, 48, 48, 24);
        Button perm = new Button(this);
        perm.setText("表示の許可を設定する");
        perm.setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });
        Button start = new Button(this);
        start.setText("アバターを出す");
        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(i);
                return;
            }
            Intent s = new Intent(this, FloatingService.class);
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(s); else startService(s);
        });
        Button stop = new Button(this);
        stop.setText("アバターを消す");
        stop.setOnClickListener(v -> stopService(new Intent(this, FloatingService.class)));

        android.widget.LinearLayout root = new android.widget.LinearLayout(this);
        root.setOrientation(android.widget.LinearLayout.VERTICAL);
        root.addView(tv); root.addView(perm); root.addView(start); root.addView(stop);
        setContentView(root);
    }
}
