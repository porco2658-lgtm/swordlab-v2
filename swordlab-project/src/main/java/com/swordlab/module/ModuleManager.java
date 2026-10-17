package com.swordlab.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class ModuleManager {
    public static final List<Module> ALL = new ArrayList<>();

    private ModuleManager() {}

    public static void init() {
        ALL.add(new TriggerBot());
        ALL.add(new SilentAim());
        ALL.add(new JumpReset());
    }

    public static List<Module> in(Category c) {
        List<Module> out = new ArrayList<>();
        for (Module m : ALL) if (m.category == c) out.add(m);
        return out;
    }

    public static void tick(Minecraft mc) {
        for (Module m : ALL) if (m.enabled) m.onTick(mc);
    }
}
