/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica11Imagenes2D;

import java.awt.Color;
import java.awt.image.*;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import rlg.*; //Donde guardo las funciones de algunas imagenes
import sm.image.*;

/**
 *<p> Clase Ventana Principal.</p>
 * 
 * @author Rafel Lachica Garrido.
 * 
 * Ventana principal que contiene todos los elementos y herramientas para el dibujado.
 * Dentro de la ventan principal tendremos las { ventanaInterna} , los paneles con las herramientas
 * tanto de dibujado como de imagen y video.
 * La clase {ventanaInterna} en si es una ventanaInterna de imagen.
 * <p>
 * Tenemos varios atributos de estado como verBarraFormas, verBarraEstado, verBarraAtributos que sirven 
 * para tener un estado que luego usamos para mostrar u ocultar estas barras.
 * </p>
 * 
 * <p>
 * Tenemos tambien una lista con las ventanasInternas de dibujo que se llama ventanasInternas.
 * Ademas de imagen auxiliar imgOriginal que la utilizamos para el brillo y filtros con deslizadores
 * </p>
 */

public class ventana extends javax.swing.JFrame {

    private boolean verBarraFormas;
    private boolean verBarraAtributos;
    private boolean verBarraEstado;
    private boolean editar; //Estado del boton editar
    private List<ventanaInterna> ventanasInternas; //Array de ventanasInternas
    private ventanaInterna vi;
    private int brilloImagen;
    private BufferedImage imgOriginal; //Imagen original
    private funcionesImagenes funcionesImg; //Clase auxiliar que implementa algunas operaciones imagenes
    private UmbralizacionOp umbralOp;
    private boolean umbralColor; //Umbralizacion a color
    private boolean colorDegradado;

    /**
     * Clase ventana principal. Dentro de ella tenemos varias ventanas internas,
     * dentro de un escritorio, y varios paneles con funciones.
     * <p>
     * Ponemos al principio todo a false por defecto para que no muestre nada.
     * </p>
     */
    public ventana() {
        initComponents();
        verBarraFormas = verBarraAtributos = verBarraEstado = true;
        ventanasInternas = new ArrayList();

        //Por defecto esta todo oculto
        this.setBarraFormas(false);
        this.setBarraAtributos(false);

        //Añadimos una ventana interna
        this.nuevoActionPerformed(null);

        //Objeto llamar clase auxiliar de algunas funciones imagenes
        funcionesImg = new funcionesImagenes();

    }

    
    /**
     * Pone el estado del boton editar cuando es seleccionado
     * El estado al que se pone el boton se lo pasa el boton editar con su correspondiente
     * evento,pasando si esta o no seleccionado    
     * @param selected
     */
    public void setEditar(boolean selected) {
        this.botonEditar.setSelected(selected);
        this.editar = this.getEditar();
        if (vi != null) {
            vi.li.setEditarLienzo(editar);
        }

    }

    /**
     * Devuelve si el botonEditar esta o no seleccionado
     * @return editar
     * 
     */
    public boolean getEditar() {
        this.editar = this.botonEditar.isSelected();
        return editar;
    }

    /**
     * Devuelve el estado de la barra formas,si esta false se supone oculto
     * @return verBarrarFormas
     */
    public boolean getBarraFormas() {
        return this.verBarraFormas;
    }

    /**
     * Redimensiona una Imagen a las dimensiones introducidas ancho y alto
     * @param ancho ancho a reescalar
     * @param alto  alto a reescalar
     */
    public void reescalarImagen(int ancho, int alto) {
        vi.getLienzo().reescale(ancho, alto); //Llama al lienzo reescale la imagen
        //Una vez reescalado ponemos la visibilidad a true en caso de que estuviera a false
        vi.setVisible(true);
    }

    /**
     * Pone la variable verBarraFormas segundo el estado introducido.
     * <p>Lo usamos para saber si se esta mostrando o no la barra o para cuando
     * seleccionamos mostrarBarra, si esta true la oculta y pone estado a false y al revés si esta a true.
     * @param estado estado true/false al que ponemos verBarraFormas
     */
    public void setBarraFormas(boolean estado) {
        this.verBarraFormas = estado;
        this.panelFormas.setVisible(estado);
    }

    /**
     * Obtiene la variable booleana verBarraAtributos
     * @return verBarraAtributos variable booleana
     */
    public boolean getBarraAtributos() {
        return this.verBarraAtributos;
    }

    /**
     * Pone el estado de visibilidad de la barra de atributos y el estado de la variable verBarraAtributos
     * @param estado Estado true/false al que se one la variable verBarraAtributos y panleAtributos.setVisible
     */
    public void setBarraAtributos(boolean estado) {
        this.verBarraAtributos = estado;
        this.panelAtributos.setVisible(estado);
    }

    /**
     * Obtiene la variable verBarraEstado     
     * @return verBarraEstado variable booleana
     */
    public boolean getBarraEstado() {
        return this.verBarraEstado;
    }

    /**
     * Pone la variable verBarraEstado y visibilidad de la barraEstado
     * @param estado Estado true/false al que se pone la variable interna verBarraEstado y el estado barraEstado.setVisible()
     */
    public void setBarraEstado(boolean estado) {
        this.verBarraEstado = estado;
        this.barraEstado.setVisible(estado);
    }

    
    /**
     * Funcion auxiliar que devuelve la ventana interna seleccionada
     * @return ventanaInterna que esta seleccionada
     */
    public ventanaInterna getVentana() {
        //Provisional hay que implementar clase ventanaInterna y que las demas hereden
        if (escritorio.getSelectedFrame() instanceof ventanaInterna) {
            return (ventanaInterna) escritorio.getSelectedFrame();
        } else {
            return null;
        }
    }

    /**
     * Devuelve la imagen que esta sobre el lienzo de la ventanaInterna seleccionada
     * @return BufferedImage
     */
    public BufferedImage getImage() {
        escritorio.setSelectedFrame(vi);
        return this.getVentana().getLienzo().getImage(true);

    }

    /**
     * Devuelve la ImagenOriginal
     * @return imagenOriginal
     */
    public BufferedImage getImagenOriginal() {
        return this.imgOriginal;
    }

    /**
     * Actualiza la imagenOriginal por la imagenDestino introducida
     * @param imgdest BufferedImage imagen destino
     */
    public void actualizarImagenOriginal(BufferedImage imgdest) {
        this.imgOriginal = imgdest;
    }

    
    /**
     * RestaurarEstados Restaura los estados de las ventanas que hemos vuelto a
     * seleccionar y que estaban abiertas
     */
    public void restaurarEstados() {
        if (this.getVentana() != null) {
            this.botonAlisar.setSelected(this.getVentana().getLienzo().getAlisado());
            this.edicionSeleccion.setSelected(this.getVentana().getLienzo().getEditar());
            this.grosorBoton.setValue(this.getVentana().getLienzo().getStroke());
            this.botonTransparencia.setSelected(this.getVentana().getLienzo().getTransparente());
            this.botonRelleno.setSelected(this.getVentana().getLienzo().getRelleno());
            this.botonColor.setBackground(this.getVentana().getLienzo().getColor());
            this.vi = this.getVentana();
            vi.li = vi.getLienzo();
            switch (vi.li.getForma()) {
                case 0:
                    this.botonRectanguloActionPerformed(null);
                    break;

                case 1:
                    this.botonOvaloActionPerformed(null);
                    break;

                case 2:
                    this.botonLineaActionPerformed(null);
                    break;

                case 3:
                    this.botonLapizActionPerformed(null);
                    break;

                //Forma sin seleccionar
                case 4:
                    //Todos a falso
                    this.botonLapiz.setSelected(false);
                    this.botonLinea.setSelected(false);
                    this.botonOvalo.setSelected(false);
                    this.botonRectangulo.setSelected(false);
                    break;
            }

        }
    }

    /**
     * setEstadosNuevos Funcion que pone todos los estados por defecto de la
     * ventana interna creada recientemente
     * Estos estados son tambien como que pinte por defecto punto y que todos
     * atributos como relleno y transparencia a falso
     */
    public void setEstadosNuevos() {
        if (this.getVentana() != null) {
            this.botonAlisar.setSelected(false);
            this.edicionSeleccion.setSelected(false);
            this.grosorBoton.setValue(1);
            this.botonTransparencia.setSelected(false);
            this.botonRelleno.setSelected(false);
            this.setBarraFormas(false);

            this.vi = this.getVentana();
            vi.li = vi.getLienzo();
            this.vi.li.setForma(3);

            //Todos a falso menos lapiz
            this.botonLapiz.setSelected(true);
            this.botonLinea.setSelected(false);
            this.botonOvalo.setSelected(false);
            this.botonRectangulo.setSelected(false);
            this.botonRectanguloRedondo.setSelected(false);
            this.botonArco.setSelected(false);
        }
    }

    /**
     * Pone un texto en el label Figura
     * Si se selecciona el lapiz por ejemplo, pone lapiz etc.
     * @param str String con el texto
     */
    public void setTextoLabel(String str) {
        this.figura.setText(str);
    }

