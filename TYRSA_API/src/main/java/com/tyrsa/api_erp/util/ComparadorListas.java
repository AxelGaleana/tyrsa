package com.tyrsa.api_erp.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ComparadorListas {

    /**
     * Compara dos listas y devuelve true si son diferentes.
     *
     * @param <T> Tipo de elementos de la lista
     * @param listaOriginal Lista original
     * @param listaNueva Lista nueva
     * @return true si hay diferencias, false si son iguales
     */
    public static <T> boolean listasDiferentes(List<T> listaOriginal, List<T> listaNueva) {
        // Caso null vs no null
        if (listaOriginal == null && listaNueva != null) return true;
        if (listaOriginal != null && listaNueva == null) return true;

        // Ambas null → iguales
        if (listaOriginal == null) return false;

        // Diferencia de tamaño
        if (listaOriginal.size() != listaNueva.size()) return true;

        // Comparación elemento a elemento
        for (int i = 0; i < listaOriginal.size(); i++) {
            if (!Objects.equals(listaOriginal.get(i), listaNueva.get(i))) {
                return true;
            }
        }

        // Todo igual
        return false;
    }

    public static <T> boolean listasDiferentes(T[] arrayOriginal, T[] arrayNueva) {
        return listasDiferentes(Arrays.asList(arrayOriginal), Arrays.asList(arrayNueva));
    }

}