package entidades;

public class Turma {
	private int idTurma;
	private String nomeTurma;
	private int turnoId;
	public int getIdTurma() {
		return idTurma;
	}
	public void setIdTurma(int idTurma) {
		this.idTurma = idTurma;
	}
	public String getNomeTurma() {
		return nomeTurma;
	}
	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}
	public int getTurnoId() {
		return turnoId;
	}
	public void setTurnoId(int turnoId) {
		this.turnoId = turnoId;
	}
	public Turma(int idTurma, String nomeTurma, int turnoId) {
		super();
		this.idTurma = idTurma;
		this.nomeTurma = nomeTurma;
		this.turnoId = turnoId;
	}
	
	
}
