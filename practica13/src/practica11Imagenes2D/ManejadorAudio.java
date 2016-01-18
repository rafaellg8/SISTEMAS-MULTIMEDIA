/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica11Imagenes2D;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import sm.sound.SMPlayer;

/**
 *
 * @author rafa
 */
public class ManejadorAudio implements LineListener {
    
    @Override
    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.START) {
        }
        if (event.getType() == LineEvent.Type.STOP) {
            //TODO
        }
        if (event.getType() == LineEvent.Type.CLOSE) {
            //TODO
        }
    }
}
