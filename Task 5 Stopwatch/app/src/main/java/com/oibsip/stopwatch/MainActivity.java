package com.oibsip.stopwatch;

import android.app.Activity;
import android.os.*;
import android.view.Gravity;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
  private Handler handler = new Handler();
  private long elapsed = 0, base = 0;
  private boolean running = false;
  private TextView clock;
  private LinearLayout lapList;
  private ArrayList<String> laps = new ArrayList<>();
  private Runnable ticker;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_main);
    if (state != null) {
      elapsed = state.getLong("elapsed", 0);
      running = state.getBoolean("running", false);
      if (running) base = SystemClock.elapsedRealtime() - elapsed;
      ArrayList<String> saved = state.getStringArrayList("laps");
      if (saved != null) laps = saved;
    }
    screen();
  }

  private void screen() {
    FrameLayout host = findViewById(R.id.root);
    host.removeAllViews();
    ScrollView scroll = new ScrollView(this);
    LinearLayout p = new LinearLayout(this);
    p.setOrientation(1);
    p.setPadding(22, 24, 22, 24);
    scroll.addView(p);
    host.addView(scroll);
    TextView label = text("TASK 5  /  STOPWATCH", 12, 0xffd65b3f, true),
        title = text("Stopwatch", 32, 0xff262724, true),
        sub = text("Monotonic timing with lap records", 14, 0xff76756e, false);
    p.addView(label);
    p.addView(title);
    p.addView(sub);
    clock =
        text(
            format(running ? SystemClock.elapsedRealtime() - base : elapsed), 39, 0xff262724, true);
    clock.setGravity(Gravity.CENTER);
    clock.setPadding(0, 28, 0, 28);
    p.addView(clock);
    LinearLayout controls = new LinearLayout(this);
    Button start = new Button(this);
    start.setText(running ? "Running" : "Start / resume");
    start.setEnabled(!running);
    start.setOnClickListener(
        v -> {
          base = SystemClock.elapsedRealtime() - elapsed;
          running = true;
          screen();
          tick();
        });
    Button pause = new Button(this);
    pause.setText("Pause");
    pause.setOnClickListener(
        v -> {
          if (running) {
            elapsed = SystemClock.elapsedRealtime() - base;
            running = false;
            handler.removeCallbacks(ticker);
            screen();
          }
        });
    Button reset = new Button(this);
    reset.setText("Reset");
    reset.setOnClickListener(
        v -> {
          running = false;
          elapsed = 0;
          laps.clear();
          handler.removeCallbacks(ticker);
          screen();
        });
    controls.addView(start, new LinearLayout.LayoutParams(0, -2, 1));
    controls.addView(pause, new LinearLayout.LayoutParams(0, -2, 1));
    controls.addView(reset, new LinearLayout.LayoutParams(0, -2, 1));
    p.addView(controls);
    Button lap = new Button(this);
    lap.setText("Record lap");
    lap.setOnClickListener(
        v -> {
          if (!running) {
            Toast.makeText(this, "Start the stopwatch first.", Toast.LENGTH_SHORT).show();
            return;
          }
          laps.add(format(SystemClock.elapsedRealtime() - base));
          screen();
          tick();
        });
    p.addView(lap);
    p.addView(text("LAPS  /  " + laps.size(), 12, 0xff76756e, true));
    for (int i = laps.size() - 1; i >= 0; i--)
      p.addView(
          text(String.format(Locale.US, "%02d     %s", i + 1, laps.get(i)), 17, 0xff262724, true));
  }

  private void tick() {
    handler.removeCallbacks(ticker);
    ticker =
        new Runnable() {
          public void run() {
            if (running) {
              if (clock != null) clock.setText(format(SystemClock.elapsedRealtime() - base));
              handler.postDelayed(this, 40);
            }
          }
        };
    handler.post(ticker);
  }

  private String format(long ms) {
    long cs = (ms % 1000) / 10, sec = (ms / 1000) % 60, min = (ms / 60000) % 60, h = ms / 3600000;
    return String.format(Locale.US, "%02d:%02d:%02d.%02d", h, min, sec, cs);
  }

  private TextView text(String s, int size, int color, boolean bold) {
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(size);
    t.setTextColor(color);
    if (bold) t.setTypeface(null, 1);
    return t;
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (running) handler.removeCallbacks(ticker);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (running) tick();
  }

  @Override
  protected void onSaveInstanceState(Bundle out) {
    out.putLong("elapsed", running ? SystemClock.elapsedRealtime() - base : elapsed);
    out.putBoolean("running", running);
    out.putStringArrayList("laps", laps);
    super.onSaveInstanceState(out);
  }
}
