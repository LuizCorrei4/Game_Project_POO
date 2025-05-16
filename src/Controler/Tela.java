// File: Tela.java

/*
A Tela gerencia a interface gráfica, entidades e eventos.
 */

package Controler;
import Auxiliar.PersonagemSaver;
import Modelo.*;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;

import javax.swing.JOptionPane;
import java.awt.*;
import java.io.File;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class Tela extends javax.swing.JFrame implements KeyListener {

    protected final Posicao spawn = new Posicao(1, 1);
    protected ArrayList<Personagem> faseAtual;
    protected Hero hero;
    protected ControleDeJogo cj = new ControleDeJogo(spawn);
    protected Graphics g2;
    protected int cameraLinha = 0;
    protected int cameraColuna = 0;
    protected Chave chave;
    protected ArrayList<Moeda> moedas;

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


        //objetos salvos em .zip para colocar durante a fase

        NaveInimiga inimigo = new NaveInimiga("Spaceship4.png", "projetil3.png", Consts.UP);
        PersonagemSaver.salvarPersonagem(inimigo, "arquivos_zip_dos_objetos/nave_inimiga_up.zip");

        BichinhoVaiVemHorizontal bichinhoHo = new BichinhoVaiVemHorizontal("Sun.png");
        PersonagemSaver.salvarPersonagem(bichinhoHo, "arquivos_zip_dos_objetos/sol_bichinhohorizontal.zip");

        BichinhoVaiVemVertical bichinhoVe = new BichinhoVaiVemVertical("RocketGrey.png");
        PersonagemSaver.salvarPersonagem(bichinhoVe, "arquivos_zip_dos_objetos/foguete_bichinhovertical.zip");

        BichinhoVaiVemHorizontal bichinhoHo1 = new BichinhoVaiVemHorizontal("Earth.png");
        PersonagemSaver.salvarPersonagem(bichinhoHo1, "arquivos_zip_dos_objetos/terra_bichinhohorizontal.zip");

        BichinhoVaiVemVertical bichinhoVe1 = new BichinhoVaiVemVertical("Saturn2.png");
        PersonagemSaver.salvarPersonagem(bichinhoVe1, "arquivos_zip_dos_objetos/saturno_bichinhovertical.zip");

        ZigueZague zig = new ZigueZague("UfoBlue.png");
        PersonagemSaver.salvarPersonagem(zig, "arquivos_zip_dos_objetos/alien_azul_zigzag.zip");

        ZigueZague zig1 = new ZigueZague("UfoGrey.png");
        PersonagemSaver.salvarPersonagem(zig1, "arquivos_zip_dos_objetos/alien_cinza_zigzag.zip");

        NaveInimiga nv1 = new NaveInimiga("Spaceship2_right.png", "projetil1_right.png", Consts.RIGHT);
        PersonagemSaver.salvarPersonagem(nv1, "arquivos_zip_dos_objetos/nave_inimiga_right.zip");

        NaveInimiga nv2 = new NaveInimiga("Spaceship2_left.png", "projetil1_left.png", Consts.LEFT);
        PersonagemSaver.salvarPersonagem(nv2, "arquivos_zip_dos_objetos/nave_inimiga_left.zip");

        NaveInimiga nv3 = new NaveInimiga("Spaceship2_down.png", "projetil1_down.png", Consts.DOWN);
        PersonagemSaver.salvarPersonagem(nv3, "arquivos_zip_dos_objetos/nave_inimiga_down.zip");


    }


    protected void arrasta() {
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);

                    // Verificação de tipo segura
                    if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> droppedFiles = (List<File>) dtde.getTransferable()
                                .getTransferData(DataFlavor.javaFileListFlavor);

                        for (File file : droppedFiles) {
                            if (file.getName().toLowerCase().endsWith(".zip")) {
                                adicionarPersonagemDeArquivo(file);
                            }
                        }
                        dtde.dropComplete(true);
                        repaint();
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    dtde.dropComplete(false);
                    JOptionPane.showMessageDialog(Tela.this,
                            "Erro ao processar arquivo: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    


protected void adicionarPersonagemDeArquivo(File arquivoZip) {
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arquivoZip))) {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.isDirectory() && entry.getName().endsWith(".ser")) {
                // Remova o try-with-resources aqui e crie o ObjectInputStream diretamente
                ObjectInputStream ois = new ObjectInputStream(zis);
                Personagem personagem = (Personagem) ois.readObject();

                // Converte coordenadas de tela para coordenadas do jogo
                Point dropPoint = getMousePosition();
                if (dropPoint != null) {
                    int coluna = (dropPoint.x - getInsets().left) / Consts.CELL_SIDE + cameraColuna;
                    int linha = (dropPoint.y - getInsets().top) / Consts.CELL_SIDE + cameraLinha;

                    personagem.setPosicao(linha, coluna);
                    addPersonagem(personagem);

                    JOptionPane.showMessageDialog(this,
                            "Personagem adicionado com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Erro ao ler arquivo: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}



    protected void carregarMenu() {
        // Fecha o menu atual
        this.setVisible(false);
        this.dispose();
        // Cria e exibe a nova fase
        Tela fase = new Menu();
        fase.setVisible(true);
        fase.createBufferStrategy(2);
        fase.go();
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
        // this.getBufferStrategy() retorna o objeto que gerencia os buffers de desenho criados por createBufferStrategy(2) na Main
        // getDrawGraphics() devolve um Graphics ligado ao buffer oculto atual
        // Tudo que você desenhar em g ficará nesse buffer, não diretamente nessa tela visível

        /*Criamos um contexto gráfico
        * g.create() retorna um novo objeto da classe Graphics.
        * Ou seja, estamos copiando o estado atual de g para um novo contexto gráfico, sobre o qual você pode desenhar sem alterar o g original.
        */
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /* g.create(int x, int t, int width, int height)
        * x, y definem a origem (0, 0) dEsse novo contexto gráfico, em relação ao contexto original g.
        * Tudo o que for desenhado em g2 usando coordenadas (x, y), irá aparecer, na janela de g.
        * Width, height definem a largura e altura de uma região de recorte.
        * Qualquer traço fora desse retângulo (x, y, width, height) será ignorado.
        * getInsets() informa o tamanho das bordas decorativas da janela;
        */

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

        /* Desenho e processamento dos personagens */
        /*
        *   faseAtual: lista de todos os personagens (herói é o índice 0)
        *   desenhaTudo: chama o autoDesenho() de cada personagem, que por sua vez usa Desenho.desenhar(...)
        *   processaTudo: atualiza a lógica do jogo - movimentação, colisões, remoção de inimigos
        */
        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }
        // Libera recursos: dispose() sinaliza que você terminou de usar os objetos
        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }
    protected void desenha_barreira(){
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
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (Save.allFasesCompleted()) {
                this.setVisible(false);
                Tela telafinal = new TelaFinal();
                //fechar o menu e dps abrir a tela final
                telafinal.setVisible(true);
                telafinal.createBufferStrategy(2);
                telafinal.go();
                javax.swing.Timer timer = new javax.swing.Timer(8500, (evt) -> {
                    System.exit(0);
                });
                timer.setRepeats(false); // Para que o timer execute apenas uma vez
                timer.start();
                return;
            }
            carregarMenu();
            return;
        }
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            // Apaga o arquivo de salvamento
            File saveFile = new File(Save.SAVE_FILE);
            if (saveFile.exists()) {
                if (saveFile.delete()) {
                    System.out.println("Arquivo de salvamento apagado com sucesso!");

                    JOptionPane.showMessageDialog(this,
                            "Progresso do jogo resetado com sucesso!",
                            "Reset de Progresso",
                            JOptionPane.INFORMATION_MESSAGE);
                    carregarMenu();
                } else {
                    System.err.println("Falha ao apagar arquivo de salvamento!");
                    JOptionPane.showMessageDialog(this,
                            "Erro ao resetar progresso!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Nenhum arquivo de salvamento encontrado!");
                JOptionPane.showMessageDialog(this,
                        "Nenhum progresso para resetar!",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            hero.moveUp();
            try {
                hero.troca_imagem("player1_front1.png");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            hero.moveDown();
            try {
                hero.troca_imagem("player1_front2.png");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            hero.moveLeft();
            try {
                hero.troca_imagem("player1_left2.png");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            try {
                hero.troca_imagem("player1_right2.png");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            hero.moveRight();
        }

        this.atualizaCamera();

        // Atualiza o título da janela para mostrar a posição atual do personagem
        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                + (hero.getPosicao().getLinha()));

        repaint();
    }




    void initComponents() {

        // Define que ao fechar a janela, o programa deve encerrar completamente.
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Define o título da janela.
        setTitle("RunSpace");

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
