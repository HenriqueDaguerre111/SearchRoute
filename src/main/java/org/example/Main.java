package org.example;

import org.example.model.Arestas;
import org.example.model.Cidade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\hdfig\\OneDrive\\Desktop\\CriadorDeRota\\src\\main\\resources\\routes.txt"));
        Map<String, List<Arestas>> grafo = new HashMap<>();
        List<Cidade> cidades = new ArrayList<>();
        String linha = null;


        while ((linha = br.readLine()) != null && !linha.equals("--")) {
            String[] parts = linha.split(",");
            cidades.add(new Cidade(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }


        while ((linha = br.readLine()) != null && !linha.equals("--")) {
            String[] parts = linha.split(",");
            grafo.putIfAbsent(parts[0], new ArrayList<>());
            grafo.putIfAbsent(parts[1], new ArrayList<>());
            double custo = Double.parseDouble(parts[2]);
            grafo.get(parts[0]).add(new Arestas(parts[1], custo));
            grafo.get(parts[1]).add(new Arestas(parts[0], custo));
        }


        linha = br.readLine();
        int originCep = Integer.parseInt(linha.split(",")[0]);
        int destinyCep = Integer.parseInt(linha.split(",")[1]);

        String Origem = null, Destino = null;

        for (Cidade c : cidades) {
            if (c.cepContem(originCep)) Origem = c.nome;
            if (c.cepContem(destinyCep)) Destino = c.nome;
        }

        if (Origem == null || Destino == null) {
            System.out.println("CEP Invalido");
            return;
        }

        //  Dijkstra
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (String cidade : grafo.keySet()) {
            dist.put(cidade, Double.MAX_VALUE);
        }
        dist.put(Origem, 0.0);
        queue.add(Origem);

        while (!queue.isEmpty()) {
            String actual = queue.poll();
            for (Arestas edge : grafo.get(actual)) {
                double cost = dist.get(actual) + edge.custo;
                if (cost < dist.get(edge.destino)) {
                    dist.put(edge.destino, cost);
                    prev.put(edge.destino, actual);
                    queue.add(edge.destino);
                }
            }
        }


        if (!dist.containsKey(Destino)) {
            System.out.println("Caminho Indisponivel");
            return;
        }

        List<String> rota = new ArrayList<>();
        for (String at = Destino; at != null; at = prev.get(at)) {
            rota.add(at);
        }
        Collections.reverse(rota);

        System.out.println(String.join(" -> ", rota));
        System.out.printf("Custo: R$%.2f%n", dist.get(Destino));

    }
}