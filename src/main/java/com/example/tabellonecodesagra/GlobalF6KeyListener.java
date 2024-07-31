package com.example.tabellonecodesagra;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class GlobalF6KeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
            DatabaseManager dbManager = null;
            try {
                dbManager = new DatabaseManager();
                dbManager.insertNumber(2);
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
