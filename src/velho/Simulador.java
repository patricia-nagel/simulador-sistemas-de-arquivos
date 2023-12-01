package velho;

import java.util.Scanner;

public class Simulador {

    private Inode currentDirectory;

    public Simulador() {
        // Inicializa o sistema de arquivos simulado com um diretório raiz
        currentDirectory = new Inode("/", true);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(currentDirectory.name + "> ");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                break;
            }

            executeCommand(command);
        }

        System.out.println("Saindo do simulador de terminal.");
    }

    private void executeCommand(String command) {
        String[] parts = command.split("\\s+");

        if (parts.length == 0) {
            return;
        }

        String cmd = parts[0];

        switch (cmd) {
            case "ls":
                listFiles();
                break;
            case "cd":
                changeDirectory(parts);
                break;
            case "mkdir":
                createDirectory(parts);
                break;
            case "touch":
                createFile(parts);
                break;
            case "cat":
                displayFileContents(parts);
                break;
            default:
                System.out.println("Comando desconhecido: " + cmd);
        }
    }

    private void listFiles() {
        for (String childName : currentDirectory.children.keySet()) {
            System.out.println(childName);
        }
    }

    private void changeDirectory(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Uso: cd <diretório>");
            return;
        }

        String targetDir = parts[1];
        Inode child = currentDirectory.children.get(targetDir);

        if (child != null && child.isDirectory) {
            currentDirectory = child;
        } else {
            System.out.println("Diretório não encontrado: " + targetDir);
        }
    }

    private void createDirectory(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Uso: mkdir <nome_diretório>");
            return;
        }

        String dirName = parts[1];
        Inode newDir = new Inode(dirName, true);
        currentDirectory.children.put(dirName, newDir);
    }

    private void createFile(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Uso: touch <nome_arquivo>");
            return;
        }

        String fileName = parts[1];
        Inode newFile = new Inode(fileName, false);
        currentDirectory.children.put(fileName, newFile);
    }

    private void displayFileContents(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Uso: cat <nome_arquivo>");
            return;
        }

        String fileName = parts[1];
        Inode file = currentDirectory.children.get(fileName);

        if (file != null && !file.isDirectory) {
            System.out.println(file.content);
        } else {
            System.out.println("Arquivo não encontrado: " + fileName);
        }
    }

    public static void main(String[] args) {
        Simulador terminal = new Simulador();
        terminal.run();
    }

}
