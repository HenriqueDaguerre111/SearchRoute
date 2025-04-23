package org.example.model;

public class Cidade {
    public String nome;
    int cepInicio;
    int cepFim;

    public Cidade(String nome, int inicio, int fim) {
        this.nome = nome;
        this.cepInicio = inicio;
        this.cepFim = fim;
    }

    public boolean cepContem(int cep) {
        return cep >= cepInicio && cep <= cepFim;
    }
}
