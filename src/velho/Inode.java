package velho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inode {

    //ideia: fazer herança aqui, inode seria o abstrato
    // diretorio seria uma imp
       // n tem content
    // arquivo seria outra imp
       // nao tem children e indirect block
       // mas tem uma lista de blocos
         // novo objeto content que tem atributo de conteudo e tamanho
         // tamanho maximo 512

    String owner;

    Inode inodePai; //guarda qual inode que abriu esse inode para dar o caminho do arquivo/diretorio

    long creationTime;

    long lastAccessTime;

    long lastUpdateTime;

    boolean isDirectory;

    long size; //maxx é 5632 bytes vamos ver se precisa

    long sizeContent; //maxx é 512

    String content;

    Map<String, Inode> children; //nome do arquivo/diretorio é o String
    //isso é o meu data, que deve ter no máximo 10

    List<Inode> indirectBlock;
    //acessa indireto por que eu preciso acessar o children desse inode pra ter o conteudo

    public Inode(String owner, boolean isDirectory, Inode inodePai) {
        this.owner = owner;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = this.creationTime;
        this.lastUpdateTime = this.creationTime;
        this.isDirectory = isDirectory;
        this.size = 0;
        this.sizeContent = 0;
        this.inodePai = inodePai;
        this.content = "";
        this.children = new HashMap<>(); //maximo 10
        this.indirectBlock = new ArrayList<>(); //1 lista com no maximo 512 inodes, pq ele guarda ponteiro e cada ponteiro tem 1 byte
    }

}
