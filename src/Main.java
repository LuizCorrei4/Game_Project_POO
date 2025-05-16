// File: Main.java
import Controler.Menu;
import Controler.Tela;
import javax.swing.JOptionPane;

public class Main {
    // Na Main, iniciamos o loop do jogo
    public static void main(String[] args) {
        // Agendamos a criação da janela no thread de eventos do Swing para evitar problemas de concorrência
        /* O Swing fornece o metodo abaixo que recebe um Runnable (pedaço de código)
        *  O java.awt.EventQueue.invokeLater(Runnable r) coloca esse Runnable numa fila interna do Swing
        *  A thread de eventos do Swing (EDT) pega o Runnable e executa o metodo run() dentro dele quando estiver pronto.
         */
        java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run() {
                    Tela tTela = new Menu();
                    mostrarInstrucoes();
                    // cria a janela do jogo
                    tTela.setVisible(true); // exibe a janela
                    /* Configura double buffering para renderização sem flicker (dois buffers)
                    *  O BufferStrategy do Tela é uma uma classe da API AQT/Swing do Java que facilita a
                    * a implementação de double ou triple buffering, permitindo desenhar imagens de forma suave.
                    */
                    tTela.createBufferStrategy(2); // criamos 2 buffers
                    // Buffer A: mostrado na tela;
                    // Buffer B: você desenha "fora de cena"
                    // Inicia o loop principal de atualização e desenho (LOOP DO JOGO)
                    //Menu menuAux = (Menu)tTela;
                    tTela.go();
                    //menuAux.setInstrucao(true);
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
                + "Boa sorte, astronauta!";

        JOptionPane.showMessageDialog(null,
                mensagem,
                "Instruções do Jogo",
                JOptionPane.INFORMATION_MESSAGE);

    }
}
