/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rlg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import sm.image.BufferedImageOpAdapter;

/**
 *
 * @author rafa
 * Transformacion Propia Infrrarojo
 * 
 * Hereda de la case BufferedImage
 */
public class InfrarrojoOp extends BufferedImageOpAdapter {

    /**
     * 
     * @param imgSource
     * @param imgdest
     * @return imgdest
     * Aplica el filtro sobre una imagen destino
     * Recorre los pixeles, almacena los colores de cada una y pone los verdes y azules a 0
     * Así consigue el efecto de infrarrojo
     */
    @Override
    public BufferedImage filter(BufferedImage imgSource, BufferedImage imgdest) {
         if (imgSource != null) {
                try {
                   for (int i=0;i<imgSource.getWidth();i++){
                       for (int j=0;j<imgSource.getHeight();j++){
                         int r = new Color(imgSource.getRGB(i, j)).getRed();
                           int b = new Color(imgSource.getRGB(i,j)).getBlue();
                           int g = new Color(imgSource.getRGB(i,j)).getGreen();
                           
                          b=g=0;
                          
                          
                          
                          imgdest.setRGB(i,j,new Color(r, g, b).getRGB());
                       }
                   }
                  
                } catch (Exception e) {
                    System.err.println("Error");
                }
            }
         return imgdest;
    }
    
}
