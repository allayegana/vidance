package br.com.omni.forum.config.validacao;

public class erroDeFormularDto {

    private String campo;
    private  String erro;

    public erroDeFormularDto(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getMensagem() {
        return erro;
    }

    public void setMensagem(String mensagem) {
        this.erro = mensagem;
    }
}
