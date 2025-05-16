// File: Main.java
import Controler.Menu;
import Controler.Tela;
import javax.swing.JOptionPane;

public class Main {
    // Na Main, iniciamos o loop do jogo
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run() {
                    Tela tTela = new Menu();
                    mostrarInstrucoes();
                    // cria a janela do jogo
                    tTela.setVisible(true); // exibe a janela

                    tTela.createBufferStrategy(2); // criamos 2 buffers

                    tTela.go();

                }
            }
        );
    }
    private static void mostrarInstrucoes() {
        String mensagem = "Bem-vindo ao RunScape!\n\n"
                + "INSTRUÇÕES:\n"
                + "1. Use as setas para mover o Hero\n"
                + "2. Entre nos buracos negros numerados para acessar as fases\n"
                + "3. Cada fase tem os objetivos:\n"
                + "   - Colete todas as moedas para desbloquear a chave \n"
                + "   - Evite os inimigos\n"
                + "   - E vá até a chave!\n\n"
                + "4. Aperte R para resetar o salvamento de progresso\n"
                + "5. Aperte ESC para sair do jogo\n\n"
                + "Boa sorte, astronauta!";

        JOptionPane.showMessageDialog(null,
                mensagem,
                "Instruções do Jogo",
                JOptionPane.INFORMATION_MESSAGE);

    }
}
