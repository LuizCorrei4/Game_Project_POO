// File: Tela.java

/*
A Tela gerencia a interface gráfica, entidades e eventos.
 */

package Controler;

import Modelo.*;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.*;

public class Tela extends javax.swing.JFrame implements KeyListener {

    protected ArrayList<Personagem> faseAtual;
    protected Hero hero;
    protected ControleDeJogo cj = new ControleDeJogo();
    protected Graphics g2;
    protected int cameraLinha = 0;
    protected int cameraColuna = 0;

    // Construtor da tela
    public Tela() {

        // Define o cenário da tela para a instância atual (útil para manipulação do cenário)
        Desenho.setCenario(this);

        // Inicializa os componentes da interface gráfica
        initComponents();

        /* Configura o ouvinte de teclado, para reagir a eventos de pressionamento de teclas */
        this.addKeyListener(this);

        /* Configura o tamanho da janela com base nas dimensões do tabuleiro e os insets (bordas da janela) */
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        // Inicializa a lista de personagens na fase atual
        faseAtual = new ArrayList<Personagem>();

        // Cria e posiciona o herói no tabuleiro, além de adicioná-lo à lista de personagens
        hero = new Hero("player1_right2.png");
        hero.setPosicao(1, 7);  // Posição inicial do herói
        this.addPersonagem(hero);

        // Atualiza a posição da câmera para que o herói fique centralizado (ou dentro da área visível)
        this.atualizaCamera();

        // Criação das barreiras (asteroides) que formam as bordas do tabuleiro
        // Barreira superior
        for (int i = 0; i < Consts.MUNDO_LARGURA; i++) {
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(0, i); // Define a posição da barreira
            this.addPersonagem(temp); // Adiciona a barreira ao jogo
        }

        // Barreira esquerda
        for (int i = 1; i < Consts.MUNDO_ALTURA; i++) {
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(i, 0); // Posição da barreira à esquerda
            this.addPersonagem(temp);
        }

        // Barreira inferior
        for (int i = 1; i < Consts.MUNDO_LARGURA; i++) {
            Barreira temp;
            temp = new Barreira("asteroid.png");
            temp.setPosicao(Consts.MUNDO_ALTURA-1, i); // Posição da barreira na parte inferior
            this.addPersonagem(temp);
        }

        // Barreira direita
        for (int i = 1; i < Consts.MUNDO_ALTURA - 1; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(i, Consts.MUNDO_LARGURA - 1); // Posição da barreira à direita
            this.addPersonagem(temp);
        }

        // Criação e posicionamento de outros personagens (exemplo de inimigos ou objetos móveis)

        // ZigueZague (movimento em ziguezague)
        ZigueZague zz = new ZigueZague("robo.png");
        zz.setPosicao(5, 5);  // Posição inicial
        this.addPersonagem(zz); // Adiciona ao jogo

        // Bichinho que se move horizontalmente
        BichinhoVaiVemHorizontal bBichinhoH = new BichinhoVaiVemHorizontal("roboPink.png");
        bBichinhoH.setPosicao(3, 3);  // Posição inicial
        this.addPersonagem(bBichinhoH);

        // Outro bichinho que se move horizontalmente
        BichinhoVaiVemHorizontal bBichinhoH2 = new BichinhoVaiVemHorizontal("roboPink.png");
        bBichinhoH2.setPosicao(6, 6);  // Posição inicial
        this.addPersonagem(bBichinhoH2);

        // Bichinho que se move verticalmente
        BichinhoVaiVemVertical bVv = new BichinhoVaiVemVertical("Caveira.png");
        bVv.setPosicao(5, 5);  // Posição inicial
        this.addPersonagem(bVv);

        // Um outro inimigo (Caveira)
        Caveira bV = new Caveira("caveira.png");
        bV.setPosicao(9, 1);  // Posição inicial
        this.addPersonagem(bV);
    }


    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    /* O metodo paint(..) é chamado pelo Swing sempre que precisa "desenhar" a janela.
    *  O parâmetro gOld é um objeto que representa "uma caneta" para desenhar na tela.
    */
    public void paint(Graphics gOld) {


        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        // **********Desenha cenário de fundo*************

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

    /*Metodo responsável por atualizar a posição da câmera com base na posição atual do herói (hero)*/
    protected void atualizaCamera() {
        // Obtém a linha atual do herói
        int linha = hero.getPosicao().getLinha();

        // Obtém a coluna atual do herói
        int coluna = hero.getPosicao().getColuna();

        // Calcula a linha da câmera:
        // - Tenta centralizar o herói verticalmente na tela.
        // - Usa Math.max para garantir que a câmera nunca vá acima do topo do mapa (linha < 0).
        // - Usa Math.min para garantir que a câmera não ultrapasse os limites inferiores do mapa.
        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));

