// File: Modelo/Hero.java
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import java.io.IOException;
import java.io.Serializable;
import javax
        .swing.ImageIcon;

public class Hero extends Personagem implements Serializable{
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG); // sNomeImagePNG will be stored by Personagem constructor
        this.bTransponivel = true;
        this.bMortal = true;
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }

    @Override
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;
    }

    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;
    }

    @Override
    public boolean moveUp() {
        if(super.moveUp()) {
            return validaPosicao();
        }
        return false;
    }

    @Override
    public boolean moveDown() {
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    @Override
    public boolean moveRight() {
        if(super.moveRight()) {
            return validaPosicao();
        }
        return false;
    }

    @Override
    public boolean moveLeft() {
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }

    public void troca_imagem(String caminho) throws IOException {
        this.sNomeImagePNG = caminho; // Update stored image name
        this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + caminho);
    }
}