//HIDE
//OUT canvas.png
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Canvas
{
    private static Canvas canvas = new Canvas();

    private ConcurrentLinkedQueue<Shape> shapes = new ConcurrentLinkedQueue<Shape>();
    private BufferedImage background;
    private JFrame frame;
    private CanvasComponent component;

    private static final int MIN_SIZE = 100;
    private static final int MARGIN = 10;
    private static final int LOCATION_OFFSET = 120;

    class CanvasComponent extends JComponent
    {
        public void paintComponent(Graphics g)
        {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(java.awt.Color.BLACK);
            if (background != null)
            {
                g.drawImage(background, 0, 0, null);
            }
            for (Shape s : shapes)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                s.paintShape(g2);
                g2.dispose();
            }
        }

        public Dimension getPreferredSize()
        {
            int maxx = MIN_SIZE;
            int maxy = MIN_SIZE;
            if (background != null)
            {
                maxx = Math.max(maxx, background.getWidth() - MARGIN);
                maxy = Math.max(maxy, background.getHeight() - MARGIN);
            }
            for (Shape s : shapes)
            {
                maxx = (int) Math.max(maxx, s.getX() + s.getWidth());
                maxy = (int) Math.max(maxy, s.getY() + s.getHeight());
            }
            return new Dimension(maxx + MARGIN, maxy + MARGIN);
        }
    }

    private Canvas()
    {
        component = new CanvasComponent();

        if (System.getProperty("com.horstmann.codecheck") == null)
        {
            frame = new JFrame();
            frame.setDefaultCloseOperation(System.getProperty("java.class.path").contains("bluej") ? JFrame.HIDE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
            frame.add(component);
            frame.pack();
            frame.setLocation(LOCATION_OFFSET, LOCATION_OFFSET);
            frame.setVisible(true);
        }
        else
        {
            final String SAVEFILE ="canvas.png";
            final Thread currentThread = Thread.currentThread();
            Thread watcherThread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        final int DELAY = 10;

                        while (currentThread.getState() != Thread.State.TERMINATED)
                        {
                            Thread.sleep(DELAY);
                        }
                        saveToDisk(SAVEFILE);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            };
            watcherThread.start();
        }
    }

    /**
        Gets the unique instance of this class.
        @return the unique Canvas instance
    */
    public static Canvas getInstance()
    {
        return canvas;
    }

    /**
        Adds a shape to this canvas.
        @param s the shape to show
    */
    public void show(Shape s)
    {
        if (!shapes.contains(s))
        {
            shapes.add(s);
        }
        repaint();
    }

    /**
        Removes a shape from this canvas.
        @param s the shape to remove
    */
    public void remove(Shape s)
    {
        if (shapes.remove(s))
        {
            getInstance().repaint();
        }
    }

    /**
        Repaints this canvas.
    */
    public void repaint()
    {
        if (frame == null) return;
        Dimension dim = component.getPreferredSize();
        frame.setVisible(true); 
        frame.toFront();
        if (dim.getWidth() > component.getWidth()
                || dim.getHeight() > component.getHeight())
        {
            frame.pack();
        }
        else
        {
            frame.repaint();
        }
    }

    /**
        Pauses so that the user can see the canvas before it is transformed.
    */
    public static void pause()
    {
        if (System.getProperty("com.horstmann.codecheck") != null) return;
        JFrame frame = getInstance().frame;
        if (frame == null) return;
        JOptionPane.showMessageDialog(frame, "Click Ok to continue");
    }

    /**
        Pauses by a given delay so that the user can see the canvas before it is transformed.
        @param millis the length of the delay in milliseconds
    */
    public static void delay(int millis)
    {
        if (System.getProperty("com.horstmann.codecheck") != null) return;
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
        Takes a snapshot of the canvas, fades it, and sets it as the background.
    */
    public static void snapshot()
    {
        delay(750);
        Dimension dim = getInstance().component.getPreferredSize();
        java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, dim.width, dim.height);
        BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, rect.width, rect.height);
        g.setColor(java.awt.Color.BLACK);
        getInstance().component.paintComponent(g);
        float factor = 0.8f;
        float base = 255f * (1f - factor);
        RescaleOp op = new RescaleOp(factor, base, null);
        BufferedImage filteredImage
            = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        op.filter(image, filteredImage);
        getInstance().background = filteredImage;
        getInstance().component.repaint();
    }

    /**
        Saves this canvas to an image file.
        @param fileName the name of the file
    */
    public void saveToDisk(String fileName)
    {
        Dimension dim = component.getPreferredSize();
        java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, dim.width, dim.height);
        BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fill(rect);
        g.setColor(java.awt.Color.BLACK);
        component.paintComponent(g);
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        try
        {
            ImageIO.write(image, extension, new File(fileName));
        }
        catch(IOException e)
        {
            System.err.println("Was unable to save the image to " + fileName);
        }
        g.dispose();
    }

    /**
        Gets all shapes in this canvas.
        @return an array of all shapes in this canvas
    */
    public Shape[] getShapes()
    {
       return shapes.toArray(new Shape[0]);
    }
}
