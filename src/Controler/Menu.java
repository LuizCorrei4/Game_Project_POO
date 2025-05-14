package Controler;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu extends Tela{


    public Menu() {

        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        /*mouse*/

        this.addKeyListener(this);
        /*teclado*/
        /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = new ArrayList<Personagem>();

        /*Cria faseAtual adiciona personagens*/
        hero = new Hero("player1_right2.png");
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

        BuracoNegro fase1 = new BuracoNegro("BuracoNegro.png");
        fase1.setPosicao(7, 10);
        this.addPersonagem(fase1);






    }

    @Override
    protected void atualizaCamera()
    {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_MENU_ALTURA) - Consts.RES);
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_MENU_LARGURA - Consts.RES));
    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        // **********Desenha cenário de fundo*************

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_MENU_ALTURA && mapaColuna < Consts.MUNDO_MENU_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "bg_02_v.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        // Atualiza animação de todos os personagens antes de desenhar
        /*
        for (Personagem p : faseAtual) {
            if (p instanceof Hero) {
                ((Hero)p).trocaIdleFrame();
            }
        } */

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
}
