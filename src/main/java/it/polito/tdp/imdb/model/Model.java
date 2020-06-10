package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private Map<Integer,Actor> idMap;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<Integer, Actor>();
		
	}
	
	
	public List<String> getGenere () {
		return this.dao.getGenere();
	}
	public void creaGrafo(String genere) {
		
		 grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		 
		 Graphs.addAllVertices(this.grafo, this.dao.getVertici(genere, idMap));
		 
		 List<Adiacenza > adiacenze = this.dao.getAdiacenze(genere, idMap);
		 for(Adiacenza a : adiacenze) {
			 if(this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
				
                  Graphs.addEdge(this.grafo, a.getA1(), a.getA2(),(int) a.getPeso());
			 }
			 
		 }
		 idMap.clear();
		 
	}
	public List<Actor> getVertici(){
		List<Actor> attori = new ArrayList<Actor>(this.grafo.vertexSet());
		return attori;
	}
	public int nVertici() {
	return this.grafo.vertexSet().size();
	}

	public int nArchi() {
	return this.grafo.edgeSet().size();
	}
	
	//PUNTO 1D
	
	public List<Actor> getAttoriConnessi(Actor sorgente){
		if (grafo == null) {
			throw new RuntimeException ("Grafo non esistente!");
		}
		
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge> (grafo);
		List<Actor> attoriConnessi = new ArrayList<>(ci.connectedSetOf(sorgente)); 
		attoriConnessi.remove(sorgente);
		return attoriConnessi;
		
	}
	
		
		
}
