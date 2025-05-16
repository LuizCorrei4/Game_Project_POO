// File: NaveInimiga

package Modelo;


import Auxiliar.Consts;
import Auxiliar.Desenho;

import java.io.Serializable;

public class NaveInimiga extends Personagem implements Serializable{
    private int iContaIntervalos;
    int direcao;
    String umNomeProjetil;

    public NaveInimiga(String sNomeImagePNG, String umNomeProjetil, int direcao) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        bMortal = false;
        this.umNomeProjetil = umNomeProjetil;
        this.iContaIntervalos = 0;
        this.direcao = direcao;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Fogo f = new Fogo(umNomeProjetil, this.direcao);
            f.setPosicao(pPosicao.getLinha(),pPosicao.getColuna());
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }
}
