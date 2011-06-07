/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t_oster.liblasercut;

import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.app.beans.SVGIcon;
import com.t_oster.liblasercut.BlackWhiteRaster.DitherAlgorithm;
import com.t_oster.util.BufferedImageAdapter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author thommy
 */
public class InteractiveBlackWhiteRasterTest
{

  public static void main(String[] args) throws MalformedURLException, IOException
  {
    JComboBox cb = new JComboBox();
    for (DitherAlgorithm da : BlackWhiteRaster.DitherAlgorithm.values())
    {
      cb.addItem(da);
    }
    int image = 0;
    while (JOptionPane.showConfirmDialog(
      null, cb, "Waehlen Sie einen Algorithmus aus", JOptionPane.OK_CANCEL_OPTION)
      == JOptionPane.OK_OPTION)
    {
      DitherAlgorithm da = (DitherAlgorithm) cb.getSelectedItem();
      BufferedImage test;
      if (image < 1)
      {
        SVGUniverse univ = new SVGUniverse();
        URI svg = univ.loadSVG(new File("tux.svg").toURI().toURL());
        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(svg);
        icon.setAntiAlias(false);
        icon.setClipToViewbox(false);
        icon.setScaleToFit(false);

        test = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        icon.paintIcon(null, test.getGraphics(), 0, 0);
      }
      else
      {
        test = ImageIO.read(new File("rastertest.png"));
      }
      BlackWhiteRaster bwr = new BlackWhiteRaster(
        new BufferedImageAdapter(test), da);
      for (int x = 0; x < bwr.getWidth(); x++)
      {
        for (int y = 0; y < bwr.getHeight(); y++)
        {
          test.setRGB(x, y, (bwr.isBlack(x, y) ? Color.BLACK : Color.WHITE).getRGB());
        }
      }
      JOptionPane.showConfirmDialog(null, new JLabel(new ImageIcon(test)), "Tada", JOptionPane.OK_OPTION);
      image = (image + 1) % 2;
    }
  }
}
