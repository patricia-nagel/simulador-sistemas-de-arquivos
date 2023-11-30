package velho;

import java.util.List;
import java.util.ArrayList;

public class SistemaArquivo {

    List<Inode> inodes;
    List<Usuario> users;
    Usuario currentUser;
    int currentDirectory;

    public SistemaArquivo() {
        this.inodes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.currentDirectory = 0; // Assume que o diretório raiz tem o inode 0
    }

    public Usuario addUser(String username, String password) {
        Usuario newUser = new Usuario(username, password);
        this.users.add(newUser);
        return newUser;
    }

    public void removeUser(String username) {
        this.users.removeIf(user -> user.username.equals(username));
    }

    public boolean authenticateUser(String username, String password) {
        for (Usuario user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void format() {
        this.inodes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.currentDirectory = 0;
    }

    public void touch(String filename) {
        Inode inode = new Inode(currentUser.username, false);
        this.inodes.add(inode);
        updateDirectoryEntry(filename, this.inodes.size() - 1);
    }

    public String cat(String filename) {
        Integer inodeNumber = getInodeNumber(filename);
        if (inodeNumber != null && !this.inodes.get(inodeNumber).isDirectory) {
            return "Content of " + filename + ":\n" + readData(inodeNumber);
        }
        return filename + " is not a valid file.";
    }

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

    public void mkdir(String dirname) {
        Inode inode = new Inode(currentUser.username, true);
        this.inodes.add(inode);
        updateDirectoryEntry(dirname, this.inodes.size() - 1);
        System.out.println("Directory " + dirname + " created.");
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

    public String cd(String dirname) {
        Integer inodeNumber = getInodeNumber(dirname);
        if (inodeNumber != null && this.inodes.get(inodeNumber).isDirectory) {
            this.currentDirectory = inodeNumber;
            return "Changed directory to " + dirname + ".";
        }
        return dirname + " is not a valid directory.";
    }

    public String ls() {
        List<Integer> entries = readDirectory(this.currentDirectory);
        List<String> names = new ArrayList<>();
        for (Integer entry : entries) {
            names.add(entry.toString()); // Pode ser melhorado para mostrar nomes reais dos arquivos/diretórios
        }
        return "Contents of current directory: " + String.join(", ", names);
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
