// File: Fase5.java

package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Fase5 extends Tela{

    public Fase5() {
        // Novo layout do labirinto
        String[] labirinto = {
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",  // Linha 0 - borda
                "O....☄........O.......♆......O", // Linha 1
                "O.OOOOO.OOOOOOO.O.OOOOOOOOOO.O",  // Linha 2
                "O.O.....☆.........O..........O",  // Linha 3
                "O.OOOO.OOOOO.O.OOOOO.O.OOOOO.O",  // Linha 4
                "O.....O...O.....♆....O....☄O.O", // Linha 5
                "O.OOOOO.O.O.OOOOOOOOOO.OOOOO.O",  // Linha 6
                "O.O.......O....☆...O.........O",  // Linha 7
                "O.OOOO.OOOOOO.O.OOOO.OOOO.OO.O",  // Linha 8
                "O.....O..☄....O....O.....O...O",  // Linha 9
                "OOOOO.O.OOOOOOOOOO.O.OOOOO.O.O",  // Linha 10
                "O...O.O.......O....O.....♆...O",  // Linha 11
                "O.O.O.O.OOOOO.O.OOOOO.OOOOOO.O",  // Linha 12
                "O.O...O....☄..O..............O",  // Linha 13
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"   // Linha 14
        };

        // Adiciona barreiras baseadas no novo labirinto
        for (int linha = 0; linha < labirinto.length; linha++) {
            for (int coluna = 0; coluna < labirinto[linha].length(); coluna++) {
                if (labirinto[linha].charAt(coluna) == 'O') {
                    Barreira barreira = new Barreira("asteroid.png");
                    barreira.setPosicao(linha, coluna);
                    this.addPersonagem(barreira);
                }
            }
        }

        // Posiciona a chave em local diferente
        chave = new Chave("KeyIcons2.png");
        chave.setPosicao(5, 15);
        this.addPersonagem(chave);

        // Inimigos com padrões de movimento distintos
        NaveInimiga nvVertical = new NaveInimiga("Spaceship2_down.png", "projetil1_down.png", Consts.DOWN);
        nvVertical.setPosicao(2, 20);
        this.addPersonagem(nvVertical);

        NaveInimiga nvHorizontal = new NaveInimiga("Spaceship2_left.png", "projetil1_left.png", Consts.LEFT);
        nvHorizontal.setPosicao(10, 5);
        this.addPersonagem(nvHorizontal);
}

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        //Criamos um contexto gráfico
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        // Desenha cenário de fundo

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
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
            if (  hero.getPosicao().igual(chave.getPosicao())  ) {
                carregarMenu();
            }

            this.atualizaCamera();
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

}
