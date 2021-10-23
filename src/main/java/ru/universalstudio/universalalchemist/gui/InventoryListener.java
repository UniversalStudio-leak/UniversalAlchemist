package ru.universalstudio.universalalchemist.gui;

import java.util.function.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class InventoryListener<T> {

    private Class<T> type;
    private Consumer<T> consumer;

    public InventoryListener(Class<T> type, Consumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public void accept(T t) { consumer.accept(t); }

    public Class<T> getType() { return type; }

}
