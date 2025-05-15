package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Fase2 extends Tela{

    public Fase2() {
        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna());
        this.addPersonagem(hero);
        this.atualizaCamera();
        this.desenha_barreira();

        String[] labirinto = {
                // colunas de 0 a 29 (linha 0 e 14 são margens visuais, não terão barreiras)
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",  // Linha 0 - borda
                "O.......O.....♆.....☄........",
                "OOOOO.OOOOOO.OOOOOOO.OOOOOOO..",
                "O.....♆..........☆.........☄.",
                "O.OOOOO.OOOOOOOOO.OOOOO.OOOOO.",
                "O.☄...........O.........☆....",
                "OOOOOOO.O.OOOOOOOOO.OOOOOOO.O.",
                "O.......O.♆.....O.....☄.....O",
                "OO.OOOOOOOOO.OOOOO.OOOOO.OOOO.",
                "O.....O...☆.....O.....♆......",
                "OOOO.OOO.OOOOOOOOOOO.OOOOOO.O.",
                "O.......O...☄.....O........☆.",
                "OOOOO.OOOOO.OOOOO.OOOOO.OOOOO.",
                "O.♆.....O.......☄...O........",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOO."
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

        chave = new Chave("KeyIcons2.png");
        chave.setPosicao(13, 27); // posição da seta de saída
        this.addPersonagem(chave);

        NaveInimiga nV = new NaveInimiga("Spaceship2_down.png", "projetil1_down.png" ,Consts.DOWN);
        nV.setPosicao(2, 20);  // Posição inicial
        this.addPersonagem(nV);

        NaveInimiga nV2 = new NaveInimiga("Spaceship2_right.png", "projetil1_right.png" , Consts.RIGHT);
        nV2.setPosicao(3, 3);  // Posição inicial
        this.addPersonagem(nV2);

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
