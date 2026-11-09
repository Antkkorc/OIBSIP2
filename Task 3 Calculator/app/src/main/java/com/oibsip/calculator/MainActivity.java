package com.oibsip.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
  private String expression = "";
  private TextView display;
  private final int[] ids = {
    R.id.key_7,
    R.id.key_8,
    R.id.key_9,
    R.id.key_divide,
    R.id.key_4,
    R.id.key_5,
    R.id.key_6,
    R.id.key_multiply,
    R.id.key_1,
    R.id.key_2,
    R.id.key_3,
    R.id.key_subtract,
    R.id.key_clear,
    R.id.key_0,
    R.id.key_dot,
    R.id.key_add,
    R.id.key_back,
    R.id.key_equals
  };

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_main);
    display = findViewById(R.id.display);
    for (int id : ids) {
      Button b = findViewById(id);
      String key = b.getText().toString();
      b.setOnClickListener(v -> tap(key));
    }
  }

  private void tap(String key) {
    if (key.equals("C")) {
      expression = "";
      show("0");
      return;
    }
    if (key.equals("⌫")) {
      if (!expression.isEmpty()) expression = expression.substring(0, expression.length() - 1);
      show(expression.isEmpty() ? "0" : expression);
      return;
    }
    if (key.equals("=")) {
      try {
        double value =
            new Parser(expression.replace('×', '*').replace('÷', '/').replace('−', '-')).parse();
        if (!Double.isFinite(value)) throw new ArithmeticException();
        expression = String.valueOf(value);
        show(expression);
      } catch (Exception e) {
        expression = "";
        show("Error");
      }
      return;
    }
    if (display.getText().toString().equals("Error")) expression = "";
    if ("+−×÷".contains(key)) {
      if (expression.isEmpty() && key.equals("−")) expression = "-";
      else if (!expression.isEmpty()
          && "+-*/".indexOf(expression.charAt(expression.length() - 1)) >= 0)
        expression =
            expression.substring(0, expression.length() - 1)
                + (key.equals("×") ? "*" : key.equals("÷") ? "/" : key.equals("−") ? "-" : key);
      else
        expression += key.equals("×") ? "*" : key.equals("÷") ? "/" : key.equals("−") ? "-" : key;
    } else expression += key;
    show(expression.replace('*', '×').replace('/', '÷').replace('-', '−'));
  }

  private void show(String s) {
    display.setText(s);
  }

  private static class Parser {
    final String s;
    int i = 0;

    Parser(String s) {
      this.s = s;
    }

    double parse() {
      double v = sum();
      if (i != s.length()) throw new IllegalArgumentException();
      return v;
    }

    double sum() {
      double x = product();
      while (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
        char op = s.charAt(i++);
        double y = product();
        x = op == '+' ? x + y : x - y;
      }
      return x;
    }

    double product() {
      double x = atom();
      while (i < s.length() && (s.charAt(i) == '*' || s.charAt(i) == '/')) {
        char op = s.charAt(i++);
        double y = atom();
        if (op == '/' && y == 0) throw new ArithmeticException();
        x = op == '*' ? x * y : x / y;
      }
      return x;
    }

    double atom() {
      int start = i;
      if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) i++;
      while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) i++;
      if (i == start) throw new IllegalArgumentException();
      return Double.parseDouble(s.substring(start, i));
    }
  }
}
