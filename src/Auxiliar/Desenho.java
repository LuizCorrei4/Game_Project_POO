// File: Desenho.java

package Auxiliar; // Pacote onde a classe Desenho está localizada

import java.awt.Graphics; // Importa a classe Graphics para manipulação de gráficos
import java.io.Serializable; // Importa Serializable para que objetos desta classe possam ser serializados
import javax.swing.ImageIcon; // Importa ImageIcon para manipulação de imagens no Swing
import Controler.Tela; // Importa a classe Tela que representa a tela do jogo
import Controler.Menu;
public class Desenho implements Serializable {

    static Tela jCenario; // Variável estática que armazenará a referência para a tela do jogo

    // Método para configurar a tela do jogo
    public static void setCenario(Tela umJCenario) {
        jCenario = umJCenario; // Define a tela do jogo, que será usada para desenhar
    }

    // Método para acessar a tela do jogo
    public static Tela acessoATelaDoJogo() {
        return jCenario; // Retorna a tela do jogo
    }

    // Método para obter o objeto Graphics da tela, utilizado para desenhar na tela
    public static Graphics getGraphicsDaTela() {
        return jCenario.getGraphicsBuffer(); // Obtém o buffer de gráficos da tela
    }

    // Método para desenhar uma imagem na tela, considerando a posição do personagem
    public static void desenhar(ImageIcon iImage, int iColuna, int iLinha) {
        // Calcula a posição na tela a partir das coordenadas da coluna e linha
        int telaX = (iColuna - jCenario.getCameraColuna()) * Consts.CELL_SIDE;
        int telaY = (iLinha - jCenario.getCameraLinha()) * Consts.CELL_SIDE;

        // Verifica se a posição calculada está dentro da área visível da tela
        if (telaX >= 0 && telaX < Consts.RES * Consts.CELL_SIDE
                && telaY >= 0 && telaY < Consts.RES * Consts.CELL_SIDE) {
            // Desenha a imagem na tela, considerando o deslocamento da câmera
            iImage.paintIcon(jCenario, getGraphicsDaTela(), telaX, telaY);
        }
    }
}
