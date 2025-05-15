## 🪐 Run Space

**Run Space** é um jogo em Java desenvolvido com foco em exercitar conceitos de **Programação Orientada a Objetos (POO)**. Nele, você controla um herói que percorre labirintos no espaço, coleta moedas e desvia de inimigos para completar desafios.

---

### 🎮 Objetivo do Jogo

O jogador deve percorrer diferentes fases espaciais, **coletando todas as moedas espalhadas pelo labirinto**. Somente após coletar todas as moedas é possível chegar ao final da fase, retornando em seguida ao menu principal para escolher outra fase.

---

### 🚀 Como funciona

* O jogo começa com um **menu contendo 5 fases**.
* As fases podem ser acessadas **em qualquer ordem**.
* Cada fase é um **labirinto no espaço**, com obstáculos e caminhos variados.
* Existem **moedas espalhadas** no mapa. **Todas devem ser coletadas** para completar a fase.
* Ao final de uma fase, o jogador retorna ao menu para escolher a próxima fase.

---

### ⚠️ Desafios

Durante o jogo, o herói deve evitar:

* **Aliens** que patrulham os corredores.
* **Naves que atiram projéteis**.

Se o herói **encostar em qualquer inimigo ou projétil**, ele **retorna ao início da fase**, e a coleta de moedas reinicia.

---

### 🧠 Conceitos de POO aplicados

O projeto foi desenvolvido com ênfase nos seguintes conceitos de Programação Orientada a Objetos:

* **Herança e polimorfismo** (para os diferentes tipos de personagens e inimigos).
* **Encapsulamento** (na manipulação de estados e posições dos objetos).
* **Modularização** do código em classes como `Hero`, `Moeda`, `Fase`, `Alien`, `Nave`, entre outras.

---

### 🖼️ Screenshots 



---

### 📁 Organização do Projeto

```
src/
├── Main.java
  Auxiliar/
├── Desenho
├── Posicao
├── Consts
  Controler/
├── Fase1.java ... Fase5.java
├── Menu.java
├── Tela.java
  Modelo/
├── Hero.java
├── Moeda.java
├── Personagem.java
├── Chave.java
├── NaveInimiga.java
└── ...
```

---

### ✅ Requisitos

* Java 8+
* IDE (como VS Code ou IntelliJ) ou terminal com `javac/java`

---

### 🏁 Como rodar

1. Compile os arquivos:

   ```bash
   javac *.java
   ```
2. Execute o jogo:

   ```bash
   java Main
   ```

---

### 👩‍💻 Desenvolvedores

* Ana Paula Batista (@anapbatista)
* Italo Bresciani (@ItaloCarlosMartinsBresciani)
* Luiz Correa (@LuizCorrei4)

