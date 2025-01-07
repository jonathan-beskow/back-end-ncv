package br.sistran.ncv.model.enums;

public enum StatusAplicacao {

    EM_DESENVOLVIMENTO(0,"Em Desenvolvimento"),
    DISPONIBILIZADA_PARA_TESTE(1,"Disponibilizada para testes"),
    EM_HOMOLOGACAO(2, "Em Homologação"),
    EM_IMPLANTACAO(3, "Em Implantação"),
    IMPLANTADA(4, "Implantada"),
    IMPEDIMENTO(5, "Impedimento");

    private Integer codigo;
    private String descricao;

    StatusAplicacao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }


    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusAplicacao toEnum(Integer cod) throws IllegalArgumentException {
        if (cod == null) {
            return null;
        }
        for (StatusAplicacao x : StatusAplicacao.values()) {
            if (cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Status Inválido");
    }

}
