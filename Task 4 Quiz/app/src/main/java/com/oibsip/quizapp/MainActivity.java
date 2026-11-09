package com.oibsip.quizapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
  private final String[][] questions = {
    {"Which planet is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Mercury", "Mars"},
    {"What is the largest ocean?", "Atlantic", "Indian", "Pacific", "Arctic", "Pacific"},
    {"How many sides has a hexagon?", "Five", "Six", "Seven", "Eight", "Six"},
    {"What is the chemical symbol for gold?", "Ag", "Go", "Gd", "Au", "Au"},
    {
      "Which gas do plants absorb?",
      "Oxygen",
      "Carbon dioxide",
      "Nitrogen",
      "Hydrogen",
      "Carbon dioxide"
    },
    {"What is the capital of Japan?", "Kyoto", "Osaka", "Tokyo", "Sapporo", "Tokyo"},
    {"Which animal is a mammal?", "Shark", "Dolphin", "Trout", "Octopus", "Dolphin"},
    {"How many minutes are in two hours?", "100", "110", "120", "140", "120"},
    {
      "Which is the closest star to Earth?",
      "Sirius",
      "The Sun",
      "Proxima Centauri",
      "Vega",
      "The Sun"
    },
    {
      "What is the largest land animal?",
      "Giraffe",
      "African elephant",
      "Polar bear",
      "Hippopotamus",
      "African elephant"
    }
  };
  private ArrayList<Integer> order = new ArrayList<>();
  private int index = 0, score = 0;
  private boolean answered = false;
  private int ink = 0xff262724, accent = 0xffd65b3f;

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_main);
    welcome();
  }

  private LinearLayout page(String label, String title, String sub) {
    FrameLayout host = findViewById(R.id.root);
    host.removeAllViews();
    ScrollView scroll = new ScrollView(this);
    LinearLayout p = new LinearLayout(this);
    p.setPadding(22, 24, 22, 24);
    p.setOrientation(1);
    scroll.addView(p);
    host.addView(scroll);
    TextView a = text(label, 12, accent, true),
        t = text(title, 30, ink, true),
        s = text(sub, 15, 0xff76756e, false);
    p.addView(a);
    p.addView(t);
    p.addView(s);
    return p;
  }

  private TextView text(String s, int size, int color, boolean bold) {
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(size);
    t.setTextColor(color);
    if (bold) t.setTypeface(null, 1);
    t.setPadding(0, 7, 0, 7);
    return t;
  }

  private Button button(LinearLayout p, String s, Runnable r) {
    Button b = new Button(this);
    b.setText(s);
    b.setOnClickListener(v -> r.run());
    p.addView(b);
    return b;
  }

  private void welcome() {
    LinearLayout p =
        page(
            "TASK 4  /  QUIZ",
            "Ten questions. One run.",
            "General knowledge. Questions change order each time.");
    button(p, "Start quiz", this::start);
  }

  private void start() {
    order.clear();
    for (int i = 0; i < questions.length; i++) order.add(i);
    Collections.shuffle(order);
    index = 0;
    score = 0;
    answered = false;
    question();
  }

  private void question() {
    String[] q = questions[order.get(index)];
    LinearLayout p =
        page(
            "TASK 4  /  QUESTION " + (index + 1) + " OF 10",
            "Choose one answer",
            "Score so far: " + score + " correct");
    p.addView(text(q[0], 22, ink, true));
    RadioGroup group = new RadioGroup(this);
    ArrayList<RadioButton> options = new ArrayList<>();
    for (int i = 1; i <= 4; i++) {
      RadioButton b = new RadioButton(this);
      b.setText(q[i]);
      b.setTextSize(17);
      b.setTextColor(ink);
      group.addView(b);
      options.add(b);
    }
    p.addView(group);
    TextView feedback = text("Select an answer to see feedback.", 14, 0xff76756e, false);
    p.addView(feedback);
    group.setOnCheckedChangeListener(
        (g, id) -> {
          if (answered) return;
          RadioButton picked = findViewById(id);
          if (picked == null) return;
          answered = true;
          if (picked.getText().toString().equals(q[5])) {
            score++;
            picked.setTextColor(0xff267442);
            feedback.setText("Correct. Nice recall.");
            feedback.setTextColor(0xff267442);
          } else {
            picked.setTextColor(0xffb23c2d);
            feedback.setText("Not quite. The answer is " + q[5] + ".");
            feedback.setTextColor(0xffb23c2d);
            for (RadioButton b : options)
              if (b.getText().toString().equals(q[5])) b.setTextColor(0xff267442);
          }
        });
    button(
        p,
        index == 9 ? "See results" : "Next question",
        () -> {
          if (!answered) {
            feedback.setText("Choose an answer first.");
            return;
          }
          if (++index == questions.length) results();
          else {
            answered = false;
            question();
          }
        });
  }

  private void results() {
    LinearLayout p =
        page(
            "TASK 4  /  RESULTS",
            "Run complete",
            "Correct: " + score + "     Incorrect: " + (10 - score));
    p.addView(text(score + " / 10", 52, accent, true));
    button(p, "Restart with new questions", this::start);
  }
}
