package com.swordlab.module;

public class Setting {
    public final String name;
    public final boolean isBool;
    public boolean bool;
    public double value, min, max, step = 1;
    public float anim; // GUI animation state

    private Setting(String name, boolean isBool) { this.name = name; this.isBool = isBool; }

    public static Setting bool(String name, boolean def) {
        Setting s = new Setting(name, true);
        s.bool = def;
        s.anim = def ? 1f : 0f;
        return s;
    }

    public static Setting number(String name, double def, double min, double max, double step) {
        Setting s = new Setting(name, false);
        s.min = min; s.max = max; s.step = step; s.value = def;
        return s;
    }

    public void set(double v) {
        v = Math.round(v / step) * step;
        value = Math.max(min, Math.min(max, v));
    }

    public int asInt() { return (int) Math.round(value); }
}
