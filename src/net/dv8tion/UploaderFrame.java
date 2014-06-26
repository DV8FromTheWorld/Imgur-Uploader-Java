package net.dv8tion;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UploaderFrame
{
    public static void main(String[] args) throws IOException
    {
        List<String> imageIds = new LinkedList<String>();
        imageIds.add("0AJX5Kc");
        imageIds.add("1pvAMrc");
        imageIds.add("T7FIUlT");
        System.out.println(Uploader.createAlbum(imageIds));        
    }
}