// File: Auxiliar/GameState.java (or create a new package like SaveLoad)
package Auxiliar; // Or your preferred package

import Modelo.Personagem;
import Modelo.Hero;
import Modelo.Chave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameState implements Serializable {
    private static final long serialVersionUID = 20250527L; // Version UID for serialization

    public String currentPhaseClassName;
    public ArrayList<Personagem> faseAtual;
    public int cameraLinha;
    public int cameraColuna;
    public String heroImageName;
    public Posicao heroPosicao;
    public String chaveImageName; // Current image name of the key
    public Posicao chavePosicao;   // Position of the key
    public Posicao spawn;          // Spawn position for the current level
    public List<Integer> fasesCompletadas;

    // Add any other essential variables, e.g., score, lives, specific enemy states if complex

    public GameState(String currentPhaseClassName, ArrayList<Personagem> faseAtual,
                     int cameraLinha, int cameraColuna, Hero hero, Chave chave, Posicao spawn, String nomeArquivoFasesCompletadas) {
        this.currentPhaseClassName = currentPhaseClassName;
        this.faseAtual = faseAtual; // The Personagem objects themselves should be serializable
        this.cameraLinha = cameraLinha;
        this.cameraColuna = cameraColuna;

        if (hero != null) {
            this.heroImageName = hero.getsNomeImagePNG();
            this.heroPosicao = new Posicao(hero.getPosicao().getLinha(), hero.getPosicao().getColuna());
        }

        if (chave != null) {
            this.chaveImageName = chave.getsNomeImagePNG();
            this.chavePosicao = new Posicao(chave.getPosicao().getLinha(), chave.getPosicao().getColuna());
        }
        this.spawn = new Posicao(spawn.getLinha(),spawn.getColuna());

        // Inicializa a lista de fases completadas
        this.fasesCompletadas = new ArrayList<>();
        // Lê o arquivo para popular a lista de fases completadas
        try {
            File arquivo = new File(nomeArquivoFasesCompletadas);
            if (arquivo.exists()) {
                Scanner scanner = new Scanner(arquivo);
                while (scanner.hasNextInt()) {
                    this.fasesCompletadas.add(scanner.nextInt());
                }
                scanner.close();
            } else {
                System.err.println("Arquivo de fases completadas não encontrado: " + nomeArquivoFasesCompletadas);
            }
        } catch (IOException e) { // Trata FileNotFoundException e outras IOExceptions
            System.err.println("Erro ao ler o arquivo de fases completadas (" + nomeArquivoFasesCompletadas + "): " + e.getMessage());
        }
    }
}
