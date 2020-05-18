package model;

public enum TipoPotenciaMotor {

    CINCO_HP("5hp", false),
    QUINZE_HP("15hp", false),
    QUARENTA_HP("40hp", false),
    SESSENTA_HP("60hp", false),
    NOVENTA_OU_MAIS("90hp ou +", false);

    private String descricao;
    private Boolean temResposta;

    private TipoPotenciaMotor(String descricao, Boolean temResposta){
        this.descricao = descricao;
        this.temResposta = temResposta;
    }

    public  String getDescricao(){
        return descricao;
    }

    public Boolean getTemReposta(){
        return temResposta;
    }
}
