package velho;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class SistemaArquivo {

    private Inode root; //pasta "/"
    List<Usuario> users;
    Usuario currentUser;
    Inode currentDirectory;

    //tamanho maximo 65536 bytes  é definido na formatação

    //espaço livre

    //maximo de 128 blocos de 512

    //grava arquivo é aumentar o tamanho dos datas....


    public SistemaArquivo() {
        this.root = new Inode("root", true, null); //barra fica sem nome pq o inode não guarda o seu nome, guarda só o nome dos seus filhos
        this.users = new ArrayList<>();
        this.currentDirectory = this.root;
        this.currentUser = null;
    }

    public void addUser(String nome, String password, boolean isSuperUsuario){
        Usuario newUser = new Usuario(nome, password, isSuperUsuario);
        this.users.add(newUser);
        System.out.println("User " + newUser.username + " added.");
    }

    public void addUser(String[] parts) {
        if (parts.length == 4) {
            addUser(parts[1], parts[2], "true".equals(parts[3]));
        } else {
            System.out.println("Usage: adduser <username> <password>");
        }
    }

    public void removeUser(String[] parts) {
        if (parts.length == 2) {
            if ("root".equals(parts[1])){
                //diz que não pode excluir o root
                return;
            } else if (currentUser.username.equals(parts[1])) {
                //diz que excluir ele mesmo
                return;
            }

            this.users.removeIf(user -> user.username.equals(parts[1]));
            System.out.println("User " + parts[1] + " removed.");
        } else {
            System.out.println("Usage: rmuser <username>");
        }
    }

    public void authenticateUser(String[] parts) {
        if (parts.length == 3) {
            boolean authenticated = false;
            for (Usuario user : users) {
                if (user.username.equals(parts[1]) && user.password.equals(parts[2])) {
                    this.currentUser = user;
                    authenticated = true;
                }
            }

            if (authenticated) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Invalid username or password.");
            }

        } else {
            System.out.println("Usage: login <username> <password>");
        }
    }

    public void format() {
        this.root = new Inode("root", true, null);
        this.currentDirectory = this.root;
        System.out.println("File system formatted.");
    }

    public void ls() {
        //sistema de mostrar data
        for (Map.Entry<String, Inode> pair : currentDirectory.children.entrySet()) {
            System.out.printf("%s, ultimo acesso em: %s, criacao em: %s, ultimo update: %s", pair.getKey(),
                    pair.getValue().lastAccessTime, pair.getValue().creationTime, pair.getValue().lastUpdateTime);
        }
    }

    public void cd(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Uso: cd <diretório>");
            return;
        }

        String targetDir = parts[1];

        Inode pastaDestino;

        if ("..".equals(targetDir)) {
            pastaDestino = currentDirectory.inodePai;
        } else {
            pastaDestino = currentDirectory.children.get(targetDir);
        }

        if (pastaDestino != null && pastaDestino.isDirectory) {
            currentDirectory = pastaDestino;
            currentDirectory.lastAccessTime = System.currentTimeMillis();
            System.out.println("Changed directory to " + targetDir + ".");
            // como o nome do arquivo/pasta está sempre dentro da estrutura do pai
            // não consigo recuperar corretamente
        } else {
            System.out.println("Diretório não encontrado: " + targetDir);
        }
    }

    public void touch(String[] parts) {
        //atraves de parametros (-a muda hora de acesso / -m muda hora de atualização/ -am muda os dois )
        // pode ser usado para alterar hora de update
        if (parts.length != 2) {
            System.out.println("Uso: touch <nome_arquivo>");
            return;
        }

        //voltar pra ver questão de extensaõ....

        String fileName = parts[1];
        Inode newFile = new Inode(currentUser.username, false, currentDirectory);
        currentDirectory.children.put(fileName, newFile);
        currentDirectory.lastUpdateTime = System.currentTimeMillis();
        System.out.println("File " + parts[1] + " created.");
    }

    public void mkdir(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Uso: mkdir <nome_diretório>");
            return;
        }

        String dirName = parts[1];
        Inode newDir = new Inode(currentUser.username, true, currentDirectory);
        currentDirectory.children.put(dirName, newDir);
        currentDirectory.lastUpdateTime = System.currentTimeMillis();
        System.out.println("Directory " + dirName + " created.");
    }

    public void cat(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Uso: cat <nome_arquivo>");
            return;
        }

        String fileName = parts[1];
        Inode file = currentDirectory.children.get(fileName);

        if (file != null && !file.isDirectory) {
            System.out.println("Content of " + fileName + ":\n" + file.content);
            return;
        }

        if (file == null) {
            //procura no indireto
            return; //return se econtrar
        }

        System.out.println("Arquivo não encontrado: " + fileName);
    }

    public void gravarConteudo(String[] parts) {

        //String nomeArquivo, Integer nbytes, String buffer
        if (parts.length != 4) {
            System.out.println("Uso: gravarConteudo <nome_arquivo> <quantidade_bytes> <conteudo>");
            return;
        }

        String fileName = parts[1];
        Inode file = currentDirectory.children.get(fileName);

        if (file != null && !file.isDirectory) {
            System.out.println("Content of " + fileName + ":\n" + file.content);
            return;
        }

        if (file == null) {
            //procura no indireto
            return; //return se econtrar
        }

        System.out.println("Arquivo não encontrado: " + fileName);



    }

        //bufer é o conteudo







    public String rm(String filename) {
        Integer inodeNumber = getInodeNumber(filename);
        if (inodeNumber != null && !this.inodes.get(inodeNumber).isDirectory) {
            deleteDirectoryEntry(filename);
            this.inodes.set(inodeNumber, null);
            return filename + " deleted.";
        }
        return filename + " is not a valid file.";
    }

    public String chown(String filename, String newOwner) {
        Integer inodeNumber = getInodeNumber(filename);
        if (inodeNumber != null) {
            this.inodes.get(inodeNumber).owner = newOwner;
            return "Ownership of " + filename + " changed to " + newOwner + ".";
        }
        return filename + " is not a valid file.";
    }

    public void chmod(String filename, String permissions) {
        Integer inodeNumber = getInodeNumber(filename);
        if (inodeNumber != null) {
            // Adicione a lógica para manipular as permissões
            System.out.println("Permissions of " + filename + " changed to " + permissions + ".");
        } else {
            System.out.println(filename + " is not a valid file.");
        }
    }



    public void rmdir(String dirname) {
        Integer inodeNumber = getInodeNumber(dirname);
        if (inodeNumber != null && this.inodes.get(inodeNumber).isDirectory) {
            List<Integer> entries = readDirectory(inodeNumber);
            if (entries.size() == 2) { // O diretório está vazio, exceto por "." e ".."
                deleteDirectoryEntry(dirname);
                this.inodes.set(inodeNumber, null);
                System.out.println("Directory " + dirname + " deleted.");
            } else {
                System.out.println(dirname + " is not an empty directory.");
            }
        } else {
            System.out.println(dirname + " is not a valid directory.");
        }
    }





    // Adicione métodos para chmod, mkdir, rmdir, cd, ls e outros conforme necessário

    private void updateDirectoryEntry(String name, int inodeNumber) {
        Inode currentDirectoryInode = this.inodes.get(this.currentDirectory);
        currentDirectoryInode.size += 1;
        currentDirectoryInode.blocks.add(inodeNumber);
    }

    private void deleteDirectoryEntry(String name) {
        Inode currentDirectoryInode = this.inodes.get(this.currentDirectory);
        currentDirectoryInode.size -= 1;
        currentDirectoryInode.blocks.removeIf(block -> block.equals(name));
    }

    private List<Integer> readDirectory(int inodeNumber) {
        return this.inodes.get(inodeNumber).blocks;
    }

    private String readData(int inodeNumber) {
        // Adicione lógica para ler dados do bloco de dados e blocos indiretos
        return "Data of file " + inodeNumber;
    }

    private Integer getInodeNumber(String name) {
        List<Integer> entries = readDirectory(this.currentDirectory);
        for (Integer entry : entries) {
            if (entry.equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
