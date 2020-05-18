package model;

public enum TipoNumeracaoFamiliar {
    ZERO("0"),
    UM("1"),
    DOIS("2"),
    TRES("3"),
    QUATRO("4"),
    CINCO("5"),
    SEIS("6"),
    SETE("7"),
    OITO("8"),
    NOVE("9"),
    DEZ("10");

    private String descricao;

    private TipoNumeracaoFamiliar(String descricao){
        this.descricao = descricao;
    }

    public  String getDescricao(){
        return descricao;
    }

}