package entidades;

//joaogabrielbz //

public class TurmaDisciplina {
	private int idTurmaDisciplina;
	private int qtdAulas;
	private int TurmaId;
	private int DisciplinaId;
	
	public TurmaDisciplina(int idTurmaDisciplina, int qtdAulas, int turmaId, int disciplinaId) {
		super();
		this.idTurmaDisciplina = idTurmaDisciplina;
		this.qtdAulas = qtdAulas;
		TurmaId = turmaId;
		DisciplinaId = disciplinaId;
	}

	public int getIdTurmaDisciplina() {
		return idTurmaDisciplina;
	}

	public void setIdTurmaDisciplina(int idTurmaDisciplina) {
		this.idTurmaDisciplina = idTurmaDisciplina;
	}

	public int getQtdAulas() {
		return qtdAulas;
	}

	public void setQtdAulas(int qtdAulas) {
		this.qtdAulas = qtdAulas;
	}

	public int getTurmaId() {
		return TurmaId;
	}

	public void setTurmaId(int turmaId) {
		TurmaId = turmaId;
	}

	public int getDisciplinaId() {
		return DisciplinaId;
	}

	public void setDisciplinaId(int disciplinaId) {
		DisciplinaId = disciplinaId;
	}
}
