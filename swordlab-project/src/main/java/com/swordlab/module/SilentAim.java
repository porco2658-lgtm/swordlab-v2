package com.swordlab.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/** Server-side-only aim assist: the camera never moves, only the rotation in outgoing packets. */
public class SilentAim extends Module {
    private final Setting range = add(Setting.number("Range", 4.2, 1, 6, 0.1));
    private final Setting fov = add(Setting.number("FOV", 90, 10, 360, 5));
    private final Setting speed = add(Setting.number("Max speed (deg/tick)", 25, 1, 180, 1));
    private final Setting height = add(Setting.number("Aim height %", 70, 10, 100, 5));
    private final Setting gcd = add(Setting.bool("Sensitivity GCD snap", true));
    private boolean has;
    private float lastYaw, lastPitch;

    public SilentAim() {
        super("Silent Aim Assist", "Rotates only the packets toward the nearest player", Category.SWORD);
    }

    @Override
    public void onDisable() { AimState.active = false; has = false; }

    @Override
    public void onTick(Minecraft mc) {
        AimState.active = false;
        LocalPlayer p = mc.player;
        if (p == null || mc.level == null || !holdingSword(mc)) { has = false; return; }

        Player best = null;
        float bestDiff = Float.MAX_VALUE;
        for (Player o : mc.level.players()) {
            if (o == p || o.isSpectator() || !o.isAlive() || p.distanceTo(o) > range.value) continue;
            float diff = Math.abs(Mth.wrapDegrees(rotations(p, o)[0] - p.getYRot()));
            if (diff > fov.value / 2.0 || diff >= bestDiff) continue;
            best = o; bestDiff = diff;
        }
        if (best == null) { has = false; return; }

        float[] target = rotations(p, best);
        float cy = has ? lastYaw : p.getYRot();
        float cp = has ? lastPitch : p.getXRot();
        float max = (float) speed.value;
        float dy = Mth.clamp(Mth.wrapDegrees(target[0] - cy), -max, max);
        float dp = Mth.clamp(target[1] - cp, -max, max);

        if (gcd.bool) {
            double f = mc.options.sensitivity().get() * 0.6 + 0.2;
            double step = f * f * f * 8.0 * 0.15;
            dy = (float) (Math.round(dy / step) * step);
            dp = (float) (Math.round(dp / step) * step);
        }

        lastYaw = cy + dy;
        lastPitch = Mth.clamp(cp + dp, -90f, 90f);
        has = true;
        AimState.yaw = lastYaw;
        AimState.pitch = lastPitch;
        AimState.active = true;
    }

    private float[] rotations(LocalPlayer p, Player o) {
        Vec3 eye = p.getEyePosition();
        double dx = o.getX() - eye.x;
        double dy = o.getY() + o.getBbHeight() * (height.value / 100.0) - eye.y;
        double dz = o.getZ() - eye.z;
        double h = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, h));
        return new float[]{yaw, pitch};
    }
}
