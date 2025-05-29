// File: Controler/Tela.java
package Controler;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.GameState; // Import the GameState class
import Auxiliar.PersonagemSaver;
import Auxiliar.Posicao;
import Modelo.*;

import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class Tela extends javax.swing.JFrame implements KeyListener {

    protected final Posicao spawn = new Posicao(1, 1);
    protected ArrayList<Personagem> faseAtual;
    protected Hero hero; // Declared here
    protected ControleDeJogo cj = new ControleDeJogo(spawn);
    protected Graphics g2;
    protected int cameraLinha = 0;
    protected int cameraColuna = 0;
    protected Chave chave;
    protected ArrayList<Moeda> moedas;

    public static final String SAVE_GAME_FILE = "gameState.dat";
    private Timer gameTimer;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addKeyListener(this);
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = new ArrayList<Personagem>();

        // Initialize hero BEFORE any method that might use it (like atualizaCamera) is called.
        // Subclasses (Menu, FaseX) will set its position and add it to faseAtual.
        hero = new Hero("player1_right2.png"); // Default image, ensures hero is not null

        this.atualizaCamera(); // Called after hero is guaranteed to be non-null

        // Drag and drop setup from original code
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
        this.arrasta();
    }

    protected void arrasta() {
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
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
                        // repaint(); // Repaint is handled by game loop
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    dtde.dropComplete(false);
                    JOptionPane.showMessageDialog(Tela.this,
                            "Erro ao processar arquivo arrastado: " + ex.getMessage(),
                            "Erro de Drag and Drop", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro no drop", ex);
                }
            }
        });
    }

    protected void adicionarPersonagemDeArquivo(File arquivoZip) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arquivoZip))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".ser")) {
                    ObjectInputStream ois = new ObjectInputStream(zis);
                    Personagem personagem = (Personagem) ois.readObject();
                    // Not closing ois here to avoid closing zis prematurely. zis is closed by try-with-resources.

                    Point dropPoint = getMousePosition(); // Make sure frame is visible and mouse is over it
                    if (dropPoint != null) {
                        int coluna = (dropPoint.x - getInsets().left) / Consts.CELL_SIDE + cameraColuna;
                        int linha = (dropPoint.y - getInsets().top) / Consts.CELL_SIDE + cameraLinha;
                        personagem.setPosicao(linha, coluna);
                        addPersonagem(personagem);
                        JOptionPane.showMessageDialog(this,
                                "Personagem adicionado com sucesso!",
                                "Sucesso Drag and Drop", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Não foi possível determinar a posição do mouse.",
                                "Erro Drag and Drop", JOptionPane.WARNING_MESSAGE);
                    }
                }
                zis.closeEntry();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao ler personagem do arquivo ZIP: " + ex.getMessage(),
                    "Erro de Drag and Drop", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao adicionar personagem de arquivo", ex);
        }
    }

    protected void saveGameState() {
        if (this.hero == null) {
            JOptionPane.showMessageDialog(this, "Não é possível salvar: Herói não encontrado.", "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Posicao currentSpawn = this.spawn;
        String phaseClassName = this.getClass().getName();
        GameState gameState = new GameState(phaseClassName, this.faseAtual,
                this.cameraLinha, this.cameraColuna,
                this.hero, this.chave, currentSpawn, "save.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_GAME_FILE))) {


            oos.writeObject(gameState);
            JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso! O jogo será fechado.", "Salvo", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao salvar o jogo.", ex);
            JOptionPane.showMessageDialog(this, "Erro ao salvar o jogo: " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void restoreGameState(GameState state) {
        this.faseAtual = state.faseAtual;
        this.cameraLinha = state.cameraLinha;
        this.cameraColuna = state.cameraColuna;

        if (state.spawn != null) {
            this.spawn.setPosicao(state.spawn.getLinha(), state.spawn.getColuna());
        }
        this.cj = new ControleDeJogo(this.spawn);

        this.hero = null;
        this.chave = null;
        ArrayList<Moeda> loadedMoedas = new ArrayList<>();

        for (Personagem p : this.faseAtual) {
            if (p instanceof Hero) {
                this.hero = (Hero) p;
            } else if (p instanceof Chave) {
                this.chave = (Chave) p;
            } else if (p instanceof Moeda) {
                loadedMoedas.add((Moeda) p);
            }
        }

        if (this.hero != null && state.heroPosicao != null) {
            this.hero.setPosicao(state.heroPosicao.getLinha(), state.heroPosicao.getColuna());
            if (state.heroImageName != null) {
                try {
                    this.hero.troca_imagem(state.heroImageName);
                } catch (IOException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao restaurar imagem do herói.", ex);
                }
            }
        } else if (this.hero == null) {
            Logger.getLogger(Tela.class.getName()).log(Level.WARNING, "Herói não encontrado no estado salvo.");
        }

        if (this.chave != null && state.chavePosicao != null) {
            this.chave.setPosicao(state.chavePosicao.getLinha(), state.chavePosicao.getColuna());
            if (state.chaveImageName != null) {
                this.chave.setImage(state.chaveImageName);
            }
        }
        this.moedas = loadedMoedas;

        if (this instanceof Fase1) {
            ((Fase1) this).moedas = this.moedas; ((Fase1) this).chave = this.chave; ((Fase1) this).hero = this.hero;
        } else if (this instanceof Fase2) {
            ((Fase2) this).moedas = this.moedas; ((Fase2) this).chave = this.chave; ((Fase2) this).hero = this.hero;
        } else if (this instanceof Fase3) {
            ((Fase3) this).moedas = this.moedas; ((Fase3) this).chave = this.chave; ((Fase3) this).hero = this.hero;
        } else if (this instanceof Fase4) {
            ((Fase4) this).moedas = this.moedas; ((Fase4) this).chave = this.chave; ((Fase4) this).hero = this.hero;
        } else if (this instanceof Fase5) {
            ((Fase5) this).moedas = this.moedas; ((Fase5) this).chave = this.chave; ((Fase5) this).hero = this.hero;
        } else if (this instanceof Menu) {
            ((Menu) this).hero = this.hero;
        } else if (this instanceof TelaFinal) {
            ((TelaFinal) this).hero = this.hero;
        }

        // Sobrescreve "save.dat" com a lista de fasesCompletadas do GameState carregado
        if (state.fasesCompletadas != null) {
            try (FileWriter writer = new FileWriter(Save.SAVE_FILE)) { // Abre no modo de sobrescrita
                for (int faseId : state.fasesCompletadas) {
                    writer.write(faseId + "\n");
                }
                System.out.println("Arquivo 'save.dat' foi sobrescrito com as fases completadas do GameState.");
            } catch (IOException e) {
                System.err.println("Erro ao sobrescrever 'save.dat' a partir do GameState: " + e.getMessage());
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao sobrescrever save.dat", e);
            }
        }

        this.atualizaCamera();
    }

    protected void carregarMenu() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
        this.setVisible(false);
        this.dispose();
        Tela menu = new Menu();
        menu.setVisible(true);
        menu.createBufferStrategy(2);
        menu.go();
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
        if (umPersonagem != null && !faseAtual.contains(umPersonagem)) {
            faseAtual.add(umPersonagem);
        }
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    @Override
    public void paint(Graphics gOld) {
        if (this.getBufferStrategy() == null) {
            this.createBufferStrategy(2); // Essential for custom painting
            return;
        }
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        if (g == null) return; // Should not happen if buffer strategy is valid

        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        int currentWorldHeight = Consts.MUNDO_ALTURA;
        int currentWorldWidth = Consts.MUNDO_LARGURA;
        String bgImageName = "bg_02_v.png"; // Default background

        // Menu and TelaFinal have their own paint methods that will draw their specific backgrounds.
        // For FaseX, draw the default background.
        if (!(this instanceof Menu || this instanceof TelaFinal)) {
            for (int i = 0; i < Consts.RES; i++) {
                for (int j = 0; j < Consts.RES; j++) {
                    int mapaLinha = cameraLinha + i;
                    int mapaColuna = cameraColuna + j;
                    if (mapaLinha >= 0 && mapaLinha < currentWorldHeight && mapaColuna >= 0 && mapaColuna < currentWorldWidth) {
                        try {
                            Image newImage = Toolkit.getDefaultToolkit().getImage(
                                    new java.io.File(".").getCanonicalPath() + Consts.PATH + bgImageName);
                            g2.drawImage(newImage, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                        } catch (IOException ex) {
                            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Error drawing background tile", ex);
                        }
                    }
                }
            }
        }
        // If it's Menu or TelaFinal, their overridden paint() method will be called, handling their specific background.

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

    protected void desenha_barreira() {
        for (int i = 0; i < Consts.MUNDO_LARGURA; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(0, i);
            this.addPersonagem(temp);
        }
        for (int i = 1; i < Consts.MUNDO_ALTURA; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(i, 0);
            this.addPersonagem(temp);
        }
        for (int i = 1; i < Consts.MUNDO_LARGURA; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(Consts.MUNDO_ALTURA - 1, i);
            this.addPersonagem(temp);
        }
        for (int i = 1; i < Consts.MUNDO_ALTURA - 1; i++) {
            Barreira temp = new Barreira("asteroid.png");
            temp.setPosicao(i, Consts.MUNDO_LARGURA - 1);
            this.addPersonagem(temp);
        }
    }

    protected void atualizaCamera() {
        if (hero == null) { // Safeguard
            // Logger.getLogger(Tela.class.getName()).log(Level.WARNING, "atualizaCamera called with null hero in " + this.getClass().getName());
            return;
        }
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();
        int currentWorldHeight = Consts.MUNDO_ALTURA;
        int currentWorldWidth = Consts.MUNDO_LARGURA;

        if (this instanceof Menu || this instanceof TelaFinal) {
            currentWorldHeight = Consts.MUNDO_MENU_ALTURA;
            currentWorldWidth = Consts.MUNDO_MENU_LARGURA;
        }
        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, currentWorldHeight - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, currentWorldWidth - Consts.RES));
    }

    public void go() {

        // Lógica de apagar a fase do save.dat
        Save.removeFase(this.getNumFase());

        if (gameTimer != null) {
            gameTimer.cancel();
        }
        gameTimer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        gameTimer.schedule(task, 0, Consts.PERIOD);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (this instanceof Fase1 || this instanceof Fase2 || this instanceof Fase3 || this instanceof Fase4 || this instanceof Fase5 || this instanceof Menu) {
                saveGameState();
            }
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (Save.allFasesCompleted() && (this instanceof Menu)) { // From game to TelaFinal
                this.setVisible(false);
                if (gameTimer != null) gameTimer.cancel();
                this.dispose();
                Tela telafinal = new TelaFinal();
                telafinal.setVisible(true);
                telafinal.createBufferStrategy(2);
                telafinal.go();
                // Timer to exit from TelaFinal
                javax.swing.Timer exitTimer = new javax.swing.Timer(8500, (evt) -> System.exit(0));
                exitTimer.setRepeats(false);
                exitTimer.start();
                return;
            } else if (this instanceof TelaFinal) { // Exiting from TelaFinal via ESC
                System.exit(0);
                return;
            }
            // For all other cases (in a phase, or in Menu and not all phases complete)
            carregarMenu();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            File saveProgressFile = new File(Save.SAVE_FILE);
            if (saveProgressFile.exists() && saveProgressFile.delete()) {
                System.out.println("Arquivo de salvamento de progresso apagado!");
            } else if (saveProgressFile.exists()) {
                System.err.println("Falha ao apagar arquivo de salvamento de progresso!");
            }
            File gameStateFile = new File(SAVE_GAME_FILE);
            if (gameStateFile.exists() && gameStateFile.delete()) {
                System.out.println("Arquivo de estado de jogo salvo apagado!");
            } else if (gameStateFile.exists()) {
                System.err.println("Falha ao apagar arquivo de estado de jogo salvo!");
            }
            JOptionPane.showMessageDialog(this, "Progresso e jogo salvo resetados!", "Reset", JOptionPane.INFORMATION_MESSAGE);
            carregarMenu();
            return;
        }

        if (hero == null) {
            return; // No hero to control
        }

        String newImage = hero.getsNomeImagePNG();
        boolean moved = true; // Assume a key press related to movement will attempt a move

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                hero.moveUp();
                newImage = "player1_front1.png";
                break;
            case KeyEvent.VK_DOWN:
                hero.moveDown();
                newImage = "player1_front2.png";
                break;
            case KeyEvent.VK_LEFT:
                hero.moveLeft();
                newImage = "player1_left2.png";
                break;
            case KeyEvent.VK_RIGHT:
                hero.moveRight();
                newImage = "player1_right2.png";
                break;
            default:
                moved = false; // Not a movement key
                break;
        }

        if (moved) {
            try {
                hero.troca_imagem(newImage);
            } catch (IOException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao trocar imagem do herói", ex);
            }
            this.atualizaCamera();
            this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", " + (hero.getPosicao().getLinha()));
        }
        // repaint(); // Game loop handles repaints
    }

    void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RunSpace");
        // setAlwaysOnTop(true); // This can be annoying for debugging, enable if desired for gameplay
        setAutoRequestFocus(false);
        setResizable(false);

        // Using a fixed size initially based on RES, can also use pack() after components added
        setPreferredSize(new Dimension(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, Consts.RES * Consts.CELL_SIDE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, Consts.RES * Consts.CELL_SIDE, Short.MAX_VALUE)
        );
        pack(); // Recalculates size based on preferred sizes or explicit settings
        setLocationRelativeTo(null); // Center on screen
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void dispose() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
        super.dispose();
    }

    public abstract int getNumFase();
}