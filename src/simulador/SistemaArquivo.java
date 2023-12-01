package simulador;

import java.time.LocalDateTime;
import java.util.*;

public class SistemaArquivo {

    private static final int TAMANHO_MAXIMO_SISTEMA = 65536;

    private Inode raiz; //pasta "/"

    List<Usuario> usuarios;

    Usuario usuariosAtual;

    Inode pastaAtual;

    Integer espacoLivre;

    Integer tamanhoDisco;

    //grava arquivo é aumentar o tamanho dos datas....

    public SistemaArquivo() {
        this.raiz = new Inode("root", true, null);
        //Diretório "/" fica sem nome pois o inode não guarda o prório nome, guarda só o nome dos seus filhos
        this.usuarios = new ArrayList<>();
        this.pastaAtual = this.raiz;
        this.usuariosAtual = null;
    }

    public void addUser(String nome, String password, boolean isSuperUsuario, List<Permissao> permissoes){
        Usuario usuario = new Usuario(nome, password, isSuperUsuario, permissoes);
        this.usuarios.add(usuario);
        System.out.println("Usuário " + usuario.getUsername() + " adicionado.");
    }

    public void addUser(String[] partes) {
        if (partes.length > 4 && usuariosAtual.isSuperUsuario()) {
            List<Permissao> permissoes = filtraPermissoes(partes);
            addUser(partes[1], partes[2], "true".equals(partes[3]), permissoes);
        } else if (!usuariosAtual.isSuperUsuario()) {
            System.out.println("Apenas super usuários podem executar esse comando");
        } else {
            System.out.println("Uso do comando: adduser <username> <password> <isSuperUsuario> <permissao w> <permissao r> <permissao x>");
        }
    }

