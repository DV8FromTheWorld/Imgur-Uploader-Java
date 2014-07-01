/**
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Core class of the program.  Controls the central GUI and core logic.
 * 
 * @author DV8FromTheWorld (Austin Keener)
 * @version Incomplete June 30, 2014
 */
@SuppressWarnings("serial")
public class UploaderFrame extends JFrame implements ActionListener
{
    /**
     * Main entry point of the program.
     * 
     * @param args
     *          Command Line Arguments.
     */
    public static void main(String[] args)
    {
        UploaderFrame f = new UploaderFrame();
        f.setVisible(true);
        f.imageIds.add("0AJX5Kc");
        f.imageIds.add("1pvAMrc");
        f.imageIds.add("T7FIUlT");
        SwingWorker s = f.getAlbumWorker();
        s.run();
    }
    
    private LinkedList<File> imagesToUpload;
    private LinkedList<String> imageIds;
    private String url;
    private boolean uploading;
    
    public final int SIZE_GUI_X = 290;
    public final int SIZE_GUI_Y = 290;
    public final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private final int BUTTON_HEIGHT = 20;
    private final Insets MARGIN = new Insets(0, 0, 0, 0);
    private final String UPLOAD_MESSAGE =
            "Upload and Preview Buttons are disabled\nuntil an image is in the Clipboard.";
    
    private JPanel panel;
    
    private JButton btnUpload;
    private JButton btnPreview;
    private JButton btnCustomCapture;
    private JButton btnOpenBrowser;
    private JButton btnCopyLink;
    
    private JTextArea lblLink;
    private JLabel lblTitle;
    private JTextPane lblUploadMessage;    

    /**
     * Creates a new UploaderFrame GUI, completely setup.
     * Does not call .setVisible(true), this will need to be done after creation.
     */
    public UploaderFrame()
    {
        initVisualComponents();
        imagesToUpload = new LinkedList<File>();
        imageIds = new LinkedList<String>();
        uploading = false;
    }

