/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rlg.dibujo_lienzo;

import sm.rlg.figuras.Circulo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import sm.rlg.figuras.*;


/**
 *
 * @author rafa
 */
public class Lienzo extends javax.swing.JPanel {

    private int forma; //1 punto 2 linea 3 circulo 4 cuadrado

    private boolean relleno;
    private boolean transparente;
    private Color color;
    private Color colorSecundario; //utilizado para el degradado
    private boolean alisado;
    private int anchoStroke;

    /**
     *
     */
    public final List<Shape> vShape; //Lista de formas
    private boolean editar; //variable donde guardamos el estado del boton Editar
    private String tipoFuente; //Tipo de fuente con la que vamos a escribir
    private Stroke stroke;
    private Point2D punto;
    private Point2D fin;
    private LineaPropia milinea;
    private Shape s;
    private Graphics2D g2d;
    private Graphics2D estadoOriginalG2D;
    private Shape recorte;
    private Rectangle2D bound;
    private boolean lineadiscontinua;
    
   

    /**
     *
     */
    public Lienzo() {
        initComponents();

        float dash[] = {0};
        //Creamos el stroke
        vShape = new ArrayList(); //Creamos el array de formas
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g2d = (Graphics2D) g;
        estadoOriginalG2D = g2d;
        
        
        //Limitamos la zona de dibujo con un recorte
        if (recorte != null) {
            g2d.clip(recorte);
        }

        

        if (editar && bound!=null){
           
             stroke = new BasicStroke(1.0f,                      // Width
                           BasicStroke.CAP_SQUARE,    // End cap
                           BasicStroke.JOIN_MITER,    // Join style
                           10.0f,                     // Miter limit
                           new float[] {1.0f,2.0f}, // Dash pattern
                           1.0f);  
            g2d.setStroke(stroke);
            g2d.draw(bound);
            
            //Actualizamos si se ha producido algun cambio con los colores o demas de la figura
            actualizarPropiedades(s);
            
        }
        
        for (Shape s : this.getVShape()) {
            /*g.setColor(this.getColor(s));
            g2d.setPaint(this.getColor(s));
            
            
            if (relleno) {
                g2d.fill(s);
            }

            g2d.draw(s);
            */
            inicializarGraficosShape(s);
            //Una vez pintada la figura restuaramos el objeto graphics
            g2d = this.estadoOriginalG2D;
        }
        
    }
    

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void clip(int x, int y, int width, int height) {
        recorte = new Rectangle2D.Double(x, y, width, height);
    }

    /**
     *
     * @param forma
     */
    public void createShape(int forma) {
        switch (forma) {
            //0 rectangulo 1 ellipse 2 linea 3 punto 4 rectangulo redondo 5 arco
            case 0:
                Rectangulo rect;
                rect = new Rectangulo(punto, 1.0, 1.0);
                rect.setColor(color);
                rect.setAncho(this.anchoStroke);
                rect.setRelleno(this.relleno);
                rect.setTransparente(this.transparente);
                rect.setAlisado(this.alisado);
                rect.setlineaDiscontinua(this.lineadiscontinua);
                rect.setColorSecundario(this.colorSecundario);
                vShape.add(rect);
                break;

            case 1:
                Circulo elli;
                elli = new Circulo(punto, 1.0, 1.0);
                elli.setColor(color);
                elli.setAncho(this.anchoStroke);
                elli.setRelleno(this.relleno);
                elli.setTransparente(this.transparente);
                elli.setAlisado(this.alisado);
                elli.setlineaDiscontinua(this.lineadiscontinua);
                elli.setColorSecundario(this.colorSecundario);
                vShape.add(elli);
                break;

            case 2: //Linea
                LineaPropia linea;
                linea = new LineaPropia(punto, punto);
                linea.setColor(color);
                linea.setAncho(this.anchoStroke);
                linea.setRelleno(this.relleno);
                linea.setTransparente(this.transparente);
                linea.setAlisado(this.alisado);
                linea.setlineaDiscontinua(this.lineadiscontinua);
                linea.setColorSecundario(this.colorSecundario);
                vShape.add(linea);
                break;

            //Punto
            case 3:
                Punto p;
                p = new Punto(punto);
                p.setColor(color);
                p.setAncho(this.anchoStroke);
                p.setRelleno(this.relleno);
                p.setTransparente(this.transparente);
                p.setAlisado(this.alisado);
                p.setlineaDiscontinua(this.lineadiscontinua);
                p.setColorSecundario(this.colorSecundario);
                vShape.add(p);

                break;

            case 4:
                RectanguloRedondo red;
                red = new RectanguloRedondo(punto, 25.0, 50.0);
                red.setColor(color);
                red.setAncho(this.anchoStroke);
                red.setRelleno(this.relleno);
                red.setTransparente(this.transparente);
                red.setAlisado(this.alisado);
                red.setlineaDiscontinua(this.lineadiscontinua);
                red.setColorSecundario(this.colorSecundario);
                vShape.add(red);
                break;

            case 5:
                Curva curva;
                curva = new Curva(punto, punto);
                curva.setColor(color);
                curva.setAncho(this.anchoStroke);
                curva.setRelleno(this.relleno);
                curva.setTransparente(this.transparente);
                curva.setAlisado(this.alisado);
                curva.setlineaDiscontinua(this.lineadiscontinua);
                curva.setColorSecundario(this.colorSecundario);
                vShape.add(curva);
                break;

            case 6:
                System.out.println("Introduzca una forma por favor");
                break;
        }

    }

