package entidades;

//joaogabrielbz//

public class TabelaDisciplina {
	private Disciplina disciplina;
	private String[][] matriz;

	public TabelaDisciplina(Disciplina disciplina, String[][] matriz) {
		super();
		this.disciplina = disciplina;
		this.matriz = matriz;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public String[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(String[][] matriz) {
		this.matriz = matriz;
	}
}