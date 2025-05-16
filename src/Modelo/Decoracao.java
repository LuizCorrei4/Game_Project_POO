package Modelo;


import Auxiliar.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Decoracao extends Personagem{
    public Decoracao(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;
        this.bAssasino= false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }

    @Override
    protected ImageIcon carregarImagemRedimensionada(String nomeImagem) {
        try {
            String caminhoImagem = new java.io.File(".").getCanonicalPath() + Consts.PATH + nomeImagem;
            ImageIcon iconOriginal = new ImageIcon(caminhoImagem);
            Image imagemOriginal = iconOriginal.getImage();

            BufferedImage imagemRedimensionada = new BufferedImage(
                    Consts.CELL_SIDE,
                    Consts.CELL_SIDE,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics g = imagemRedimensionada.createGraphics();
            g.drawImage(imagemOriginal, 0, 0, Consts.CELL_SIDE/2, Consts.CELL_SIDE/2, null);
            g.dispose(); // libera recursos do gráfico

            return new ImageIcon(imagemRedimensionada);

        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
            return null;
        }
    }

}