    /**
     * Funcion getColorDegradado devuelve el estado del boolean colorDegradado
     * que es usado para saber si el boton ha sido clickado y poner el segundo
     * color degradado
     *
     * @return colorDegradado booleano si el degradado esta o no seleccionado
     */
    public boolean getColorDegradado() {
        return this.colorDegradado;
    }

    /**
     * setColorDegradado Pone el estado del colorDegradado
     *
     * @param estado booleano con estado al que se pone la variable colorDegradado
     */
    public void setColorDegradado(boolean estado) {
        this.colorDegradado = estado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ventanaHistograma = new javax.swing.JDialog();
        panelAtributos = new javax.swing.JPanel();
        panelBrillo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        botonDeslizadorBrillo = new javax.swing.JSlider();
        panelFiltro = new javax.swing.JPanel();
        botonFiltro = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        panelContraste = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        contrasteBoton = new javax.swing.JButton();
        iluminarBoton = new javax.swing.JButton();
        oscurecerBoton = new javax.swing.JButton();
        panelRotacion = new javax.swing.JPanel();
        deslizadorRot = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        botonRotar90 = new javax.swing.JButton();
        botonRotar180 = new javax.swing.JButton();
        botonRotar270 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        panelEscala = new javax.swing.JPanel();
        botonAumentarReescalado = new javax.swing.JButton();
        botonDisminuirReescalado = new javax.swing.JButton();
        botonSeno = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        botonBinarioSuma = new javax.swing.JButton();
        botonBinarioResta = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        umbralizacionRojo = new javax.swing.JToggleButton();
        umbralizacionVerde = new javax.swing.JToggleButton();
        botonUmbral = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        umbralizacionAzul = new javax.swing.JToggleButton();
        escritorio = new javax.swing.JDesktopPane();
        barraEstado = new javax.swing.JPanel();
        figura = new javax.swing.JLabel();
        pixelRGB = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        panelFormas = new javax.swing.JPanel();
        panelAudio_Video = new javax.swing.JPanel();
        botonCaptura = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        barraFormas = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        botonLapiz = new javax.swing.JToggleButton();
        botonLinea = new javax.swing.JToggleButton();
        botonOvalo = new javax.swing.JToggleButton();
        botonRectangulo = new javax.swing.JToggleButton();
        botonRectanguloRedondo = new javax.swing.JToggleButton();
        botonArco = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        panelArchivos = new javax.swing.JPanel();
        abrirBoton = new javax.swing.JButton();
        guardarBoton = new javax.swing.JButton();
        nuevoBoton = new javax.swing.JButton();
        edicionSeleccion = new javax.swing.JToggleButton();
        botonRelleno = new javax.swing.JToggleButton();
        botonTransparencia = new javax.swing.JToggleButton();
        botonAlisar = new javax.swing.JToggleButton();
        panelGrosor = new javax.swing.JPanel();
        grosorBoton = new javax.swing.JSpinner();
        botonDisc = new javax.swing.JCheckBox();
        paletaColores = new javax.swing.JPanel();
        rojo = new javax.swing.JButton();
        verde = new javax.swing.JButton();
        azul = new javax.swing.JButton();
        negro = new javax.swing.JButton();
        amarillo = new javax.swing.JButton();
        blanco = new javax.swing.JButton();
        botonColor = new javax.swing.JButton();
        colorSecundario = new javax.swing.JButton();
        checkDiscontinuo = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        botonArchivo = new javax.swing.JMenu();
        nuevo = new javax.swing.JMenuItem();
        abrir = new javax.swing.JMenuItem();
        guardar = new javax.swing.JMenuItem();
        acercade = new javax.swing.JMenuItem();
        botonEditar = new javax.swing.JMenu();
        botonBarraEstado = new javax.swing.JMenuItem();
        botonBarraFormas = new javax.swing.JMenuItem();
        botonBarraAtributos = new javax.swing.JMenuItem();
        botonImagen = new javax.swing.JMenu();
        botonModificarTamaño = new javax.swing.JMenuItem();
        botonReescalado = new javax.swing.JMenuItem();
        botonAffineTransformOp = new javax.swing.JMenuItem();
        botonLookUpOp = new javax.swing.JMenuItem();
        botonBandCombineOp = new javax.swing.JMenuItem();
        botonColorConvertOp = new javax.swing.JMenuItem();
        duplicarImagen = new javax.swing.JMenuItem();
        escalaGrises = new javax.swing.JMenuItem();
        negativo = new javax.swing.JMenuItem();
        infrarrojos = new javax.swing.JMenuItem();
        botonAudio = new javax.swing.JMenu();
        reproducirAudio = new javax.swing.JMenuItem();
        grabarAudio = new javax.swing.JMenuItem();

        javax.swing.GroupLayout ventanaHistogramaLayout = new javax.swing.GroupLayout(ventanaHistograma.getContentPane());
        ventanaHistograma.getContentPane().setLayout(ventanaHistogramaLayout);
        ventanaHistogramaLayout.setHorizontalGroup(
            ventanaHistogramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        ventanaHistogramaLayout.setVerticalGroup(
            ventanaHistogramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(200, 105));

        panelAtributos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelAtributos.setMinimumSize(new java.awt.Dimension(200, 50));
        panelAtributos.setPreferredSize(new java.awt.Dimension(200, 50));
        panelAtributos.setLayout(new javax.swing.BoxLayout(panelAtributos, javax.swing.BoxLayout.LINE_AXIS));

        panelBrillo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("Brillo");

        botonDeslizadorBrillo.setMaximum(255);
        botonDeslizadorBrillo.setMinimum(-255);
        botonDeslizadorBrillo.setValue(0);
        botonDeslizadorBrillo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonDeslizadorBrilloStateChanged(evt);
            }
        });
        botonDeslizadorBrillo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                botonDeslizadorBrilloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                botonDeslizadorBrilloFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panelBrilloLayout = new javax.swing.GroupLayout(panelBrillo);
        panelBrillo.setLayout(panelBrilloLayout);
        panelBrilloLayout.setHorizontalGroup(
            panelBrilloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBrilloLayout.createSequentialGroup()
                .addGroup(panelBrilloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBrilloLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(botonDeslizadorBrillo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );
        panelBrilloLayout.setVerticalGroup(
            panelBrilloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBrilloLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonDeslizadorBrillo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelAtributos.add(panelBrillo);

        panelFiltro.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        botonFiltro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "media", "laplaciano", "binomial", "relieve", "enfoque", "media5x5", "media7x7" }));
        botonFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFiltroActionPerformed(evt);
            }
        });

        jLabel3.setText("Filtro");

        javax.swing.GroupLayout panelFiltroLayout = new javax.swing.GroupLayout(panelFiltro);
        panelFiltro.setLayout(panelFiltroLayout);
        panelFiltroLayout.setHorizontalGroup(
            panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltroLayout.createSequentialGroup()
                .addGroup(panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltroLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltroLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        panelFiltroLayout.setVerticalGroup(
            panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltroLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAtributos.add(panelFiltro);

        panelContraste.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Contraste");

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        contrasteBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/contraste.png"))); // NOI18N
        contrasteBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contrasteBotonActionPerformed(evt);
            }
        });
        jPanel1.add(contrasteBoton);

        iluminarBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/iluminar.png"))); // NOI18N
        iluminarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iluminarBotonActionPerformed(evt);
            }
        });
        jPanel1.add(iluminarBoton);

        oscurecerBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/oscurecer.png"))); // NOI18N
        oscurecerBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oscurecerBotonActionPerformed(evt);
            }
        });
        jPanel1.add(oscurecerBoton);

        javax.swing.GroupLayout panelContrasteLayout = new javax.swing.GroupLayout(panelContraste);
        panelContraste.setLayout(panelContrasteLayout);
        panelContrasteLayout.setHorizontalGroup(
            panelContrasteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelContrasteLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContrasteLayout.setVerticalGroup(
            panelContrasteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContrasteLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAtributos.add(panelContraste);

        panelRotacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        deslizadorRot.setMaximum(270);
        deslizadorRot.setPaintLabels(true);
        deslizadorRot.setPaintTicks(true);
        deslizadorRot.setSnapToTicks(true);
        deslizadorRot.setToolTipText("Rotacion Grados");
        deslizadorRot.setValue(0);
        deslizadorRot.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                deslizadorRotStateChanged(evt);
            }
        });
        deslizadorRot.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                deslizadorRotFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                deslizadorRotFocusLost(evt);
            }
        });

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        botonRotar90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/rotacion90.png"))); // NOI18N
        botonRotar90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotar90ActionPerformed(evt);
            }
        });
        jPanel2.add(botonRotar90);

        botonRotar180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/rotacion180.png"))); // NOI18N
        botonRotar180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotar180ActionPerformed(evt);
            }
        });
        jPanel2.add(botonRotar180);

        botonRotar270.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/rotacion270.png"))); // NOI18N
        botonRotar270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotar270ActionPerformed(evt);
            }
        });
        jPanel2.add(botonRotar270);

        jLabel5.setText("Rotacion");

        javax.swing.GroupLayout panelRotacionLayout = new javax.swing.GroupLayout(panelRotacion);
        panelRotacion.setLayout(panelRotacionLayout);
        panelRotacionLayout.setHorizontalGroup(
            panelRotacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRotacionLayout.createSequentialGroup()
                .addGroup(panelRotacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRotacionLayout.createSequentialGroup()
                        .addComponent(deslizadorRot, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRotacionLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRotacionLayout.setVerticalGroup(
            panelRotacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRotacionLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelRotacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deslizadorRot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelAtributos.add(panelRotacion);

        panelEscala.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelEscala.setLayout(new javax.swing.BoxLayout(panelEscala, javax.swing.BoxLayout.LINE_AXIS));

        botonAumentarReescalado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/aumentar.png"))); // NOI18N
        botonAumentarReescalado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAumentarReescaladoActionPerformed(evt);
            }
        });
        panelEscala.add(botonAumentarReescalado);

        botonDisminuirReescalado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/disminuir.png"))); // NOI18N
        botonDisminuirReescalado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDisminuirReescaladoActionPerformed(evt);
            }
        });
        panelEscala.add(botonDisminuirReescalado);

        panelAtributos.add(panelEscala);

        botonSeno.setText("seno");
        botonSeno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSenoActionPerformed(evt);
            }
        });
        panelAtributos.add(botonSeno);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        botonBinarioSuma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/suma.png"))); // NOI18N
        botonBinarioSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBinarioSumaActionPerformed(evt);
            }
        });

        botonBinarioResta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconosPractica11/resta.png"))); // NOI18N
        botonBinarioResta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBinarioRestaActionPerformed(evt);
            }
        });

        jLabel6.setText("Binarias");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(botonBinarioSuma, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonBinarioResta, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonBinarioSuma, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBinarioResta, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        panelAtributos.add(jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new java.awt.BorderLayout());

        umbralizacionRojo.setBackground(new java.awt.Color(255, 0, 0));
        umbralizacionRojo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                umbralizacionRojoActionPerformed(evt);
            }
        });
        jPanel4.add(umbralizacionRojo, java.awt.BorderLayout.PAGE_END);

        umbralizacionVerde.setBackground(new java.awt.Color(0, 255, 0));
        umbralizacionVerde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                umbralizacionVerdeActionPerformed(evt);
            }
        });
        jPanel4.add(umbralizacionVerde, java.awt.BorderLayout.LINE_END);

        botonUmbral.setMaximum(255);
        botonUmbral.setValue(0);
        botonUmbral.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonUmbralStateChanged(evt);
            }
        });
        botonUmbral.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                botonUmbralFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                botonUmbralFocusLost(evt);
            }
        });
        jPanel4.add(botonUmbral, java.awt.BorderLayout.CENTER);

        jLabel7.setText("Umbralizacion");
        jPanel4.add(jLabel7, java.awt.BorderLayout.PAGE_START);

        umbralizacionAzul.setBackground(new java.awt.Color(0, 0, 255));
        umbralizacionAzul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                umbralizacionAzulActionPerformed(evt);
            }
        });
        jPanel4.add(umbralizacionAzul, java.awt.BorderLayout.LINE_START);

        panelAtributos.add(jPanel4);

        javax.swing.GroupLayout barraEstadoLayout = new javax.swing.GroupLayout(barraEstado);
        barraEstado.setLayout(barraEstadoLayout);
        barraEstadoLayout.setHorizontalGroup(
            barraEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraEstadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(figura, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pixelRGB, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        barraEstadoLayout.setVerticalGroup(
            barraEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraEstadoLayout.createSequentialGroup()
                .addGroup(barraEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pixelRGB, javax.swing.GroupLayout.DEFAULT_SIZE, 12, Short.MAX_VALUE)
                    .addComponent(figura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        botonCaptura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/capture.png"))); // NOI18N
        botonCaptura.setToolTipText("screenshot del video");
        botonCaptura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCapturaActionPerformed(evt);
            }
        });

        jLabel8.setText("Captura");

        javax.swing.GroupLayout panelAudio_VideoLayout = new javax.swing.GroupLayout(panelAudio_Video);
        panelAudio_Video.setLayout(panelAudio_VideoLayout);
        panelAudio_VideoLayout.setHorizontalGroup(
            panelAudio_VideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAudio_VideoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCaptura)
                .addContainerGap(225, Short.MAX_VALUE))
        );
        panelAudio_VideoLayout.setVerticalGroup(
            panelAudio_VideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAudio_VideoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAudio_VideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCaptura)
                    .addComponent(jLabel8))
                .addGap(33, 33, 33))
        );

        barraFormas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator2);

        botonLapiz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/punto.png"))); // NOI18N
        botonLapiz.setToolTipText("Dibujar puntos");
        botonLapiz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLapizActionPerformed(evt);
            }
        });
        jToolBar1.add(botonLapiz);

        botonLinea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/linea.png"))); // NOI18N
        botonLinea.setToolTipText("Linea");
        botonLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLineaActionPerformed(evt);
            }
        });
        jToolBar1.add(botonLinea);

        botonOvalo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/elipse.png"))); // NOI18N
        botonOvalo.setToolTipText("Ovalo");
        botonOvalo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOvaloActionPerformed(evt);
            }
        });
        jToolBar1.add(botonOvalo);

        botonRectangulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/rectangulo.png"))); // NOI18N
        botonRectangulo.setToolTipText("Rectangulo");
        botonRectangulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRectanguloActionPerformed(evt);
            }
        });
        jToolBar1.add(botonRectangulo);

        botonRectanguloRedondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/roundrectangle.png"))); // NOI18N
        botonRectanguloRedondo.setToolTipText("rectangulo redondeado");
        botonRectanguloRedondo.setFocusable(false);
        botonRectanguloRedondo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRectanguloRedondo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRectanguloRedondo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRectanguloRedondoActionPerformed(evt);
            }
        });
        jToolBar1.add(botonRectanguloRedondo);

        botonArco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/quadCurve.gif"))); // NOI18N
        botonArco.setToolTipText("arco");
        botonArco.setFocusable(false);
        botonArco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonArco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonArco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonArcoActionPerformed(evt);
            }
        });
        jToolBar1.add(botonArco);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 10));
        jSeparator1.setMinimumSize(new java.awt.Dimension(20, 10));
        jToolBar1.add(jSeparator1);
        jToolBar1.add(jSeparator3);
        jToolBar1.add(jSeparator4);

        barraFormas.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 270, 29));

        panelArchivos.setLayout(new javax.swing.BoxLayout(panelArchivos, javax.swing.BoxLayout.LINE_AXIS));

        abrirBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/abrir.png"))); // NOI18N
        abrirBoton.setFocusable(false);
        abrirBoton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        abrirBoton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        abrirBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirBotonActionPerformed(evt);
            }
        });
        panelArchivos.add(abrirBoton);

        guardarBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/guardar.png"))); // NOI18N
        guardarBoton.setFocusable(false);
        guardarBoton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        guardarBoton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        guardarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBotonActionPerformed(evt);
            }
        });
        panelArchivos.add(guardarBoton);

        nuevoBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/nuevo.png"))); // NOI18N
        nuevoBoton.setFocusable(false);
        nuevoBoton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nuevoBoton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nuevoBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoBotonActionPerformed(evt);
            }
        });
        panelArchivos.add(nuevoBoton);

        barraFormas.add(panelArchivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -6, 180, 40));

        edicionSeleccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/seleccion.png"))); // NOI18N
        edicionSeleccion.setToolTipText("Editar y seleccionar una forma");
        edicionSeleccion.setFocusable(false);
        edicionSeleccion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        edicionSeleccion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        edicionSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edicionSeleccionActionPerformed(evt);
            }
        });
        barraFormas.add(edicionSeleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 30, 25));

        botonRelleno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/rellenar.png"))); // NOI18N
        botonRelleno.setToolTipText("Rellenar una forma");
        botonRelleno.setFocusable(false);
        botonRelleno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRelleno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRelleno.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonRellenoStateChanged(evt);
            }
        });
        botonRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRellenoActionPerformed(evt);
            }
        });
        barraFormas.add(botonRelleno, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 40, 25));

        botonTransparencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/transparencia.png"))); // NOI18N
        botonTransparencia.setToolTipText("Transparencia");
        botonTransparencia.setFocusable(false);
        botonTransparencia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonTransparencia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonTransparencia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonTransparenciaStateChanged(evt);
            }
        });
        barraFormas.add(botonTransparencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 30, 25));

        botonAlisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/alisar.png"))); // NOI18N
        botonAlisar.setToolTipText("Alisar una figura");
        botonAlisar.setFocusable(false);
        botonAlisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAlisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAlisar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                botonAlisarStateChanged(evt);
            }
        });
        barraFormas.add(botonAlisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 30, 25));

        panelGrosor.setLayout(new javax.swing.BoxLayout(panelGrosor, javax.swing.BoxLayout.LINE_AXIS));

        grosorBoton.setToolTipText("grosor");
        grosorBoton.setValue(1);
        grosorBoton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                grosorBotonStateChanged(evt);
            }
        });
        panelGrosor.add(grosorBoton);

        botonDisc.setText("discontinuo");
        botonDisc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDiscActionPerformed(evt);
            }
        });
        panelGrosor.add(botonDisc);

        barraFormas.add(panelGrosor, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        paletaColores.setMaximumSize(new java.awt.Dimension(100, 10));
        paletaColores.setRequestFocusEnabled(false);

        rojo.setBackground(new java.awt.Color(255, 0, 0));
        rojo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rojoActionPerformed(evt);
            }
        });

        verde.setBackground(new java.awt.Color(0, 255, 0));
        verde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verdeActionPerformed(evt);
            }
        });

        azul.setBackground(new java.awt.Color(0, 0, 255));
        azul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                azulActionPerformed(evt);
            }
        });

        negro.setBackground(new java.awt.Color(0, 0, 0));
        negro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negroActionPerformed(evt);
            }
        });

        amarillo.setBackground(new java.awt.Color(255, 255, 51));
        amarillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amarilloActionPerformed(evt);
            }
        });

        blanco.setBackground(new java.awt.Color(255, 255, 255));
        blanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blancoActionPerformed(evt);
            }
        });

        botonColor.setText("+");
        botonColor.setToolTipText("paletaColores");
        botonColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonColorActionPerformed(evt);
            }
        });

        colorSecundario.setBackground(new java.awt.Color(255, 255, 255));
        colorSecundario.setToolTipText("color secundario degradado");
        colorSecundario.setMaximumSize(new java.awt.Dimension(44, 25));
        colorSecundario.setMinimumSize(new java.awt.Dimension(44, 25));
        colorSecundario.setPreferredSize(new java.awt.Dimension(44, 25));
        colorSecundario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorSecundarioActionPerformed(evt);
            }
        });

        checkDiscontinuo.setText("Boton + añadir color, dcha. color degradado");

        javax.swing.GroupLayout paletaColoresLayout = new javax.swing.GroupLayout(paletaColores);
        paletaColores.setLayout(paletaColoresLayout);
        paletaColoresLayout.setHorizontalGroup(
            paletaColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paletaColoresLayout.createSequentialGroup()
                .addGroup(paletaColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paletaColoresLayout.createSequentialGroup()
                        .addComponent(rojo)
                        .addGap(0, 0, 0)
                        .addComponent(verde)
                        .addGap(0, 0, 0)
                        .addComponent(azul)
                        .addGap(0, 0, 0)
                        .addComponent(negro)
                        .addGap(0, 0, 0)
                        .addComponent(amarillo)
                        .addGap(0, 0, 0)
                        .addComponent(blanco)
                        .addGap(18, 18, 18)
                        .addComponent(botonColor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkDiscontinuo))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        paletaColoresLayout.setVerticalGroup(
            paletaColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paletaColoresLayout.createSequentialGroup()
                .addComponent(checkDiscontinuo)
                .addGap(7, 7, 7)
                .addGroup(paletaColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rojo)
                    .addComponent(verde)
                    .addComponent(azul)
                    .addComponent(negro)
                    .addComponent(amarillo)
                    .addComponent(botonColor, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blanco))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        barraFormas.add(paletaColores, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, 40));

        javax.swing.GroupLayout panelFormasLayout = new javax.swing.GroupLayout(panelFormas);
        panelFormas.setLayout(panelFormasLayout);
        panelFormasLayout.setHorizontalGroup(
            panelFormasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormasLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(barraFormas, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAudio_Video, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );
        panelFormasLayout.setVerticalGroup(
            panelFormasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormasLayout.createSequentialGroup()
                .addGroup(panelFormasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(barraFormas, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                        .addComponent(jSeparator6))
                    .addComponent(panelAudio_Video, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botonArchivo.setText("Archivo");
        botonArchivo.setToolTipText("Gestion de Archivos");

        nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/nuevo.png"))); // NOI18N
        nuevo.setText("Nuevo");
        nuevo.setToolTipText("Abre una nueva imagen");
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });
        botonArchivo.add(nuevo);

        abrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/abrir.png"))); // NOI18N
        abrir.setText("Abrir");
        abrir.setToolTipText("Abre una imagen ya existente");
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });
        botonArchivo.add(abrir);

        guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/practica11Imagenes2D/iconos/guardar.png"))); // NOI18N
        guardar.setText("Guardar");
        guardar.setToolTipText("Guardar imagen");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        botonArchivo.add(guardar);

        acercade.setText("Acerca de");
        acercade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acercadeActionPerformed(evt);
            }
        });
        botonArchivo.add(acercade);

        jMenuBar1.add(botonArchivo);

        botonEditar.setText("Editar");

        botonBarraEstado.setText("ver Barra Estado");
        botonBarraEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraEstadoActionPerformed(evt);
            }
        });
        botonEditar.add(botonBarraEstado);

        botonBarraFormas.setText("ver Barra Formas");
        botonBarraFormas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraFormasActionPerformed(evt);
            }
        });
        botonEditar.add(botonBarraFormas);

        botonBarraAtributos.setText("ver Barra Herramientas Inferior");
        botonBarraAtributos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBarraAtributosActionPerformed(evt);
            }
        });
        botonEditar.add(botonBarraAtributos);

        jMenuBar1.add(botonEditar);

        botonImagen.setText("Imagen");

        botonModificarTamaño.setText("Tamaño nuevo imagen ALT+T");
        botonModificarTamaño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarTamañoActionPerformed(evt);
            }
        });
        botonImagen.add(botonModificarTamaño);

        botonReescalado.setText("Reescalado");
        botonReescalado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReescaladoActionPerformed(evt);
            }
        });
        botonImagen.add(botonReescalado);

        botonAffineTransformOp.setText("Affine Transform OP");
        botonAffineTransformOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAffineTransformOpActionPerformed(evt);
            }
        });
        botonImagen.add(botonAffineTransformOp);

        botonLookUpOp.setText("LookUp OP");
        botonLookUpOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLookUpOpActionPerformed(evt);
            }
        });
        botonImagen.add(botonLookUpOp);

        botonBandCombineOp.setText("Band Combine OP");
        botonBandCombineOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBandCombineOpActionPerformed(evt);
            }
        });
        botonImagen.add(botonBandCombineOp);

        botonColorConvertOp.setText("Color Convert OP");
        botonColorConvertOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonColorConvertOpActionPerformed(evt);
            }
        });
        botonImagen.add(botonColorConvertOp);

        duplicarImagen.setText("Duplicar Imagen");
        duplicarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicarImagenActionPerformed(evt);
            }
        });
        botonImagen.add(duplicarImagen);

        escalaGrises.setText("Escalara a Grises");
        escalaGrises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                escalaGrisesActionPerformed(evt);
            }
        });
        botonImagen.add(escalaGrises);

        negativo.setText("Negativo");
        negativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negativoActionPerformed(evt);
            }
        });
        botonImagen.add(negativo);

        infrarrojos.setText("Infrarrojos");
        infrarrojos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infrarrojosActionPerformed(evt);
            }
        });
        botonImagen.add(infrarrojos);

        jMenuBar1.add(botonImagen);

        botonAudio.setText("Audio");

        reproducirAudio.setText("abrir");
        reproducirAudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reproducirAudioActionPerformed(evt);
            }
        });
        botonAudio.add(reproducirAudio);

        grabarAudio.setText("guardar");
        grabarAudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabarAudioActionPerformed(evt);
            }
        });
        botonAudio.add(grabarAudio);

        jMenuBar1.add(botonAudio);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addComponent(barraEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAtributos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(escritorio)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelFormas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFormas, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAtributos, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Recibe el evento del boton nuevo y crea una nueva ventana de imagen Pone
     * por defecto los estados y selecciona la forma punto para dibujar sin
     * ningun tipo de relleno
     *
     * @see nuevoBoton
     * @param evt evento
     */
    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        vi = new ventanaInterna(this);
        vi.setTitle("nueva ventana");
        escritorio.add(vi);//Añadimos al escritorio
        vi.setVisible(false);
        this.ventanasInternas.add(vi); //Añadimos la ventana interna a la lista
        this.vi.li.setForma(3); //Ponemos a forma por defecto al punto
        //this.botonLapizActionPerformed(null); //Llamamos a la accion del botonLapiz
        //Para que deseleccione los demas botones
        BufferedImage img;
        img = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        vi.getLienzo().setImage(img);
        vi.getLienzo().lienzoInicio = true;
        this.imgOriginal = this.getImage();
        this.setEstadosNuevos();

        //Pedimos el tamaño de la nueva ventana abierta
        this.botonModificarTamañoActionPerformed(evt);

    }//GEN-LAST:event_nuevoActionPerformed

    /**
     * Accion del botonBarraAtributos
     * Pone a visible u oculto el panel barraAtributos
     * @param evt evento
     */
    private void botonBarraAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraAtributosActionPerformed
        if (this.getBarraAtributos() == false) {
            this.setBarraAtributos(true);
        } else {
            this.setBarraAtributos(false);
        }
    }//GEN-LAST:event_botonBarraAtributosActionPerformed

    /**
     * Accion del botonBarraEstado
     * Pone a visible u oculto el panel barraEstado
     * @param evt evento
     */
    private void botonBarraEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraEstadoActionPerformed
        if (this.getBarraEstado() == false) {
            this.setBarraEstado(true);

        } else {
            this.setBarraEstado(false);

        }
    }//GEN-LAST:event_botonBarraEstadoActionPerformed

    /**
     * Accion botonBarraFormas
     * Pone el estado del la variable barraFormas para que se oculte o no
     * @param evt 
     */
    private void botonBarraFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBarraFormasActionPerformed
        if (this.getBarraFormas() == false) {
            this.setBarraFormas(true);

        } else {
            this.setBarraFormas(false);
        }
    }//GEN-LAST:event_botonBarraFormasActionPerformed

    /**
     * Guarda una imagen que hay en la ventanaInterna seleccionada
     * Aplicamos unos filtros para las extensiones y unos nombres por defecto.
     * @param evt evento que genera la accion
     */
    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        vi = (ventanaInterna) escritorio.getSelectedFrame();
        if (vi != null) {
            JFileChooser dlg = new JFileChooser();
            dlg.setAcceptAllFileFilterUsed(false);
            //Filtros aceptados para la escritura
            FileNameExtensionFilter filterWrite = new FileNameExtensionFilter("jpg, png, jpeg,...",
                    ImageIO.getWriterFormatNames());
            dlg.setFileFilter(filterWrite);
            //Para cada filtro aceptado para la escritura lo añadimos al FileChooser
            for (String s : ImageIO.getWriterFormatNames()) {
                filterWrite = new FileNameExtensionFilter(s, s);
                dlg.addChoosableFileFilter(filterWrite);
            }
            int resp = dlg.showSaveDialog(this);
            if (resp == JFileChooser.APPROVE_OPTION) {
                try {
                    //Ponemos a falso para que no muestre todos los tipos de archivos

                    BufferedImage img = vi.getLienzo().getImage(true);
                    if (img != null) {
                        File f = dlg.getSelectedFile();
                        String extension;
                        /*
                         Obtenemos la extension a traves de los caracteres despues del punto
                         */
                        int i = f.getName().lastIndexOf('.');
                        if (i > 0) {
                            extension = f.getName().substring(i + 1);
                            ImageIO.write(img, extension, f);

                            vi.setTitle(f.getName());

                        } else {
                            extension = "jpg";

                            System.out.println("Formato desconocido introducido, por defecto aplicamos JPG");

                            //Creamos un nuevo archivo con el nombre de la extension
                            String nombre = f.getName() + "." + extension;

                            File ext = new File(nombre);

                            ImageIO.write(img, extension, ext);
                            vi.setTitle(ext.getName());
                        }

                    }
                } catch (Exception ex) {
                    System.err.println("Error al guardar la imagen");
                }
            }
        }
    }//GEN-LAST:event_guardarActionPerformed

    /**
     * Abre un ventanaInterna de segun que tipo segun el filtro seleccionado a la hora de abrir el archivo
     * @param evt evento que genera la accion
     */
    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirActionPerformed
        JFileChooser dlg = new JFileChooser();

        dlg.setAcceptAllFileFilterUsed(false); //Ponemos a falso para solo muestre filtro
        //Añadimos los filtros aceptados para la lectura
        //Filtros imagenes
        FileNameExtensionFilter filterRead = new FileNameExtensionFilter("Formatos Imagenes", ImageIO.getReaderFormatNames());

        //Filtro todo tipo de archivos
        dlg.setAcceptAllFileFilterUsed(true);
        //Añadimos los filtros aceptados para la lectura
        AudioFileFormat.Type[] formatTypes = AudioSystem.getAudioFileTypes();

        //Creamos un array con los tipos de audio y se los asignamos
        String[] types = new String[formatTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = formatTypes[i].getExtension();
        }

        //Filtros audio
        FileNameExtensionFilter filterReadAudio = new FileNameExtensionFilter("Tipos archivos audio", types);
        //Filtros video
        FileNameExtensionFilter filterReadVideo = new FileNameExtensionFilter("Tipos archivos video", "avi", "mp4", "flv");

        //Establecemos los filtros
        dlg.setFileFilter(filterReadAudio);
        dlg.setFileFilter(filterReadVideo);
        dlg.setFileFilter(filterRead);

        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                File f = dlg.getSelectedFile();

                //abrimos el tipo de ventanaInterna segun filtro seleccionado
                if (dlg.getFileFilter() == filterRead) {
                    this.abrirImagen(f);
                } else if (dlg.getFileFilter() == filterReadAudio) {
                    this.abrirAudio(f);
                } else if (dlg.getFileFilter() == filterReadVideo) {
                    this.abrirVideo(f);
                } else {
                    String info;
                    info = "Autor : Filtro incorrecto seleccione uno de los 3 tipos:" + "\n" + " audio, video, imagen" + "\n";
                    JOptionPane.showMessageDialog(this, info, "Acerca de", 1);
                    this.abrirActionPerformed(evt);
                }

            } catch (Exception ex) {
                System.err.println("Error al leer la imagen");
            }
        }
    }//GEN-LAST:event_abrirActionPerformed

    /**
     * Funcion auxiliar que abre un archivo de tipo imagen
     * @param f archivo a abrir
     * @throws IOException excepcion en caso de error
     */
    private void abrirImagen(File f) throws IOException {
        BufferedImage img = ImageIO.read(f);
        vi = new ventanaInterna(this);
        vi.getLienzo().setImage(img);
        this.escritorio.add(vi);
        vi.setTitle(f.getName());
        vi.setVisible(true);
        vi.getLienzo().lienzoInicio = false; //Ponemos el rectangulo gris lienzo a falso par que no lo pinte
        this.setEstadosNuevos();
        this.imgOriginal = img;
        this.getImage();
    }

    /**
     * Funcion auxiliar que abre un archivo de tipo audio
     * @param f archivo a abrir
     * @throws IOException excepcion en caso de error
     */
    private void abrirAudio(File f) {
        ventanaReproduccion vr = new ventanaReproduccion(this, f);
        this.escritorio.add(vr);
        vr.setTitle(f.getName());
        vr.setVisible(true);
        this.setEstadosNuevos();
        
    }

    /**
     * Funcion auxiliar que abre un archivo de tipo imagen
     * @param f archivo a abrir
     * @throws IOException excepcion en caso de error
     */
    private void abrirVideo(File f) {
        ventanaInternaJMFPlayer vr = ventanaInternaJMFPlayer.getInstance(this, f);
        this.escritorio.add(vr);
        vr.setTitle(f.getName());
        vr.setVisible(true);
        this.setEstadosNuevos();
    }

    private void blancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blancoActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.white);
        }
    }//GEN-LAST:event_blancoActionPerformed

    private void amarilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amarilloActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.yellow);
        }
    }//GEN-LAST:event_amarilloActionPerformed

    private void negroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negroActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.black);
        }
    }//GEN-LAST:event_negroActionPerformed

    private void azulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_azulActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.blue);
        }
    }//GEN-LAST:event_azulActionPerformed

    private void verdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verdeActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.green);
        }
    }//GEN-LAST:event_verdeActionPerformed

    private void rojoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rojoActionPerformed
        vi = this.getVentana();
        if (vi != null) {
            vi.li.setColor(Color.red);
        }
    }//GEN-LAST:event_rojoActionPerformed

    private void grosorBotonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_grosorBotonStateChanged
        vi = this.getVentana(); //Obtenemos la ventana
        int grosor;
        grosor = (int) grosorBoton.getValue();
        if (vi != null) {
            vi.li.setStroke(grosor);
        }
    }//GEN-LAST:event_grosorBotonStateChanged

    private void botonRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRellenoActionPerformed

    }//GEN-LAST:event_botonRellenoActionPerformed

    private void edicionSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edicionSeleccionActionPerformed
        vi = this.getVentana(); //Obtenemos la ventana
        if (vi != null) {
            if (edicionSeleccion.isSelected()) {
                //Establecer los demas botones a falso
                //Al seleccionar un objeto , lo establece por defecto
                //Dejandolo sin relleno ni transparencias
                this.botonLapiz.setSelected(false);
                this.botonLinea.setSelected(false);
                this.botonOvalo.setSelected(false);
                this.botonRectangulo.setSelected(false);
                this.botonArco.setSelected(false);
                this.botonRectanguloRedondo.setSelected(false);
                this.botonTransparencia.setSelected(false);
                this.botonRelleno.setSelected(false);
                this.botonAlisar.setSelected(false);
                vi.li.setEditarLienzo(true);
            } else {
                this.edicionSeleccion.setSelected(false);
                vi.li.setEditarLienzo(false);
                this.vi.li.setForma(6); //Para que no pinte nada por defecto
            }

            //this.setCursor(Cursor.CURSOR_OPEN_HAND);
        }
    }//GEN-LAST:event_edicionSeleccionActionPerformed

    private void botonRectanguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRectanguloActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonLapiz.setSelected(false);
        this.botonOvalo.setSelected(false);
        this.botonLinea.setSelected(false);
        this.edicionSeleccion.setSelected(false);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   rectangulo");
        if (vi != null) {
            this.botonRectangulo.setSelected(true);
            vi.li.setForma(0);
        }
    }//GEN-LAST:event_botonRectanguloActionPerformed

    private void botonOvaloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOvaloActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonLapiz.setSelected(false);
        this.botonRectangulo.setSelected(false);
        this.botonLinea.setSelected(false);
        this.edicionSeleccion.setSelected(false);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   elipse");
        if (vi != null) {
            this.botonOvalo.setSelected(true);
            vi.li.setForma(1);
        }
    }//GEN-LAST:event_botonOvaloActionPerformed

    private void botonLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLineaActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonOvalo.setSelected(false);
        this.botonRectangulo.setSelected(false);
        this.botonLapiz.setSelected(false);
        this.edicionSeleccion.setSelected(false);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   linea");
        if (vi != null) {
            this.botonLinea.setSelected(true);
            vi.li.setForma(2);
        }
    }//GEN-LAST:event_botonLineaActionPerformed

    private void botonLapizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLapizActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonOvalo.setSelected(false);
        this.botonRectangulo.setSelected(false);
        this.botonLinea.setSelected(false);
        this.edicionSeleccion.setSelected(editar);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   lapiz");

        if (vi != null) {
            this.botonLapiz.setSelected(true);
            vi.li.setForma(3);
        }

    }//GEN-LAST:event_botonLapizActionPerformed

    private void guardarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBotonActionPerformed
        this.guardarActionPerformed(evt);
    }//GEN-LAST:event_guardarBotonActionPerformed

    private void nuevoBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoBotonActionPerformed
        this.nuevoActionPerformed(evt);
    }//GEN-LAST:event_nuevoBotonActionPerformed

    private void abrirBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirBotonActionPerformed
        this.abrirActionPerformed(evt);
    }//GEN-LAST:event_abrirBotonActionPerformed

    private void botonFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFiltroActionPerformed
        switch (this.botonFiltro.getSelectedIndex()) {
            case (0): //Media
                this.setFiltroMedia();
                break;

            case 1:
                this.setFiltroLaplaciano();
                break;

            case 2:
                this.setFiltroBinomial();
                break;

            case 3:
                this.setFiltroRelieve();
                break;

            case 4:
                this.setFitroEnfoque();
                break;

            case 5:
                this.setFiltroMediaN(5);
                break;

            case 6:
                this.setFiltroMediaN(7);
                break;
        }

        /*
         media 0
         laplaciano 1
         binomial 2
         relieve 3
         enfoque 4
         */

    }//GEN-LAST:event_botonFiltroActionPerformed

    private void botonModificarTamañoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarTamañoActionPerformed
        ventanaTamaño vent = new ventanaTamaño(this);
        escritorio.add(vent);//Añadimos al escritorio la ventana nueva
        vent.setVisible(true); //La hacemos visible
        //La ventana nueva es la que gestionara el paso del tamaño

    }//GEN-LAST:event_botonModificarTamañoActionPerformed

    private void botonDeslizadorBrilloStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonDeslizadorBrilloStateChanged
        if (vi != null) {
            int valor = this.botonDeslizadorBrillo.getValue();
            this.brilloImagen = valor; //Mandamos a la imagen el valor del brillo
            this.botonReescaladoActionPerformed(null);
        }
    }//GEN-LAST:event_botonDeslizadorBrilloStateChanged

    private void botonDeslizadorBrilloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_botonDeslizadorBrilloFocusGained
        this.imgOriginal = this.getImage();
    }//GEN-LAST:event_botonDeslizadorBrilloFocusGained

    private void botonDeslizadorBrilloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_botonDeslizadorBrilloFocusLost
        this.imgOriginal = null;
    }//GEN-LAST:event_botonDeslizadorBrilloFocusLost

    private void botonReescaladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReescaladoActionPerformed
        funcionesImagenes.Reescalado(this, brilloImagen);
    }//GEN-LAST:event_botonReescaladoActionPerformed

    //Transformacion Affine Shear activada
    private void botonAffineTransformOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAffineTransformOpActionPerformed
        this.funcionesImg.AffineTransformOP(this);
    }//GEN-LAST:event_botonAffineTransformOpActionPerformed

    private void botonLookUpOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLookUpOpActionPerformed
        //Llamamos a una clase auxiliar que implementa metodos imagenes
        funcionesImagenes.LookupOP(this);

    }//GEN-LAST:event_botonLookUpOpActionPerformed

    private void botonBandCombineOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBandCombineOpActionPerformed
        funcionesImagenes.BandCombineOp(this);
    }//GEN-LAST:event_botonBandCombineOpActionPerformed

    private void botonColorConvertOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonColorConvertOpActionPerformed
        funcionesImagenes.ColorConvertOp(this);
    }//GEN-LAST:event_botonColorConvertOpActionPerformed

    private void contrasteBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contrasteBotonActionPerformed
        funcionesImagenes.ContrasteNormal(this);
    }//GEN-LAST:event_contrasteBotonActionPerformed

    private void iluminarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iluminarBotonActionPerformed
        funcionesImagenes.incrementarContraste(this);
    }//GEN-LAST:event_iluminarBotonActionPerformed

    private void oscurecerBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oscurecerBotonActionPerformed
        funcionesImagenes.decrementarContraste(this);
    }//GEN-LAST:event_oscurecerBotonActionPerformed

    private void deslizadorRotStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_deslizadorRotStateChanged
        double valor = this.deslizadorRot.getValue();
        this.funcionesImg.rotarGrados(this, valor);
    }//GEN-LAST:event_deslizadorRotStateChanged

    private void deslizadorRotFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_deslizadorRotFocusGained
        this.imgOriginal = vi.getLienzo().getImage(true);
        this.deslizadorRotStateChanged(null);
    }//GEN-LAST:event_deslizadorRotFocusGained

    private void deslizadorRotFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_deslizadorRotFocusLost
        this.imgOriginal = null;
    }//GEN-LAST:event_deslizadorRotFocusLost

    private void botonRotar90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotar90ActionPerformed
        this.funcionesImg.rotarImagen(this, 90.0);
    }//GEN-LAST:event_botonRotar90ActionPerformed

    private void botonRotar270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotar270ActionPerformed
        this.funcionesImg.rotarImagen(this, 270.0);
    }//GEN-LAST:event_botonRotar270ActionPerformed

    private void botonRotar180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotar180ActionPerformed
        this.funcionesImg.rotarImagen(this, 180.0);
    }//GEN-LAST:event_botonRotar180ActionPerformed

    private void botonSenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSenoActionPerformed
        funcionesImagenes.LookupOPSeno(this);
    }//GEN-LAST:event_botonSenoActionPerformed

    private void botonAumentarReescaladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAumentarReescaladoActionPerformed
        funcionesImagenes.escaladoImagen(this, 1.25);
    }//GEN-LAST:event_botonAumentarReescaladoActionPerformed

    private void botonDisminuirReescaladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDisminuirReescaladoActionPerformed
        funcionesImagenes.escaladoImagen(this, 0.75);
    }//GEN-LAST:event_botonDisminuirReescaladoActionPerformed

    private void negativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negativoActionPerformed
        funcionesImagenes.Negativo(this);
    }//GEN-LAST:event_negativoActionPerformed

    private void escalaGrisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_escalaGrisesActionPerformed
        funcionesImagenes.ColorConvertOp(this);
    }//GEN-LAST:event_escalaGrisesActionPerformed

    /**
     * duplica la imagen seleccionada
     * @param evt Evento acciona
     */
    private void duplicarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicarImagenActionPerformed
        //Si la ventanaInterna que queremos duplicar no es nula
        if (vi != null) {
            BufferedImage imgSource = this.getImage();

            //Creamos la ventana interna duplicada
            ventanaInterna ventanaDup = new ventanaInterna(this);
            ventanaDup.getLienzo().setImage(imgSource);
            ventanaDup.setTitle("Imagen duplicada");
            escritorio.add(ventanaDup);
            ventanaDup.setVisible(true);
        }

    }//GEN-LAST:event_duplicarImagenActionPerformed

    /**
     * Suma de forma binaria 2 imagenes seleccionadas
     * @param evt evento que genera la accion
     */
    private void botonBinarioSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBinarioSumaActionPerformed
        ventanaInterna vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            ventanaInterna viNext = (ventanaInterna) escritorio.selectFrame(true);
            if (viNext != null) {
                BufferedImage imgRight = vi.getLienzo().getImage(true);
                BufferedImage imgLeft = viNext.getLienzo().getImage(true);
                if (imgRight != null && imgLeft != null) {
                    try {
                        BinaryOp op = new SumaOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new ventanaInterna(this);
                        vi.setTitle("Ventana Suma OP");
                        vi.getLienzo().setImage(imgdest);
                        this.escritorio.add(vi);
                        vi.setVisible(true);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_botonBinarioSumaActionPerformed

    /**
     * Barra de desplazamiento que obtiene el valor para la umbralizacion
     * @param evt evento genera la accion
     */
    private void botonUmbralStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonUmbralStateChanged
        if (this.vi != null) {
            if (this.umbralizacionAzul.isSelected()) {
                funcionesImagenes.UmbralizacionColor(this, this.botonUmbral.getValue(), Color.blue);
            } else if (this.umbralizacionRojo.isSelected()) {
                funcionesImagenes.UmbralizacionColor(this, this.botonUmbral.getValue(), Color.red);
            } else if (this.umbralizacionVerde.isSelected()) {
                funcionesImagenes.UmbralizacionColor(this, this.botonUmbral.getValue(), Color.green);
            } else {
                funcionesImagenes.UmbralizacionOp(this, this.botonUmbral.getValue());
            }
        }
    }//GEN-LAST:event_botonUmbralStateChanged

    private void botonUmbralFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_botonUmbralFocusGained
        this.imgOriginal = this.getImage();
    }//GEN-LAST:event_botonUmbralFocusGained

    private void botonUmbralFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_botonUmbralFocusLost
        this.imgOriginal = null;
    }//GEN-LAST:event_botonUmbralFocusLost

    /**
     * Crea la resta binaria de 2 imagenes
     * @param evt evento que genera la accion
     */
    private void botonBinarioRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBinarioRestaActionPerformed
        ventanaInterna vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            ventanaInterna viNext = (ventanaInterna) escritorio.selectFrame(true);
            if (viNext != null) {
                BufferedImage imgRight = vi.getLienzo().getImage(true);
                BufferedImage imgLeft = viNext.getLienzo().getImage(true);
                if (imgRight != null && imgLeft != null) {
                    try {
                        BinaryOp op = new RestaOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new ventanaInterna(this);
                        vi.setTitle("Ventana Resta OP");
                        vi.getLienzo().setImage(imgdest);
                        this.escritorio.add(vi);
                        vi.setVisible(true);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_botonBinarioRestaActionPerformed

    /**
     * Funcion que crea la imagen infrarroja
     * llama a la operacin ImagenInfrarroja
     * @param evt evento que genera la accion
     */
    private void infrarrojosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infrarrojosActionPerformed
        funcionesImagenes.ImagenInfrarroja(this);
    }//GEN-LAST:event_infrarrojosActionPerformed

    /**
     * Abre una ventana para reproducir audio
     * 
     * @param evt evento que genera la accion
     */
    private void reproducirAudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reproducirAudioActionPerformed
        JFileChooser dlg = new JFileChooser();

        dlg.setAcceptAllFileFilterUsed(true); //Ponemos a falso para solo muestre filtro
        //Añadimos los filtros aceptados para la lectura
        AudioFileFormat.Type[] formatTypes = AudioSystem.getAudioFileTypes();

        //Creamos un array con los tipos de audio y se los asignamos
        String[] types = new String[formatTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = formatTypes[i].getExtension();
        }

        FileNameExtensionFilter filterRead = new FileNameExtensionFilter("Tipos archivos audio", types);
       

        dlg.setFileFilter(filterRead);
        

        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                File f = dlg.getSelectedFile();

                this.abrirAudio(f);

            } catch (Exception ex) {
                System.err.println("Error al abrir audio-video");
            }
        }
    }//GEN-LAST:event_reproducirAudioActionPerformed

    /**
     * Graba un audio
     * @param evt evento que genera la accion
     */
    private void grabarAudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabarAudioActionPerformed
        JFileChooser dlg = new JFileChooser();
        dlg.setAcceptAllFileFilterUsed(true);

        //Añadimos los filtros aceptados para la lectura
        AudioFileFormat.Type[] formatTypes = AudioSystem.getAudioFileTypes();

        //Creamos un array con los tipos de audio y se los asignamos
        String[] types = new String[formatTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = formatTypes[i].getExtension();
        }

        //Filtro archivos de audio
        FileNameExtensionFilter filterWrite = new FileNameExtensionFilter("Tipos archivos audio", types);
       
        dlg.setFileFilter(filterWrite);
        

        //Para cada filtro aceptado para la escritura lo añadimos al FileChooser
        int resp = dlg.showSaveDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                //Ponemos a falso para que no muestre todos los tipos de archivos

                File f = dlg.getSelectedFile();

                ventanaGrabacion vg = new ventanaGrabacion(this,f);

                //Añadimos la ventana al escritorio y generamos la ventana de grabar
                this.escritorio.add(vg);
                vg.setTitle(f.getName());
                vg.setVisible(true);
                this.setEstadosNuevos();

            } catch (Exception ex) {
                System.err.println("Error al guardar audio");
            }
        }
    }//GEN-LAST:event_grabarAudioActionPerformed

    private void botonArcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonArcoActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonOvalo.setSelected(false);
        this.botonRectangulo.setSelected(false);
        this.botonLapiz.setSelected(false);
        this.botonLinea.setSelected(false);
        this.botonRectanguloRedondo.setSelected(false);
        this.edicionSeleccion.setSelected(false);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   curva con punto de control");
        if (vi != null) {
            this.botonArco.setSelected(true);
            vi.li.setForma(5);
        }
    }//GEN-LAST:event_botonArcoActionPerformed

    private void botonRectanguloRedondoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRectanguloRedondoActionPerformed
        /*Ponemos los demás botones sin seleccionar*/
        this.botonOvalo.setSelected(false);
        this.botonRectangulo.setSelected(false);
        this.botonLapiz.setSelected(false);
        this.botonLinea.setSelected(false);
        this.botonArco.setSelected(false);
        this.edicionSeleccion.setSelected(false);
        this.edicionSeleccionActionPerformed(evt);

        this.figura.setText("   rectangulo redondo");
        if (vi != null) {
            this.botonRectanguloRedondo.setSelected(true);
            vi.li.setForma(4);
        }
    }//GEN-LAST:event_botonRectanguloRedondoActionPerformed

    private void botonColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonColorActionPerformed
        ventanaColores colorin = new ventanaColores(this);
        escritorio.add(colorin);
        colorin.setVisible(true);
        //Como la ventana interna de colores ya establece el color
        //lo ponemos el fondo boton a ese color
        this.botonColor.setBackground(vi.li.getColor());
    }//GEN-LAST:event_botonColorActionPerformed

    /**
     *
     * @param color
     */
    public void botonColorBackGround(Color color) {
        this.botonColor.setBackground(color);
    }
    private void acercadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acercadeActionPerformed
        String info;
        info = "Autor : Rafael Lachica Garrido" + "\n" + "Paint RLG" + "\n" + "version 13.5" + "\n";
        JOptionPane.showMessageDialog(this, info, "Acerca de", 1);
    }//GEN-LAST:event_acercadeActionPerformed

    private void umbralizacionRojoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_umbralizacionRojoActionPerformed
        //Ponemos los demas botones a falso
        this.umbralizacionAzul.setSelected(false);
        this.umbralizacionVerde.setSelected(false);

    }//GEN-LAST:event_umbralizacionRojoActionPerformed

    private void umbralizacionVerdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_umbralizacionVerdeActionPerformed
        //Ponemos los demas botones a falso
        this.umbralizacionRojo.setSelected(false);
        this.umbralizacionAzul.setSelected(false);
    }//GEN-LAST:event_umbralizacionVerdeActionPerformed

    private void umbralizacionAzulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_umbralizacionAzulActionPerformed
        //Ponemos los demas botones a falso
        this.umbralizacionRojo.setSelected(false);
        this.umbralizacionVerde.setSelected(false);
    }//GEN-LAST:event_umbralizacionAzulActionPerformed

    private void botonRellenoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonRellenoStateChanged
        vi = this.getVentana();
        if (vi != null) {
            if (botonRelleno.isSelected()) {
                vi.li.setRelleno(true);
            } else {
                vi.li.setRelleno(false);
            }
        }
    }//GEN-LAST:event_botonRellenoStateChanged
