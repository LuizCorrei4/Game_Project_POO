package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Fase4 extends Tela{

    public Fase4() {
        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna());
        this.addPersonagem(hero);
        this.atualizaCamera();
        this.desenha_barreira();

        String[] labirinto = {
                // colunas de 0 a 29 (linha 0 e 14 são margens visuais, não terão barreiras)
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
                "O..J..O..V..O.....O.A..O.L..O",
                "OOO.OOOOOO.OOOOOOO.OOOOOOO.OO",
                "O.VV....O..A...O.....J....O.O",
                "O.OOOOO.OOOOO.OOOOO.OOOOOOOOO",
                "O.A..O.VV....O...VV..O..L..O.O",
                "OOOO.OOO.OOOOOOOOO.OOOOO.OOOOO",
                "O.....J..O.A.....O.VV....A.O",
                "O.OOO.OOOOO.OOOOO.OOOOOO.OOOO",
                "O.VV..O.A...O.L..O....J...O.O",
                "OOOOO.OOO.OOOOOOOOO.OOOOOO.OO",
                "O..A....O.....VV..O.A....J.O",
                "O.OOOOOOOO.OOOOO.OOOOOOOOOOOO",
                "O.L..O.VV..A...O.J....O..L.O.O",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"
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
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

}
