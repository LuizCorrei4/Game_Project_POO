package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fase1 extends Tela {
    Chave chave;

    public Fase1() {
        faseAtual = new ArrayList<Personagem>();

        // Posição inicial do herói
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna()); // posição de entrada no labirinto
        this.addPersonagem(hero);
        this.atualizaCamera();

        String[] labirinto = {
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOO0O",
                "O.S.....O....O.O.S.O.....T...0O",
                "OOOOO.OOOOOO.OOOOOOO.OOOOOOO.O0",
                "O........O....S....O.......O.O0",
                "O.OOOOO.OOOOOOOOO.OOOOO.OOOOOO0",
                "O.T.....O....O.......O....S..O0",
                "OOOOOOO.O.OOOOOOOOO.OOOOOOO.OO0",
                "O.....O.S.....O.....T.....S.O00",
                "OO.OOOOOOOOO.OOOOO.OOOOO.OOOOO0",
                "O.....O...S.....O.....T.....O.O",
                "OOOO.OO..OOOOOOOOOOO.OOOOOO.OO0",
                "O...........T..............SO00",
                "OOOOO.OOOOO.OOOOO.OOOOO.OOOOOO0",
                "O.S.....O.......T...O.......O.O",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO0"
        };

        for (int linha = 0; linha < labirinto.length; linha++) {
            for (int coluna = 0; coluna < labirinto[linha].length(); coluna++) {
                if (labirinto[linha].charAt(coluna) == 'O') {
                    Barreira barreira = new Barreira("asteroid.png");
                    barreira.setPosicao(linha, coluna);
                    this.addPersonagem(barreira);
                }
            }
        }

        // Coloca a chave
        chave = new Chave("KeyIcons1.png");
        chave.setPosicao(7, 28); // posição da seta de saída
        this.addPersonagem(chave);

        
    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top,
                getWidth() - getInsets().right, getHeight() - getInsets().top);

        // Desenha fundo
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
            if (hero.getPosicao().igual(chave.getPosicao())) {
                carregarMenu();
            }
        }
        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }
}
