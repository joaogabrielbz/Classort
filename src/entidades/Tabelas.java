package entidades;

import java.util.ArrayList;

//joaogabrielbz//

public class Tabelas {
	public ArrayList<TabelaTurma> tabelaturmas;
	public ArrayList<TabelaDisciplina> tabeladisciplinas;
	public ArrayList<Realocacao> realocacoes;

	public Tabelas(ArrayList<TabelaTurma> tabelaturmas, ArrayList<TabelaDisciplina> tabeladisciplinas,
			ArrayList<Realocacao> realocacoes) {
		super();
		this.tabelaturmas = tabelaturmas;
		this.tabeladisciplinas = tabeladisciplinas;
		this.realocacoes = realocacoes;
	}

	public int compareTo(Tabelas other) {
		return Integer.compare(this.realocacoes.size(), other.realocacoes.size());
	}
}