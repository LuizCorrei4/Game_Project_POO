// File: Fase2.java

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
        this.moedas = new ArrayList<Moeda>(3);



        String[] labirinto = {

                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
                "O.......O..............S.....",
                "OOOOO.OOOOOO.OOOOOOO.OOOOOOOC.",
                "O............W...............",
                "O.OOOOO.OOOOOOOOO.OOOOO.OOOOO.",
                "O....S........O..............",
                "OOOOOOO.O.OOOOOOOOO.OOOOOOO.O.",
                "O.......O.......O...........O",
                "OO.OOOOOOOOO.OOOOO.OOOOO.OOOO.",
                "O.....O.........O.........W..",
                "O  .OOOCOOOOOOOOOOO.OOOOOO.O.",
                "O......O ....S....O..........",
                "OOOOO.OOOOO.OOOOO.OOOOO.OOOOO.",
                "O......CO......W....O.....K..",
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOO."
        };

        // 10, 8  13,7  2,28
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
                    chave = new Chave("KeyIcons2_translucent.png");
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



        NaveInimiga nV = new NaveInimiga("Spaceship2_down.png", "projetil1_down.png" ,Consts.DOWN);
        nV.setPosicao(0, 20);
        this.addPersonagem(nV);

        NaveInimiga nV2 = new NaveInimiga("Spaceship2_right.png", "projetil1_right.png" , Consts.RIGHT);
        nV2.setPosicao(3, 1);
        this.addPersonagem(nV2);

        BichinhoVaiVemHorizontal bichinho = new BichinhoVaiVemHorizontal("UfoBlue.png");
        bichinho.setPosicao(1, 11);
        this.addPersonagem(bichinho);

        BichinhoVaiVemHorizontal bichinho2 = new BichinhoVaiVemHorizontal("RedMoon.png");
        bichinho2.setPosicao(7, 12);
        this.addPersonagem(bichinho2);

        BichinhoVaiVemHorizontal bichinho3 = new BichinhoVaiVemHorizontal("Saturn2.png");
        bichinho3.setPosicao(9, 12);
        this.addPersonagem(bichinho3);

        BichinhoVaiVemHorizontal bichinho4 = new BichinhoVaiVemHorizontal("UfoGrey.png");
        bichinho4.setPosicao(7, 18);
        this.addPersonagem(bichinho4);

        ZigueZague zig = new ZigueZague("Sun.png");
        zig.setPosicao(9,1); //ta certo
        this.addPersonagem(zig);

        super.arrasta();

    }

 @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();

        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);


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
         this.isProcessingEntities = true;


         this.cj.desenhaTudo(faseAtual);


         this.applyPendingModifications();


         this.cj.processaTudo(faseAtual);


         this.applyPendingModifications();



         ArrayList<Moeda> moedasARemover = new ArrayList<>();
         for (Moeda c : moedas) {
             if (c.isCatched()) {
                 moedasARemover.add(c);
             }
         }
         if (!moedasARemover.isEmpty()) {
             moedas.removeAll(moedasARemover);
         }


         if (moedas.isEmpty()) {
             this.chave.setImage("KeyIcons1.png");
         }
         if (hero.getPosicao().igual(chave.getPosicao()) && moedas.isEmpty()) {
             Save.saveProgress(getNumFase());
             this.isProcessingEntities = false;
             this.applyPendingModifications();
             carregarMenu();
             return;
         }

         this.isProcessingEntities = false;
         this.atualizaCamera();
     }

     g.dispose();
     g2.dispose();
     if (!getBufferStrategy().contentsLost()) {
         getBufferStrategy().show();
     }
    }

    @Override
    public int getNumFase(){
        return 2;
    }
}
