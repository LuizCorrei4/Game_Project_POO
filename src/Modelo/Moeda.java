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

            // Cria uma imagem do tamanho da célula
            BufferedImage imagemFinal = new BufferedImage(
                    Consts.CELL_SIDE,
                    Consts.CELL_SIDE,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics g = imagemFinal.createGraphics();

            // Calcula o deslocamento para centralizar a imagem menor
            int larguraMoeda = Consts.CELL_SIDE / 2;
            int alturaMoeda = Consts.CELL_SIDE / 2;
            int offsetX = (Consts.CELL_SIDE - larguraMoeda) / 2;
            int offsetY = (Consts.CELL_SIDE - alturaMoeda) / 2;

            // Desenha a imagem redimensionada no centro
            g.drawImage(imagemOriginal, offsetX, offsetY, larguraMoeda, alturaMoeda, null);
            g.dispose();

            return new ImageIcon(imagemFinal);

        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem: " + e.getMessage());
            return null;
        }
    }

}
