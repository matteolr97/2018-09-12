package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {
private Graph<Nerc,DefaultWeightedEdge> grafo;
private Map<Integer, Nerc>idMap;
private PowerOutagesDAO dao;
private List<Nerc> neighbors;


public Model() {
	grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	dao = new PowerOutagesDAO();
	idMap = new HashMap<>();
	neighbors = new ArrayList<>();
	
}
public void creaGrafo() {
	dao.loadAllNercs(idMap);
	Graphs.addAllVertices(grafo, idMap.values());
	for(Nerc n : this.grafo.vertexSet())
		 {neighbors = dao.getVicini(idMap, n);
		 for(Nerc ntemp : neighbors) {
			 int correlazione = dao.getCorrelazione(n, ntemp);
			 DefaultWeightedEdge d = grafo.getEdge(n, ntemp);
			 if(d == null)
				 Graphs.addEdge(grafo, n, ntemp, correlazione);
			 
		 }
		
	}
	System.out.println("Vertici = "+grafo.vertexSet().size());
	System.out.println("Archi = "+grafo.edgeSet().size());

	
	
}
public List<Nerc> getVerticiGrafo() {

	return dao.loadAllNercs(idMap);
}
public List<NeighborNerc> getCorrelatedNeighbors(Nerc nerc){
	List<NeighborNerc> correlatedNercs = new ArrayList<NeighborNerc>();
	List<Nerc> neighbors = Graphs.neighborListOf(grafo, nerc);
	for(Nerc neighbor : neighbors) {
		DefaultWeightedEdge edge = grafo.getEdge(nerc, neighbor);
		correlatedNercs.add(new NeighborNerc(neighbor,(int) grafo.getEdgeWeight(edge)));
	}
	Collections.sort(correlatedNercs);
	return correlatedNercs;
}





}
