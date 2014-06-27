package net.dv8tion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class UploaderFrame extends JFrame implements ActionListener
{
    public static void main(String[] args) throws IOException
    {
        UploaderFrame f = new UploaderFrame();
        f.setVisible(true);
//        LinkedList<String> imageIds = new LinkedList<String>();
//        imageIds.add("0AJX5Kc");
//        imageIds.add("1pvAMrc");
//        imageIds.add("T7FIUlT");
//        System.out.println(Uploader.createAlbum(imageIds));        
    }
    
    private SwingWorker uploader;
    private SwingWorker uploaderAlbum;
    private LinkedList<File> imagesToUpload;
    private LinkedList<String> albumIds;
    private String url;
    private boolean uploading;
    
    private JPanel panel;
    
    private JButton btnUpload;
    private JButton btnPreview;
    private JButton btnCustomCapture;
    private JButton btnOpenBrowser;
    private JButton btnCopyLink;
    
    private JLabel lblLink;
    private JLabel lblTitle;
    private JTextArea lblUploadMessage;

    public UploaderFrame()
    {
        this.setTitle("Imgur Uploader");
        this.setSize(290, 290);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel();
        
        btnUpload = new JButton();
        btnUpload.setText("Upload");
        btnUpload.addActionListener(this);
        
        btnPreview = new JButton();
        btnPreview.setText("Preview");
        btnPreview.addActionListener(this);
        
        btnCustomCapture = new JButton();
        btnCustomCapture.setText("Custom Screen Capture");
        btnCustomCapture.addActionListener(this);
        
        btnOpenBrowser = new JButton();
        btnOpenBrowser.setText("Open in Browser");
        btnOpenBrowser.addActionListener(this);
        
        btnCopyLink = new JButton();
        btnCopyLink.setText("Copy Link");
        btnCopyLink.addActionListener(this);
        
        lblLink = new JLabel();
        lblLink.setText("NO CURRENT LINK");
        
        lblTitle = new JLabel();
        lblTitle.setText("Imgur Link:");
        
        lblUploadMessage = new JTextArea();
        lblUploadMessage.setText("Upload and Preview Buttons are disabled\nuntil an image is in the Clipboard.");
        
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == btnUpload)
        {
            
        }
        else if (source == btnPreview)
        {
            
        }
        else if (source == btnCustomCapture)
        {
            
        }
        else if (source == btnOpenBrowser)
        {
            
        }
        else if (source == btnCopyLink)
        {
            
        }
    }
    
    
    
    
}