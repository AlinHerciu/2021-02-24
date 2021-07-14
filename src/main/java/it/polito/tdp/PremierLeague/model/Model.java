package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Player, DefaultWeightedEdge> grafo;
	Map<Integer, Player> idMap;
	Map<Integer, Team> idMapSquadre;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		idMapSquadre = new HashMap<>();
		
		
		dao.listAllPlayers(idMap);
		dao.listAllTeams(idMapSquadre);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getGiocatoriAllaPartita(m, idMap));
		
		//Aggiungo gli archi
		List<Adiacenza> archi = dao.calcolaArchi(m, idMap);
		for(Adiacenza a: archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getDifEFF());
		}
		
		
		}
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> partite(){
		return dao.listAllMatches();
	}
	
	
	
	public GiocatoreMigliore getMigliore() {
		GiocatoreMigliore g = null;
		double deltaMax = Integer.MIN_VALUE;
		
		for(Player p: this.grafo.vertexSet()) {
			double pesoUscente = 0;
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(e);
			}
			
			
			double pesoEntrante = 0;
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(e);
			}
			
			double delta = pesoUscente - pesoEntrante;
			if(delta>deltaMax) {
				deltaMax = delta;
				g = new GiocatoreMigliore(p, delta);
			}
		}
		
		return g;
	}
	
	public Team getSquadraGiocatoreMigliore() {
		return dao.getSquadraByPlayer(this.getMigliore().getP(), idMapSquadre);
	}
	
	public String simula(int N, Match partita) {
		Simulator r = new Simulator(this, N, partita, this.idMapSquadre);
		r.init();
		return r.run();
		
	}
}
