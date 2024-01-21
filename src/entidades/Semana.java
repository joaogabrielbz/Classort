package entidades;

//joaogabrielbz//

public class Semana {
	private boolean segunda;
	private boolean terca;
	private boolean quarta;
	private boolean quinta;
	private boolean sexta;
	private boolean sabado;
	private boolean domingo;
	private int qtdDias;

	public Semana(boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado,
			boolean domingo) {
		super();
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
		this.domingo = domingo;

		int cont = 0;

		if (isSegunda())
			cont++;
		if (isTerca())
			cont++;
		if (isQuarta())
			cont++;
		if (isQuinta())
			cont++;
		if (isSexta())
			cont++;
		if (isSabado())
			cont++;
		if (isDomingo())
			cont++;

		this.qtdDias = cont;
	}

	public int getQtdDias() {
		return qtdDias;
	}

	public boolean isSegunda() {
		return segunda;
	}

	public void setSegunda(boolean segunda) {
		this.segunda = segunda;
	}

	public boolean isTerca() {
		return terca;
	}

	public void setTerca(boolean terca) {
		this.terca = terca;
	}

	public boolean isQuarta() {
		return quarta;
	}

	public void setQuarta(boolean quarta) {
		this.quarta = quarta;
	}

	public boolean isQuinta() {
		return quinta;
	}

	public void setQuinta(boolean quinta) {
		this.quinta = quinta;
	}

	public boolean isSexta() {
		return sexta;
	}

	public void setSexta(boolean sexta) {
		this.sexta = sexta;
	}

	public boolean isSabado() {
		return sabado;
	}

	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}

	public boolean isDomingo() {
		return domingo;
	}

	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}
}