// File: Modelo/Personagem.java
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.io.ObjectInputStream; // Import for readObject

public abstract class Personagem implements Serializable {

    protected transient ImageIcon iImage; // Make ImageIcon transient
    protected String sNomeImagePNG;      // Ensure this field exists and is used
    protected Posicao pPosicao;
    protected boolean bTransponivel;
    protected boolean bMortal;
    protected boolean bAssasino;

    protected Personagem(String sNomeImagePNG) {
        this.sNomeImagePNG = sNomeImagePNG; // Store the image name
        this.pPosicao = new Posicao(1, 1);
        this.bTransponivel = true;
        this.bMortal = false;
        this.bAssasino = false;
        this.iImage = carregarImagemRedimensionada(sNomeImagePNG); // Load initial image
    }

    public String getsNomeImagePNG() {
        return sNomeImagePNG;
    }

    protected ImageIcon carregarImagemRedimensionada(String nomeImagem) {
        if (nomeImagem == null) return null;
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
            g.drawImage(imagemOriginal, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            g.dispose();

            return new ImageIcon(imagemRedimensionada);

        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem: " + nomeImagem + " -> " + e.getMessage());
            return null;
        }
    }

    // Custom deserialization logic
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Reads non-transient fields (like sNomeImagePNG)
        // Re-initialize transient fields
        if (this.sNomeImagePNG != null) {
            this.iImage = carregarImagemRedimensionada(this.sNomeImagePNG);
        }
    }

    public Posicao getPosicao() {
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public boolean isbMortal() {
        return bMortal;
    }

    public boolean isbAssasino() { return bAssasino; }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho(){
        Desenho.desenhar(this.iImage,
                this.pPosicao.getColuna(),
                this.pPosicao.getLinha());
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    public boolean moveUp()    { return this.pPosicao.moveUp(); }
    public boolean moveDown()  { return this.pPosicao.moveDown(); }
    public boolean moveRight() { return this.pPosicao.moveRight(); }
    public boolean moveLeft()  { return this.pPosicao.moveLeft(); }
}