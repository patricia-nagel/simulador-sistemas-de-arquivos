package simulador;

import java.util.List;

public class Usuario {

    private String username;

    private String password;

    private boolean isSuperUsuario;

    private List<Permissao> permissoes;

    public Usuario(String username, String password, boolean isSuperUsuario, List<Permissao> permissoes) {
        this.username = username;
        this.password = password;
        this.isSuperUsuario = isSuperUsuario;
        this.permissoes = permissoes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuperUsuario() {
        return isSuperUsuario;
    }

    public void setSuperUsuario(boolean superUsuario) {
        isSuperUsuario = superUsuario;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }
}