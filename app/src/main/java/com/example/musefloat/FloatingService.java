package com.example.musefloat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.util.Random;

public class FloatingService extends Service {
    private WindowManager wm;
    private TextView avatar;
    private WindowManager.LayoutParams params;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private float vx = 3f, vy = 2.4f;
    private final Random rnd = new Random();
    private final String[] phrases = {"こんにちは！", "うろうろ中…", "タップしたね", "今日も元気だよ", "わーい！"};
    private boolean dragging = false;

    @Override public void onCreate() {
        super.onCreate();
        createChannel();
        Notification n = new Notification.Builder(this, "muse_float")
                .setContentTitle("Muse Float")
                .setContentText("アバターが動き回っています。止めるにはアプリで「アバターを消す」を押してください。")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
        startForeground(1, n);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        avatar = new TextView(this);
        avatar.setText("Muse");
        avatar.setTextColor(0xFFFFFFFF);
        avatar.setTextSize(15);
        avatar.setGravity(Gravity.CENTER);
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(0xFF6C5CE7);
        avatar.setBackground(bg);
        int size = 220;
        avatar.setWidth(size); avatar.setHeight(size);

        int type = Build.VERSION.SDK_INT >= 26
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
        params = new WindowManager.LayoutParams(size, size, type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 80; params.y = 300;

        avatar.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY; float touchX, touchY; long downAt;
            @Override public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dragging = false; downAt = System.currentTimeMillis();
                        startX = params.x; startY = params.y;
                        touchX = e.getRawX(); touchY = e.getRawY(); return true;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int)(e.getRawX() - touchX), dy = (int)(e.getRawY() - touchY);
                        if (Math.abs(dx) + Math.abs(dy) > 12) dragging = true;
                        params.x = startX + dx; params.y = startY + dy;
                        wm.updateViewLayout(avatar, params); return true;
                    case MotionEvent.ACTION_UP:
                        if (!dragging && System.currentTimeMillis() - downAt < 300) {
                            avatar.setText(phrases[rnd.nextInt(phrases.length)]);
                            vx = (rnd.nextFloat() * 4 + 2) * (rnd.nextBoolean() ? 1 : -1);
                            vy = (rnd.nextFloat() * 4 + 2) * (rnd.nextBoolean() ? 1 : -1);
                            handler.postDelayed(() -> avatar.setText("Muse"), 1200);
                        }
                        return true;
                }
                return false;
            }
        });

        wm.addView(avatar, params);
        handler.post(tick);
    }

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (avatar != null && !dragging) {
                int maxX = getResources().getDisplayMetrics().widthPixels - avatar.getWidth();
                int maxY = getResources().getDisplayMetrics().heightPixels - avatar.getHeight() - 80;
                params.x += (int) vx; params.y += (int) vy;
                if (params.x < 0) { params.x = 0; vx = Math.abs(vx); }
                if (params.x > maxX) { params.x = maxX; vx = -Math.abs(vx); }
                if (params.y < 0) { params.y = 0; vy = Math.abs(vy); }
                if (params.y > maxY) { params.y = maxY; vy = -Math.abs(vy); }
                try { wm.updateViewLayout(avatar, params); } catch (Exception ignored) {}
            }
            handler.postDelayed(this, 16);
        }
    };

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel("muse_float", "Muse Float", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);
        }
    }

    @Override public void onDestroy() {
        handler.removeCallbacks(tick);
        if (avatar != null) { try { wm.removeView(avatar); } catch (Exception ignored) {} }
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
