package net.dv8tion;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.LinkedList;

import javax.swing.SwingWorker;

public class temp
{
    private SwingWorker uploader;
    private SwingWorker uploaderAlbum;
    private LinkedList<File> imagesToUpload;
    private LinkedList<String> albumIds;
    private String url;
    private boolean uploading;
    
    public temp()
    {
        imagesToUpload = new LinkedList<File>();
        albumIds = new LinkedList<String>();
        uploader = new SwingWorker();
        uploader.DoWork += new DoWorkEventHandler(
            delegate(object o, DoWorkEventArgs args)
            {
                uploading = true;
                url = GetLink(Uploader.Upload(imagesToUpload[0]));
            });
    
        uploader.RunWorkerCompleted += new RunWorkerCompletedEventHandler(
            delegate(object o, RunWorkerCompletedEventArgs args)
            {
                UploadComplete();
            });
    
        uploaderAlbum = new BackgroundWorker();
        uploaderAlbum.WorkerReportsProgress = true;
        uploaderAlbum.DoWork += new DoWorkEventHandler(
            delegate(object o, DoWorkEventArgs args)
            {
                uploading = true;
                for (int i = 0; i < imagesToUpload.Count; i++)
                {
                    albumIds.Add(GetId(Uploader.Upload(imagesToUpload[i])));
                    uploaderAlbum.ReportProgress((int)(((double)i / imagesToUpload.Count) * 100));
                }
                url = "https://imgur.com/a/" + GetId(Uploader.CreateAlbum(albumIds));
    
            });
        uploaderAlbum.ProgressChanged +=new ProgressChangedEventHandler(
            delegate(object o, ProgressChangedEventArgs args)
            {
                lblLink.Text = "Uploading " + imagesToUpload.Count + " images..."
                + " Completed: " + args.ProgressPercentage + "%";
            });
        uploaderAlbum.RunWorkerCompleted += new RunWorkerCompletedEventHandler(
            delegate(object o, RunWorkerCompletedEventArgs args)
            {
                albumIds.Clear();
                UploadComplete();
                lblLink.Text = url;
            });
    }
    
    public int GetImageAmount()
    {
        return imagesToUpload.size();
    }
    
    public File GetImage(int index)
    {
        if (index >= 0 && index < imagesToUpload.size())
        {
            return imagesToUpload.get(index);
        }
        return null;
    }
    
    public void ClearImages()
    {
        imagesToUpload.clear();
    }
    
    private void UploaderFrame_Resize(object sender, EventArgs e)
    {
        if (this.WindowState == FormWindowState.Minimized && Screen.GetWorkingArea(this).Contains(MousePosition))
        {
            //notifyIcon.ShowBalloonTip(3000);
            this.ShowInTaskbar = false;
            this.Hide();
        }
    }
    
    private void btnUpload_Click(object sender, EventArgs e)
    {
        Upload();
    }
    
    private void btnBrowser_Click(object sender, EventArgs e)
    {
        Process.Start(url);
    }
    
    private void btnCopyLink_Click(object sender, EventArgs e)
    {
        Clipboard.SetText(url);
        UploadButtonStatus(sender, e);
    }
    
    private void notifyIcon_MouseDoubleClick(object sender, MouseEventArgs e)
    {
        ShowProgram();
    }
    
    private void menuShow_Click(object sender, EventArgs e)
    {
        ShowProgram();
    }
    
    private void menuUpload_Click(object sender, EventArgs e)
    {
        Upload();
        ShowProgram();
    }
    
    private void notifyIconMenu_Opening(object sender, CancelEventArgs e)
    {
        UploadButtonStatus(sender, e);
    }
    private void menuExit_Click(object sender, EventArgs e)
    {
        this.Close();
    }
    
    private void btnPreview_Click(object sender, EventArgs e)
    {
        LoadImages();
        new ImagePreview(this, getImage(0)).Show();
    }
    
    private void btnCapture_Click(object sender, EventArgs e)
    {
        new ScreenCapture().Show();
    }
    
    private void UploadButtonStatus(object sender, EventArgs e)
    {
        if (!uploading)
        {
            if (ClipboardContainsImage())
            {
                btnUpload.Enabled = true;
                menuUpload.Enabled = true;
                btnPreview.Enabled = true;
                lblUploadMessage.Text = "";
            }
            else
            {
                btnUpload.Enabled = false;
                menuUpload.Enabled = false;
                btnPreview.Enabled = false;
                lblUploadMessage.Text = "Upload and Preview Buttons are disabled \nuntil an image is in the Clipboard.";
            }
        }
    }
    
    private void ConfirmClose(object sender, FormClosingEventArgs e)
    {
        DialogResult result = MessageBox.Show("Are you sure you want to close Imgur Uploader?", "Confirm Exit", MessageBoxButtons.YesNo);
        if (result.Equals(DialogResult.Yes))
        {
            return;
        }
        else
        {
            e.Cancel = true;
        }
    }
    
    private void ShowProgram()
    {
        this.WindowState = FormWindowState.Normal;
        this.ShowInTaskbar = true;
        this.Show();
    }
    
    private void Upload()
    {
        LoadImages();
        if (imagesToUpload.size() > 1)
        {
            lblLink.Text = "Uploading " + imagesToUpload.size() + " images..."
                + " Completed: 0%";
            uploaderAlbum.RunWorkerAsync();
        }
        else
        {
            lblLink.Text = "Uploading and fetching URL...";
            uploader.RunWorkerAsync();
        }
        btnUpload.Enabled = false;
        btnPreview.Enabled = false;
        menuUpload.Enabled = false;
        btnBrowser.Enabled = false;
        btnCopyLink.Enabled = false;
    }
    
    private void UploadComplete()
    {
        uploading = false;
        lblLink.Text = url;
        UploadButtonStatus(null, null);
        btnBrowser.Enabled = true;
        btnCopyLink.Enabled = true;
        ClearImages();
    }
    
    private String GetLink(String jsonResponse)
    {
        System.Text.RegularExpressions.Regex reg = new System.Text.RegularExpressions.Regex("link\":\"(.*?)\"");
        return reg.Match(jsonResponse).ToString().Replace("link\":\"", "").Replace("\"", "").Replace("\\/", "/");
    }
    
    private String GetId(String jsonResponse)
    {
        System.Text.RegularExpressions.Regex reg = new System.Text.RegularExpressions.Regex("id\":\"(.*?)\"");
        return reg.Match(jsonResponse).ToString().Replace("id\":\"", "").Replace("\"", "");
    }
    
    /// <summary>
    /// Checks to see if there is an image or a collection of images to upload.
    /// </summary>
    /// <returns>true if there are images to upload.</returns>
    private boolean ClipboardContainsImage()
    {
        LoadImages();
        int count = imagesToUpload.size();
        ClearImages();//Clear this to unlock the images (can't delete/rename/move without this).
        return count > 0;
    }
    
    /// <summary>
    /// Takes all images given and loads them into a List in preparation for upload.
    /// </summary>
    private void LoadImages()
    {
        ClearImages();
        if (Clipboard.ContainsImage())
        {
            imagesToUpload.Add(Clipboard.GetImage());
            return;
        }
    
        IDataObject d = Clipboard.GetDataObject();
        if (d.GetDataPresent(DataFormats.FileDrop))
        {
            string[] copiedFiles = (String[]) d.GetData(DataFormats.FileDrop);
            for (String filePath : copiedFiles)
            {
                try
                {
                    imagesToUpload.Add(Image.FromFile(filePath));
                }
                catch (Exception e)
                {
                    //Do Nothing
                }
            }
            return;
        }
    }
}
