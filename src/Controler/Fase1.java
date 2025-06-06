package Controler;
import Auxiliar.*;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fase1 extends Tela {


    public Fase1() {
        faseAtual = new ArrayList<Personagem>();

        this.moedas = new ArrayList<Moeda>(4);

        // Posição inicial do herói
        hero.setPosicao(this.spawn.getLinha(), this.spawn.getColuna()); // posição de entrada no labirinto
        this.addPersonagem(hero);
        this.atualizaCamera();


        String[] labirinto = {
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                "@      S           @   @@@@@@@",
                "@  @    @@@@B @@   @C        @",
                "@  @@@@@@     @@   @@@@@    @@",
                "@C            @@    @ W    @@@",
                "@@@  @@@@@@@@@@@    @    @@@@@@",
                "@ W  @     @              N@@@",
                "@    @     @    @@@@@@@@@@@@@@",
                "@  N     B @    @@@@  S      @",
                "@  @@@@  @@@@@@@@@@@     @@@@@",
                "@  @@ W        B        @@ K @",
                "@  @@  @@@@@   @    @@@@@@   @",
                "@  @@@@@  @@   @@@@@@        @",
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

        NaveInimiga nave1 = new NaveInimiga("Spaceship4.png","projetil3.png",Consts.UP);
        nave1.setPosicao(8,3);
        this.addPersonagem(nave1);

        NaveInimiga nave2 = new NaveInimiga("Spaceship2_left.png","projetil1_left.png",Consts.LEFT);
        nave2.setPosicao(6,26);
        this.addPersonagem(nave2);

        BichinhoVaiVemHorizontal b1 = new BichinhoVaiVemHorizontal("UfoBlue.png");
        b1.setPosicao(9,7);
        this.addPersonagem(b1);

        BichinhoVaiVemVertical b2 = new BichinhoVaiVemVertical("UfoGrey1.png");
        b2.setPosicao(11,12);
        this.addPersonagem(b2);

        BichinhoVaiVemHorizontal b3 = new BichinhoVaiVemHorizontal("RedMoon.png");
        b3.setPosicao(2,12);
        this.addPersonagem(b3);


        this.arrasta();

    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top,
                getWidth() - getInsets().right, getHeight() - getInsets().top);

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
        return 1;
    }
}