    /**
     *
     * @param s Shape
     */
    public void updateShape(Shape s) {

        if (s instanceof Line2D) {
            LineaPropia linea = (LineaPropia) s;
            if (!editar) {
                linea.setLine(punto, fin);
            } else {
                /*
                 fin = linea.getP2();
                 double distancia = linea.ptLineDist(fin);
                 linea.setLocation(fin, distancia);
                 */
                linea.setLocation(fin);
            }
        }

        //Añadimos el Double para que no se confunda con el punto que es un rectangule float
        if (s instanceof Rectangulo) {
            Rectangulo rect = (Rectangulo) s; //Asignamos rectangulo al shape para crear diagonal
            if (!editar) { //Si no hay que editar
                rect.setFrameFromDiagonal(punto, fin);
            } else {
                Double ancho, alto;
                alto = rect.getHeight();
                ancho = rect.getWidth();
                rect.setRect(fin.getX(), fin.getY(), ancho, alto);
                rect.setTransparente(rect.getTransparente());
            }
        } else if (s instanceof Ellipse2D) {
            Ellipse2D elli = (Ellipse2D) s;

            if (!editar) {
                elli.setFrameFromDiagonal(punto, fin);
            } else {
                Double ancho, alto;
                alto = elli.getHeight();
                ancho = elli.getWidth();
                elli.setFrame(fin.getX(), fin.getY(), ancho, alto);
            }
        }

        if (s instanceof RoundRectangle2D.Double) {
            RoundRectangle2D rect = (RoundRectangle2D) s; //Asignamos rectangulo al shape para crear diagonal
            if (!editar) { //Si no hay que editar
                rect.setFrameFromDiagonal(punto, fin);
            } else {
                Double ancho, alto;
                alto = rect.getHeight();
                ancho = rect.getWidth();
                rect.setRoundRect(fin.getX(), fin.getY(), ancho, alto, rect.getArcWidth(), rect.getArcHeight());

            }
        }

        if (s instanceof Curva) {
            Curva linea = (Curva) s;
            if (!editar) {
                Point2D p = linea.calcularPuntoCtlr(punto, fin);
                linea.setCurve(punto, p, fin);
            } else {
                /*
                 fin = linea.getP2();
                 double distancia = linea.ptLineDist(fin);
                 linea.setLocation(fin, distancia);
                 */
                linea.setLocation(fin);

            }
        }

    }

    /**
     *
     * @param s
     * @return Color
     */
    public Color getColor(Shape s) {

        if (s instanceof Rectangulo) {
            Rectangulo figura = (Rectangulo) s;
            this.color = figura.getColor();
            return this.color;
        }

        if (s instanceof RectanguloRedondo) {
            RectanguloRedondo figura = (RectanguloRedondo) s;
            this.color = figura.getColor();
            return this.color;
        } else if (s instanceof LineaPropia) {
            LineaPropia figura = (LineaPropia) s;
            this.color = figura.getColor();
            return this.color;
        } else if (s instanceof Punto) {
            Punto figura = (Punto) s;
            this.color = figura.getColor();
            return this.color;
        } else if (s instanceof Circulo) {
            Circulo figura = (Circulo) s;
            this.color = figura.getColor();
            return this.color;
        } else if (s instanceof Curva) {
            Curva figura = (Curva) s;
            this.color = figura.getColor();
            return this.color;
        }
        return null;

    }

    

    /**
     *
     * @return vShape con el vector formas
     */
    
    public List<Shape> getVShape() {
        return this.vShape;
    }

