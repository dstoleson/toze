package edu.uwlax.toze.editor;


/*
 * Class to manage layout of items within a paragraph.
 */

import java.awt.*;

public class ParaLayout implements LayoutManager
{
   final Placement m_placement;
   
   public ParaLayout(Placement p)
   {
      m_placement = p;
   }

   public void addLayoutComponent(String name, Component comp)
   {
   }
   
   public void layoutContainer(Container parent)
   {
      m_placement.layout();
    }
   
   public Dimension minimumLayoutSize(Container parent)
   {
      Dimension dim = new Dimension(0, 0);
      
      //Always add the container's insets!
      Insets insets = parent.getInsets();
      
      dim.width = insets.left + insets.right;
      dim.height = insets.top + insets.bottom;
      
      return dim;
   }
   
   public Dimension preferredLayoutSize(Container parent)
   {
      Dimension dim = new Dimension(0, 0);

      //Always add the container's insets!
      Insets insets = parent.getInsets();
      dim.width = insets.left + insets.right;
      dim.height = insets.top + insets.bottom;

      return dim;
      
   }
   
   public void removeLayoutComponent(Component comp)
   {
      
   }
}