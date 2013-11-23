package edu.uwlax.toze.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.List;


/*
 * Print an image.
 *
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing.html
 */

public class PrintUtilities implements Printable
{
    private BufferedImage img;
    private List<Integer> pageBreaks;
    private double scale = 1.0;

    private int nextPageBreakIndex = 0;
    private int totalPrintedHeight = 0;

    // need to keep track in case called with
    // same page twice and have to adjust
    // which area of the image to print
    private int lastPagePrinted = -1;
    private int lastPrintedHeight = 0;
    private int lastPageBreakIndex = 0;


    public static boolean checkPageWidth(int width)
    {
        return (width <= PrinterJob.getPrinterJob().defaultPage().getWidth());
    }

    public static void printImage(BufferedImage img, List<Integer> pageBreaks)
    {
        new PrintUtilities(img, pageBreaks).print();
    }

    public PrintUtilities(BufferedImage img, List<Integer> pageBreaks)
    {
        this.img = img;
        this.pageBreaks = pageBreaks;
    }

    public void print()
    {
        // Ask the user for the page format they prefer
        PageFormat pageFormat = PrinterJob.getPrinterJob().pageDialog(PrinterJob.getPrinterJob().defaultPage());

        // check to see that the page fits
        // it not warn user and they can print at a smaller scale
        boolean shouldPrint = PrintUtilities.checkPageWidth(img.getWidth());

        int state = JOptionPane.YES_OPTION;

        if (!shouldPrint)
            {
            state = JOptionPane.showConfirmDialog(new JFrame(),
                                                  "The specification is to wide for the page to be printed.  Would you like to scale specification (75%) to fit the page?",
                                                  "Scale and Print", JOptionPane.OK_CANCEL_OPTION
            );
            if (state == JOptionPane.YES_OPTION)
                {
                scale = 0.75;
                }
            }

        if (state == JOptionPane.YES_OPTION)
            {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(this, pageFormat);
            if (printJob.printDialog())
                {
                try
                    {
                    printJob.print();
                    }
                catch (Throwable e)
                    {
                    System.out.println("Error printing: " + e);
                    }
                }
            }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    {
        if (lastPagePrinted == pageIndex)
            {
            totalPrintedHeight -= lastPrintedHeight;
            nextPageBreakIndex = lastPageBreakIndex;
            }

        int imageHeight = img.getHeight();
        int imageWidth = img.getWidth();

        // get printable page boundaries
        double imageableX = pageFormat.getImageableX();
        double imageableY = pageFormat.getImageableY();
        double imageableHeight = pageFormat.getImageableHeight();
        double imageableWidth = pageFormat.getImageableWidth();

        // don't print anything past the total height of
        // the image to be printed (no extra pages)
        // need to compensate for the last page
        int lastPageBreak = pageBreaks.get(pageBreaks.size() - 1);

        if (totalPrintedHeight > lastPageBreak)
            {
            return NO_SUCH_PAGE;
            }

        // shift Graphics to printable region
        // of the page
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(imageableX, imageableY);

        // make it a reasonable printed size
        g2d.scale(scale, scale);

        // adjust the printed width for the scaling
        int printWidth = (int) (imageableWidth * (1 / scale));

        // adjust for scaling and
        // don't go beyond the bounds of the source image
        // throws a RasterFormatException
        if (printWidth > imageWidth)
            {
            printWidth = imageWidth;
            }

        // get the remaining scaled height to print
        int printHeight = (int) (imageableHeight * (1 / scale));

        // don't beyond the end of the image
        if (printHeight > imageHeight - totalPrintedHeight)
            {
            printHeight = imageHeight - totalPrintedHeight;
            }

        // keep this around
        lastPageBreakIndex = nextPageBreakIndex;

        // figure out where to break the page
        int pageBreak = 0;

        for (; nextPageBreakIndex < pageBreaks.size(); nextPageBreakIndex++)
            {
            int tmpPageBreak = pageBreaks.get(nextPageBreakIndex);

            // break the page when the next page break
            // is greater than the previous printed page plus
            // the current printed page
            if (tmpPageBreak > printHeight + totalPrintedHeight)
                {
                break;
                }
            pageBreak = tmpPageBreak;
            }

        if (pageBreak != 0)
            {
            // only print from the total printed height so
            // far up to the page break
            // add 1 to the printHeight so that it prints 1 past the existing page break
            printHeight = pageBreak - totalPrintedHeight + 1;
            }

        // draw the scaled subimage that fits into the printable
        // region of the page
        BufferedImage subImage = img.getSubimage(0, totalPrintedHeight, printWidth, printHeight);
        g.drawImage(subImage, subImage.getMinX(), subImage.getMinY(), subImage.getWidth(), subImage.getHeight(), null);

        // increment the height of the amount of the scaled image
        // that has been printed
        lastPrintedHeight = printHeight;
        totalPrintedHeight += printHeight;
        lastPagePrinted = pageIndex;

        return PAGE_EXISTS;
    }
}
