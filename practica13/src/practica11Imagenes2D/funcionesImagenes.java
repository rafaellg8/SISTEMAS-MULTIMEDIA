package practica11Imagenes2D;

import rlg.UmbralizacionColorOp;
import rlg.UmbralizacionOp;
import java.awt.Color;
import java.awt.Point;
import java.awt.color.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import static java.lang.Math.*;
import static java.lang.StrictMath.PI;
import rlg.InfrarrojoOp;
import sm.image.BufferedImagePixelIterator;
import sm.image.ImageTools;
import sm.image.LookupTableProducer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rafa
 * Clase auxiliar que lo unico que hace es preparar algunas operaciones de imagenes
 * Como obtener imagen destino de la umbralizacion y pintarla etc
 * El objetivo era no tener tanto código en la clase ventana
 * Todas las funciones son estaticas para que puedan ser llamadas fuera de la clase sin crear el objeto
 * Tiene de parametro desde donde se llama, ventana principal, para poder establecer la imagen en el lienzo ventana activa
 */
public class funcionesImagenes {

    public static void UmbralizacionOp(ventana v, int umbral) {

        UmbralizacionOp umbralOp = new UmbralizacionOp(umbral); //Creamos el umbral
        ventanaInterna vi = v.getVentana();
        BufferedImage imgOrigen = v.getImagenOriginal();
        BufferedImage imgSource = imgOrigen;
        BufferedImage dest = null;
        if (vi != null) {
            dest = umbralOp.filter(imgOrigen, dest);

            vi.getLienzo().setImage(dest);
            vi.getLienzo().repaint();
        }
    }
    /**
     * 
     * @param v
     * @param umbral
     * @param color 
     * Funcion auxiliar que prepara la funcion real que se encuentra en UmbralizacionColorOp
     */
    public static void UmbralizacionColor(ventana v,int umbral,Color color){
        UmbralizacionColorOp umbralOp = new UmbralizacionColorOp(umbral,color); //Creamos el umbral
        ventanaInterna vi = v.getVentana();
        BufferedImage imSrc = v.getImagenOriginal();
        BufferedImage imgSource = imSrc;
        BufferedImage dest = null;
        if (vi!=null){
            dest = umbralOp.filter(imgSource, dest);
            vi.getLienzo().setImage(dest);
            vi.getLienzo().repaint();
        }
     }
    

