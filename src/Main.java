// File: Main.java
import Controler.Tela;

public class Main {

    public static void main(String[] args) {
        // Agendamos a criação da janela no thread de eventos do Swing para evitar problemas de concorrência
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Cria instância da tela principal do jogo
                Tela tTela = new Tela();
                // Torna a janela visível
                tTela.setVisible(true);
                // Configura double buffering para renderização sem flicker (dois buffers)
                tTela.createBufferStrategy(2);
                // Inicia o loop principal de atualização e desenho
                tTela.go();
            }
        });
    }
}

