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

        this.moedas = new ArrayList<Moeda>(4);



        String[] labirinto = {
                // colunas de 0 a 29 (linha 0 e 14 são margens visuais, não terão barreiras)
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
                "O.....O........O.. .O ..C.. .O",
                "OOOOO. OOOOO.OOO0C. O0O0OOO .O",
                "O..S......C...........W......O",
                "O.OOOOO.OOOOOOOOO.OOOOO.OOOOOO",
                "O.....O....O......O..... ..O.O",
                "OOOOO.O.OOOOOOOOO.OOOOOOO.OOOO",
                "O...S...O..C....O.....W....O.O",
                "OO.OOOOOOOOO.OOOOO. OOOO.OOOOO",
                "O.....O.......S... .........O.O",
                "OOOO.OOO. OOOOOOOOOOO.OOOOOO.OO",
                "O.....O.........O...   .....D.O",
                "OOOOO.OOOOO.OOOOO. OOOOO.OOOOOO",
                "O.C...O.......W...O..  ....KO.O",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO0"
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
                if (labirinto[linha].charAt(coluna)=='S'){
                    Decoracao star1 = new Decoracao("YellowStars2.png");
                    star1.setPosicao(linha, coluna);
                    this.addPersonagem(star1);
                }
                if (labirinto[linha].charAt(coluna)=='W'){
                    Decoracao star2 = new Decoracao("WhiteStars2.png");
                    star2.setPosicao(linha, coluna);
                    this.addPersonagem(star2);
                }

            }
        }


        ZigueZague zig = new ZigueZague("UfoBlue.png");
        zig.setPosicao(1,16); //ta certo
        this.addPersonagem(zig);

        BichinhoVaiVemHorizontal b1 = new BichinhoVaiVemHorizontal("UfoBlue.png");
        b1.setPosicao(8,18); //ta certo
        this.addPersonagem(b1);

        BichinhoVaiVemHorizontal b3 = new BichinhoVaiVemHorizontal("WhiteMoon.png");
        b3.setPosicao(2,4); //ta certo
        this.addPersonagem(b3);

        NaveInimiga nave1 = new NaveInimiga("Spaceship4.png","projetil3.png",Consts.UP);
        nave1.setPosicao(11,19);
        this.addPersonagem(nave1);

        NaveInimiga nave2 = new NaveInimiga("Spaceship2_right.png","projetil1_right.png", Consts.RIGHT);
        nave2.setPosicao(5,1); //ta certo
        this.addPersonagem(nave2);

        BichinhoVaiVemHorizontal b4 = new BichinhoVaiVemHorizontal("PurplePlanet.png");
        b4.setPosicao(11,3);
        this.addPersonagem(b4);

        BichinhoVaiVemHorizontal b5 = new BichinhoVaiVemHorizontal("Saturn2.png");
        b5.setPosicao(3,27);
        this.addPersonagem(b5);

        ZigueZague zig1 = new ZigueZague("Sun.png");
        zig1.setPosicao(7,10); //ta certo
        this.addPersonagem(zig1);

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
