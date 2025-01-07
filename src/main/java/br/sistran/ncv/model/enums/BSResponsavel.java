package br.sistran.ncv.model.enums;

public enum BSResponsavel {

    CAMILA(0,"Camila"),
    ALEX(1, "Alex"),
    LEILA(2, "Leila"),
    RICARDO(3, "Ricardo");

    private Integer codigo;
    private String nomeResponsavel;

    BSResponsavel(Integer codigo, String nomeResponsavel) {
        this.codigo = codigo;
        this.nomeResponsavel = nomeResponsavel;
    }


    public Integer getCodigo() {
        return codigo;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public static BSResponsavel toEnum(Integer cod) throws IllegalArgumentException {
        if (cod == null) {
            return null;
        }
        for (BSResponsavel x : BSResponsavel.values()) {
            if (cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Responsável Inválido");
    }
}
