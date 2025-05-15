package Modelo;

import Auxiliar.Desenho;

public class ZigueZague extends Personagem {
    private int passoAtual;
    private int linhaBase;    // Armazena a linha base do quadrado
    private int colunaBase;  // Armazena a coluna base do quadrado
    private boolean posicaoInicialDefinida = false;

    public ZigueZague(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bAssasino = true;
        this.bTransponivel = false;
        this.passoAtual = 0;
    }

    public void autoDesenho() {
        // Só define a base uma vez, na primeira execução
        if (!posicaoInicialDefinida) {
            this.linhaBase = this.getPosicao().getLinha();
            this.colunaBase = this.getPosicao().getColuna();
            posicaoInicialDefinida = true;
        }

        // Calcula a próxima posição relativa à base
        int proximaLinha = linhaBase;
        int proximaColuna = colunaBase;

        switch(passoAtual) {
            case 0: // Lado superior (direita)
                proximaColuna += 1;
                break;
            case 1:
                proximaColuna += 2;
                break;
            case 2: // Lado direito (baixo)
                proximaLinha += 1;
                proximaColuna += 2;
                break;
            case 3:
                proximaLinha += 2;
                proximaColuna += 2;
                break;
            case 4: // Lado inferior (esquerda)
                proximaLinha += 2;
                proximaColuna += 1;
                break;
            case 5:
                proximaLinha += 2;
                break;
            case 6: // Lado esquerdo (cima)
                proximaLinha += 1;
                break;
            case 7: // Volta ao início
                break;
        }

        this.setPosicao(proximaLinha, proximaColuna);
        passoAtual = (passoAtual + 1) % 8;

        super.autoDesenho();
    }
}