package Modelo;


public class Decoracao extends Personagem{
    public Decoracao(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
