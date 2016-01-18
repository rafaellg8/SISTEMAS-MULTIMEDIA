/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rlg;

import java.awt.image.*;
import sm.image.*;

/**
 *
 * @author rafa
 */
public class SumaOp extends sm.image.BinaryOp {

    private float alpha;
    private int value;

    /**
     *
     * @param img
     */
    public SumaOp(BufferedImage img) {
        super(img);
        this.setAlpha(0.5f);
        alpha = this.getAlpha();
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    @Override
    public int binaryOp(int v1, int v2) {
        int rdo = (int) ((alpha * v1) + ((1 - alpha) * v2));
        if (rdo <= 0) {
            rdo = 0;
        } else if (rdo >= 255) {
            rdo = 255;
        }
        return rdo;
    }

    /**
     *
     * @param alpha
     */
    public final void setAlpha(float alpha) {
        if (alpha < 0.0f) {
            alpha = 0.0f;
        } else if (alpha > 1.0f) {
            alpha = 1.0f;
        }
        this.alpha = alpha;
    }

    /**
     *
     * @return
     */
    public float getAlpha() {
        return alpha;
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage right, ColorModel destCM) {
        if (destCM == null) {
            if (left.getRaster().getNumBands() < right.getRaster().getNumBands()) {
                destCM = left.getColorModel();
            } else {
                destCM = right.getColorModel();
            }
        }
        int widthInsersection = Math.min(left.getWidth(), right.getWidth());
        int heightInsersection = Math.min(left.getHeight(), right.getHeight());
        WritableRaster wr = destCM.createCompatibleWritableRaster(widthInsersection, heightInsersection);
        return new BufferedImage(destCM, wr, destCM.isAlphaPremultiplied(), null);
    }

    @Override
    public BufferedImage filter(BufferedImage right, BufferedImage dest) {
        if (dest == null) {
            dest = createCompatibleDestImage(right, null);
        }
        WritableRaster rightRaster = right.getRaster();
        WritableRaster destRaster = dest.getRaster();
        WritableRaster leftRaster = left.getRaster();

        for (BufferedImageSampleIterator it = new BufferedImageSampleIterator(dest); it.hasNext();) {
            BufferedImageSampleIterator.SampleData sample = it.next(); // Sample del destino
// Aplicamos operación
            int s1 = rightRaster.getSample(sample.col, sample.row, sample.band);
            int s2 = leftRaster.getSample(sample.col, sample.row, sample.band);
            sample.value = binaryOp(s1, s2);
// Almacenamos resultado en destino
            destRaster.setSample(sample.col, sample.row, sample.band, sample.value);
        }
        return dest;
    }
}
