//HIDE
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

/**
    A picture from an image file.
*/
public class Picture implements Shape
{
    private BufferedImage image;
    private JLabel label = new JLabel();
    private String source;
    private double x;
    private double y;
    private double xGrow;
    private double yGrow;

   /**
      Gets the name of this picture.
      @return the filename or URL from which picture was loaded,
      or "" if it was not loaded from anywhere.
   */
   public String getName()
   {
      if (source == null) { return ""; } else { return source; }
   }

    /**
        Constructs a picture with no image.
    */
    public Picture()
    {
    }

    /**
        Constructs a picture with a given width and height.
        @param width the desired width
        @param height the desired height
    */
    public Picture(double width, double height)
    {
        image = new BufferedImage((int) Math.round(width),
                                  (int) Math.round(height), BufferedImage.TYPE_INT_ARGB);
        label.setIcon(new ImageIcon(image));
        label.setText("");
    }

    /**
        Constructs a picture from a given file or URL.
        @param source the filename or URL
    */
    public Picture(String source)
    {
        load(source);
    }

    /**
        Loads a new picture from a given file or URL.

        @param source the filename or URL
    */
    public void load(String source)
    {
        try
        {
            this.source = source;
            BufferedImage loadedImage;
            if (source.startsWith("http://"))
                loadedImage = ImageIO.read(new URL(source).openStream());
            else
                loadedImage = ImageIO.read(new File(source));
            image = new BufferedImage(loadedImage.getWidth(),
                                      loadedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < image.getWidth(); i++)
                for (int j = 0; j < image.getHeight(); j++)
                    image.setRGB(i, j, loadedImage.getRGB(i, j));

            label.setIcon(new ImageIcon(image));
            label.setText("");
        }
        catch (Exception ex)
        {
            image = null;
            label.setIcon(null);
            ex.printStackTrace();
        }
        Canvas.getInstance().repaint();
    }

    /**
        Gets the leftmost x-position of the shape.
        @return the leftmost x-position
    */
    public int getX()
    {
        return (int) Math.round(x - xGrow);
    }

    /**
        Gets the topmost y-position of the shape.
        @return the topmost y-position
    */
    public int getY()
    {
        return (int) Math.round(y - yGrow);
    }

    /**
        Gets the rightmost x-position of the shape.
        @return the rightmost x-position
    */
    public int getMaxX()
    {
        return getX() + getWidth();
    }

    /**
        Gets the bottommost y-position of the shape.
        @return the bottommost y-position
    */
    public int getMaxY()
    {
        return getY() + getHeight();
    }

    /**
        Gets the width of this picture.
    */
    public int getWidth()
    {
        return (int) Math.round(
                   (image == null ? 0 : image.getWidth()) + 2 * xGrow);
    }

    /**
        Gets the height of this picture.
    */
    public int getHeight()
    {
        return (int) Math.round(
                   (image == null ? 0 : image.getHeight()) + 2 * yGrow);
    }

    /**
        The number of pixels in this picture.
        @return the number of pixels
    */
    public int pixels()
    {
        if (image == null)
        {
            return 0;
        }
        else
        {
            return image.getWidth() * image.getHeight();
        }
    }

    /**
        Gets a two-dimensional array of the gray levels of all pixels
        in this picture.
        @return an array of gray levels (between 0 and 255) for each pixel
    */
    public int[][] getGrayLevels()
    {
        if (image == null) return new int[0][0];
        int[][] grayLevels = new int[getHeight()][getWidth()];

        for (int i = 0; i < grayLevels.length; i++)
            for (int j = 0; j < grayLevels[i].length; j++)
            {
                int rgb = image.getRGB(j, i);
                // Use NTSC/PAL algorithm to convert RGB to gray level
                grayLevels[i][j] = (int)(0.2989 * ((rgb >> 16) & 0xFF) + 0.5866 * ((rgb >> 8) & 0xFF) + 0.1144 * (rgb & 0xFF));
            }
        return grayLevels;
    }

    /**
        Constructs a picture from an array of gray levels.
        @param an array of gray levels (between 0 and 255) for each pixel
    */
    public Picture(int[][] grayLevels)
    {
        image = new BufferedImage(grayLevels[0].length, grayLevels.length, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++)
            {
                int gray = grayLevels[j][i];
                if (gray < 0) gray = 0;
                if (gray > 255) gray = 255;
                int rgb = gray * (65536 + 256 + 1);
                image.setRGB(i, j, rgb + 0xFF000000);
            }
        label.setIcon(new ImageIcon(image));
        label.setText("");
    }

