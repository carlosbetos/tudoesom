package br.com.tudoesom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CuttingBoard {

    private static CuttingBoard cuttingBoard = new CuttingBoard();
    private int tabs[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            39, 43, 44, 46, 47, 53, 54, 55, 58, 61, 62, 63, 68, 69, 70, 72, 73, 74, 75, 78, 80, 81, 83, 94, 96, 97, 100, 101, 103, 106, 107,
            122, 126, 149, 153, 155, 163, 171, 189, 203, 204, 215, 231, 232, 233, 236, 238, 256, 257, 272, 285, 290, 291, 295, 329, 342, 348, 376,
            377, 378, 382, 383, 385, 388, 389, 390, 391, 392};
    
    private int cards[] = new int[]{31, 32, 34, 35, 38, 40, 42, 50, 52, 57, 60, 71, 82, 86, 88, 89, 93, 95, 98, 104, 109, 110, 113, 114, 115, 129, 138, 139, 140, 142, 145,
            147, 151, 156, 158, 166, 167, 168, 170, 172, 198, 211, 303, 345, 387, 394, 396};

    private int tabsAmarelo[] = new int[]{43, 46, 54, 55, 58, 62, 68, 69, 70, 74, 75, 78, 81, 83, 103, 107, 189, 388};
    private int tabsVerde[] = new int[]{39, 47, 61, 100, 122, 126, 171, 231, 256, 257, 285, 290, 291, 342, 385, 392};
    private int tabsAzul[] = new int[]{44, 53, 63, 72, 73, 80, 94, 96, 97, 101, 106, 149, 153, 163, 215, 271, 272};
    private int tabsVermelho[] = new int[]{155, 203, 204, 232, 233, 236, 238, 295, 329, 348, 376, 377, 378, 382, 383, 389, 390, 391};
    private int tabsNumero[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private int etiqueta[] = new int[]{99, 386, 165};

    private List<Integer> tabuleiro = new ArrayList<>(); // 98
    private List<Integer> baralho = new ArrayList<>(); // 47

    private Integer numero;
    private boolean selectedModal = false; // false tabuleiro, true
    private String sigla = "pt";
    private Language language = Language.PORTUGUES;
    private int seek = 0;

    private CuttingBoard() {
    }

    public static CuttingBoard getInstance() {

        return cuttingBoard;
    }

    protected boolean temNaCartaOuTabuleiroOuEtiqueta(String valorQrcode) {

        if (valorQrcode.length() > 3) {
            return false;
        }

        if (!valorQrcode.matches("[0-9]+")) {
            return false;
        }

        int intValor = Integer.parseInt(valorQrcode);


        for (int i = 0; i < tabs.length; i++) {
            if (intValor == tabs[i]) {
                return true;
            }
        }

        for (int j = 0; j < cards.length; j++) {
            if (intValor == cards[j]) {
                return true;
            }
        }

        for (int j = 0; j < etiqueta.length; j++) {
            if (intValor == etiqueta[j]) {
                return true;
            }
        }

        return false;
    }

    public void reset() {

        tabuleiro.clear();
        baralho.clear();

        Random rand = new Random();

        List<Integer> toTabuleiro = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            toTabuleiro.add(new Integer(tabs[i]));
        }

        for (int x = 0; x < 98; x++) { // 98
            Integer choose = toTabuleiro.get(rand.nextInt(toTabuleiro.size()));
            tabuleiro.add(choose);
            toTabuleiro.remove(choose);
        }

        List<Integer> toTCard = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {

            toTCard.add(new Integer(cards[i]));
        }

        for (int x = 0; x < 47; x++) { // 47

            Integer choose = toTCard.get(rand.nextInt(toTCard.size()));
            baralho.add(choose);

            toTCard.remove(choose);
        }
    }

    public String getLanguageSigla() {

        return sigla;
    }

    public Language getLanguage() {

        return language;
    }

    public void doRandom() {

        if (isBaralho() && baralho.size() > 0) {

            if (++seek >= baralho.size()) {

                seek = 0;
            }
            numero = baralho.get(seek);

        } else if (isTabuleiro()) {

            if (++seek >= tabuleiro.size()) {

                seek = 0;
            }
            numero = tabuleiro.get(seek);
        }
    }

    public int length() {

        if (isBaralho()) {

            return baralho.size();

        } else if (isTabuleiro()) {

            return tabuleiro.size();
        }

        return 0;
    }

    public Integer getNumero() {

        if (numero == null) {

            doRandom();
        }
        return numero;
    }

    public boolean isBaralho() {

        return selectedModal;
    }

    public boolean isTabuleiro() {

        return !selectedModal;
    }

    public void selectBaralho() {

        selectedModal = true;
    }

    public void selectTabuleiro() {

        selectedModal = false;
    }

    public void selectPortuguesLanguage() {

        sigla = "pt";
        language = Language.PORTUGUES;
    }

    public void selectEnglishLanguage() {

        sigla = "en";
        language = Language.ENGLISH;
    }

    public boolean removeNumero() {

        if (isBaralho()) {

            baralho.remove(numero);

            if (baralho.size() == 0) {

                return false;
            }
            if (++seek >= baralho.size()) {
                seek = 0;
            }
            numero = baralho.get(seek);

        } else if (isTabuleiro()) {

            if (++seek >= tabuleiro.size()) {
                seek = 0;
            }
            numero = tabuleiro.get(seek);
        }

        return true;
    }

    public void selectSpanishLanguage() {

        sigla = "es";
        language = Language.SPANISH;
    }


    public void selectFranchLanguage() {

        sigla = "fr";
        language = Language.FRENCH;
    }


    public boolean comsorteouazar(int valor) {

        Random randSorte = new Random();

        int listadasorte[] = new int[]{tabs[randSorte.nextInt(tabs.length)],
                tabs[randSorte.nextInt(tabs.length)]};

        for (int i = 0; i < 2; i++) {

            if (valor == listadasorte[i]) {
                return true;
            }

        }

        return false;
    }


    enum Language {SPANISH, ENGLISH, PORTUGUES, FRENCH}
}
