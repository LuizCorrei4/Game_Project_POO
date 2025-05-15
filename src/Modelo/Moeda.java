package Modelo;

import Auxiliar.Consts;
import Controler.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Moeda extends Personagem{
    protected boolean isCatched;

    public Moeda(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = true;
        this.isCatched = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }

    public void pegouMoeda(){
        this.isCatched = true;
    }

    public boolean isCatched() {
        return isCatched;
    }

    @Override
    protected ImageIcon carregarImagemRedimensionada(String nomeImagem) {
        try {
            String caminhoImagem = new java.io.File(".").getCanonicalPath() + Consts.PATH + nomeImagem;
            ImageIcon iconOriginal = new ImageIcon(caminhoImagem);
            Image imagemOriginal = iconOriginal.getImage();

            BufferedImage imagemRedimensionada = new BufferedImage(
                    Consts.CELL_SIDE/2,
                    Consts.CELL_SIDE/2,
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
