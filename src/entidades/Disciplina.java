package entidades;

//joaogabrielbz //

public class Disciplina {
	private int idDisciplina;
    private String nomeDisciplina;
    private String professorDisciplina;
    private int turnoId;
    
    private int aulasTotais;
    
    public int getAulasTotais() {
		return aulasTotais;
	}    
    public void setAulasTotais(int aulasTotais) {
		this.aulasTotais = aulasTotais;
	}   
	public int getIdDisciplina() {
		return idDisciplina;
	}
	public void setIdDisciplina(int idDisciplina) {
		this.idDisciplina = idDisciplina;
	}
	public String getNomeDisciplina() {
		return nomeDisciplina;
	}
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}
	public String getProfessorDisciplina() {
		return professorDisciplina;
	}
	public void setProfessorDisciplina(String professorDisciplina) {
		this.professorDisciplina = professorDisciplina;
	}
	public int getTurnoId() {
		return turnoId;
	}
	public void setTurnoId(int turnoId) {
		this.turnoId = turnoId;
	}
	public Disciplina(int idDisciplina, String nomeDisciplina, String professorDisciplina, int turnoId) {
		super();
		this.idDisciplina = idDisciplina;
		this.nomeDisciplina = nomeDisciplina;
		this.professorDisciplina = professorDisciplina;
		this.turnoId = turnoId;
	}
    
    
}
