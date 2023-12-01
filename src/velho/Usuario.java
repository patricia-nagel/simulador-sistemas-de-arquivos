package velho;

public class Usuario {
    String username;
    String password;

    //criar estrutura da get/set

    boolean isSuperUsuario;

    public Usuario(String username, String password, boolean isSuperUsuario) {
        this.username = username;
        this.password = password;
        this.isSuperUsuario = isSuperUsuario;
    }
}
