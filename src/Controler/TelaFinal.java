package Controler;

import Auxiliar.Consts;
import Modelo.Barreira;
import Modelo.Decoracao;
import Modelo.Personagem;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelaFinal extends Tela{

    public TelaFinal() {

        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(1, 7);
        this.addPersonagem(hero);


        this.atualizaCamera();

        for (int i = 0; i < Consts.MUNDO_MENU_LARGURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(0, i);
            this.addPersonagem(temp);
        }

        for (int i = 1; i < Consts.MUNDO_MENU_ALTURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(i, 0);
            this.addPersonagem(temp);
        }

        for (int i = 1; i < Consts.MUNDO_MENU_LARGURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(Consts.MUNDO_MENU_ALTURA-1, i);
            this.addPersonagem(temp);
        }
        for (int i = 1; i < Consts.MUNDO_MENU_ALTURA - 1; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(i, Consts.MUNDO_MENU_LARGURA - 1);
            this.addPersonagem(temp);
        }

        Decoracao star1 = new Decoracao("YellowStars2.png");
        star1.setPosicao(2, 12);
        this.addPersonagem(star1);

        Decoracao star2 = new Decoracao("WhiteStars2.png");
        star2.setPosicao(2, 2);
        this.addPersonagem(star2);

        Decoracao star3 = new Decoracao("YellowStars2.png");
        star3.setPosicao(12, 2);
        this.addPersonagem(star3);

        Decoracao star4 = new Decoracao("WhiteStars2.png");
        star4.setPosicao(12, 12);
        this.addPersonagem(star4);


    }
    @Override
    public void paint(Graphics gOld) {

        Graphics g = this.getBufferStrategy().getDrawGraphics();

        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);


        // **********Desenha cenário de fundo*************

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_MENU_ALTURA && mapaColuna < Consts.MUNDO_MENU_LARGURA) {
                    try {
                        String imageName;

                        // Verifica se a célula atual é a especial
                        if (i == 5 && j == 3) {
                            imageName = "7.png"; // C
                        } else if( i == 5 && j== 4) {
                            imageName = "1.png"; // R
                        } else if( i == 5 && j== 5) {
                            imageName = "I.png"; // I
                        } else if( i == 5 && j== 6) {
                            imageName = "6.png"; // A
                        } else if( i == 5 && j== 7) {
                            imageName = "D(1).png"; // D
                        } else if( i == 5 && j== 8) {
                            imageName = "O.png"; // O
                        } else if( i == 5 && j== 9) {
                            imageName = "1.png"; // R
                        } else if( i == 5 && j== 10) {
                            imageName = "8.png"; // E
                        } else if( i == 5 && j== 11) {
                            imageName = "4.png"; // S
                        } else if( i == 6 && j== 3) {
                            imageName = "6.png"; // A
                        } else if( i == 6 && j== 4) {
                            imageName = "3.png"; // N
                        } else if( i == 6 && j== 5) {
                            imageName = "6.png"; // A
                        } else if( i == 6 && j== 7) {
                            imageName = "5.png"; // P
                        } else if( i == 6 && j== 8) {
                            imageName = "6.png"; // A
                        } else if( i == 6 && j== 9) {
                            imageName = "2.png"; // U
                        } else if( i == 6 && j== 10) {
                            imageName = "L.png"; // L
                        } else if( i == 6 && j== 11) {
                            imageName = "6.png"; // A
                        } else if( i == 7 && j== 5) {
                            imageName = "I.png"; // I
                        } else if( i == 7 && j== 6) {
                            imageName = "T.png"; // T
                        } else if( i == 7 && j== 7) {
                            imageName = "6.png"; // A
                        } else if( i == 7 && j== 8) {
                            imageName = "L.png"; // L
                        } else if( i == 7 && j== 9) {
                            imageName = "O.png"; // O
                        } else if( i == 8 && j== 5) {
                            imageName = "L.png"; // L
                        } else if( i == 8 && j== 6) {
                            imageName = "2.png"; // U
                        } else if( i == 8 && j== 7) {
                            imageName = "I.png"; // I
                        } else if( i == 8 && j== 8) {
                            imageName = "Z.png"; // Z

                        } else {
                            imageName = "bg_02_v.png"; // imagem padrão
                        }

                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }


        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    @Override
    protected void atualizaCamera() {

        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_MENU_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_MENU_LARGURA - Consts.RES));
    }

    public int getNumFase(){
        return -1;
    }


}
