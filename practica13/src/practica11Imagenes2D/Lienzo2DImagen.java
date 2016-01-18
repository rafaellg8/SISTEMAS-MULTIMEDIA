/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica11Imagenes2D;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import sm.rlg.dibujo_lienzo.*;
import sm.rlg.figuras.Circulo;
import sm.rlg.figuras.Curva;
import sm.rlg.figuras.LineaPropia;
import sm.rlg.figuras.Punto;
import sm.rlg.figuras.Rectangulo;
import sm.rlg.figuras.RectanguloRedondo;

/**
 *
 * @author rafa
 */
public class Lienzo2DImagen extends Lienzo {

    private BufferedImage img;
    private Graphics2D g2d;
    protected boolean lienzoInicio; //Pinta un lienzo gris al crear ventana nueva

    public void setImage(BufferedImage img) {
        this.img = img;
        if (img != null) {
            this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        }
    }

    public void reescale(int ancho, int alto) {
        BufferedImage local = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        Graphics g = local.getGraphics();
        g.drawImage(img, 0, 0, ancho, alto, null);
        g.dispose();

        this.setImage(local);

    }

    public BufferedImage getImage(boolean estado) {
        if (img != null) {
            g2d = img.createGraphics(); //Creamos el objeto graphics de la imagen
        }
        if (estado) { //Si en la clase padre vShape no esta vacío
            if (lienzoInicio) {
                this.setLienzo();//Ponemos un lienzo rectangular para que no se vea la imagen fondo negro
            }
                //Aplicamos las transparencias alisado al dibujo y dibujamos las formas
            //Transparencia
            Composite comp;
            comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

            //Alisado
            RenderingHints render;
            render = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (super.getTransparente()) {
                g2d.setComposite(comp);
            }

            if (super.getAlisado()) {
                g2d.setRenderingHints(render);
            }

            Stroke stroke;
            stroke = new BasicStroke(super.getStroke());
            g2d.setStroke(stroke);

            for (Shape s : this.getVShape()) {
               inicializarGraficosShapeImagen(s);
            }

        }

        return img;
    }

    public void setLienzo() { //Pone un rectangulo gris para dibujar encima imagen
        Rectangle2D dibujo = new Rectangle2D.Double();
        //Añadimos +10 a los minimos para que se vea el marco exterior
        dibujo.setRect(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        g2d.setColor(Color.WHITE);
        g2d.fill(dibujo);

        float patronDiscontinuidad[] = {10.0f, 10.0f, 10.0f};
        //Marco Exterior
        Stroke s = new BasicStroke(1.0f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 1.0f,
                patronDiscontinuidad, 0.0f);
        dibujo.setRect(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.setStroke(s);
        g2d.draw(dibujo);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (img != null) {
            super.clip(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        }

        super.paintComponent(g);

        g2d = (Graphics2D) g;

        if (img != null) {
            //Lo situamos un poco mas lejos de la ventana para que se aprecie marco
            g.drawImage(img, 0, 0, this);
        }

        //Pintamos el Rectangulo Blanco del lienzo encima imagen
        //Una sola vez
        if (this.lienzoInicio) {
            this.setLienzo();
            this.lienzoInicio = false;
        }

    }
    
     //Obtiene el objeto grapchis2d de la figura
    public void inicializarGraficosShapeImagen(Shape s) {
        if (s instanceof Rectangulo) {
           Rectangulo figura = (Rectangulo) s;
           figura.paint(g2d);
           
        }

        if (s instanceof RectanguloRedondo) {
            RectanguloRedondo figura = (RectanguloRedondo) s;
            figura.paint(g2d);
        } 
        else if (s instanceof LineaPropia) {
            LineaPropia figura = (LineaPropia) s;
            figura.paint(g2d);
        }
        else if (s instanceof Punto) {
            Punto figura = (Punto) s;
            figura.paint(g2d);
        }
        else if (s instanceof Circulo) {
            Circulo figura = (Circulo) s;
            figura.paint(g2d);
        }
        else if (s instanceof Curva) {
            Curva figura = (Curva) s;
            figura.paint(g2d);
        }
       
    }

}
