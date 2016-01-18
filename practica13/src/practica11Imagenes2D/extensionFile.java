/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica11Imagenes2D;
import java.io.*;
/**
 *
 * @author rafa
 */
public class extensionFile implements FilenameFilter {
    private String ext; //extension del archivo

    public extensionFile(String ext) {
        this.ext = "." + ext; //Añadimos la extension al archivo
    }
    
    
   /**
    @return True o false dependiendo si el archivo acaba con el nombre extension
    */
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(ext);
    }
    
}
