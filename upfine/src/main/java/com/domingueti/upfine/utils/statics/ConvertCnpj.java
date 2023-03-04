package com.domingueti.upfine.utils.statics;

public class ConvertCnpj {

    public static String formattedToRaw(String cnpj) {
        return cnpj.replace(".", "").replace("-", "").replace("/", "");
    }

    public static String rawToFormatted(String cnpj) {
        return cnpj.substring(0, 2) + "." +
                cnpj.substring(2, 5) + "." +
                cnpj.substring(5, 8) + "/" +
                cnpj.substring(8, 12) + "-" +
                cnpj.substring(12);
    }
}
