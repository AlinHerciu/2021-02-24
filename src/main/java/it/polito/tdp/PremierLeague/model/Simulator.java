package it.polito.tdp.PremierLeague.model;


import java.util.Map;


public class Simulator {
	
	public Simulator(Model m, int n, Match match, Map<Integer, Team> idMapSquadre) {
		super();
		this.m = m;
		N = n;
		this.partita = match;
		this.idMapSquadre = idMapSquadre;
	}
	
	
	
	
	//Parametri di simulazione
	private int N ; // numero di azioni da simulare, scelto dall'utente
	private Match partita;
	Model m = new Model();
	
	//Altro input implicito
	private Map<Integer, Team> idMapSquadre;
		
	
	// 3)Stato del sistema
	private int numeroGiocatorisquadraA ;
	private int numeroGiocatorisquadraB ;
	private int numeroAzioni;
	
	private Team squadraA = idMapSquadre.get(partita.getTeamHomeID());
	private Team squadraB = idMapSquadre.get(partita.getTeamAwayID());
	
	
	// 4)Misure in uscita
	private int nEspulsiA ;					
	private int nEspulsiB ;	
	private int nGoalA ;			
	private int nGoalB ;	
	
	
	
	public void init() {
		
		// Stato iniziale
		this.numeroGiocatorisquadraA=11;
		this.numeroGiocatorisquadraB=11;
		this.numeroAzioni = N;
		nEspulsiA = 0;					
		nEspulsiB = 0;	
		nGoalA = 0;			
		nGoalB = 0 ;	
	}	
	
	
	public String run(){
		String s="";
		while(this.numeroAzioni!=0){
		if(Math.random()*100<50.0) { //GOAL
				if(this.numeroGiocatorisquadraA<this.numeroGiocatorisquadraB) { 	//Goal squadra B
					this.nGoalB++;
				}else {
					if(this.numeroGiocatorisquadraA>this.numeroGiocatorisquadraB) 	//Goal squadra A
					this.nGoalA++;
					else {
						//segna la squadra a cui appartiene il giocatore migliore;
						Team t = this.m.getSquadraGiocatoreMigliore();
						if(t.equals(squadraA))
							this.nGoalA++;
						else
							this.nGoalB++;
					}
				}
		}else {
			if(Math.random()*100<30.0) {    //ESPULSIONE
				if(Math.random()*100<60.0) {
					Team t = this.m.getSquadraGiocatoreMigliore();
					if(t.equals(squadraA))
						this.numeroGiocatorisquadraA--;
					else
						this.numeroGiocatorisquadraB--;
				}
			}else {
				if(Math.random()*100<20.0) {
					if((Math.random()*100)<50.0 ) {
						this.numeroGiocatorisquadraA--;
						if(Math.random()*100<50.0){
							this.numeroAzioni += 2;
						}else {
							this.numeroAzioni +=3;
						}
					}else {
						this.numeroGiocatorisquadraB--;
						if(Math.random()*100<50.0){
							this.numeroAzioni += 2;
						}else {
							this.numeroAzioni +=3;
						}
					}
				}
			}
		}
	
		}
		s = "Risultato: " + this.squadraA.toString() +"   "+ this.nGoalA + ":" + this.nGoalB + "  "+ this.squadraB.toString() + "\n" + this.squadraA.toString() + ": " + this.nEspulsiA + "\n"+ this.squadraB.toString() + ": " + this.nEspulsiB;              
		return s;
	}


	public void setN(int n) {
		N = n;
	}


	public void setPartita(Match partita) {
		this.partita = partita;
	}


	public void setIdMapSquadre(Map<Integer, Team> idMapSquadre) {
		this.idMapSquadre = idMapSquadre;
	}
}
