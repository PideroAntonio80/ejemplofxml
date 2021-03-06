package com.sanvalero.ejemplofxml.domain;

public class Coche {

    private int id;
    private String matricula;
    private String marca;
    private String modelo;
    private String tipo;
    private String matriculaVieja;

    public Coche(int id, String matricula, String marca, String modelo, String tipo) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public Coche(String matriculaVieja, String matricula, String marca, String modelo, String tipo) {
        this.matriculaVieja = matriculaVieja;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public Coche(String matricula, String marca, String modelo, String tipo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public Coche(String matricula) {

        this.matricula = matricula;
    }

    public int getId() {
        return id;
    }

    public String getMatricula() {

        return matricula;
    }

    public void setMatricula(String matricula) {

        this.matricula = matricula;
    }

    public String getMarca() {

        return marca;
    }

    public void setMarca(String marca) {

        this.marca = marca;
    }

    public String getModelo() {

        return modelo;
    }

    public void setModelo(String modelo) {

        this.modelo = modelo;
    }

    public String getTipo() {

        return tipo;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public String getMatriculaVieja() {
        return matriculaVieja;
    }

    public void setMatriculaVieja(String matriculaVieja) {
        this.matriculaVieja = matriculaVieja;
    }

    public String toString() {
        return this.tipo + ": " + this.marca + " " + this.modelo + ". Matrícula: " + this.matricula;
    }
}
