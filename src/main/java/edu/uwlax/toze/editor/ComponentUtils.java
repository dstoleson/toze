package edu.uwlax.toze.editor;

import java.awt.Component;
import java.awt.Container;

public class ComponentUtils
{
    /**
     *
     * @param component
     *
     * @return
     */
    public static int getIndexOfComponent(Component component)
    {
        if (component != null && component.getParent() != null)
            {
            Container c = component.getParent();
            for (int i = 0; i < c.getComponentCount(); i++)
                {
                if (c.getComponent(i) == component)
                    {
                    return i;
                    }
                }
            }

        return -1;
    }
}
