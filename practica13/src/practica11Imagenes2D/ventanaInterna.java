/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica11Imagenes2D;

/**
 *
 * @author rafa
 * 
 * ventanaInterna.
 * 
 * ventanaInterna, dentro de la cual tenemos un Lienzo2DImagen sobre el que pintamos.
 */
public class ventanaInterna extends javax.swing.JInternalFrame {

   public Lienzo2DImagen li;
   private ventana parent;
    public ventanaInterna(ventana v) {
        initComponents();
        li = this.lienzo2DImagen;
        this.parent = v;
        li.setForma(4);
    }


    public Lienzo2DImagen getLienzo(){
        if (this.lienzo2DImagen!=null)
        li = this.lienzo2DImagen;
        
        return this.li;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lienzo2DImagen = new practica11Imagenes2D.Lienzo2DImagen();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setAlignmentX(10.5F);
        setAlignmentY(10.5F);
        setPreferredSize(new java.awt.Dimension(350, 300));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseMoved(evt);
            }
        });

        lienzo2DImagen.setMinimumSize(new java.awt.Dimension(300, 300));
        lienzo2DImagen.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lienzo2DImagenMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout lienzo2DImagenLayout = new javax.swing.GroupLayout(lienzo2DImagen);
        lienzo2DImagen.setLayout(lienzo2DImagenLayout);
        lienzo2DImagenLayout.setHorizontalGroup(
            lienzo2DImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );
        lienzo2DImagenLayout.setVerticalGroup(
            lienzo2DImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(lienzo2DImagen);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Evento que imprime la posicion del raton
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
       this.parent.setTextoLabel(evt.getX()+" "+evt.getY());
    }//GEN-LAST:event_formMouseMoved
    //Evento que imprime la posicion del raton
    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
         this.parent.setTextoLabel(evt.getX()+" "+evt.getY());
    }//GEN-LAST:event_formMouseEntered
    //Evento que imprime la posicion del raton
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
       this.parent.setTextoLabel(evt.getX()+" "+evt.getY());
    }//GEN-LAST:event_formMouseReleased
    //Evento que imprime la posicion del raton
    private void jScrollPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseMoved
       this.formMouseMoved(evt);
    }//GEN-LAST:event_jScrollPane1MouseMoved
    //Evento que imprime la posicion del raton
    private void lienzo2DImagenMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lienzo2DImagenMouseMoved
       this.formMouseMoved(evt);
    }//GEN-LAST:event_lienzo2DImagenMouseMoved

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
      
    }//GEN-LAST:event_formFocusGained

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        this.parent.getVentana();
        this.parent.restaurarEstados();
    }//GEN-LAST:event_formInternalFrameActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private practica11Imagenes2D.Lienzo2DImagen lienzo2DImagen;
    // End of variables declaration//GEN-END:variables
}
