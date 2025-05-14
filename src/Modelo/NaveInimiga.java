// File: NaveInimiga

package Modelo;


import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import Controler.Menu;
import java.awt.Graphics;
import java.io.Serializable;

public class NaveInimiga extends Personagem implements Serializable{
    private int iContaIntervalos;
    int direcao;

    public NaveInimiga(String sNomeImagePNG, int direcao) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        bMortal = false;
        this.iContaIntervalos = 0;
        this.direcao = direcao;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Fogo f = new Fogo("projetil1_up.png", this.direcao);
            f.setPosicao(pPosicao.getLinha(),pPosicao.getColuna());
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }
}