    /**
        Tints this picture with the given color.
        @param c the color to tint with
    */
    public void tint(Color c)
    {
        if (image == null) return;
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++)
            {
                int rgb = image.getRGB(i, j);
                // Use NTSC/PAL algorithm to convert RGB to gray level
                double gray = (0.2989 * ((rgb >> 16) & 0xFF) + 0.5866 * ((rgb >> 8) & 0xFF) + 0.1144 * (rgb & 0xFF)) / 255;
                int r = (int) (c.getRed() * gray);
                int g = (int) (c.getGreen() * gray);
                int b = (int) (c.getBlue() * gray);
                image.setRGB(i, j, (rgb & 0xFF000000) + r * 65536 + g * 256 + b);
            }
        Canvas.getInstance().repaint();
    }

    /**
        Turns this picture by a given number of 90 degree turns.
        @param turns the number of times to turn to the right
    */
    public void turn(int turns)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        turns = (turns % 4 + 4) % 4; // in case turns is < 0
        if (turns == 0) return;
        if (turns == 2)
        {
            for (int i = 0; i < width / 2; i++)
                for (int j = 0; j < height; j++)
                {
                    int rgb = image.getRGB(i, j);
                    int i2 = width - 1 - i;
                    int j2 = height - 1 - j;
                    int rgb2 = image.getRGB(i2, j2);
                    image.setRGB(i2, j2, rgb);
                    image.setRGB(i, j, rgb2);
                }
            if (width % 2 == 1)
            {
                int i = width / 2;
                for (int j = 0; j < height / 2; j++)
                {
                    int rgb = image.getRGB(i, j);
                    int j2 = height - 1 - j;
                    int rgb2 = image.getRGB(i, j2);
                    image.setRGB(i, j2, rgb);
                    image.setRGB(i, j, rgb2);
                }
            }
        }
        else
        {
            BufferedImage newImage = new BufferedImage(height,
                    width, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                {
                    int rgb = image.getRGB(i, j);
                    if (turns == 1)
                        newImage.setRGB(height - 1 - j, i, rgb);
                    else
                        newImage.setRGB(j, width - 1 - i, rgb);
                }
            image = newImage;
            label.setIcon(new ImageIcon(newImage));
        }
        Canvas.getInstance().repaint();
    }

    public String toString()
    {
        return "Picture[x=" + getX() + ",y=" + getY() + ",width=" + getWidth() + ",height=" + getHeight() + ",source=" + source + "]";
    }

    /**
        Gets the color of a pixel.
        @param i the pixel index
        @return the color at pixel i
    */
    public Color getColorAt(int i)
    {
        if (image == null || i < 0 || i >= pixels())
        {
            throw new IndexOutOfBoundsException("" + i);
        }
        else
        {
            return getColorAt(i % image.getWidth(), i / image.getWidth());
        }
    }

    /**
        Sets the color of a pixel.
        @param i the pixel index
        @param color the new color for the pixel
    */
    public void setColorAt(int i, Color color)
    {
        if (image == null || i < 0 || i >= pixels())
        {
            throw new IndexOutOfBoundsException("" + i);
        }
        else
        {
            setColorAt(i % image.getWidth(), i / image.getWidth(), color);
        }
    }

    /**
        Gets the color of a pixel.
        @param x the x-coordinate (column) of the pixel
        @param y the y-coordinate (row) of the pixel
        @param color the new color for the pixel
    */
    public Color getColorAt(int x, int y)
    {
        if (image == null || x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
        {
            throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
        else
        {
            int rgb = image.getRGB(x, y) & 0xFFFFFF;
            return new Color(rgb / 65536, (rgb / 256) % 256, rgb % 256);
        }
    }

    /**
        Sets the color of a pixel.
        @param x the x-coordinate (column) of the pixel
        @param y the y-coordinate (row) of the pixel
        @param the color of the pixel at the given row and column
    */
    public void setColorAt(int x, int y, Color color)
    {
        if (image == null || x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
        {
            throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
        else
        {
            image.setRGB(x, y, ((int) color.getRed()) * 65536 + ((int) color.getGreen()) * 256 + (int) color.getBlue() + 0xFF000000);
            Canvas.getInstance().repaint();
        }
    }

    /**
        Moves this picture by a given amount.
        @param dx the amount by which to move in x-direction
        @param dy the amount by which to move in y-direction
    */
    public void translate(double dx, double dy)
    {
        x += dx;
        y += dy;
        Canvas.getInstance().repaint();
    }

    /**
        Resizes this picture both horizontally and vertically.
        @param dw the amount by which to resize the width on each side
        @param dw the amount by which to resize the height on each side
    */
    public void grow(double dw, double dh)
    {
        xGrow += dw;
        yGrow += dh;
        Canvas.getInstance().repaint();
    }

    /**
        Shows this picture on the canvas.
    */
    public void draw()
    {
        Canvas.getInstance().show(this);
    }

    /**
        Draws this shape.
        @param g2 the graphics context
    */
    public void paintShape(Graphics2D g2)
    {
        if (image != null)
        {
            Dimension dim = label.getPreferredSize();
            if (dim.width > 0 && dim.height > 0)
            {
                label.setBounds(0, 0, dim.width, dim.height);
                g2.translate(getX(), getY());
                g2.scale((image.getWidth() + 2 * xGrow) / dim.width,
                         (image.getHeight() + 2 * yGrow) / dim.height);
                label.paint(g2);
            }
        }
    }
}
