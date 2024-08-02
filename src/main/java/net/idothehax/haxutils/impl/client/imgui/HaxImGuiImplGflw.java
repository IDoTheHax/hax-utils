package net.idothehax.haxutils.impl.client.imgui;

import imgui.glfw.ImGuiImplGlfw;
import net.idothehax.haxutils.HaxUtils;

public class HaxImGuiImplGflw extends ImGuiImplGlfw {
    @Override
    public void scrollCallback(long windowId, double xOffset, double yOffset) {
        try {
            HaxUtils.beginImGui();
            super.scrollCallback(windowId, xOffset, yOffset);
        } finally {
            HaxUtils.endImGui();
        }
    }

    @Override
    public void keyCallback(long windowId, int key, int scancode, int action, int mods) {
        try {
            HaxUtils.beginImGui();
            super.keyCallback(windowId, key, scancode, action, mods);
        } finally {
            HaxUtils.endImGui();
        }
    }

    @Override
    public void windowFocusCallback(long windowId, boolean focused) {
        try {
            HaxUtils.beginImGui();
            super.windowFocusCallback(windowId, focused);
        } finally {
            HaxUtils.endImGui();
        }
    }

    @Override
    public void charCallback(long windowId, int c) {
        try {
            HaxUtils.beginImGui();
            super.charCallback(windowId, c);
        } finally {
            HaxUtils.endImGui();
        }
    }
}
