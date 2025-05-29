// File: Main.java
import Controler.Menu;
import Controler.Tela;
import Controler.Fase1;
import Controler.Fase2;
import Controler.Fase3;
import Controler.Fase4;
import Controler.Fase5;
import Auxiliar.GameState;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                File saveFile = new File(Tela.SAVE_GAME_FILE);
                Tela tTela = null;
                GameState loadedState = null;

                if (saveFile.exists()) {
                    int response = JOptionPane.showConfirmDialog(null,
                            "Um jogo salvo foi encontrado. Deseja carregá-lo?",
                            "Carregar Jogo Salvo", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
                            loadedState = (GameState) ois.readObject();
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Erro ao carregar o jogo salvo.", ex);
                            JOptionPane.showMessageDialog(null, "Erro ao carregar o jogo salvo. Iniciando novo jogo.", "Erro de Carregamento", JOptionPane.ERROR_MESSAGE);

                        }
                    }
                }

                if (loadedState != null) {
                    try {

                        Class<?> phaseClass = Class.forName(loadedState.currentPhaseClassName);
                        tTela = (Tela) phaseClass.getDeclaredConstructor().newInstance();

                        tTela.restoreGameState(loadedState);

                    } catch (Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Erro fatal ao restaurar a fase.", ex);
                        JOptionPane.showMessageDialog(null, "Erro fatal ao restaurar a fase. Iniciando novo jogo.", "Erro de Restauração", JOptionPane.ERROR_MESSAGE);
                        tTela = new Menu();
                        mostrarInstrucoes();
                    }
                } else {
                    tTela = new Menu();
                    mostrarInstrucoes();
                }

                tTela.setVisible(true);
                tTela.createBufferStrategy(2);
                tTela.go();
            }
        });
    }

    private static void mostrarInstrucoes() {
        String mensagem = "Bem-vindo ao RunScape!\n\n"
                + "INSTRUÇÕES:\n\n"
                + "1. Use as setas para mover o Hero\n"
                + "2. Entre nos buracos negros numerados para acessar as fases\n"
                + "3. Cada fase tem os objetivos:\n"
                + "   - Colete todas as moedas para desbloquear a chave \n"
                + "   - Evite os inimigos para não voltar ao início da fase\n"
                + "   - E pegue a chave para concluir a fase!\n\n"
                + "4. Aperte R para resetar o salvamento de progresso (e o jogo salvo atual)\n"
                + "5. Aperte S para SALVAR o jogo atual e sair (durante uma fase)\n"
                + "6. Aperte ESC para voltar ao Menu ou sair do jogo (se todas as fases forem completadas)\n"
                + "7. Aperte E para fechar o jogo sem salvar\n\n\n"
                + "Boa sorte, astronauta!";

        JOptionPane.showMessageDialog(null,
                mensagem,
                "Instruções do Jogo",
                JOptionPane.INFORMATION_MESSAGE);
    }
}