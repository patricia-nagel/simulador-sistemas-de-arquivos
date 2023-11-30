package velho;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        SistemaArquivo fileSystem = new SistemaArquivo();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao FileSystem Simulator!");

        Usuario usuarioRoot = fileSystem.addUser("root", "root");
        fileSystem.mkdir("/");

        while (true) {
            System.out.print(fileSystem.currentUser != null
                    ? fileSystem.currentUser.username + "@" + fileSystem.currentDirectory + "$ "
                    : "guest@0$ ");

            String command = scanner.nextLine();

            String[] parts = command.split(" ");
            String cmd = parts[0];

            switch (cmd) {
                case "adduser":
                    if (parts.length == 3) {
                        Usuario newUser = fileSystem.addUser(parts[1], parts[2]);
                        System.out.println("User " + newUser.username + " added.");
                    } else {
                        System.out.println("Usage: adduser <username> <password>");
                    }
                    break;

                case "rmuser":
                    if (parts.length == 2) {
                        fileSystem.removeUser(parts[1]);
                        System.out.println("User " + parts[1] + " removed.");
                    } else {
                        System.out.println("Usage: rmuser <username>");
                    }
                    break;

                case "login":
                    if (parts.length == 3) {
                        boolean authenticated = fileSystem.authenticateUser(parts[1], parts[2]);
                        if (authenticated) {
                            System.out.println("Login successful.");
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } else {
                        System.out.println("Usage: login <username> <password>");
                    }
                    break;

                case "format":
                    fileSystem.format();
                    System.out.println("File system formatted.");
                    break;

                // Adicione outros casos para os comandos como mkdir, touch, ls, cat, rm, cd, etc.

                case "exit":
                    System.out.println("Exiting FileSystem Simulator. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                case "mkdir":
                    if (parts.length == 2) {
                        fileSystem.mkdir(parts[1]);
                        System.out.println("Directory " + parts[1] + " created.");
                    } else {
                        System.out.println("Usage: mkdir <directory_name>");
                    }
                    break;

                case "touch":
                    if (parts.length == 2) {
                        fileSystem.touch(parts[1]);
                        System.out.println("File " + parts[1] + " created.");
                    } else {
                        System.out.println("Usage: touch <file_name>");
                    }
                    break;

                case "ls":
                    String content = fileSystem.ls();
                    System.out.println(content);
                    break;

                case "cat":
                    if (parts.length == 2) {
                        String result = fileSystem.cat(parts[1]);
                        System.out.println(result);
                    } else {
                        System.out.println("Usage: cat <file_name>");
                    }
                    break;

                case "rm":
                    if (parts.length == 2) {
                        String result = fileSystem.rm(parts[1]);
                        System.out.println(result);
                    } else {
                        System.out.println("Usage: rm <file_name>");
                    }
                    break;

                case "cd":
                    if (parts.length == 2) {
                        String result = fileSystem.cd(parts[1]);
                        System.out.println(result);
                    } else {
                        System.out.println("Usage: cd <directory_name>");
                    }
                    break;

                case "rmdir":
                    if (parts.length == 2) {
                        fileSystem.rmdir(parts[1]);
                    } else {
                        System.out.println("Usage: rmdir <directory_name>");
                    }
                    break;

                case "chmod":
                    if (parts.length == 3) {
                        fileSystem.chmod(parts[1], parts[2]);
                    } else {
                        System.out.println("Usage: chmod <file_name> <permissions>");
                    }
                    break;

                case "chown":
                    if (parts.length == 3) {
                        fileSystem.chown(parts[1], parts[2]);
                    } else {
                        System.out.println("Usage: chown <file_name> <new_owner>");
                    }
                    break;

                default:
                    System.out.println("Comando desconhecido: " + cmd);
            }
        }
    }
}