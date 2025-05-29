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
        if (umaFase.isEmpty()) return;

        Hero hero = null;
        // It's safer to find the hero instance if the list order isn't strictly guaranteed
        // or if the hero could potentially be removed and re-added.
        for (Personagem p : umaFase) {
            if (p instanceof Hero) {
                hero = (Hero) p;
                break;
            }
        }
        if (hero == null) return; // No hero to process against

        java.util.Iterator<Personagem> iterator = umaFase.iterator();
        while (iterator.hasNext()) {
            Personagem pIesimoPersonagem = iterator.next();

            if (pIesimoPersonagem == hero) { // Skip hero interacting with self
                continue;
            }

            // Check for collision with hero
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem.isbTransponivel()) {
                    if (pIesimoPersonagem.isbMortal()) { // E.g., Moeda
                        if (pIesimoPersonagem instanceof Moeda) {
                            ((Moeda) pIesimoPersonagem).pegouMoeda();
                            // The coin is removed from 'umaFase' (which is 'faseAtual') here.
                            // The removal from the separate 'moedas' list is handled in FaseX.paint().
                        }
                        iterator.remove(); // Safe removal from 'umaFase'
                    }
                    // If it's transponivel and NOT mortal (e.g. a power-up to collect but not remove)
                    // specific logic would go here.
                } else { // It's not transponivel, hero is on it. This implies a collision with a solid object.
                    // Hero's movement validation (ehPosicaoValida) should prevent this state for non-transponivel objects.
                    // If it's also an assassin (e.g. a solid spike trap)
                    if (pIesimoPersonagem.isbAssasino()) {
                        hero.setPosicao(spawn.getLinha(), spawn.getColuna());
                        // Consider if game should 'break' or continue processing other collisions this frame
                    }
                }
            }
            // This handles cases where hero is on the same spot as an assassin character,
            // irrespective of transponibility (e.g., Fogo is transponivel but assassin).
            // This might be slightly redundant if the above block catches all assassin cases,
            // but ensures assassins are processed.
            if (pIesimoPersonagem.isbAssasino() && hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                // Check if pIesimoPersonagem was already removed by the iterator in a previous condition.
                // This is tricky. If it was removed, hero shouldn't interact with it.
                // To be fully safe, after hero position reset, you might want to re-evaluate or break.
                // For simplicity, current iterator pattern means pIesimoPersonagem is valid for this check
                // if not removed above.
                hero.setPosicao(spawn.getLinha(), spawn.getColuna());
                // Original code had 'break;' here. If hero dying should stop all further interactions
                // in this tick, then 'break;' might be appropriate for the outer loop,
                // or a flag should be set.
            }
        }
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
