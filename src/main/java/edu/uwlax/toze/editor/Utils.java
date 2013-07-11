package edu.uwlax.toze.editor;

import java.util.HashMap;
import java.util.List;

public class Utils
{
    static public void mapRemoveNotNull(HashMap hashMap, Object key)
    {
        if (key != null)
            {
            hashMap.remove(key);
            }
    }

    static public void listMove(List list, Object object, int newIndex)
    {
        list.remove(object);
        list.add(newIndex, object);
    }
}
