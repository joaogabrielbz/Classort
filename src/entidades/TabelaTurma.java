package entidades;

// joaogabrielbz //



public class TabelaTurma {	
	private Turma turma;
	private String[][] matriz;

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public String[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(String[][] matriz) {
		this.matriz = matriz;
	}

	public TabelaTurma(Turma turma, String[][] matriz) {		
		this.turma = turma;		
		this.matriz = matriz;
	}
		
	
	
	
}
