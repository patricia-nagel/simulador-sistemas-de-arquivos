package simulador;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        SistemaArquivo sistemaArquivo = new SistemaArquivo();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Simulador de arquivos em execução");

        List<Permissao> permissoesRoot = Arrays.asList(Permissao.values());
        sistemaArquivo.addUser("root", "root", true, permissoesRoot);
        String[] loginUsuarioRoot = {"login", "root", "root"};
        sistemaArquivo.loginUser(loginUsuarioRoot);

        while (true) {
            System.out.print(sistemaArquivo.usuariosAtual != null ? sistemaArquivo.usuariosAtual.getUsername() +
                    " > " : "visitante > ");

            String comando = scanner.nextLine();
            String[] partes = comando.split(" ");
            String cmd = partes[0];
            switch (cmd) {
                case "adduser":
                    sistemaArquivo.addUser(partes);
                    break;
                case "gravaconteudo":
                    sistemaArquivo.gravarConteudo(partes);
                    break;
                case "rmuser":
                    sistemaArquivo.removeUser(partes);
                    break;
                case "lsuser":
                    sistemaArquivo.lsUser(partes);
                    break;
                case "login":
                    sistemaArquivo.loginUser(partes);
                    break;
                case "format":
                    sistemaArquivo.format(partes);
                    break;
                case "mkdir":
                    sistemaArquivo.mkdir(partes);
                    break;
                case "touch":
                    sistemaArquivo.touch(partes);
                    break;
                case "ls":
                    sistemaArquivo.ls(partes);
                    break;
                case "cat":
                    sistemaArquivo.cat(partes);
                    break;
                case "rm":
                    sistemaArquivo.rm(partes);
                    break;
                case "cd":
                    sistemaArquivo.cd(partes);
                    break;
                case "rmdir":
                    sistemaArquivo.rmdir(partes);
                    break;
                case "chown":
                    sistemaArquivo.chown(partes);
                    break;
                case "exit":
                    System.out.println("Encerrando Simulador de arquivos.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Comando desconhecido: " + cmd);
            }
        }
    }
}