    //Devuelve la forma seleccionada
    private Shape getSelectedShape(Point2D p) {
        for (Shape s : vShape) {
            if (s.contains(p)) {
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param estado
     */
    public void setEditarLienzo(boolean estado) {
        this.editar = estado; //Actualizamos el estado
    }

    /**
     *
     * @return
     */
    public boolean getEditar() {
        return this.editar;
    }

    //Recibe un mensaje y establece la fuente

    /**
     *
     * @param fuenteRecibida
     */
        public void setFuente(String fuenteRecibida) {
        this.tipoFuente = fuenteRecibida;
        this.repaint();
    }

    /**
     *
     * @param forma
     */
    public void setForma(int forma) {
        /*Comprobamos que esa forma no esta seleccionada
         si ya lo esta, ponemos la forma a 4, para que no pinte nada
         */
        this.forma = forma;

    }

    /**
     *
     * @return
     */
    public int getForma() {
        return this.forma;
    }

    /**
     *
     * @param var
     */
    public void setTransparente(boolean var) {
        transparente = var;
        this.repaint();
    }

    /**
     *
     * @return
     */
    public boolean getTransparente() {
        return this.transparente;
    }

    /**
     *
     * @param var
     */
    public void setRelleno(boolean var) {
        relleno = var;
        this.repaint();
    }

    /**
     *
     * @return
     */
    public boolean getRelleno() {
        return this.relleno;
    }

    /**
     *
     * @param var
     */
    public void setAlisado(boolean var) {
        alisado = var;
        this.repaint();
    }

    /**
     *
     * @return
     */
    public boolean getAlisado() {
        return this.alisado;
    }

    /**
     *
     * @param ancho
     */
    public void setStroke(int ancho) {
        this.anchoStroke = ancho; //Guardamos el ancho del Stroke
        stroke = new BasicStroke(ancho);
        this.repaint();
    }

    /**
     *
     * @return
     */
    public int getStroke() {
        return this.anchoStroke;
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return this.color;
    }
    
    /**
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
        this.repaint();
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
     * @param colorSecundario
     */
    public void setColorSecundario(Color colorSecundario) {
        this.colorSecundario = colorSecundario;
    }

    /**
     * 
     * @return 
     */
     public boolean isLineadiscontinua() {
        return lineadiscontinua;
    }

     /**
      * 
      * @param estado 
      */
    public void setLineadiscontinua(boolean estado) {
        this.lineadiscontinua = estado;
        this.repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
      
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FormListener formListener = new FormListener();

        setBackground(java.awt.Color.lightGray);
        setMinimumSize(new java.awt.Dimension(200, 300));
        addMouseMotionListener(formListener);
        addMouseListener(formListener);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
        FormListener() {}
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMouseClicked(evt);
            }
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        public void mousePressed(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMousePressed(evt);
            }
        }

        public void mouseReleased(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMouseReleased(evt);
            }
        }

        public void mouseDragged(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == Lienzo.this) {
                Lienzo.this.formMouseDragged(evt);
            }
        }

        public void mouseMoved(java.awt.event.MouseEvent evt) {
        }
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("empty-statement")
    /**
     * 
     */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        bound = null; //Deseleccionamos la figura y asi no pinta seleccion
        if (this.forma != 6) {
            fin = evt.getPoint();

            if (editar) {
                this.updateShape(s);
            } else {
                if (vShape.size() > 0) //Seleccionamos el ultimo Shape del vector de vShape
                {
                    this.updateShape(this.vShape.get(vShape.size() - 1));
                }
            }

            this.repaint();
        }
    }//GEN-LAST:event_formMouseDragged
/**
 * 
 * @param evt 
 */
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        punto = evt.getPoint();
        fin = evt.getPoint();
        bound=null; //Deseleccionar la figura
        //Seleccionamos la forma
        if (this.forma != 6) {
            s = this.getSelectedShape(punto);
            if (!editar) {
                this.createShape(forma);
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
      
    }//GEN-LAST:event_formMouseReleased
/**
 * 
 * @param evt 
 */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        punto = evt.getPoint();
        fin = evt.getPoint();
        if (this.forma != 6) {
            if (this.getSelectedShape(punto)!=null){
                //Seleccionamos el shape
            s = this.getSelectedShape(punto);
            bound =  s.getBounds2D();
            bound.setFrame(bound.getX()-10, bound.getY()-10,bound.getWidth()+20,bound.getHeight()+20);
            }
            else{
                bound=null;
                s=null;
             
            }
        }
        //Pinta el bound alrededor de la forma
            this.repaint();
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    //Obtiene el objeto grapchis2d de la figura

    /**
     *
     * @param s
     */
        public void inicializarGraficosShape(Shape s) {
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

        /**
         * 
         * @param s 
         */
    private void actualizarPropiedades(Shape s) {
        if (s instanceof Rectangulo) {
            Rectangulo figura = (Rectangulo) s;            
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setRelleno(this.relleno);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
            
        } 
        
       if (s instanceof RectanguloRedondo) {
            RectanguloRedondo figura = (RectanguloRedondo) s;
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setRelleno(this.relleno);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
            
        } 
        else if (s instanceof LineaPropia) {
            LineaPropia figura = (LineaPropia) s;
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
            
        }
        else if (s instanceof Punto) {
            Punto figura = (Punto) s;
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setRelleno(this.relleno);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
           
        }
        else if (s instanceof Circulo) {
            Circulo figura = (Circulo) s;
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setRelleno(this.relleno);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
           
        }
        else if (s instanceof Curva) {
            Curva figura = (Curva) s;
            figura.setAlisado(this.alisado);
            figura.setAncho(this.anchoStroke);
            figura.setColor(this.color);
            figura.setRelleno(this.relleno);
            figura.setTransparente(this.transparente);
            figura.setColorSecundario(this.colorSecundario);
        }
  
  

    }
}
