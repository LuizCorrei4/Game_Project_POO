// File: ControleDeJogo
/*
    Aqui fazemos a lógica de colisões e validações
 */

package Controler; // Pacote onde a classe ControleDeJogo está localizada
import Modelo.Personagem; // Importa a classe Personagem do pacote Modelo
import Modelo.Moeda; // Importa a classe Personagem do pacote Modelo
import Modelo.Hero; // Importa a classe Hero (provavelmente uma subclasse de Personagem)
import Auxiliar.Posicao; // Importa a classe Posicao do pacote Auxiliar
import java.util.ArrayList; // Importa a classe ArrayList

public class ControleDeJogo {
    private Posicao spawn;
    public ControleDeJogo(Posicao spawn){
        this.spawn = spawn;
    }
    // Metodo responsável por desenhar todos os personagens na tela
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (Personagem personagem : e) {
            // Para cada personagem na lista, chama o metodo autoDesenho() para desenhá-lo
            personagem.autoDesenho();
        }
    }

    // Metodo para processar as interações de todos os personagens na fase
    public void processaTudo(ArrayList<Personagem> umaFase) {
        Hero hero = (Hero) umaFase.get(0); // Assume que o primeiro personagem da lista é o herói (cast para Hero)
        Personagem pIesimoPersonagem;

        // Laço para verificar as colisões entre o herói e os outros personagens
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i); // Obtém o personagem da posição i

            //  Verifica se o héroi está na mesma posição do i-ésimo personagem
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem.isbTransponivel()) { // Verifica se o personagem é transponível
                    if (pIesimoPersonagem.isbMortal()) {
                        if(pIesimoPersonagem instanceof Moeda moedaAux){
                            moedaAux.pegouMoeda();
                        }
                        // Se o personagem for mortal, remove o personagem da fase
                        umaFase.remove(pIesimoPersonagem);

                    }
                }
            }
            // Verifica se o personagem vai morrer
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao()) && pIesimoPersonagem.isbAssasino()) {
                hero.setPosicao(spawn.getLinha(), spawn.getColuna());
                break;
            }
        }

        // A segunda parte do loop parece estar sem funcionalidade, apenas percorre a lista sem fazer nada
        // Esse trecho pode ser removido ou completado, caso tenha uma ação a ser realizada aqui.

    }

    /* Retorna true se a posição p é válida para o Hero em relação a todos os personagens no array */
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        Personagem pIesimoPersonagem;

        // Laço para verificar se a posição desejada é válida
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i); // Obtém o personagem da posição i
            if (!pIesimoPersonagem.isbTransponivel()) {
                // Se o personagem não for transponível (não pode ser atravessado)
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    // Se a posição do personagem for igual à posição desejada, retorna false
                    return false;
                }
            }
        }
        return true; // Se não houver colisões, a posição é válida
    }
}
