package edu.uwlax.toze.objectz;

import java.awt.*;

public class TozeEvent extends AWTEvent
{
    static public final int TOZE_DEL_ME = RESERVED_ID_MAX + 1;
    static public final int TOZE_MENU = RESERVED_ID_MAX + 2;
    static public final int TOZE_SP_FOCUS = RESERVED_ID_MAX + 3;
    static public final int TOZE_CHECK = RESERVED_ID_MAX + 4;
    static public final int TOZE_MOVE_UP = RESERVED_ID_MAX + 5;
    static public final int TOZE_MOVE_DOWN = RESERVED_ID_MAX + 6;

    public TozeEvent(Object eventSource, int eventId)
    {
        super(eventSource, eventId);
    }
}