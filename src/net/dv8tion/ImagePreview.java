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

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

/**
 * Provides functionality to view images before uploading them.
 * 
 * @author DV8FromTheWorld (Austin Keener)
 * @version v1.0  July 8, 2014
 *
 */
@SuppressWarnings("serial")
public class ImagePreview extends JFrame implements ActionListener
{
    private ImagePanel imagePanel;
    private ArrayList<File> images;

    private JMenuBar menuBar;
    private JButton previousButton;
    private JButton nextButton;
    private JLabel imageCount;

    private int imageIndex;
    
    /**
     * Creates a new instance of the ImagePreview GUI.
     * 
     * @param images
     *          The images that will be viewed.
     */
    public ImagePreview(ArrayList<File> images)
    {
        this.images = images;
        this.imageIndex = 0;
        
        imagePanel = new ImagePanel();
        menuBar = new JMenuBar();
        previousButton = new JButton("Previous Image");
        previousButton.setFont(UploaderFrame.FONT);
        previousButton.addActionListener(this);
        nextButton = new JButton("Next Image");
        nextButton.setFont(UploaderFrame.FONT);
        nextButton.addActionListener(this);
        imageCount = new JLabel("1 / " + images.size());
        imageCount.setFont(UploaderFrame.FONT);
        menuBar.add(previousButton);
        menuBar.add(nextButton);
        menuBar.add(imageCount);
        this.setContentPane(imagePanel);
        this.setJMenuBar(menuBar);
        this.setIconImage(UploaderFrame.IMAGE_ICON.getImage());
        
        loadImage();
        buttonStatusUpdate();
    }

    /**
     * Handles the button presses to switch between images.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (previousButton == source)
        {
            imageIndex--;
            loadImage();
            buttonStatusUpdate();
            
        }
        else if (nextButton == source)
        {
            imageIndex++;
            
            loadImage();
            buttonStatusUpdate();
        }
    }

    /**
     * Loads an images based on the current image index.
     */
    private void loadImage()
    {
        Image i = this.getToolkit().createImage(images.get(imageIndex).getAbsolutePath());
        ImageIcon icon = new ImageIcon(i);
        imagePanel.setImage(i);        
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
    }
    
    /**
     * Updates the previous and next buttons.  Also updates the image count.
     */
    private void buttonStatusUpdate()
    {
        previousButton.setEnabled(imageIndex != 0);
        nextButton.setEnabled(imageIndex < images.size() - 1);
        imageCount.setText((imageIndex + 1) + " / " + images.size());
    }
}
