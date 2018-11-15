package br.com.projetopausa.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Converter implements Serializable {
	private Long id;
	private String nome;

	public Converter(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Converter() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Converter other = (Converter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Classe [id=" + id + ", nome=" + nome + "]";
	}
}