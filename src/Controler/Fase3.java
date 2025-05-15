package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Fase3 extends Tela{

    public Fase3() {
        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna());
        this.addPersonagem(hero);
        this.atualizaCamera();
        this.desenha_barreira();
        int contador_moeda = 0;
        this.moedas = new ArrayList<Moeda>(3);



        String[] labirinto = {
                // colunas de 0 a 29 (linha 0 e 14 são margens visuais, não terão barreiras)
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
                "O.D...O...E....O.P.O.X.C..O.O",
                "OOOOO.OOOOOO.OOO...O.OOOOOOO.O",
                "O.....R...C...P.....E.....D..O",
                "O.OOOOO.OOOOOOOOO.OOOOO.OOOOOO",
                "O.X...O....O.P....O...R...O.O",
                "OOOOO.O.OOOOOOOOO.OOOOOOO.OOOO",
                "O...R...O..C..D.O.....X....O.O",
                "OO.OOOOOOOOO.OOOOO.OOOOO.OOOOO",
                "O.P...O.....E..... .....R...O.O",
                "OOOO.OOO.OOOOOOOOOOO.OOOOOO.OO",
                "O.....O...X.....O........D.O",
                "OOOOO.OOOOO.OOOOO.OOOOO.OOOOOO",
                "O.E...O.P.....R...O.X....KO.O",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"
        };

        for (int linha = 0; linha < labirinto.length; linha++) {
            for (int coluna = 0; coluna < labirinto[linha].length(); coluna++) {
                if (labirinto[linha].charAt(coluna) == 'O') {
                    Barreira barreira = new Barreira("asteroid.png");
                    barreira.setPosicao(linha, coluna);
                    this.addPersonagem(barreira);
                }
                if (labirinto[linha].charAt(coluna)=='C'){
                    Moeda moeda = new Moeda("coin.png");
                    moeda.setPosicao(linha, coluna);
                    this.moedas.add(moeda);
                    this.addPersonagem(moeda);
                }
                if (labirinto[linha].charAt(coluna)=='K'){
                    // Coloca a chave
                    chave = new Chave("KeyIcons3_translucent.png");
                    chave.setPosicao(linha, coluna); // posição da seta de saída
                    this.addPersonagem(chave);
                }
            }
        }


        ZigueZague zig = new ZigueZague("UfoBlue.png");
        zig.setPosicao(22,20);
        this.addPersonagem(zig);

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

            for(Moeda c : moedas) {
                if( c.isCatched() ){
                    moedas.remove(c);
                }
            }
            if (moedas.isEmpty()){
                this.chave.setImage("KeyIcons3.png");
            }
            if (hero.getPosicao().igual(chave.getPosicao()) && moedas.isEmpty()) {
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
