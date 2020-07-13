package it.polito.tdp.extflightdelays.model;

public class Collegamento implements Comparable<Collegamento> {
	
	private Airport a1;
	private Airport a2;
	private Integer peso;
	
	public Collegamento(Airport a1, Airport a2, Integer peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	

	public Airport getA1() {
		return a1;
	}

	public void setA1(Airport a1) {
		this.a1 = a1;
	}

	public Airport getA2() {
		return a2;
	}

	public void setA2(Airport a2) {
		this.a2 = a2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}


	@Override
	public int compareTo(Collegamento o) {
		return -this.peso.compareTo(o.getPeso());
	}


}
