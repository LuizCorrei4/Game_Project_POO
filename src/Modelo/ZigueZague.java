package Modelo;



public class ZigueZague extends Personagem {
    private int passoAtual;
    private int linhaBase;
    private int colunaBase;
    private boolean posicaoInicialDefinida = false;

    private int iContador = 0; // contador para controlar a velocidade

    public ZigueZague(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bAssasino = true;
        this.bTransponivel = false;
        this.passoAtual = 0;
    }

    public void autoDesenho() {
        if (!posicaoInicialDefinida) {
            this.linhaBase = this.getPosicao().getLinha();
            this.colunaBase = this.getPosicao().getColuna();
            posicaoInicialDefinida = true;
        }

        if (iContador == 4) { // controla a velocidade (4 ciclos por movimento)
            iContador = 0;

            int proximaLinha = linhaBase;
            int proximaColuna = colunaBase;

            switch(passoAtual) {
                case 0: proximaColuna += 1; break;
                case 1: proximaColuna += 2; break;
                case 2: proximaLinha += 1; proximaColuna += 2; break;
                case 3: proximaLinha += 2; proximaColuna += 2; break;
                case 4: proximaLinha += 2; proximaColuna += 1; break;
                case 5: proximaLinha += 2; break;
                case 6: proximaLinha += 1; break;
                case 7: break; // volta ao início
            }

            this.setPosicao(proximaLinha, proximaColuna);
            passoAtual = (passoAtual + 1) % 8;
        }

        super.autoDesenho();
        iContador++;
    }
}
