/*

Add the following methods:
{=codecheck.selectItem("getTallest - gets the picture with the largest height", "getShortest - gets the picture with the smallest height", "getWidest - gets the picture with the widest width", "getNarrowest - gets the picture with the narrowest width", "getLargest - gets the picture with the largest area (width * height)", "getLargest - gets the picture with the largest extent (width + height)", "getSmallest - gets the picture with the smallest area (width * height)", "getSmallest - gets the picture with the smallest extent (width + height)")}
(You may assume that the gallery has at least one element.)
[8 points]

〈getHeights - returns an int[] array of the height of each picture in the gallery
∣getWidths - returns an int[] array of the width of each picture in the gallery
∣getAreas - returns an int[] array of the area (width * height) of each picture in the gallery
∣getPerimeters - returns an int[] array of the perimeter (2 * (width + height)) of each picture in the gallery
∣getNames - returns a String[] array of the name of each picture in the gallery
 (which you can obtain by calling the getName method of the Picture class).
〉
[8 points]

〈countShort - returns the number of pictures that are < 100 pixels high
∣countTall - returns the number of pictures that are > 100 pixels high
∣countWide - returns the number of pictures that are > 100 pixels wide
∣countNarrow - returns the number of pictures that are < 100 pixels wide
∣countSmall - returns the number of pictures that are small (width + height < 230)
∣countLarge - returns the number of pictures that are large (width + height > 230)
〉
[6 points]

〈removeWide(double w) - removes all pictures with width greater than than w
∣removeNarrow(double w) - removes all pictures with width less than w
∣removeShort(double h) - removes all pictures with height less than h
∣removeTall(double h) - removes all pictures with height greater than h
〉

Actually remove the pictures, don't paint them over. (There is 
no painting in this problem.)

[8 points]

 */

import java.util.ArrayList;

/**
   A gallery of pictures.
*/
public class Problem1
{
    private ArrayList<Picture> pictures;

    /**
        Constructs an empty gallery.
    */
    public Problem1()
    {
        pictures = new ArrayList<Picture>();
    }

    /**
        Adds a picture to the gallery.
        @param filename the name of the picture file
    */
    public void add(String filename)
    {
        pictures.add(new Picture(filename));
    }

    /**
        Gets a string representation of this gallery.
        @return a string describing all pictures
    */
    public String toString()
    {
       return pictures.toString();
    }

}