        // Calcula a coluna da câmera (mesmo raciocínio da linha):
        // - Centraliza horizontalmente, respeitando os limites laterais do mundo.
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }


    public void go() {
        /* TimerTask é uma classe abstrata que representa "o que" você quer executar.
        * Então é criado uma subclasse anônima ( o new TimerTask() { ... } e implementa o
        * metodo run( ) dentro dela, definido "o que" acontecerá a cada execução
         */
        TimerTask task = new TimerTask() {
            /* O run() chama repaint()
            * O repaint() não desenha imediatamente; ele marca a janela como "precisando ser repintada" no EDT (thread de eventos do Swing)
             */
            public void run() {
                repaint(); // o Swing, no EDT, chama o paint()
            }
        };
        // java.util.Timer é uma "ferramenta" do Java que permite agenda tarefas para rodar no futuro, periodicamente se quisermos.
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
        /*   task: o que será executado
        *   0: atraso inicial em milissegundos ("zero" faz com que comece imediatamente.
        *   Consts.PERIOD é o período entre execuções, em milissegundos.
        *   Ou seja, a cada determinado período de tempo (PERIOD = x ms), a tarefa task.run é chamada pelo Timer.
        *   Enquanto o Timer não é cancelado, esses passos se repetem automaticamente.
         */
    }

    public void keyPressed(KeyEvent e) {
        // Verifica se a tecla pressionada foi 'C' (limpa a fase atual)
        if (e.getKeyCode() == KeyEvent.VK_C) {
            // Limpa a lista de personagens ou objetos da fase atual
            this.faseAtual.clear();
        }
        // Verifica se a tecla pressionada foi 'UP' (seta para cima)
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            // Move o personagem (herói) para cima
            hero.moveUp();
            try {
                // Troca a imagem do personagem para a imagem correspondente ao movimento para cima
                hero.troca_imagem("player1_front1.png");
            } catch (IOException ex) {
                // Lança uma exceção em caso de erro ao tentar trocar a imagem
                throw new RuntimeException(ex);
            }
        }
        // Verifica se a tecla pressionada foi 'DOWN' (seta para baixo)
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            // Move o personagem (herói) para baixo
            hero.moveDown();
            try {
                // Troca a imagem do personagem para a imagem correspondente ao movimento para baixo
                hero.troca_imagem("player1_front2.png");
            } catch (IOException ex) {
                // Lança uma exceção em caso de erro ao tentar trocar a imagem
                throw new RuntimeException(ex);
            }
        }
        // Verifica se a tecla pressionada foi 'LEFT' (seta para a esquerda)
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Move o personagem (herói) para a esquerda
            hero.moveLeft();
            try {
                // Troca a imagem do personagem para a imagem correspondente ao movimento para a esquerda
                hero.troca_imagem("player1_left2.png");
            } catch (IOException ex) {
                // Lança uma exceção em caso de erro ao tentar trocar a imagem
                throw new RuntimeException(ex);
            }
        }
        // Verifica se a tecla pressionada foi 'RIGHT' (seta para a direita)
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            try {
                // Troca a imagem do personagem para a imagem correspondente ao movimento para a direita
                hero.troca_imagem("player1_right2.png");
            } catch (IOException ex) {
                // Lança uma exceção em caso de erro ao tentar trocar a imagem
                throw new RuntimeException(ex);
            }
            // Move o personagem (herói) para a direita
            hero.moveRight();
        }

        // Atualiza a posição da câmera, para que ela acompanhe o personagem
        this.atualizaCamera();

        // Atualiza o título da janela para mostrar a posição atual do personagem
        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                + (hero.getPosicao().getLinha()));

        // Desenha a tela novamente (comentado, mas poderia ser usado para redibujar a interface)
        repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }




    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Este marcador é usado pelo NetBeans ou IDEs similares para permitir que esse bloco seja recolhido/expandido na interface.

    void initComponents() {

        // Define que ao fechar a janela, o programa deve encerrar completamente.
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Define o título da janela.
        setTitle("POO2023-1 - Skooter");

        // Garante que essa janela fique sempre sobre as outras (útil para jogos ou aplicações visuais).
        setAlwaysOnTop(true);

        // Desativa o foco automático (a janela não força a captura do foco).
        setAutoRequestFocus(false);

        // Impede que a janela seja redimensionada pelo usuário.
        setResizable(false);

        // Define um layout de grupo (GroupLayout) para organizar os componentes dentro do JFrame.
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // Define o layout horizontal: neste caso, nenhuma largura específica, só um "vão" de 561 pixels.
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 561, Short.MAX_VALUE)
        );

        // Define o layout vertical: similar ao horizontal, com um espaço de 500 pixels.
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );

        // Ajusta automaticamente o tamanho da janela com base nos componentes e layout definidos.
        pack();
    }
// </editor-fold>

// Declaração de variáveis geradas automaticamente pela IDE.
// Essas variáveis normalmente representam componentes gráficos (botões, painéis etc).
// No momento, não há nenhum componente declarado.

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
