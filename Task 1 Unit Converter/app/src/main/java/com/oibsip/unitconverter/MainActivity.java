package com.oibsip.unitconverter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
  private final String[] categories = {"Length", "Mass", "Volume", "Temperature"};
  private final String[][] units = {
    {"Metres", "Centimetres", "Kilometres", "Miles", "Feet", "Inches"},
    {"Kilograms", "Grams", "Pounds", "Ounces"},
    {"Litres", "Millilitres", "US gallons", "Cups"},
    {"Celsius", "Fahrenheit", "Kelvin"}
  };
  private final double[][] factors = {
    {1, .01, 1000, 1609.344, .3048, .0254},
    {1, .001, .45359237, .028349523125},
    {1, .001, 3.785411784, .2365882365}
  };
  private Spinner category, source, target;
  private EditText value;
  private TextView result;
  private int ink = 0xff262724, accent = 0xffd65b3f, muted = 0xff76756e;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_main);
    LinearLayout page =
        page(
            "TASK 1  /  UNIT CONVERTER",
            "Convert a measurement",
            "Length, mass, volume and temperature");
    category = new Spinner(this);
    category.setAdapter(adapter(categories));
    value = input("Enter a number", 8194);
    source = new Spinner(this);
    target = new Spinner(this);
    result = text("Your result will appear here", 22, ink, true);
    result.setPadding(18, 20, 18, 20);
    addLabel(page, "CATEGORY");
    page.addView(category);
    addLabel(page, "VALUE");
    page.addView(value);
    addLabel(page, "FROM");
    page.addView(source);
    addLabel(page, "TO");
    page.addView(target);
    result.setBackgroundColor(0xffe8e2d8);
    page.addView(result, space());
    Button swap = button("Swap units");
    page.addView(swap, space());
    Button convert = button("Convert");
    page.addView(convert, space());
    category.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          public void onNothingSelected(AdapterView<?> p) {}

          public void onItemSelected(AdapterView<?> p, View v, int i, long id) {
            source.setAdapter(adapter(units[i]));
            target.setAdapter(adapter(units[i]));
            target.setSelection(Math.min(1, units[i].length - 1));
            result.setText("Your result will appear here");
          }
        });
    swap.setOnClickListener(
        v -> {
          int a = source.getSelectedItemPosition(), b = target.getSelectedItemPosition();
          source.setSelection(b);
          target.setSelection(a);
        });
    convert.setOnClickListener(v -> convertValue());
  }

  private void convertValue() {
    String raw = value.getText().toString().trim();
    if (raw.isEmpty()) {
      toast("Enter a number first.");
      return;
    }
    double x;
    try {
      x = Double.parseDouble(raw);
    } catch (NumberFormatException e) {
      toast("That is not a valid number.");
      return;
    }
    int c = category.getSelectedItemPosition(),
        a = source.getSelectedItemPosition(),
        b = target.getSelectedItemPosition();
    double y;
    if (c == 3) {
      double k = a == 0 ? x + 273.15 : a == 1 ? (x - 32) * 5 / 9 + 273.15 : x;
      y = b == 0 ? k - 273.15 : b == 1 ? (k - 273.15) * 9 / 5 + 32 : k;
    } else y = x * factors[c][a] / factors[c][b];
    result.setText(String.format(Locale.US, "%.8g %s", y, units[c][b]));
  }

  private LinearLayout page(String eyebrow, String title, String sub) {
    FrameLayout host = findViewById(R.id.root);
    LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(22, 24, 22, 20);
    ScrollView scroll = new ScrollView(this);
    scroll.addView(root);
    host.addView(scroll);
    TextView e = text(eyebrow, 12, accent, true),
        t = text(title, 30, ink, true),
        s = text(sub, 14, muted, false);
    root.addView(e);
    root.addView(t);
    root.addView(s);
    return root;
  }

  private void addLabel(LinearLayout p, String s) {
    TextView v = text(s, 11, muted, true);
    v.setPadding(0, 16, 0, 4);
    p.addView(v);
  }

  private ArrayAdapter<String> adapter(String[] a) {
    return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, a);
  }

  private EditText input(String hint, int type) {
    EditText e = new EditText(this);
    e.setSingleLine(true);
    e.setHint(hint);
    e.setInputType(type);
    e.setTextSize(17);
    return e;
  }

  private TextView text(String s, int z, int c, boolean b) {
    TextView v = new TextView(this);
    v.setText(s);
    v.setTextSize(z);
    v.setTextColor(c);
    if (b) v.setTypeface(null, 1);
    return v;
  }

  private Button button(String s) {
    Button b = new Button(this);
    b.setText(s);
    return b;
  }

  private LinearLayout.LayoutParams space() {
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
    p.topMargin = 10;
    return p;
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
  }
}