    /**
     * Controls what happens when any of the buttons are pressed.
     * 
     * @param e
     *          The event of the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == btnUpload)
        {
            upload();
        }
        else if (source == btnPreview)
        {
            
        }
        else if (source == btnCustomCapture)
        {
            
        }
        else if (source == btnOpenBrowser)
        {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try
                {
                    desktop.browse(new URI(url));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        else if (source == btnCopyLink)
        {
            Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(url), null);
            uploadButtonStatus();
        }
    }
    
    /**
     * Initializes all visual components of the GUI and adds them to the GUI.
     * Also sets the GUI's settings.
     */
    private void initVisualComponents()
    {
        this.setTitle("Imgur Uploader");
        this.setSize(SIZE_GUI_X, SIZE_GUI_Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        panel = new JPanel();
        panel.setLayout(null);
        
        btnUpload = new JButton();
        btnUpload.setText("Upload");
        btnUpload.setFont(FONT);
        btnUpload.setMargin(MARGIN);
        btnUpload.setLocation(93, 39);
        btnUpload.setSize(104, 38);
        btnUpload.addActionListener(this);
        
        btnPreview = new JButton();
        btnPreview.setText("Preview");
        btnPreview.setFont(FONT);
        btnPreview.setMargin(MARGIN);
        btnPreview.setLocation(110, 15);
        btnPreview.setSize(70, BUTTON_HEIGHT);
        btnPreview.addActionListener(this);
        
        btnCustomCapture = new JButton();
        btnCustomCapture.setText("Custom Screen Capture");
        btnCustomCapture.setFont(FONT);
        btnCustomCapture.setMargin(MARGIN);
        btnCustomCapture.setLocation(74, 130);
        btnCustomCapture.setSize(140, BUTTON_HEIGHT);
        btnCustomCapture.addActionListener(this);
        
        btnOpenBrowser = new JButton();
        btnOpenBrowser.setText("Open in Browser");
        btnOpenBrowser.setFont(FONT);
        btnOpenBrowser.setMargin(MARGIN);
        btnOpenBrowser.setLocation(15, 208);
        btnOpenBrowser.setSize(100, BUTTON_HEIGHT);
        btnOpenBrowser.addActionListener(this);
        btnOpenBrowser.setEnabled(false);
        
        btnCopyLink = new JButton();
        btnCopyLink.setText("Copy Link");
        btnCopyLink.setFont(FONT);
        btnCopyLink.setMargin(MARGIN);
        btnCopyLink.setLocation(152, 208);
        btnCopyLink.setSize(100, BUTTON_HEIGHT);
        btnCopyLink.addActionListener(this);
        btnCopyLink.setEnabled(false);
        
        lblLink = new JTextArea();
        lblLink.setText("NO CURRENT LINK");
        lblLink.setFont(FONT);
        lblLink.setLocation(88, 160);
        lblLink.setSize(190, 40);
        lblLink.setBackground(null);
        lblLink.setEditable(false);
        lblLink.setWrapStyleWord(true);
        lblLink.setLineWrap(true);
        
        lblTitle = new JLabel();
        lblTitle.setText("Imgur Link:");
        lblTitle.setFont(FONT);
        lblTitle.setLocation(15, 157);
        lblTitle.setSize(65, 20);
        
        lblUploadMessage = new JTextPane();
        lblUploadMessage.setText(UPLOAD_MESSAGE);
        lblUploadMessage.setLocation(25, 80);
        lblUploadMessage.setSize(235, 35);
        lblUploadMessage.setBackground(null);
        lblUploadMessage.setEditable(false);
        setupTextCentering(lblUploadMessage);
        
        panel.add(btnUpload);
        panel.add(btnPreview);
        panel.add(btnCustomCapture);
        panel.add(btnOpenBrowser);
        panel.add(btnCopyLink);
        panel.add(lblLink);
        panel.add(lblTitle);
        panel.add(lblUploadMessage);
        this.add(panel);
    }
    
    private void upload()
    {
        //LoadImages();
        if (imagesToUpload.size() > 1)
        {
            lblLink.setText("Uploading " + imagesToUpload.size() + " images..."
                + " Completed: 0%");
            getAlbumWorker().run();
        }
        else
        {
            lblLink.setText("Uploading and fetching URL...");
            getUploadWorker().run();
        }
        btnUpload.setEnabled(false);
        btnPreview.setEnabled(false);
        //menuUpload.setEnabled(false);
        btnOpenBrowser.setEnabled(false);
        btnCopyLink.setEnabled(false);
    }
    
    /**
     * Updates the Upload and Preview button statuses.
     */
    private void uploadButtonStatus()
    {
        if (!uploading)
        {
            //if (ClipboardContainsImage())
            {
                btnUpload.setEnabled(true);
                //menuUpload.setEnabled(true);
                btnPreview.setEnabled(true);
                lblUploadMessage.setText("");
            }
            //else
            {
                btnUpload.setEnabled(false);
                //menuUpload.setEnabled(false;
                btnPreview.setEnabled(false);
                lblUploadMessage.setText(UPLOAD_MESSAGE);
            }
        }
    }
    
    /**
     * Displays the link generated from the upload.
     * Enables the OpenBrowser and CopyLink buttons.
     */
    private void uploadComplete()
    {
        uploading = false;
        lblLink.setText(url);
        uploadButtonStatus();
        btnOpenBrowser.setEnabled(true);
        btnCopyLink.setEnabled(true);
        //ClearImages();
    }
    
    /**
     * Creates a new instance of a Swing Worker for Image uploading.
     * 
     * @return
     *          A new instance of the ImageUpload Swing worker.
     */
    private SwingWorker getUploadWorker()
    {
        return new SwingWorker<String, Void>()
                {

                    @Override
                    public String doInBackground() throws Exception
                    {
                        uploading = true;
                        return getLink(Uploader.upload(imagesToUpload.get(0)));
                    }
                    
                    @Override
                    public void done()
                    {
                        try
                        {
                            url = get();
                            uploadComplete();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
    }
    
    /**
     * Creates a new instance of a Swing Worker for Album uploading.
     * 
     * @return
     *          A new instance of the Album Swing Worker.
     */
    private SwingWorker getAlbumWorker()
    {
        return new SwingWorker<String, Void>()
                {
                    @Override
                    public String doInBackground() throws Exception
                    {
                        uploading = true;
                        for (File f : imagesToUpload)
                        {
                            imageIds.add(getId(Uploader.upload(f)));
                        }
                        return getId(Uploader.createAlbum(imageIds));
                    }
                    
                    @Override
                    public void done()
                    {
                        try
                        {
                            url = "https://imgur.com/a/" + get();
                            uploadComplete();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
    }
    
    /**
     * Helper method that sets a JTextPane to have text centering.
     */
    private void setupTextCentering(JTextPane pane)
    {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
    
    /**
     * Uses Regex on the provided JSON String to find the 'link' tag.
     * 
     * @param jsonResponse
     *          The JSON response from Imgur.
     * @return
     *          The link to the image.
     */
    private String getLink(String jsonResponse)
    {
        Pattern pattern = Pattern.compile("link\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonResponse);
        matcher.find();
        return matcher.group().replace("link\":\"", "").replace("\"", "").replace("\\/", "/");
    }

    /**
     * Uses Regex on the provided JSON String to find the 'id' tag.
     * 
     * @param jsonResponse
     *          The JSON response from Imgur.
     * @return
     *          The id of the image or album.
     */
    private String getId(String jsonResponse)
    {
        Pattern pattern = Pattern.compile("id\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonResponse);
        matcher.find();
        return matcher.group().replace("id\":\"", "").replace("\"", "");
    }
}
