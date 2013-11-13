package edu.uwlax.toze.editor.bindings;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Bind a property in a POJO to other objects for updates.
 */
public class Binding<T, V>
{
    /**
     * The source or model object that provides the values
     */
    private final T source;

    /**
     * The source property to update with new values
     */
    private final String property;

    public Binding(T source, String property)
    {
        this.source = source;
        this.property = property;
    }

    /**
     * Set the value on the source object.
     *
     * @param value The value to set on the source object.
     */
    public void setValue(V value)
    {
        try
            {
            BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor foundProperty = null;

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
                {
                if (propertyDescriptor.getName().equals(this.property))
                    {
                    foundProperty = propertyDescriptor;
                    }
                }
            if (foundProperty != null)
                {
                foundProperty.getWriteMethod().invoke(source, value);
                }
            }
        catch (InvocationTargetException e)
            {
            System.out.println("Problem calling method for: " + ((source != null) ? source.getClass() : null));
            }
        catch (IllegalAccessException e)
            {
            System.out.println("Problem calling method for: " + ((source != null) ? source.getClass() : null));
            }
        catch (IntrospectionException e)
            {
            System.out.println("Problem getting BeanInfo for: " + ((source != null) ? source.getClass() : null));
            }
    }
}
