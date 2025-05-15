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
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                "@H                      @   K@",
                "@@@@@@@  @       N@@@@@@@  B @",
                "@     B  @@@@@@@@@@     @@@  @",
                "@                       @    @",
                "@@@@  @@@@@@@@@@@@@@@@@@@    @",
                "@     @@                     @",
                "@     @@B @@@@@@@@@@@@@@@@@@@@",
                "@     @@                     @",
                "@  @@@@@@@@@@@@@@@@@@@@@@@@  @",
                "@  @              @    B @@  @",
                "@  @              @N         @",
                "@  @@B @@@@@@@@@@@@@@@  @@@@@@",
                "@                            @",
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
        };

        for (int linha = 0; linha < labirinto.length; linha++) {
            for (int coluna = 0; coluna < labirinto[linha].length(); coluna++) {
                if (labirinto[linha].charAt(coluna) == '@') {
                    Barreira barreira = new Barreira("asteroid.png");
                    barreira.setPosicao(linha, coluna);
                    this.addPersonagem(barreira);}
                if (labirinto[linha].charAt(coluna)=='K'){
                    // Coloca a chave
                    chave = new Chave("KeyIcons3.png");
                    chave.setPosicao(linha, coluna); // posição da seta de saída
                    this.addPersonagem(chave);
                }

            }
        }

        BichinhoVaiVemHorizontal b1 = new BichinhoVaiVemHorizontal("UfoBlue.png");
        b1.setPosicao(3,5);
        this.addPersonagem(b1);

        BichinhoVaiVemVertical b2 = new BichinhoVaiVemVertical("UfoGrey1.png");
        b2.setPosicao(11,24);
        this.addPersonagem(b2);

        BichinhoVaiVemHorizontal b3 = new BichinhoVaiVemHorizontal("RedPlanet.png");
        b3.setPosicao(7,8);
        this.addPersonagem(b3);

        BichinhoVaiVemHorizontal b4 = new BichinhoVaiVemHorizontal("PurplePlanet.png");
        b4.setPosicao(12,4);
        this.addPersonagem(b4);

        BichinhoVaiVemHorizontal b5 = new BichinhoVaiVemHorizontal("Saturn2.png");
        b5.setPosicao(2,27);
        this.addPersonagem(b5);

        NaveInimiga nave1 = new NaveInimiga("Spaceship4.png","projetil3.png",Consts.UP);
        nave1.setPosicao(2,16);
        this.addPersonagem(nave1);

        NaveInimiga nave2 = new NaveInimiga("Spaceship2_right.png","projetil1_right.png", Consts.RIGHT);
        nave2.setPosicao(11,19);
        this.addPersonagem(nave2);




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
