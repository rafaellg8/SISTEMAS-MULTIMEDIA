/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rlg.figuras;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author rafa
 */
public class LineaPropia extends Line2D.Float {

    //Atributos
    private Color color;
    private int ancho;
    private Stroke trazo;
    private Composite comp;
    private boolean esRelleno;
    private boolean esAlisado;
    private boolean esTransparente;
    private RenderingHints render;
    private Composite composite;
    private Graphics2D g2d;
    private boolean lineaDiscontinua;
    private Color colorSecundario;

    /**
     *
     * @param p1
     * @param p2
     */
    public LineaPropia(Point2D p1, Point2D p2) {
        super(p1, p2);

        ancho = 1;
    }
    /*  public void setLocation(Point2D p1, double distancia){
     //Tenemos que calcular con la distancia el punto 2
     Point2D p2 = new Point2D.Double(p1.getX()+distancia,p1.getY());
     System.out.println(p2.toString());
     super.setLine(p1, p2);
     }*/

    /**
     *
     * @param pos
     */
    public void setLocation(Point2D pos) {

        double dx = pos.getX() - this.getX1();
        double dy = pos.getY() - this.getY1();
        Point2D newp2 = new Point2D.Double(this.getX2() + dx, this.getY2() + dy);
        this.setLine(pos, newp2);
    }

    //Devuelve si un punto esta cerca de la linea
    /**
     *
     * @param p
     * @return
     */
    public boolean isNear(Point2D p) {
        return this.ptLineDist(p) <= 2.0;
    }

    @Override
    public boolean contains(Point2D p) {
        return isNear(p);
    }

    /**
     *
     * @return
     */
    public double getAncho() {
        return ancho;
    }

    /**
     * setAncho
     *
     * @param anchura Vemos que por defecto si es menor que 1 ponemos a 1 la
     * anchura
     */
    public void setAncho(int anchura) {
        if (anchura > 0) {
            this.ancho = anchura;
        } else {
            anchura = 1;
        }
        trazo = new BasicStroke(ancho);
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @param col
     */
    public void setColor(Color col) {
        this.color = col;
    }

    /**
     *
     * @return
     */
    public Color getColorSecundario() {
        return colorSecundario;
    }

    /**
     *
     * @param col
     */
    public void setColorSecundario(Color col) {
        this.colorSecundario = col;
    }
    
    /**
     *
     * @param estado
     */
    public void setAlisado(boolean estado) {
        this.esAlisado = estado;
    }

    /**
     *
     * @return
     */
    public boolean getAlisado() {
        return this.esAlisado;
    }

    /**
     *
     * @param estado
     */
    public void setRelleno(boolean estado) {
        this.esRelleno = estado;
    }

    /**
     *
     * @return
     */
    public boolean getRelleno() {
        return this.esRelleno;
    }

    /**
     *
     * @param estado
     */
    public void setTransparente(boolean estado) {
        this.esTransparente = estado;
    }

    /**
     *
     * @param estado
     */
    public void setlineaDiscontinua(boolean estado) {
        this.lineaDiscontinua = estado;
    }

    /**
     *
     * @return
     */
    public boolean getlineaDiscontinua() {
        return this.lineaDiscontinua;
    }

    /**
     *
     * @return
     */
    public boolean getTransparente() {
        return this.esTransparente;
    }

    /**
     *
     * @param g2d
     */
    public void paint(Graphics2D g2d) {
        if (lineaDiscontinua) {
            trazo = new BasicStroke(ancho, // Width
                    BasicStroke.CAP_SQUARE, // End cap
                    BasicStroke.JOIN_MITER, // Join style
                    10.0f, // Miter limit
                    new float[]{ancho * 10, ancho * 20}, // Dash pattern
                    ancho * 10);
        } else {
            trazo = new BasicStroke(ancho);
        }

        g2d.setStroke(trazo);

        if (esAlisado) {
            //Alisado
            render = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(render);

        }

        if (esTransparente) {
            //Transparencia
            comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(comp);
        }

        //Si el colorSecundario ha sido establecido o es distinto del color principal
        if (this.colorSecundario != null && this.colorSecundario!=color) {
            Paint relleno;
            relleno = new GradientPaint(this.getP1(), this.color, this.getP2(), this.colorSecundario);
            g2d.setPaint(relleno);
        } else {
            g2d.setColor(color);
        }
        //Es una linea no sirve el relleno
        g2d.draw(this);

    }

}
