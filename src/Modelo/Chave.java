package Modelo;

import Controler.Menu;

import javax.swing.*;


public class Chave extends Personagem{
    protected boolean isTranslucent;

    public Chave(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
        this.isTranslucent = true;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }

    public boolean isTranslucent() {
        return isTranslucent;
    }

    public void translucentOff() {
        this.isTranslucent = false;
    }


    public void setImage(String image){
        this.iImage = this.carregarImagemRedimensionada(image);
    }
}
