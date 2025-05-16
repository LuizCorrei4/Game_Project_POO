// Fogo.java

package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;

import java.io.Serializable;

public class Fogo extends Personagem implements Serializable{
            
    int direcao;

    public Fogo(String sNomeImagePNG, int direcao) {
        super(sNomeImagePNG);
        this.bAssasino = true;
        this.direcao = direcao;

    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        switch (this.direcao) {
            case (Consts.RIGHT):
                if(!this.moveRight())
                    Desenho.acessoATelaDoJogo().removePersonagem(this);
                break;
            case (Consts.LEFT):
                if(!this.moveLeft())
                    Desenho.acessoATelaDoJogo().removePersonagem(this);
                break;
            case (Consts.DOWN):
                if(!this.moveDown())
                    Desenho.acessoATelaDoJogo().removePersonagem(this);
                break;
            case (Consts.UP):
                if(!this.moveUp())
                    Desenho.acessoATelaDoJogo().removePersonagem(this);
                break;
        }

    }
    
}
