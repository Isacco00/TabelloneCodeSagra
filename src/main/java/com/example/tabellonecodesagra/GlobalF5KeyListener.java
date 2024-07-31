package com.example.tabellonecodesagra;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class GlobalF5KeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_F5) {
            DatabaseManager dbManager = null;
            try {
                dbManager = new DatabaseManager();
                dbManager.insertNumber(1);
            } finally {
                if (dbManager != null) {
                    dbManager.destroy();
                }
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // Do nothing
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }
}