    /**
     * 
     * @param v
     * @param brilloImagen 
     * Aumenta el brillo de una imagen en funcion parametro brilloImagen
     */
    public static void Reescalado(ventana v, int brilloImagen) {
        ventanaInterna vi;
        vi = v.getVentana();
        if (vi != null) {
            BufferedImage imgOrigen = v.getImagenOriginal();
            BufferedImage imgSource = imgOrigen;


            if (imgSource != null) {
                try {
                    //Si la imagen tiene activos los canales alpha
                    if (imgSource.getColorModel().hasAlpha()) {
                        //Creamos un vector de colores con alpha a 1 y un vector de offset con el brillo
                        float[] colores = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                        float[] offset = new float[]{brilloImagen, brilloImagen, brilloImagen};
                        RescaleOp rop = new RescaleOp(colores, offset, null);
                        BufferedImage imgdest = rop.filter(imgSource, null);
                        imgdest = ImageTools.convertImageType(imgdest,imgSource.getType());
                        vi.getLienzo().setImage(imgdest);
                        vi.getLienzo().repaint();
                    } else {

                        RescaleOp rop = new RescaleOp(1.0F, brilloImagen, null);
                        BufferedImage imgdest = rop.filter(imgSource, null);
                        vi.getLienzo().setImage(imgdest);
                        vi.getLienzo().repaint();
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }
    }

    /**
     * 
     * @param v 
     * Funcion LookupOp
     */
    public static void LookupOP(ventana v) {
        ventanaInterna vi;
        vi = v.getVentana();
        if (vi != null) {
            BufferedImage imgSource = v.getImagenOriginal();
            BufferedImage imgdest = null;

            if (imgSource != null) {
                try {
                    LookupTable lt = LookupTableProducer.sFuction(128.0, 3.0);
                    LookupOp lop = new LookupOp(lt, null);

                    imgdest = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);
                    imgdest = lop.filter(imgSource, null);

                } catch (Exception e) {
                    System.err.println("Error");
                }
            }
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();
        }
    }

    public static void AffineTransformOP(ventana v) {
        ventanaInterna vi;
        vi = v.getVentana(); //Obtenemos la ventana interna
        if (vi != null) {
            AffineTransform at;
            BufferedImage imgSource = v.getImage(); //Obtenemos la imagen
            //AffineTransform at = AffineTransform.getScaleInstance(1.25,1.25);
            //at = AffineTransform.getRotateInstance(Math.toRadians(180.0),imgSource.getWidth()/2,imgSource.getHeight()/2);
            //  at = AffineTransform.getTranslateInstance(20.0,20.0);

            //Creamos una transformacion de tipo Shear
            at = AffineTransform.getShearInstance(0.5, 0.0);

            if (imgSource != null) {
                try {
                    AffineTransformOp atop = new AffineTransformOp(at,
                            AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage imgdest = atop.filter(imgSource, null);

                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }

    }

    public static void escaladoImagen(ventana v, double factorEscalado) {
        ventanaInterna vi;
        vi = v.getVentana(); //Obtenemos la ventana interna
        if (vi != null) {
            AffineTransform at;
            BufferedImage imgSource = v.getImage(); //Obtenemos la imagen
            //AffineTransform at = AffineTransform.getScaleInstance(1.25,1.25);
            //at = AffineTransform.getRotateInstance(Math.toRadians(180.0),imgSource.getWidth()/2,imgSource.getHeight()/2);
            //  at = AffineTransform.getTranslateInstance(20.0,20.0);

            //Creamos una transformacion de tipo Shear
            at = AffineTransform.getScaleInstance(factorEscalado, factorEscalado);

            if (imgSource != null) {
                try {
                    AffineTransformOp atop = new AffineTransformOp(at,
                            AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage imgdest = atop.filter(imgSource, null);

                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }

    }

    public static void BandCombineOp(ventana v) {
        WritableRaster rasterdest;
        BufferedImage imgdest;
        ventanaInterna vi = v.getVentana();

        if (vi != null) {
            BufferedImage imgSource = v.getImage();
            if (imgSource != null) {
                try {
                    float[][] m = {{0.0F, 1.0F, 0.0F}, {0.0F, 1.0F, 0.0F}, {1.0F, 0.0F, 0.0F}};
                    BandCombineOp bcop = new BandCombineOp(m, null);
                    rasterdest = bcop.filter(imgSource.getRaster(), null);

                    imgdest = new BufferedImage(imgSource.getColorModel(),
                            rasterdest, false, null);

                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }

    public static void ColorConvertOp(ventana v) {
        //Inicializamos las variables y obtenemos la ventana interna asocidad
        BufferedImage imgdest = null;
        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImage();

        if (vi != null && imgSource != null) {

            ICC_Profile ip;
            ip = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
            ColorSpace cs = new ICC_ColorSpace(ip);
            ColorConvertOp ccop = new ColorConvertOp(cs, null);
            imgdest = ccop.filter(imgSource, null);

            //Pintamos en el lienzo la imagen y hacemos un repaint
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();

        }
    }

    public static void ContrasteNormal(ventana v) {
        //Inicializamos las variables y obtenemos la ventana interna asocidad

        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImage();
        //Ponemos por defecto a int argb para que no de problemas de incompatibilidad
        BufferedImage imgdest = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        if (vi != null && imgSource != null) {

            try {
                LookupTable lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_SFUNCION);
                LookupOp lop = new LookupOp(lt, null);

                imgSource = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);

                imgdest = lop.filter(imgSource, null);

            } catch (Exception e) {
                System.err.println("Error");
            }

            //Pintamos en el lienzo la imagen y hacemos un repaint
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();
            
        }
    }

    public static void incrementarContraste(ventana v) {
        //Inicializamos las variables y obtenemos la ventana interna asocidad

        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImage();
        BufferedImage imgdest = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        if (vi != null && imgSource != null) {

            try {
                LookupTable lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_LOGARITHM);
                LookupOp lop = new LookupOp(lt, null);

                imgSource = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);
                imgdest = lop.filter(imgSource, null);

            } catch (Exception e) {
                System.err.println("Error");
            }

            //Pintamos en el lienzo la imagen y hacemos un repaint
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();

        }
    }

    public static void decrementarContraste(ventana v) {
        //Inicializamos las variables y obtenemos la ventana interna asocidad
        BufferedImage imgdest = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImage();

        if (vi != null && imgSource != null) {

            try {
                LookupTable lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_POWER);
                LookupOp lop = new LookupOp(lt, null);

                imgSource = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);
                imgdest = lop.filter(imgSource, null);

            } catch (Exception e) {
                System.err.println("Error");
            }

            //Pintamos en el lienzo la imagen y hacemos un repaint
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();

        }
    }

    public static void rotarGrados(ventana v, double grados) {

        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImagenOriginal();

        if (vi != null && imgSource != null) {
            double r = Math.toRadians((double) grados);
            Point c = new Point(imgSource.getWidth() / 2, imgSource.getHeight() / 2);
            AffineTransform at = AffineTransform.getRotateInstance(r, c.x, c.y);
            AffineTransformOp atop;
            atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage imgdest = atop.filter(imgSource, null);

            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();

        }
    }

    public static void rotarImagen(ventana v, double grados) {

        ventanaInterna vi = v.getVentana();
        BufferedImage imgSource = v.getImage();

        if (vi != null && imgSource != null) {
            double r = Math.toRadians((double) grados);
            Point c = new Point(imgSource.getWidth() / 2, imgSource.getHeight() / 2);
            AffineTransform at = AffineTransform.getRotateInstance(r, c.x, c.y);
            AffineTransformOp atop;
            atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage localSource = new BufferedImage(imgSource.getWidth(), imgSource.getHeight(), BufferedImage.TYPE_INT_ARGB);

            BufferedImage imgdest = atop.filter(imgSource, localSource);

            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();

        }
    }

    //Creamos un LookupTable con la funcion seno
    public static LookupTable senoFuction(double w) {
        double K = 255.0;
        byte lt[] = new byte[256];
        lt[0] = 0;
        for (int l = 1; l < 256; l++) {
            lt[l] = (byte) (K*Math.abs(Math.sin(Math.toRadians(w*l))));
        }
        ByteLookupTable slt = new ByteLookupTable(0, lt);
        return slt;
    }

    public static void LookupOPSeno(ventana v) {
        ventanaInterna vi;
        vi = v.getVentana();
        if (vi != null) {
            BufferedImage imgSource = v.getImage();
            BufferedImage imgdest = null;

            if (imgSource != null) {
                try {
                    LookupTable lt = funcionesImagenes.senoFuction(1.0);
                    LookupOp lop = new LookupOp(lt, null);

                    imgdest = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);
                    imgdest = lop.filter(imgSource, null);

                } catch (Exception e) {
                    System.err.println("Error");
                }
            }
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();
        }
    }

    //Negativo
    public static void Negativo(ventana v) {
        ventanaInterna vi;
        vi = v.getVentana();
        if (vi != null) {
            BufferedImage imgSource = v.getImage();
            BufferedImage imgdest = null;

            if (imgSource != null) {
                try {
                    LookupTable lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_NEGATIVE);
                    LookupOp lop = new LookupOp(lt, null);

                    imgdest = ImageTools.convertImageType(imgSource, BufferedImage.TYPE_INT_ARGB);
                    imgdest = lop.filter(imgSource, null);

                } catch (Exception e) {
                    System.err.println("Error");
                }
            }
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();
        }
    }
    
    //Calcula la media de un color, util para la funcion infrarroja
    private static int mediaColor(Color color){
        int averageColor;
        averageColor=(int)((color.getRed()+color.getGreen()+color.getBlue())/3);
        return averageColor;
    }
    
    public static void ImagenInfrarroja(ventana v){
        ventanaInterna vi;
        vi = v.getVentana();
        if (vi != null) {
            BufferedImage imgSource = v.getImage();
            BufferedImage imgdest = imgSource;

           InfrarrojoOp op = new InfrarrojoOp();
           imgdest = op.filter(imgSource, imgdest);
            vi.getLienzo().setImage(imgdest);
            vi.getLienzo().repaint();
        }
    }

     
     

    private static int colorRGBaSRGB(Color color) {
        
        int colorSRGB;
        colorSRGB=(color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
        return colorSRGB;
    
    }

}
