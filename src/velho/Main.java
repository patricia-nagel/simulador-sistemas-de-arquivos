package velho;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SistemaArquivo fileSystem = new SistemaArquivo();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao FileSystem Simulator!");

        //fileSystem.addUser("root", "root");
        //fileSystem.authenticateUser("root", "root");

        while (true) {
            System.out.print(fileSystem.currentUser != null
                    ? fileSystem.currentUser.username + "@" + fileSystem.currentDirectory + "$ ou > "
                    : "guest@0$ ");

            String command = scanner.nextLine();

            String[] parts = command.split(" ");
            //String[] parts = command.split("\\s+");

//            //condicao para string vazia
//            if (parts.length == 0) {
//                return;
//            }

            String cmd = parts[0];


            switch (cmd) {
                case "addUser":
                    fileSystem.addUser(parts);
                    break;

                case "rmuser":
                    fileSystem.removeUser(parts);
                    break;

                case "login":
                    fileSystem.authenticateUser(parts);
                    break;

                case "format":
                    fileSystem.format();
                    break;

                case "mkdir":
                    fileSystem.mkdir(parts);
                    break;

                case "touch":
                    fileSystem.touch(parts);
                    break;

                case "ls":
                    fileSystem.ls();
                    break;

                case "cat":
                    fileSystem.cat(parts);
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
                    fileSystem.cd(parts);
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

                case "exit":
                    System.out.println("Exiting FileSystem Simulator. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Comando desconhecido: " + cmd);
            }
        }
    }
}