    private List<Permissao> filtraPermissoes(String[] partes) {
        return Arrays.stream(partes)
                .filter(parte -> Arrays.stream(Permissao.values())
                        .anyMatch(perm -> perm.getSigla().equals(parte)))
                .map(parte -> Arrays.stream(Permissao.values())
                        .filter(perm -> perm.getSigla().equals(parte))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public void lsUser(String[] partes) {
        if (partes.length != 1) {
            System.out.println("Uso do comando: lsuser");
            return;
        }

        for (Usuario usuario : this.usuarios) {
            System.out.println(usuario.getUsername());
        }
    }

    public void removeUser(String[] partes) {
        if (partes.length == 2) {
            if ("root".equals(partes[1])){
                System.out.println("Não é possível excluir o usuário root");
                return;
            } else if (usuariosAtual.getUsername().equals(partes[1])) {
                System.out.println("Não é possível excluir seu próprio usuário");
                return;
            }

            this.usuarios.removeIf(user -> user.getUsername().equals(partes[1]));

            System.out.println("Usuário " + partes[1] + " removido.");
        } else {
            System.out.println("Uso do comando: rmuser <username>");
        }
    }

    public void loginUser(String[] partes) {
        if (partes.length == 3) {
            boolean authenticated = false;
            for (Usuario user : usuarios) {
                if (user.getUsername().equals(partes[1]) && user.getPassword().equals(partes[2])) {
                    this.usuariosAtual = user;
                    authenticated = true;
                }
            }

            if (authenticated) {
                System.out.println("Login usuário: " + partes[1] + " bem sucedido.");
            } else {
                System.out.println("Usuário ou senha inválida.");
            }

        } else {
            System.out.println("Uso do comando: login <username> <password>");
        }
    }

    public void format(String[] partes) {

        if (partes.length != 2) {
            System.out.println("Uso do comando: format <tamanho disco>");
            return;
        } else if (Integer.parseInt(partes[1]) > TAMANHO_MAXIMO_SISTEMA) {
            System.out.println("Tamanho máximo do sistema de arquivos é 65536 bytes");
        }

        this.raiz = new Inode("root", true, null);
        this.pastaAtual = this.raiz;
        this.tamanhoDisco = Integer.parseInt(partes[1]);
        System.out.println("Sistema de arquivos formatado com tamanho " + partes[1] + " bytes.");
    }

    public void ls(String[] partes) {
        if (partes.length != 1) {
            System.out.println("Uso do comando: ls");
            return;
        }

        for (Map.Entry<String, Inode> pair : pastaAtual.getBlocosDiretos().entrySet()) {
            System.out.printf("%s    Último acesso em: %s    Criado em: %s    Atualizado por último em: %s \n", pair.getKey(),
                    pair.getValue().getDataUltimoAcesso(), pair.getValue().getDataCriacao(), pair.getValue().getDataUltimaAtualizacao());
        }
    }

    public void cd(String[] partes) {

        if (partes.length != 2) {
            System.out.println("Uso do comando: cd <diretório>");
            return;
        }

        String targetDir = partes[1];

        Inode pastaDestino;

        if ("..".equals(targetDir)) {
            pastaDestino = pastaAtual.getInodePai();
        } else {
            pastaDestino = pastaAtual.getBlocosDiretos().get(targetDir);
        }

        if (pastaDestino != null && pastaDestino.isDiretorio()) {
            pastaAtual = pastaDestino;
            pastaAtual.setDataUltimoAcesso(LocalDateTime.now());
            System.out.println("Alterado para o diretório " + targetDir + ".");
            // como o nome do arquivo/pasta está sempre dentro da estrutura do pai
            // não consigo recuperar corretamente
        } else {
            System.out.println("Diretório não encontrado: " + targetDir);
        }
    }

    public void touch(String[] partes) {
        // Poderia evoluir e suportar parâmetros (-a altera hora de acesso | -m altera hora de atualização | -am altera os dois )
        if (partes.length != 2) {
            System.out.println("Uso do comando: touch <nome_arquivo>");
            return;
        }

        String fileName = partes[1];
        Inode newFile = new Inode(usuariosAtual.getUsername(), false, pastaAtual);
        pastaAtual.getBlocosDiretos().put(fileName, newFile);
        pastaAtual.setDataUltimaAtualizacao(LocalDateTime.now());
        System.out.println("Arquivo " + partes[1] + " criado.");
    }

    public void mkdir(String[] partes) {

        if (partes.length != 2) {
            System.out.println("Uso do comando: mkdir <nome_diretório>");
            return;
        }

        String dirName = partes[1];
        Inode newDir = new Inode(usuariosAtual.getUsername(), true, pastaAtual);
        pastaAtual.getBlocosDiretos().put(dirName, newDir);
        pastaAtual.setDataUltimaAtualizacao(LocalDateTime.now());
        System.out.println("Diretório " + dirName + " criado.");
    }

    public void cat(String[] partes) {
        if (partes.length != 2) {
            System.out.println("Uso do comando: cat <nome_arquivo>");
            return;
        } else if (usuarioNaoPossuiPermissao(Permissao.LEITURA)) {
            System.out.println("Usuário não possui permissão de leitura");
        }

        String nomeArquivo = partes[1];
        Inode arquivo = pastaAtual.getBlocosDiretos().get(nomeArquivo);
        if (arquivo != null && !arquivo.isDiretorio()) {
            System.out.println("Conteúdo de " + nomeArquivo + ":\n" + arquivo.getConteudo());
            return;
        }

        if (arquivo == null) {
            //Busca nos blocos indiretos
            return;
        }

        System.out.println("Arquivo não encontrado: " + nomeArquivo);
    }

    private boolean usuarioNaoPossuiPermissao(Permissao permissaoNecessaria) {
        return !this.usuariosAtual.getPermissoes().contains(permissaoNecessaria);
    }

    public void gravarConteudo(String[] partes) {

        if (partes.length != 4) {
            System.out.println("Uso do comando: gravarConteudo <nome_arquivo> <quantidade_bytes> <conteudo>");
            return;
        }

        String nomeArquivo = partes[1];
        Inode arquivo = pastaAtual.getBlocosDiretos().get(nomeArquivo);
        if (arquivo != null && !arquivo.isDiretorio()) {
            arquivo.setConteudo(partes[3]);
            arquivo.setTamanhoConteudo(Long.parseLong(partes[2]));
            System.out.println("O conteúdo foi gravado no arquivo : " + nomeArquivo);
            return;
        }

        if (arquivo == null) {
            //Busca nos blocos indiretos
            return;
        }
        System.out.println("Arquivo não encontrado: " + nomeArquivo);
    }

    public void rm(String[] partes) {

        if (partes.length != 2) {
            System.out.println("Uso do comando: rm <file_name>");
            return;
        } else if (usuarioNaoPossuiPermissao(Permissao.ESCRITA)) {
            System.out.println("Usuário não possui permissão de escrita");
        }

        String nomeArquivo = partes[1];
        Inode arquivo = pastaAtual.getBlocosDiretos().get(nomeArquivo);
        if (arquivo != null && !arquivo.isDiretorio()) {
            pastaAtual.getBlocosDiretos().remove(arquivo);
            System.out.println("Arquivo " + nomeArquivo + " excluído.");
            return;
        }

        if (arquivo == null) {
            //Busca nos blocos indiretos
            return;
        }
    }

    public void chown(String[] partes) {

        if (partes.length != 3) {
            System.out.println("Uso do comando: chown <file_name> <new_owner>");
            return;
        } else if (!usuariosAtual.isSuperUsuario()) {
            System.out.println("Apenas super usuários podem executar esse comando");
            return;
        }

        String nomeArquivo = partes[1];
        Inode arquivo = pastaAtual.getBlocosDiretos().get(nomeArquivo);
        if (arquivo != null) {
            arquivo.setUsuarioDono(partes[2]);
            System.out.println("O arquivo " + nomeArquivo + " agora pertence ao usuário: " + partes[2]);
        }
    }

    public void rmdir(String[] partes) {

        if (partes.length != 2) {
            System.out.println("Uso do comando: rmdir <diretorio>");
            return;
        } else if (usuarioNaoPossuiPermissao(Permissao.ESCRITA)) {
            System.out.println("Usuário não possui permissão de escrita");
        }

        String nomeDiretorio = partes[1];
        Inode diretorio = pastaAtual.getBlocosDiretos().get(nomeDiretorio);
        if (diretorio != null && diretorio.isDiretorio()) {
            pastaAtual.getBlocosDiretos().remove(diretorio);
            System.out.println("Diretório " + diretorio + " excluído.");
            return;
        }

        if (diretorio == null) {
            //Busca nos blocos indiretos
            return;
        }
    }

}
