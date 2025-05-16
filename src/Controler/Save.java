package Controler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Save {
    protected static final String SAVE_FILE = "save.dat";

    public static void saveProgress(int faseCompletada) {
        try {
            // Carrega o progresso atual
            List<Integer> fasesCompletas = loadAllCompletedFases();

            // Adiciona a nova fase se não existir
            if (!fasesCompletas.contains(faseCompletada)) {
                fasesCompletas.add(faseCompletada);
            }

            // Salva todas as fases completas
            FileWriter writer = new FileWriter(SAVE_FILE);
            for (int fase : fasesCompletas) {
                writer.write(fase + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar progresso: " + e.getMessage());
        }
    }

    public static List<Integer> loadAllCompletedFases() {
        List<Integer> fasesCompletas = new ArrayList<>();
        try {
            File file = new File(SAVE_FILE);
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextInt()) {
                    fasesCompletas.add(scanner.nextInt());
                }
                scanner.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar progresso: " + e.getMessage());
        }
        return fasesCompletas;
    }

    public static boolean isFaseCompleted(int fase) {
        return loadAllCompletedFases().contains(fase);
    }

    public static boolean allFasesCompleted() {
        return loadAllCompletedFases().size() >= 5;
    }
}