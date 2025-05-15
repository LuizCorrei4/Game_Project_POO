
package Modelo;

import Auxiliar.Desenho;
import java.util.Random;
import java.io.Serializable;


public class BichinhoVaiVemVertical extends Personagem implements Serializable {
    private boolean bUp;
    private int iContador;

    public BichinhoVaiVemVertical(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bUp = true;
        iContador = 0;
        this.bAssasino = true;
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;

            if (bUp) {
                this.setPosicao(pPosicao.getLinha() - 1, pPosicao.getColuna());
            } else {
                this.setPosicao(pPosicao.getLinha() + 1, pPosicao.getColuna());
            }

            bUp = !bUp;
        }

        super.autoDesenho();
        iContador++;
    }
}
