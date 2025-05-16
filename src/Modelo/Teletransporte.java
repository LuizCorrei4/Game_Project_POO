package Modelo;


public class Teletransporte extends Personagem{
    public Teletransporte(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }


}