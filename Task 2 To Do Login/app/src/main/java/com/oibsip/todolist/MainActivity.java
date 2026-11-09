package com.oibsip.todolist;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {
  private Store store;
  private long userId = -1;
  private String userName = "";
  private LinearLayout list;
  private int ink = 0xff262724, accent = 0xffd65b3f, muted = 0xff76756e;

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_main);
    store = new Store(this);
    loginScreen();
  }

  private void show(String eyebrow, String title, String sub) {
    FrameLayout host = findViewById(R.id.root);
    host.removeAllViews();
    ScrollView scroll = new ScrollView(this);
    LinearLayout page = new LinearLayout(this);
    page.setOrientation(1);
    page.setPadding(24, 24, 24, 24);
    scroll.addView(page);
    host.addView(scroll);
    TextView a = text(eyebrow, 12, accent, true),
        t = text(title, 30, ink, true),
        s = text(sub, 14, muted, false);
    page.addView(a);
    page.addView(t);
    page.addView(s);
    list = page;
  }

  private TextView text(String s, int size, int color, boolean bold) {
    TextView v = new TextView(this);
    v.setText(s);
    v.setTextColor(color);
    v.setTextSize(size);
    if (bold) v.setTypeface(null, 1);
    v.setPadding(0, 7, 0, 7);
    return v;
  }

  private EditText field(String hint, int type) {
    EditText e = new EditText(this);
    e.setHint(hint);
    e.setSingleLine(true);
    e.setInputType(type);
    e.setTextSize(16);
    list.addView(e);
    return e;
  }

  private Button button(String label, Runnable run) {
    Button b = new Button(this);
    b.setText(label);
    b.setOnClickListener(v -> run.run());
    list.addView(b);
    return b;
  }

  private void loginScreen() {
    show(
        "TASK 2  /  TO DO WITH LOGIN",
        "Your task list",
        "Accounts and tasks are saved on this device.");
    EditText email = field("Email address", 33), pass = field("Password", 129);
    button("Sign in", () -> login(email.getText().toString().trim(), pass.getText().toString()));
    button("Create an account", this::registerScreen);
  }

  private void registerScreen() {
    show(
        "TASK 2  /  TO DO WITH LOGIN",
        "Create an account",
        "Use a password you do not use elsewhere.");
    EditText name = field("Name", 8193),
        email = field("Email address", 33),
        pass = field("Password, at least 8 characters", 129);
    button(
        "Register",
        () -> {
          String n = name.getText().toString().trim(),
              e = email.getText().toString().trim(),
              p = pass.getText().toString();
          if (n.isEmpty() || !e.contains("@") || p.length() < 8) {
            toast("Enter a name, valid email and password of at least 8 characters.");
            return;
          }
          try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            String s = android.util.Base64.encodeToString(salt, android.util.Base64.NO_WRAP),
                h = hash(p, salt);
            store
                .getWritableDatabase()
                .execSQL(
                    "INSERT INTO users(name,email,salt,hash) VALUES(?,?,?,?)",
                    new Object[] {n, e, s, h});
            login(e, p);
          } catch (Exception ex) {
            toast("That email is already registered.");
          }
        });
    button("Back to sign in", this::loginScreen);
  }

  private String hash(String password, byte[] salt) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    byte[] first = Arrays.copyOf(salt, salt.length + 4);
    first[first.length - 1] = 1;
    byte[] u = mac.doFinal(first), out = u.clone();
    for (int round = 1; round < 120000; round++) {
      u = mac.doFinal(u);
      for (int i = 0; i < out.length; i++) out[i] ^= u[i];
    }
    return android.util.Base64.encodeToString(out, android.util.Base64.NO_WRAP);
  }

  private void login(String email, String password) {
    try (Cursor c =
        store
            .getReadableDatabase()
            .rawQuery("SELECT id,name,salt,hash FROM users WHERE email=?", new String[] {email})) {
      if (!c.moveToFirst()) {
        toast("Account not found. Register first.");
        return;
      }
      byte[] salt = android.util.Base64.decode(c.getString(2), android.util.Base64.DEFAULT);
      byte[] given = android.util.Base64.decode(hash(password, salt), android.util.Base64.DEFAULT),
          expected = android.util.Base64.decode(c.getString(3), android.util.Base64.DEFAULT);
      if (!MessageDigest.isEqual(given, expected)) {
        toast("Password does not match.");
        return;
      }
      userId = c.getLong(0);
      userName = c.getString(1);
    } catch (Exception e) {
      toast("Could not sign in.");
      return;
    }
    taskScreen();
  }

  private void taskScreen() {
    show("TASK 2  /  TO DO WITH LOGIN", "Hello, " + userName, "Your tasks belong to this account.");
    button("Add a task", this::addTask);
    button(
        "Log out",
        () -> {
          userId = -1;
          userName = "";
          loginScreen();
        });
    Cursor c =
        store
            .getReadableDatabase()
            .rawQuery(
                "SELECT id,title,note,done FROM tasks WHERE user_id=? ORDER BY id DESC",
                new String[] {Long.toString(userId)});
    if (!c.moveToFirst())
      list.addView(text("Nothing here yet. Add your first task.", 17, muted, false));
    else
      do {
        long id = c.getLong(0);
        String title = c.getString(1), note = c.getString(2);
        boolean done = c.getInt(3) == 1;
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(1);
        row.setPadding(12, 12, 12, 12);
        row.setBackgroundColor(0xffffffff);
        TextView t = text((done ? "✓  " : "○  ") + title, 17, done ? muted : ink, true);
        if (done) t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        row.addView(t);
        if (note != null && !note.isEmpty()) row.addView(text(note, 14, muted, false));
        LinearLayout actions = new LinearLayout(this);
        Button toggle = new Button(this);
        toggle.setText(done ? "Reopen" : "Complete");
        toggle.setOnClickListener(
            v -> {
              store
                  .getWritableDatabase()
                  .execSQL(
                      "UPDATE tasks SET done=? WHERE id=? AND user_id=?",
                      new Object[] {done ? 0 : 1, id, userId});
              taskScreen();
            });
        Button delete = new Button(this);
        delete.setText("Delete");
        delete.setOnClickListener(
            v ->
                new AlertDialog.Builder(this)
                    .setMessage("Delete this task permanently?")
                    .setNegativeButton("Keep", null)
                    .setPositiveButton(
                        "Delete",
                        (d, w) -> {
                          store
                              .getWritableDatabase()
                              .execSQL(
                                  "DELETE FROM tasks WHERE id=? AND user_id=?",
                                  new Object[] {id, userId});
                          taskScreen();
                        })
                    .show());
        actions.addView(toggle, new LinearLayout.LayoutParams(0, -2, 1));
        actions.addView(delete, new LinearLayout.LayoutParams(0, -2, 1));
        row.addView(actions);
        list.addView(row);
      } while (c.moveToNext());
    c.close();
  }

  private void addTask() {
    LinearLayout box = new LinearLayout(this);
    box.setOrientation(1);
    EditText title = new EditText(this);
    title.setHint("Task name");
    EditText note = new EditText(this);
    note.setHint("Optional notes");
    box.addView(title);
    box.addView(note);
    new AlertDialog.Builder(this)
        .setTitle("Add a task")
        .setView(box)
        .setNegativeButton("Cancel", null)
        .setPositiveButton(
            "Save",
            (d, w) -> {
              String t = title.getText().toString().trim();
              if (t.isEmpty()) {
                toast("A task needs a name.");
                return;
              }
              store
                  .getWritableDatabase()
                  .execSQL(
                      "INSERT INTO tasks(user_id,title,note,done) VALUES(?,?,?,0)",
                      new Object[] {userId, t, note.getText().toString().trim()});
              taskScreen();
            })
        .show();
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
  }

  private static class Store extends SQLiteOpenHelper {
    Store(Context c) {
      super(c, "oibsip_tasks.db", null, 1);
    }

    public void onCreate(SQLiteDatabase d) {
      d.execSQL(
          "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,email TEXT"
              + " NOT NULL UNIQUE,salt TEXT NOT NULL,hash TEXT NOT NULL)");
      d.execSQL(
          "CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER NOT NULL,title"
              + " TEXT NOT NULL,note TEXT,done INTEGER NOT NULL DEFAULT 0)");
    }

    public void onUpgrade(SQLiteDatabase d, int oldV, int newV) {}
  }
}
