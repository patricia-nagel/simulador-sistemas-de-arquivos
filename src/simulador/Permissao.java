package simulador;

public enum Permissao {

    ESCRITA("w"),

    LEITURA("r"),

    EXECUCAO("x");

    String sigla;

    Permissao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}