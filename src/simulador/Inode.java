package simulador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inode {

    private static final int TAMANHO_MAXIMO_INODE = 5632;

    private static final int TAMANHO_MAXIMO_BLOCO = 512;

    private String usuarioDono;

    private Inode inodePai;
    // Necessário para possibilitar a navegação

    private LocalDateTime dataCriacao;

    private LocalDateTime dataUltimoAcesso;

    private LocalDateTime dataUltimaAtualizacao;

    private boolean isDiretorio;

    private long tamanhoInode;

    private long tamanhoConteudo;

    private String conteudo;

    private Map<String, Inode> blocosDiretos;
    // String(nome do diretório) é a chave e o Inode é o valor

    private List<Inode> blocosIndiretos;
    // Blocos indiretos, pois aqui é necessário abrir o inode e então acessar o bloco do inode

    public Inode(String usuarioDono, boolean isDiretorio, Inode inodePai) {
        this.usuarioDono = usuarioDono;
        this.dataCriacao = LocalDateTime.now();
        this.dataUltimoAcesso = this.dataCriacao;
        this.dataUltimaAtualizacao = this.dataCriacao;
        this.isDiretorio = isDiretorio;
        this.tamanhoInode = 0;
        this.tamanhoConteudo = 0;
        this.inodePai = inodePai;
        this.conteudo = "";
        this.blocosDiretos = new HashMap<>(); // tamanho máximo 10
        this.blocosIndiretos = new ArrayList<>(); // lista com no maximo 512 ponteiros (1byte)
    }

    public String getUsuarioDono() {
        return usuarioDono;
    }

    public void setUsuarioDono(String usuarioDono) {
        this.usuarioDono = usuarioDono;
    }

    public Inode getInodePai() {
        return inodePai;
    }

    public void setInodePai(Inode inodePai) {
        this.inodePai = inodePai;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public boolean isDiretorio() {
        return isDiretorio;
    }

    public void setDiretorio(boolean diretorio) {
        isDiretorio = diretorio;
    }

    public long getTamanhoInode() {
        return tamanhoInode;
    }

    public void setTamanhoInode(long tamanhoInode) {
        this.tamanhoInode = tamanhoInode;
    }

    public long getTamanhoConteudo() {
        return tamanhoConteudo;
    }

    public void setTamanhoConteudo(long tamanhoConteudo) {
        this.tamanhoConteudo = tamanhoConteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Map<String, Inode> getBlocosDiretos() {
        return blocosDiretos;
    }

    public void setBlocosDiretos(Map<String, Inode> blocosDiretos) {
        this.blocosDiretos = blocosDiretos;
    }

    public List<Inode> getBlocosIndiretos() {
        return blocosIndiretos;
    }

    public void setBlocosIndiretos(List<Inode> blocosIndiretos) {
        this.blocosIndiretos = blocosIndiretos;
    }
}