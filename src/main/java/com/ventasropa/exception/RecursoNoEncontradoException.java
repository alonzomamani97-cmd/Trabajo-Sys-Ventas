package com.ventasropa.exception;
// herencia (extends) palabra clave o implemets cuando implementa
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}