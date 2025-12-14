package com.ortzion_technology.ortzion_telecom_server.utils;

public class DocumentoUtils {

    public static String desformatadorDocumento(String documento) {
        if(documento == null) {
            return null;
        }
        return  documento.replaceAll("[^0-9]", "");
    }

    public static String desformatadorTelefone(String documento) {
        if(documento == null) {
            return null;
        }
        return  documento.replaceAll("[^0-9]", "");
    }

}
