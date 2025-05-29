// File: Modelo/Chave.java
package Modelo;

import javax.swing.ImageIcon; // Not strictly needed here if relying on Personagem's loading

public class Chave extends Personagem{
    // protected boolean isTranslucent; // This state is managed by changing the image file name

    public Chave(String sNomeImagePNG) {
        super(sNomeImagePNG); // sNomeImagePNG stored by Personagem
        this.bTransponivel = true;
        this.bMortal = false;
        // this.isTranslucent = sNomeImagePNG.contains("_translucent"); // Infer from name if needed
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }

    public void setImage(String imageName){
        this.sNomeImagePNG = imageName; // Update stored name
        this.iImage = this.carregarImagemRedimensionada(imageName);
    }
}