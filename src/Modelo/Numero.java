package Modelo;

import Auxiliar.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Numero extends Personagem {

    public Numero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
    }

    @Override
    protected ImageIcon carregarImagemRedimensionada(String nomeImagem) {
        try {
            String caminhoImagem = new java.io.File(".").getCanonicalPath() + Consts.PATH + nomeImagem;
            ImageIcon iconOriginal = new ImageIcon(caminhoImagem);
            Image imagemOriginal = iconOriginal.getImage();

            int ladoImagem = Consts.CELL_SIDE / 3;

            BufferedImage imagemFinal = new BufferedImage(
                    Consts.CELL_SIDE,
                    Consts.CELL_SIDE,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics g = imagemFinal.createGraphics();
            g.drawImage(imagemOriginal,
                    Consts.CELL_SIDE - ladoImagem,  // x (borda direita)
                    0,                              // y (topo)
                    ladoImagem,
                    ladoImagem,
                    null
            );
            g.dispose();

            return new ImageIcon(imagemFinal);

        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
            return null;
        }
    }



    public void autoDesenho() {
        super.autoDesenho();
    }


}
