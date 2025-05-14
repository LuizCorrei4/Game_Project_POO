package Controler;
import Auxiliar.Consts;
import Modelo.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Menu extends Tela{
    int fase_cont = 0;
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

    }



    private void carregarFase(int numeroFase) {
        // Fecha o menu atual
        this.setVisible(false);
        this.dispose();

        // Cria e exibe a nova fase
        Tela fase = null;
        switch(numeroFase) {
            case 1:
                fase = new Fase1();
                break;

        }
        fase.setVisible(true);
        fase.createBufferStrategy(2);
        fase.go();
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

                if (mapaLinha < Consts.MUNDO_MENU_ALTURA && mapaColuna < Consts.MUNDO_MENU_LARGURA) {
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
            for (BuracoNegro buraco : buracosNegros) {
                if (hero.getPosicao().igual(buraco.getPosicao())) {
                    int faseNumero = buracosNegros.indexOf(buraco) + 1;
                    carregarFase(faseNumero);
                    break;
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
}
