package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;



public class Menu extends Tela implements KeyListener {

    private ArrayList<Decoracao> checkmarks = new ArrayList<>();
    protected boolean showGameCompleted = false;
    private ArrayList<BuracoNegro> buracosNegros = new ArrayList<>();


    public Menu() {


        faseAtual = new ArrayList<Personagem>();
        hero.setPosicao(1, 7);
        this.addPersonagem(hero);

        this.atualizaCamera();

        for (int i = 0; i < Consts.MUNDO_MENU_LARGURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(0, i);
            this.addPersonagem(temp);
        }

        for (int i = 1; i < Consts.MUNDO_MENU_ALTURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(i, 0);
            this.addPersonagem(temp);
        }

        for (int i = 1; i < Consts.MUNDO_MENU_LARGURA; i++){
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(Consts.MUNDO_MENU_ALTURA-1, i);
            this.addPersonagem(temp);
        }
        for (int i = 1; i < Consts.MUNDO_MENU_ALTURA - 1; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(i, Consts.MUNDO_MENU_LARGURA - 1);
            this.addPersonagem(temp);
        }

        // Buracos Negros
        BuracoNegro fase1 = new BuracoNegro("BuracoNegro.png");
        fase1.setPosicao(3, 10);
        this.addPersonagem(fase1);
        buracosNegros.add(fase1);

        BuracoNegro fase2 = new BuracoNegro("BuracoNegro.png");
        fase2.setPosicao(5, 10);
        this.addPersonagem(fase2);
        buracosNegros.add(fase2);

        BuracoNegro fase3 = new BuracoNegro("BuracoNegro.png");
        fase3.setPosicao(7, 10);
        this.addPersonagem(fase3);
        buracosNegros.add(fase3);

        BuracoNegro fase4 = new BuracoNegro("BuracoNegro.png");
        fase4.setPosicao(9, 10);
        this.addPersonagem(fase4);
        buracosNegros.add(fase4);

        BuracoNegro fase5 = new BuracoNegro("BuracoNegro.png");
        fase5.setPosicao(11, 10);
        this.addPersonagem(fase5);
        buracosNegros.add(fase5);

        for (int i = 0; i < 5; i++) {
            if (Save.isFaseCompleted(i + 1)) {
                Decoracao check = new Decoracao("completedMap.png"); // Você precisa ter esta imagem
                check.setPosicao(3 + i*2, 11); // Posição ao lado do buraco negro
                checkmarks.add(check);
                this.addPersonagem(check);

            }
        }


        showGameCompleted = Save.allFasesCompleted();
        if (Save.allFasesCompleted() && showGameCompleted) {
            JOptionPane.showMessageDialog(this,
                    "Parabéns! Você já completou todas as fases do jogo!\n Aperte 'esc' para finalizar ou 'R' para resetar seu progresso!",
                    "Jogo Completo",
                    JOptionPane.INFORMATION_MESSAGE);
        }

// Estrelas
        Decoracao star1 = new Decoracao("YellowStars2.png");
        star1.setPosicao(2, 12);
        this.addPersonagem(star1);

        Decoracao star2 = new Decoracao("WhiteStars2.png");
        star2.setPosicao(2, 2);
        this.addPersonagem(star2);

        Decoracao star3 = new Decoracao("YellowStars2.png");
        star3.setPosicao(12, 2);
        this.addPersonagem(star3);

        Decoracao star4 = new Decoracao("WhiteStars2.png");
        star4.setPosicao(12, 12);
        this.addPersonagem(star4);

// Naves e planetas (evitando repetição de nomes)
        Decoracao nave1 = new Decoracao("UfoGrey1.png");
        nave1.setPosicao(7, 3);
        this.addPersonagem(nave1);

        Decoracao foguete = new Decoracao("RocketWhite.png");
        foguete.setPosicao(4, 7);
        this.addPersonagem(foguete);

        Decoracao planeta1 = new Decoracao("RedPlanet.png");
        planeta1.setPosicao(8, 5);
        this.addPersonagem(planeta1);

        Decoracao satelite = new Decoracao("Satellite.png");
        satelite.setPosicao(10, 7); // posição ajustada para não repetir
        this.addPersonagem(satelite);

        Decoracao terra = new Decoracao("Earth.png");
        terra.setPosicao(4, 9); // posição ajustada
        this.addPersonagem(terra);

        Decoracao sol = new Decoracao("Sun.png");
        sol.setPosicao(7, 8); // posição ajustada
        this.addPersonagem(sol);

        Decoracao lua = new Decoracao("WhiteMoon.png");
        lua.setPosicao(12, 9); // posição ajustada
        this.addPersonagem(lua);

        Decoracao nave2 = new Decoracao("Spaceship1.png");
        nave2.setPosicao(11, 4); // posição ajustada
        this.addPersonagem(nave2);

        Decoracao nave3 = new Decoracao("Spaceship4.png");
        nave3.setPosicao(8, 12); // posição ajustada
        this.addPersonagem(nave3);



        Numero one = new Numero("n1.png");
        one.setPosicao(3, 9);
        this.addPersonagem(one);

        Numero two = new Numero("n2.png");
        two.setPosicao(5, 9);
        this.addPersonagem(two);

        Numero three = new Numero("n3.png");
        three.setPosicao(7, 9);
        this.addPersonagem(three);

        Numero four = new Numero("n4.png");
        four.setPosicao(9, 9);
        this.addPersonagem(four);

        Numero five = new Numero("n5.png");
        five.setPosicao(11, 9);
        this.addPersonagem(five);


    }


    private void carregarFase(int numeroFase) {
        this.setVisible(false);
        this.dispose();

        Tela fase = null;
        switch(numeroFase) {
            case 1:
                fase = new Fase1();

                break;
            case 2:
                fase = new Fase2();
                break;
            case 3:
                fase = new Fase3();
                break;
            case 4:
                fase = new Fase4();
                break;
            case 5:
                fase = new Fase5();
                break;
        }
        fase.setVisible(true);
        fase.createBufferStrategy(2);
        fase.go();
    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);



        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_MENU_ALTURA && mapaColuna < Consts.MUNDO_MENU_LARGURA) {
                    try {
                        String imageName;

                        // Verifica se a célula atual é a especial
                        if (i == 3 && j == 3) {
                            imageName = "1.png"; // R
                        } else if( i == 3 && j== 4) {
                            imageName = "2.png"; // U
                        } else if( i == 3 && j== 5) {
                            imageName = "3.png"; // N
                        } else if( i == 4 && j== 2) {
                            imageName = "4.png"; // S
                        } else if( i == 4 && j== 3) {
                            imageName = "5.png"; // P
                        } else if( i == 4 && j== 4) {
                            imageName = "6.png"; // A
                        } else if( i == 4 && j== 5) {
                            imageName = "7.png"; // C
                        } else if( i == 4 && j== 6) {
                            imageName = "8.png"; // E
                        } else {
                            imageName = "bg_02_v.png"; // imagem padrão
                        }

                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
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
            for (BuracoNegro buraco : buracosNegros) {
                if (hero.getPosicao().igual(buraco.getPosicao())) {
                    int faseNumero = buracosNegros.indexOf(buraco) + 1;
                    carregarFase(faseNumero);
                    return;
                    //break;
                }
            }
        }


        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }


    @Override
    protected void atualizaCamera() {

        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_MENU_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_MENU_LARGURA - Consts.RES));
    }



    public int getNumFase(){
        return 0;
    }
}
