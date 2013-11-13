package edu.uwlax.toze.editor;

import java.util.List;

public class Utils
{
    static public void listMove(List list, Object object, int newIndex)
    {
        list.remove(object);
        list.add(newIndex, object);
    }
}
