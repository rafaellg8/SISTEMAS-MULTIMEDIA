/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rlg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;
import sm.image.ThresholdOp;

/**
 *
 * @author rafa
 */
public class UmbralizacionColorOp extends BufferedImageOpAdapter {

    private int umbral;
    private Color colUmbral;
    private int colorUmbralizadoT;

    /**
     *
     * @param umbral
     * @param color
     */
    public UmbralizacionColorOp(int umbral, Color color) {
        this.umbral = umbral;
        this.colUmbral = color;
        
        //Establecemos el color al que aplicamos la umbralizacion
        if (color == Color.red) {
            colorUmbralizadoT = 0;
        } else if (color == Color.blue) {
            this.colorUmbralizadoT = 1;
        } else {
            this.colorUmbralizadoT = 2;
        }
    }

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

                //Si el color es mayor = T pasamos a mayor 255
            if (pixel.sample[this.colorUmbralizadoT]>=T)
                pixel.sample[this.colorUmbralizadoT]=255;
            else
                pixel.sample[this.colorUmbralizadoT]=0;

            destRaster.setPixel(pixel.col, pixel.row, pixel.sample);

        }

        return dest;
    }

   
}
