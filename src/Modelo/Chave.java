package Modelo;

import Controler.Menu;


public class Chave extends Personagem{
    public Chave(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;

    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
