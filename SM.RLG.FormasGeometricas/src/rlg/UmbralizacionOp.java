/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rlg;

import static com.sun.javafx.application.PlatformImpl.exit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;
import sm.image.*;

/**
 *
 * @author rafa
 */
public class UmbralizacionOp extends BufferedImageOpAdapter {

    private int umbral;

    /**
     *
     * @param umbral
     */
    public UmbralizacionOp(int umbral) {
        this.umbral = umbral;
    }

    /**
     * 
     * @param src
     * @param dest
     * @return dest imagen destino
     * Recorre los pixeles, suma la intensidad de estos, y si es mayor umbral pone 255, sino a 0
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        int intensidadPixel;
        int T = this.umbral; //Umbral

        if (src == null) {
            throw new NullPointerException("imagen origen nula");
        }

        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }

        WritableRaster srcRaster = src.getRaster();
        WritableRaster destRaster = dest.getRaster();
        for (BufferedImagePixelIterator it = new BufferedImagePixelIterator(src); it.hasNext();) {
            BufferedImagePixelIterator.PixelData pixel = it.next();

           
                //Si es mayor = T pasamos a negro si no a blanco
                if (intensidadPixel(pixel) >= T) {
                
                    pixel.sample[0] = pixel.sample[1] = pixel.sample[2] = 255;
                } else {
                    pixel.sample[0] = pixel.sample[1] = pixel.sample[2] = 0;
                }

                destRaster.setPixel(pixel.col, pixel.row, pixel.sample);
            
        }

        return dest;
    }

    /**
     * 
     * @param pixel
     * @return intensidad
     * 
     */
    private int intensidadPixel(BufferedImagePixelIterator.PixelData pixel) {
        int intensidad = 0;
        
        //Recorremos los pixeles y al final calculamos la media
        for (int i=0;i<pixel.sample.length;i++)
            intensidad += (pixel.sample[i]);
        intensidad = intensidad/pixel.sample.length;
        return intensidad;
    }

}
