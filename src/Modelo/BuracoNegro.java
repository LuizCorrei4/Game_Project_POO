package Modelo;

import Controler.Menu;


public class BuracoNegro extends Personagem{
    public BuracoNegro(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
    }

    public void autoDesenho() {
        super.autoDesenho();}

}
