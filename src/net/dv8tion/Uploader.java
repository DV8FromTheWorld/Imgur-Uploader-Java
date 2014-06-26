package net.dv8tion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class Uploader
{
  //CHANGE TO @CLIENT_ID@ and replace with buildscript.
    private final static String CLIENT_ID = "efce6070269a7f1";

    //CHANGE TO @CLIENT_SECRET@ and replace with buildscript.
    private final static String CLIENT_SECRET = "bc038b940212ad28e0de3eddea1a18375434b143";

    public static String upload(File file)
    {
        HttpURLConnection conn = getHttpConnection("https://api.imgur.com/3/image");
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }

    public static String createAlbum(List<String> imageIds)
    {
        HttpURLConnection conn = getHttpConnection("https://api.imgur.com/3/album");
        String ids = "";
        for (String id : imageIds)
        {
            if (!ids.equals(""))
            {
                ids += ",";
            }
            ids += id;
        }
        writeToConnection(conn, "ids=" + ids);
        return getResponse(conn);
    }
    
    private static String toBase64(File file)
    {
        try
        {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private static HttpURLConnection getHttpConnection(String url)
    {
        HttpURLConnection conn;
        try
        {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void writeToConnection(HttpURLConnection conn, String message)
    {
        OutputStreamWriter writer;
        try
        {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static String getResponse(HttpURLConnection conn)
    {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                str.append(line);
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return str.toString();
    }
}
