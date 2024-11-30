package Enums;

public enum TipoItens {
    KIT_DE_VIDA("kit de vida"),
    COLETE("colete");

    private final String code;

    TipoItens(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
