package edu.uwlax.toze.editor;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


/*
 * Print an image.
 *
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing.html
 */

public class PrintUtilities implements Printable
{
    public final BufferedImage img;
    public int numPages = 0;

    public static void printImage(BufferedImage img)
    {
        new PrintUtilities(img).print();
    }

    public PrintUtilities(BufferedImage img)
    {
        this.img = img;
    }

    public void print()
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog())
            {
            try
                {
                printJob.print();
                }
            catch (PrinterException pe)
                {
                System.out.println("Error printing: " + pe);
                }
            }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    {
        Rectangle r = g.getClipBounds();

        int w = r.width;
        int h = r.height;
        int iw = img.getWidth();
        int ih = img.getHeight();

        // how many times does the image width fit onto the page
        double ratio = (double) w / (double) iw;
        //  number of image pixels at this scale.
        double sh = (double) ih * ratio;

        double numpages = (float) r.height / (float) sh;

        int ty = (int) ((float) h / ratio) * pageIndex;
        int th = (int) ((float) h / ratio);
        int oth = th;
        if (ty > ih)
            {
            return NO_SUCH_PAGE;
            }
        if ((ty + th) > ih)
            {
            th = ih - ty;
            }
        g.drawImage(img.getSubimage(0,
                                    ty,
                                    iw,
                                    th),
                    r.x,
                    r.y,
                    r.width,
                    (int) ((float) r.height * ((float) th / (float) oth)),
                    null);

        numPages++;
        return (PAGE_EXISTS);
    }
}
