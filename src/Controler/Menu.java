package Controler;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelo.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Menu extends Tela{

    public Menu() {

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

        BuracoNegro fase1 = new BuracoNegro("BuracoNegro.png");
        fase1.setPosicao(7, 10);
        this.addPersonagem(fase1);

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

    @Override    /*Metodo responsável por atualizar a posição da câmera com base na posição atual do herói (hero)*/
    protected void atualizaCamera() {
        // Obtém a linha atual do herói
        int linha = hero.getPosicao().getLinha();

        // Obtém a coluna atual do herói
        int coluna = hero.getPosicao().getColuna();

        // Calcula a linha da câmera:
        // - Tenta centralizar o herói verticalmente na tela.
        // - Usa Math.max para garantir que a câmera nunca vá acima do topo do mapa (linha < 0).
        // - Usa Math.min para garantir que a câmera não ultrapasse os limites inferiores do mapa.
        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_MENU_ALTURA - Consts.RES));

        // Calcula a coluna da câmera (mesmo raciocínio da linha):
        // - Centraliza horizontalmente, respeitando os limites laterais do mundo.
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_MENU_LARGURA - Consts.RES));
    }
}
