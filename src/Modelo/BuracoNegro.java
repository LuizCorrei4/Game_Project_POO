package Modelo;


public class BuracoNegro extends Personagem{
    public BuracoNegro(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
