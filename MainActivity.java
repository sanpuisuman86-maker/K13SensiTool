package com.example.k13sensi;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private final Map<String, Integer> values = new LinkedHashMap<>();
    private final Map<String, TextView> labels = new LinkedHashMap<>();
    private final Map<String, SeekBar> seekBars = new LinkedHashMap<>();
    private LinearLayout root;

    private int dp(float v) { 
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f); 
    }

    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        values.put("General", 195); 
        values.put("Red Dot", 190); 
        values.put("2x Scope", 180);
        values.put("4x Scope", 165); 
        values.put("Sniper Scope", 110); 
        values.put("Free Look", 150);
        
        buildUi();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(18,18,22));
        
        root = new LinearLayout(this); 
        root.setOrientation(LinearLayout.VERTICAL); 
        root.setPadding(dp(20), dp(22), dp(20), dp(24));
        scroll.addView(root);
        
        TextView title = text("K13 TURBO SENSI", 26, Color.WHITE, true); 
        root.addView(title);
        
        TextView subtitle = text("OPPO K13 Turbo • 8GB RAM • Headshot profile", 14, Color.LTGRAY, false); 
        subtitle.setPadding(0, 0, 0, dp(18)); 
        root.addView(subtitle);
        
        TextView note = text("এটি শুধু sensitivity সাজিয়ে রাখার অ্যাপ—Aimbot বা Aim Lock নয়।", 14, Color.rgb(255,210,130), false);
        note.setPadding(dp(12), dp(12), dp(12), dp(12)); 
        note.setBackgroundColor(Color.rgb(45,36,24)); 
        root.addView(note);
        
        for (String name : values.keySet()) {
            addSlider(name, values.get(name));
        }
        
        addInfo("DPI", "360 (ফোনের সেটিংসে আলাদাভাবে সেট করতে হয়)");
        addInfo("Fire Button Size", "35% (গেমের HUD-এ সেট করতে হয়)");
        addInfo("Aim Precision", "Default");
        
        Button preset = button("প্রস্তাবিত প্রোফাইল ফিরিয়ে আনুন");
        preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                int[] defaults = {195, 190, 180, 165, 110, 150}; 
                int i = 0; 
                for (String k : values.keySet()) {
                    values.put(k, defaults[i]); 
                    TextView label = labels.get(k);
                    SeekBar seekBar = seekBars.get(k);
                    if (label != null) label.setText(String.valueOf(defaults[i]));
                    if (seekBar != null) seekBar.setProgress(defaults[i]);
                    i++;
                } 
                Toast.makeText(MainActivity.this, "ডিফল্ট সেটিংস রিসেট হয়েছে", Toast.LENGTH_SHORT).show();
            } 
        });
        root.addView(preset);
        
        Button copy = button("সব সেটিংস কপি করুন");
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder b = new StringBuilder("OPPO K13 Turbo Headshot Sensi\nDPI: 360\nFire Button: 35%\nAim Precision: Default\n");
                for (String k : values.keySet()) {
                    b.append(k).append(": ").append(values.get(k)).append('\n');
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("Sensitivity", b.toString()));
                    Toast.makeText(MainActivity.this, "সেটিংস কপি হয়েছে", Toast.LENGTH_SHORT).show();
                }
            }
        }); 
        root.addView(copy);
        
        TextView footer = text("টিপস: Aim ওপরে চলে গেলে drag একটু কমাও; বুকে আটকে গেলে সামান্য বেশি drag করে Training Ground-এ পরীক্ষা করো। কোনো মানই নিশ্চিত headshot দেয় না।", 13, Color.LTGRAY, false);
        footer.setPadding(0, dp(18), 0, 0); 
        root.addView(footer);
        
        setContentView(scroll);
    }

    private void addSlider(final String name, int initial) {
        LinearLayout block = new LinearLayout(this); 
        block.setOrientation(LinearLayout.VERTICAL); 
        block.setPadding(dp(12), dp(10), dp(12), dp(8));
        
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); 
        bp.topMargin = dp(10); 
        block.setLayoutParams(bp); 
        block.setBackgroundColor(Color.rgb(32,32,38));
        
        LinearLayout line = new LinearLayout(this); 
        line.setGravity(Gravity.CENTER_VERTICAL);
        
        TextView n = text(name, 16, Color.WHITE, true); 
        line.addView(n, new LinearLayout.LayoutParams(0, -2, 1));
        
        final TextView val = text(String.valueOf(initial), 17, Color.rgb(255,90,75), true); 
        line.addView(val); 
        labels.put(name, val); 
        block.addView(line);
        
        SeekBar seek = new SeekBar(this); 
        seek.setMax(200); 
        seek.setProgress(initial); 
        seek.setContentDescription(name);
        
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            public void onProgressChanged(SeekBar s, int p, boolean user) {
                values.put(name, p); 
                val.setText(String.valueOf(p));
            } 
            public void onStartTrackingTouch(SeekBar s) {} 
            public void onStopTrackingTouch(SeekBar s) {} 
        });
        
        block.addView(seek); 
        seekBars.put(name, seek);
        root.addView(block);
    }
    
    private void addInfo(String name, String value) { 
        TextView t = text(name + ":  " + value, 14, Color.WHITE, false); 
        t.setPadding(dp(4), dp(12), dp(4), dp(2)); 
        root.addView(t); 
    }
    
    private TextView text(String s, int size, int color, boolean bold) { 
        TextView t = new TextView(this); 
        t.setText(s); 
        t.setTextSize(size); 
        t.setTextColor(color); 
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); 
        return t; 
    }
    
    private Button button(String s) { 
        Button b = new Button(this); 
        b.setText(s); 
        b.setAllCaps(false); 
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); 
        p.topMargin = dp(12); 
        b.setLayoutParams(p); 
        return b; 
    }
}
