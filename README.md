# simulador-sistemas-de-arquivos




Segunda versão:
classe: SistemaArquivo
deve ter um atributo  Map<Integer, Inode> inodes;
Integer é identificador do inode e o Inode é o inode
manteria todos os inodes

classe: Inode:
teria que ter uma lista de List<BlocoDados> blocosDeDados;
perderia o children
mudaria o bloco indireto -> ele seria assim:
 class bloco indireto {
   list <blocos> tamanho 512
}
inode pai mudaria para ID



o meu bloco de dados
conteudo
tamanho
private Map<String, Integer> directoryEntries;
            nome da pasta, identificação no inode  
todo inode que eu criar tenho que dar um id e colocar dentro
dessa lista de bloco do pai e tb na lista inodes do sistema de arquivos






