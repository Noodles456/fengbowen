package com.fbw.OneBoot.enums;

public enum  TypeEnum {
QUESTION(1),
    COMMENT(2);
private Integer type;

    public static boolean isExist(Integer type) {
        for (TypeEnum typeEnum:TypeEnum.values()) {
            if(typeEnum.getType()==type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    TypeEnum(Integer type) {
        this.type = type;
    }
}
