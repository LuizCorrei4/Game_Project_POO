package Auxiliar;

import Modelo.Personagem;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PersonagemSaver {
    public static void salvarPersonagem(Personagem personagem, String nomeArquivo) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(nomeArquivo))) {
            zos.putNextEntry(new ZipEntry("personagem.ser"));
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(personagem);
            oos.flush();
            zos.closeEntry();
        } catch (Exception ex) {
            System.err.println("Erro ao salvar personagem: " + ex.getMessage());
        }
    }
}