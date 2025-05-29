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


        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna());
        this.addPersonagem(hero);
        this.atualizaCamera();
        this.moedas = new ArrayList<Moeda>(4);


        String[] labirinto = {
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                "@      S           @   @@@@@@@",
                "@  @    @@@@B @@   @C        @",
                "@  @@@@@@     @@   @@@@@    N@", // 3,28 <-
                "@C            @@    @ W    @@@",
                "@@@  @@@@@@@@@@@    @   @@@@@@",
                "@ W  @     @N@            N@@@", //       6,12^
                "@    @     N@@   @@@@@@@@@@@@@", //  <-7,11
                "@          @N@   @@@  S      @", //       8,12
                "@  @@@@  @@@ @@@@@@@     @@@@@",
                "@  @@ W        B         @@ K@",
                "@  @@  @@@@@   @    @@@@@@   @",
                "@N  @@@@  @@   @@@@@@        @", // 12,1 ->
                "@C        @@C          S     @",
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
                    chave = new Chave("KeyIcons1_translucent.png");
                    chave.setPosicao(linha, coluna); // posição da seta de saída
                    this.addPersonagem(chave);
                }
                if (labirinto[linha].charAt(coluna)=='C'){
                    Moeda moeda = new Moeda("coin.png");
                    moeda.setPosicao(linha, coluna);
                    this.moedas.add(moeda);
                    this.addPersonagem(moeda);
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

        NaveInimiga nvVertical = new NaveInimiga("Spaceship2_right.png", "projetil1_right.png", Consts.RIGHT);
        nvVertical.setPosicao(12, 1);
        this.addPersonagem(nvVertical);

        NaveInimiga nvHorizontal = new NaveInimiga("Spaceship2_left.png", "projetil1_left.png", Consts.LEFT);
        nvHorizontal.setPosicao(3, 28);
        this.addPersonagem(nvHorizontal);

        NaveInimiga nv3 = new NaveInimiga("Spaceship2_left.png", "projetil2.png", Consts.LEFT);
        nv3.setPosicao(7, 11);
        this.addPersonagem(nv3);


        NaveInimiga nv5 = new NaveInimiga("Spaceship2_up.png", "projetil3.png", Consts.UP);
        nv5.setPosicao(6, 12);
        this.addPersonagem(nv5);

        NaveInimiga nv6 = new NaveInimiga("Spaceship2_down.png", "projetil3.png", Consts.DOWN);
        nv6.setPosicao(8, 12);
        this.addPersonagem(nv6);

        BichinhoVaiVemHorizontal b1 = new BichinhoVaiVemHorizontal("UfoBlue.png");
        b1.setPosicao(9,7);
        this.addPersonagem(b1);

        BichinhoVaiVemVertical b2 = new BichinhoVaiVemVertical("UfoGrey1.png");
        b2.setPosicao(11,12);
        this.addPersonagem(b2);

        BichinhoVaiVemHorizontal b3 = new BichinhoVaiVemHorizontal("RedPlanet.png");
        b3.setPosicao(2,12);
        this.addPersonagem(b3);

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
        return 5;
    }
}
