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
                // colunas de 0 a 29
                "                              ",
                " .....O........O.. .O ..C.. . ",
                " OOOO. OOOOO.OOOOC. OOOOOOO . ",
                " ..S......C...........W...... ",
                " .OOOOO.OOOOOOOOO.OOOOO.OOOOO ",
                " .....O....O......O..... ..O. ",
                " OOOO.O.OOOOOOOOO.OOOOOOO.OOO ",
                " ...S...O..C..T.O.....W....O. ", // 7,13
                " O.OOOOOOOOOOOOOO . OOOO.OOOOO",
                " .....O.......S... .........O. ",
                " OOO.OOO. OOOOOOOOOOO.OOOOOO.O ",
                " .....O.........O...   .....D. ",
                " OOO .OOOOO.OOOOO. OOOOO.OOOOO ",
                "   TC...O.......W...O..  ...KO.", //13,1
                "                               "
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
                    chave = new Chave("KeyIcons3_translucent.png");
                    chave.setPosicao(linha, coluna);
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
        zig.setPosicao(1,16);
        this.addPersonagem(zig);

        BichinhoVaiVemHorizontal b1 = new BichinhoVaiVemHorizontal("UfoBlue.png");
        b1.setPosicao(8,18);
        this.addPersonagem(b1);

        BichinhoVaiVemHorizontal b3 = new BichinhoVaiVemHorizontal("WhiteMoon.png");
        b3.setPosicao(2,4);
        this.addPersonagem(b3);

        NaveInimiga nave1 = new NaveInimiga("Spaceship4.png","projetil3.png",Consts.UP);
        nave1.setPosicao(11,19);
        this.addPersonagem(nave1);

        NaveInimiga nave2 = new NaveInimiga("Spaceship2_right.png","projetil1_right.png", Consts.RIGHT);
        nave2.setPosicao(5,1);
        this.addPersonagem(nave2);

        BichinhoVaiVemHorizontal b4 = new BichinhoVaiVemHorizontal("PurplePlanet.png");
        b4.setPosicao(11,3);
        this.addPersonagem(b4);

        BichinhoVaiVemHorizontal b5 = new BichinhoVaiVemHorizontal("Saturn2.png");
        b5.setPosicao(2,27);
        this.addPersonagem(b5);



        Teletransporte t1 = new Teletransporte("BuracoNegro.png");
        t1.setPosicao(7, 13); //7,13
        this.addPersonagem(t1);

        Teletransporte t2 = new Teletransporte("BuracoNegro_right.png");
        t2.setPosicao(13, 2);
        this.addPersonagem(t2);

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
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);

            Teletransporte t1 = null;
            Teletransporte t2 = null;


            for(int i = 0; i<this.faseAtual.size(); i++) {
                if(faseAtual.get(i) instanceof Teletransporte) {
                    t1 = (Teletransporte) faseAtual.get(i);
                    t2 = (Teletransporte) faseAtual.get(i+1);
                    break;
                }
            }

            assert t1 != null;
            if (hero.getPosicao().igual(t1.getPosicao())) {
                hero.setPosicao(t2.getPosicao().getLinha(), t2.getPosicao().getColuna()+1);//t1 para t2
            }
            if (hero.getPosicao().igual(t2.getPosicao())){
                hero.setPosicao(t1.getPosicao().getLinha(), t1.getPosicao().getColuna()-1);
            }



            for(Moeda c : moedas) {
                if( c.isCatched() ){
                    moedas.remove(c);
                }
            }
            if (moedas.isEmpty()){
                this.chave.setImage("KeyIcons3.png");
            }
            if (hero.getPosicao().igual(chave.getPosicao()) && moedas.isEmpty()) {
                Save.saveProgress(3);
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
