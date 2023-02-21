package com.domingueti.upfine.utils.statics;

public class ConvertToRawCnpj {

    public static String execute(String cnpj) {
        return cnpj.replace(".", "").replace("-", "").replace("/", "");
    }
}
