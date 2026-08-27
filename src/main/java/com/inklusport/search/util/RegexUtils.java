package com.inklusport.search.util;

import java.util.regex.Pattern;

/**
 * Los filtros de texto libre (nombre, email, ubicacion, etc.) se traducen a
 * un $regex de Mongo para lograr "contiene, sin importar mayusculas". El
 * valor viene de un query param controlado por el usuario, asi que hay que
 * tratarlo como texto literal: sin escapar, caracteres como ( ) . * + dentro
 * del termino de busqueda romperian el regex o, peor, permitirian construir
 * patrones costosos (ReDoS) contra Mongo.
 */
public final class RegexUtils {

    private RegexUtils() {
    }

    public static String literalContains(String value) {
        return Pattern.quote(value);
    }
}
