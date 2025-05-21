
import java.util.Scanner;

class JogoQuadrados {
    private boolean[][] horizontalLines;
    private boolean[][] verticalLines;
    private int[] pontuacoes = new int[2]; // 0 = jogador 1, 1 = jogador 2
    private int jogadorAtual = 0;
    private int boardSize;
    private String[] nomesJogadores = new String[2];
    private char[] iniciaisJogadores = new char[2];
    private char[][] donoDoQuadrado;

    public JogoQuadrados(int size) {
        this.boardSize = size;
        this.horizontalLines = new boolean[size][size - 1];
        this.verticalLines = new boolean[size - 1][size];
        this.donoDoQuadrado = new char[size - 1][size - 1];
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                donoDoQuadrado[i][j] = ' ';
            }
        }
    }

    public void setNomes(String nome1, String nome2) {
        nomesJogadores[0] = nome1;
        nomesJogadores[1] = nome2;
        iniciaisJogadores[0] = Character.toUpperCase(nome1.charAt(0));
        iniciaisJogadores[1] = Character.toUpperCase(nome2.charAt(0));
    }

    public boolean fazerJogada(int linha1, int coluna1, int linha2, int coluna2) {
        if (!saoAdjacentes(linha1, coluna1, linha2, coluna2) || !estaDentroDoTabuleiro(linha1, coluna1) || !estaDentroDoTabuleiro(linha2, coluna2) || linhaJaDesenhada(linha1, coluna1, linha2, coluna2)) {
            System.out.println("Jogada inválida. Tente novamente.");
            return false;
        }

        int quadradosFechadosNestaJogada = 0;

        if (linha1 == linha2) {
            int coluna = Math.min(coluna1, coluna2);
            horizontalLines[linha1][coluna] = true;

            if (linha1 > 0 && verticalLines[linha1 - 1][coluna] && verticalLines[linha1 - 1][coluna + 1] && horizontalLines[linha1 - 1][coluna]) {
                donoDoQuadrado[linha1 - 1][coluna] = iniciaisJogadores[jogadorAtual];
                quadradosFechadosNestaJogada++;
            }
            if (linha1 < boardSize - 1 && verticalLines[linha1][coluna] && verticalLines[linha1][coluna + 1] && horizontalLines[linha1 + 1][coluna]) {
                donoDoQuadrado[linha1][coluna] = iniciaisJogadores[jogadorAtual];
                quadradosFechadosNestaJogada++;
            }
        } else {
            int linha = Math.min(linha1, linha2);
            verticalLines[linha][coluna1] = true;

            if (coluna1 > 0 && horizontalLines[linha][coluna1 - 1] && horizontalLines[linha + 1][coluna1 - 1] && verticalLines[linha][coluna1 - 1]) {
                donoDoQuadrado[linha][coluna1 - 1] = iniciaisJogadores[jogadorAtual];
                quadradosFechadosNestaJogada++;
            }
            if (coluna1 < boardSize - 1 && horizontalLines[linha][coluna1] && horizontalLines[linha + 1][coluna1] && verticalLines[linha][coluna1 + 1]) {
                donoDoQuadrado[linha][coluna1] = iniciaisJogadores[jogadorAtual];
                quadradosFechadosNestaJogada++;
            }
        }

        pontuacoes[jogadorAtual] += quadradosFechadosNestaJogada;
        return quadradosFechadosNestaJogada > 0;
    }

    private boolean saoAdjacentes(int r1, int c1, int r2, int c2) {
        return (r1 == r2 && Math.abs(c1 - c2) == 1) || (c1 == c2 && Math.abs(r1 - r2) == 1);
    }

    private boolean estaDentroDoTabuleiro(int r, int c) {
        return r >= 0 && r < boardSize && c >= 0 && c < boardSize;
    }

    private boolean linhaJaDesenhada(int r1, int c1, int r2, int c2) {
        if (r1 == r2) {
            int coluna = Math.min(c1, c2);
            return horizontalLines[r1][coluna];
        } else {
            int linha = Math.min(r1, r2);
            return verticalLines[linha][c1];
        }
    }

    public boolean jogoTerminado() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize - 1; j++) {
                if (!horizontalLines[i][j]) return false;
            }
        }
        for (int i = 0; i < boardSize - 1; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!verticalLines[i][j]) return false;
            }
        }
        return true;
    }

    public static void exibirTabuleiro(JogoQuadrados jogo) {
        int size = jogo.boardSize;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(".");
                if (j < size - 1) {
                    System.out.print(jogo.horizontalLines[i][j] ? " -- " : "    ");
                }
            }
            System.out.println();
            if (i < size - 1) {
                for (int j = 0; j < size; j++) {
                    System.out.print(jogo.verticalLines[i][j] ? "|   " : "    ");
                }
                System.out.println();
                for (int j = 0; j < size - 1; j++) {
                    System.out.print(" " + jogo.donoDoQuadrado[i][j] + "  ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o tamanho do tabuleiro (número de pontos por lado): ");
        int size = scanner.nextInt();
        scanner.nextLine(); // consumir quebra de linha

        JogoQuadrados jogo = new JogoQuadrados(size);

        System.out.print("Nome do Jogador 1: ");
        String nome1 = scanner.nextLine();
        System.out.print("Nome do Jogador 2: ");
        String nome2 = scanner.nextLine();
        jogo.setNomes(nome1, nome2);

        while (!jogo.jogoTerminado()) {
            System.out.println("\nTabuleiro:");
            exibirTabuleiro(jogo);

            System.out.println("Vez de " + jogo.nomesJogadores[jogo.jogadorAtual] + " (" + jogo.iniciaisJogadores[jogo.jogadorAtual] + ")");
            System.out.print("Digite as coordenadas do primeiro ponto (linha coluna): ");
            int r1 = scanner.nextInt() - 1;
            int c1 = scanner.nextInt() - 1;
            System.out.print("Digite as coordenadas do segundo ponto (linha coluna): ");
            int r2 = scanner.nextInt() - 1;
            int c2 = scanner.nextInt() - 1;

            if (!jogo.fazerJogada(r1, c1, r2, c2)) {
                jogo.jogadorAtual = 1 - jogo.jogadorAtual;
            } else {
                System.out.println("Quadrado(s) fechado(s)! " + jogo.nomesJogadores[jogo.jogadorAtual] + " joga novamente.");
            }

            System.out.println("Pontuação: " + jogo.nomesJogadores[0] + " = " + jogo.pontuacoes[0] + ", " + jogo.nomesJogadores[1] + " = " + jogo.pontuacoes[1]);
        }

        System.out.println("\nJogo Terminado!");
        if (jogo.pontuacoes[0] > jogo.pontuacoes[1]) {
            System.out.println("Vitória de " + jogo.nomesJogadores[0] + "!");
        } else if (jogo.pontuacoes[1] > jogo.pontuacoes[0]) {
            System.out.println("Vitória de " + jogo.nomesJogadores[1] + "!");
        } else {
            System.out.println("Empate!");
        }

        scanner.close();
    }
}
