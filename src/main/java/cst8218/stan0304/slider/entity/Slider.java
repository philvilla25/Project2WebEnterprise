/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.slider.entity;

import cst8218.stan0304.slider.resources.sliderNumbers;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
/**
 *
 * @author thpst
 */
@Entity
public class Slider extends sliderNumbers implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int size = INITIAL_SIZE;
    private int x = 0;
    private int y = 0;
    private int currentTravel = INITIAL_SIZE;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int x() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public int y() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public int currentTravel() {
        return currentTravel;
    }

    public void setCurrentTravel(int currentTravel) {
        this.currentTravel = currentTravel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slider)) {
            return false;
        }
        Slider other = (Slider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cst8218.stan0304.slider.resources.Slider[ id=" + id + " ]";
    }
    
}
