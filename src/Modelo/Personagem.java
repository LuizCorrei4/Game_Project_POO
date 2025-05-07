// File: Modelo/Personagem.java
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * Classe base para todos os personagens do jogo.
 * Implementa carregamento de sprite, posição e propriedades de colisão.
 */
public abstract class Personagem implements Serializable {

    protected ImageIcon iImage;       // Sprite do personagem
    protected Posicao pPosicao;       // Posição atual (linha, coluna) no grid
    protected boolean bTransponivel;  // Pode atravessar outros personagens?
    protected boolean bMortal;        // Se verdadeiro, ao colidir, personagem é removido

    public boolean isbMortal() {
        return bMortal;
    }

    /**
     * Construtor recebe nome de arquivo de imagem e monta um ImageIcon do tamanho CELL_SIDE.
     */
    protected Personagem(String sNomeImagePNG) {
        this.pPosicao = new Posicao(1, 1);
        this.bTransponivel = true;
        this.bMortal = false;
        try {
            // Carrega imagem original
            iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
            Image img = iImage.getImage();
            // Redimensiona para o tamanho de célula
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            iImage = new ImageIcon(bi);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Posicao getPosicao() {
        // Retorna objeto de posição (pode permitir alterações externas)
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    /** Desenha o sprite na tela, usando utilitário de desenho. */
    public void autoDesenho(){
        Desenho.desenhar(this.iImage,
                this.pPosicao.getColuna(),
                this.pPosicao.getLinha());
    }

    // Atualiza posição no grid
    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    // Movimentações simples delegadas a Posicao
    public boolean moveUp()    { return this.pPosicao.moveUp(); }
    public boolean moveDown()  { return this.pPosicao.moveDown(); }
    public boolean moveRight() { return this.pPosicao.moveRight(); }
    public boolean moveLeft()  { return this.pPosicao.moveLeft(); }
}
