/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SingletonEjbClass.java to edit this template
 */
package cst8218.stan0304.slider.resources;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Startup;
import cst8218.stan0304.slider.business.SliderFacade;
import cst8218.stan0304.slider.entity.Slider;
import jakarta.ejb.EJB;
import java.util.List;

import static cst8218.stan0304.slider.resources.sliderNumbers.CHANGE_RATE;

/**
 *
 * @author thpst
 */
@Singleton
@LocalBean
@Startup
public class SliderSingleton {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @EJB
    private SliderFacade sliderFacade;
    
    @PostConstruct
    public void go() {
        new Thread(new Runnable() {
        public void run() {
            // the game runs indefinitely
            while (true) {
                //update all the sliders and save changes to the database
                List<Slider> sliders = sliderFacade.findAll(); //error
                for (Slider slider : sliders) { //error
                    slider.timeStep();
                    sliderFacade.edit(slider); //error
                }
                //sleep while waiting to process the next frame of the animation
                try {
                    // wake up roughly CHANGE_RATE times per second
                    Thread.sleep((long)(1.0/CHANGE_RATE*1000));                               
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }).start();
}

}
