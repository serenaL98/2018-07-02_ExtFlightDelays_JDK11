package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	private List<Airport> aeroporti;
	private Map<Integer, Airport> mappa;
	
	//Grafo semplice, pessato, non orientato
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
		this.aeroporti = new ArrayList<>(this.dao.loadAllAirports());
		
		this.mappa = new HashMap<>();
		
		for(Airport a: this.aeroporti) {
			mappa.put(a.getId(), a);
		}
		
	}
	
	public void creaGrafo(int miglia) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.collegamenti = new ArrayList<>(this.dao.prendiCollegamenti(miglia, mappa));
		
		//VERTICI e ARCHI
		for(Collegamento c: this.collegamenti) {
			Graphs.addEdgeWithVertices(this.grafo, c.getA1(), c.getA2(), c.getPeso());
		}
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Airport> elencoAeroporti(){
		
		List<Airport> vertici = new ArrayList<>();
		
		for( Airport a: this.grafo.vertexSet()) {
			vertici.add(a);
		}
		
		Collections.sort(vertici);
		
		return vertici;
	}
	
	public String aeroportiConnessi(Airport partenza) {
		
		List<Collegamento> ordinata = new ArrayList<>();
		
		for(Airport a: Graphs.neighborListOf(this.grafo, partenza)) {
			ordinata.add(new Collegamento(partenza, a, (int)(this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, a)))));
		}
		
		Collections.sort(ordinata);
		
		String stampa = "";
		for(Collegamento c: ordinata) {
			stampa += c.getA2()+" ---> "+c.getPeso()+"\n";
		}
		return stampa;
	}
	
	//---PUNTO 2---
	private List<Airport> soluzione;
	private int migliamax;
	
	public String itinerario(Airport partenza, int miglia) {
		
		this.soluzione = new ArrayList<>();
		
		this.migliamax = miglia;
		
		List<Airport> parziale = new ArrayList<>();
		
		parziale.add(partenza);
		
		ricorsione(parziale, 0);
		
		soluzione.remove(partenza);
		
		String stampa = "";
		for(Airport a: this.soluzione) {
			stampa += a.getAirportName()+"\n";
		}
		return stampa+"Distanza totale percorsa "+this.distanzaTot(soluzione)+" miglia.";
		
	}
	
	private void ricorsione(List<Airport> parziale, int percorsi) {
		//caso finale: ho percorso il maggior numero di nodi, rispettando le miglia
		if(parziale.size()>soluzione.size() && percorsi<=this.migliamax) {
			this.soluzione = new ArrayList<>(parziale);
		}
		
		//caso intermedio
		Airport ultimo = parziale.get(parziale.size()-1);
		
		for(Airport a: Graphs.neighborListOf(this.grafo, ultimo)) {
			
			int peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a));
			
			if(!parziale.contains(a) && peso <= this.migliamax) {
				parziale.add(a);
				ricorsione(parziale, peso);
				parziale.remove(a);
			}
		}
		
	}
	
	private int distanzaTot(List<Airport> parziale) {
		
		int somma = 0;
		
		for(DefaultWeightedEdge c: this.grafo.edgeSet()) {
			for(int i=1; i<parziale.size(); i++) {
				if(this.grafo.getEdgeSource(c).equals(parziale.get(i-1)) && this.grafo.getEdgeTarget(c).equals(parziale.get(i))) {
					somma += this.grafo.getEdgeWeight(c);
				}
			}
		}
		
		return somma;
	}
}