/**
 * 
 * @param evt 
 */
    private void botonTransparenciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonTransparenciaStateChanged
        vi = this.getVentana();
        if (vi != null) {
            if (botonTransparencia.isSelected()) {
                vi.li.setTransparente(true);
            } else {
                vi.li.setTransparente(false);
            }
        }
    }//GEN-LAST:event_botonTransparenciaStateChanged
/**
 * Captura la imagen de un video
 * @param evt eento que genera la accion
 */
    private void botonCapturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCapturaActionPerformed
        //Seleccionamos la ventana interna que se esta reproduciendo
        ventanaInternaJMFPlayer vr = (ventanaInternaJMFPlayer) this.escritorio.getSelectedFrame();
        if (vr != null) {
            BufferedImage img = vr.captura();
            try {
                vi = new ventanaInterna(this);
                vi.setTitle("Captura Video");
                escritorio.add(vi);//Añadimos al escritorio
                vi.setVisible(true);
                this.ventanasInternas.add(vi); //Añadimos la ventana interna a la lista    
                vi.getLienzo().setImage(img);
            } catch (Exception e) {
                System.out.println("Error guardar imagen capturada: " + e);
            }

        }
    }//GEN-LAST:event_botonCapturaActionPerformed
    /**
     * 
     * @param evt
     */
    private void botonAlisarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_botonAlisarStateChanged
        vi = this.getVentana();
        if (vi != null) {
            if (botonAlisar.isSelected()) {
                vi.li.setAlisado(true);
            } else {
                vi.li.setAlisado(false);
            }
        }
    }//GEN-LAST:event_botonAlisarStateChanged

    /**
     * Funcion Recoge si se ha seleccionado el checkbox para linea discontinua
     * @param evt
     */
    private void botonDiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDiscActionPerformed
        //Si se ha seleccionado le enviamos el mensaje al objeto lienzo ventanaInterna
        if (botonDisc.isSelected()){
            vi = this.getVentana();
            vi.li.setLineadiscontinua(true);
        }
        else{
            vi = this.getVentana();
            vi.li.setLineadiscontinua(false);
        }
            
    }//GEN-LAST:event_botonDiscActionPerformed

    /**
     * colorSecundarioActionPerformed. Se acciona al recibir el evento.
     * Encargada de establecer el color del degradado que se utilizara después.
     *
     * @param evt
     */
    private void colorSecundarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorSecundarioActionPerformed
        ventanaColores colorin = new ventanaColores(this);
        escritorio.add(colorin);
        colorin.setVisible(true);
        this.setColorDegradado(true);
        
    }//GEN-LAST:event_colorSecundarioActionPerformed

    /**
     * colorSecundario.
     * Pone el color secundario de background del boton colorSecundario
     * @param color 
     */
    public void colorSecundario(Color color){
        this.colorSecundario.setBackground(color);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem abrir;
    private javax.swing.JButton abrirBoton;
    private javax.swing.JMenuItem acercade;
    private javax.swing.JButton amarillo;
    private javax.swing.JButton azul;
    private javax.swing.JPanel barraEstado;
    private javax.swing.JPanel barraFormas;
    private javax.swing.JButton blanco;
    private javax.swing.JMenuItem botonAffineTransformOp;
    private javax.swing.JToggleButton botonAlisar;
    private javax.swing.JMenu botonArchivo;
    private javax.swing.JToggleButton botonArco;
    private javax.swing.JMenu botonAudio;
    private javax.swing.JButton botonAumentarReescalado;
    private javax.swing.JMenuItem botonBandCombineOp;
    private javax.swing.JMenuItem botonBarraAtributos;
    private javax.swing.JMenuItem botonBarraEstado;
    private javax.swing.JMenuItem botonBarraFormas;
    private javax.swing.JButton botonBinarioResta;
    private javax.swing.JButton botonBinarioSuma;
    private javax.swing.JButton botonCaptura;
    private javax.swing.JButton botonColor;
    private javax.swing.JMenuItem botonColorConvertOp;
    private javax.swing.JSlider botonDeslizadorBrillo;
    private javax.swing.JCheckBox botonDisc;
    private javax.swing.JButton botonDisminuirReescalado;
    private javax.swing.JMenu botonEditar;
    private javax.swing.JComboBox botonFiltro;
    private javax.swing.JMenu botonImagen;
    private javax.swing.JToggleButton botonLapiz;
    private javax.swing.JToggleButton botonLinea;
    private javax.swing.JMenuItem botonLookUpOp;
    private javax.swing.JMenuItem botonModificarTamaño;
    private javax.swing.JToggleButton botonOvalo;
    private javax.swing.JToggleButton botonRectangulo;
    private javax.swing.JToggleButton botonRectanguloRedondo;
    private javax.swing.JMenuItem botonReescalado;
    private javax.swing.JToggleButton botonRelleno;
    private javax.swing.JButton botonRotar180;
    private javax.swing.JButton botonRotar270;
    private javax.swing.JButton botonRotar90;
    private javax.swing.JButton botonSeno;
    private javax.swing.JToggleButton botonTransparencia;
    private javax.swing.JSlider botonUmbral;
    private javax.swing.JLabel checkDiscontinuo;
    private javax.swing.JButton colorSecundario;
    private javax.swing.JButton contrasteBoton;
    private javax.swing.JSlider deslizadorRot;
    private javax.swing.JMenuItem duplicarImagen;
    private javax.swing.JToggleButton edicionSeleccion;
    private javax.swing.JMenuItem escalaGrises;
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JLabel figura;
    private javax.swing.JMenuItem grabarAudio;
    private javax.swing.JSpinner grosorBoton;
    private javax.swing.JMenuItem guardar;
    private javax.swing.JButton guardarBoton;
    private javax.swing.JButton iluminarBoton;
    private javax.swing.JMenuItem infrarrojos;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem negativo;
    private javax.swing.JButton negro;
    private javax.swing.JMenuItem nuevo;
    private javax.swing.JButton nuevoBoton;
    private javax.swing.JButton oscurecerBoton;
    private javax.swing.JPanel paletaColores;
    private javax.swing.JPanel panelArchivos;
    private javax.swing.JPanel panelAtributos;
    private javax.swing.JPanel panelAudio_Video;
    private javax.swing.JPanel panelBrillo;
    private javax.swing.JPanel panelContraste;
    private javax.swing.JPanel panelEscala;
    private javax.swing.JPanel panelFiltro;
    private javax.swing.JPanel panelFormas;
    private javax.swing.JPanel panelGrosor;
    private javax.swing.JPanel panelRotacion;
    private javax.swing.JLabel pixelRGB;
    private javax.swing.JMenuItem reproducirAudio;
    private javax.swing.JButton rojo;
    private javax.swing.JToggleButton umbralizacionAzul;
    private javax.swing.JToggleButton umbralizacionRojo;
    private javax.swing.JToggleButton umbralizacionVerde;
    private javax.swing.JDialog ventanaHistograma;
    private javax.swing.JButton verde;
    // End of variables declaration//GEN-END:variables

    /*------------------Filtro de las imagenes------------
     -------------------------------------------------------*/
    
    /**
     * Pone le filtroMedia con un tamaña n introducido en el parametro tamanio
     * @param tamanio tamañio para crear el filtro
     */
    private void setFiltroMediaN(float tamanio) {
        //Donde el tamanio es la dimension 5x5 --> tamanio = 5
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = vi.getLienzo().getImage(true);
            if (imgSource != null) {
                try {
                    float tam = tamanio * tamanio;
                    float[] M = new float[(int) tam];

                    for (int i = 0; i < tam; i++) {
                        M[i] = 1 / (float) tam;
                    }

                    for (int i = 0; i < tam; i++) {
                        System.out.println(M[i] + " ");
                    }

                    Kernel k = new Kernel((int) tamanio, (int) tamanio, M);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }

    }

    /**
     * Filtro media 3x3
     */
    private void setFiltroMedia() {
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = vi.getLienzo().getImage(true);
            if (imgSource != null) {
                try {
                    Kernel k = KernelProducer.createKernel(KernelProducer.TYPE_MEDIA_3x3);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }

    }

    /**
     * Filtro laplacianio
     */
    private void setFiltroLaplaciano() {
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = this.getImagenOriginal();
            if (imgSource != null) {
                try {
                    Kernel k = KernelProducer.createKernel(KernelProducer.TYPE_LAPLACIANA_3x3);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }
    }

    /**
     * FiltroBinomial
     */
    private void setFiltroBinomial() {
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = vi.getLienzo().getImage(true);
            if (imgSource != null) {
                try {
                    Kernel k = KernelProducer.createKernel(KernelProducer.TYPE_BINOMIAL_3x3);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }
    }

    /**
     * FiltroRelieve
     */
    private void setFiltroRelieve() {
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = vi.getLienzo().getImage(true);
            if (imgSource != null) {
                try {
                    Kernel k = KernelProducer.createKernel(KernelProducer.TYPE_RELIEVE_3x3);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }
    }

    /**
     * FiltroEnfoque
     */
    private void setFitroEnfoque() {
        vi = (ventanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            BufferedImage imgSource = vi.getLienzo().getImage(true);
            if (imgSource != null) {
                try {
                    Kernel k = KernelProducer.createKernel(KernelProducer.TYPE_ENFOQUE_3x3);
                    ConvolveOp cop = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(imgSource, null);
                    vi.getLienzo().setImage(imgdest);
                    vi.getLienzo().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            }
        }
    }

}
