package br.com.projetopausa.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Usuario {

	@Id
	@Column(length = 7)
	private String matricula;

	@Column(length = 32, nullable = false)
	private String senha;

	@Transient
	private String senhaCriptografada;

	@Column(length = 55, nullable = false)
	private String nome;

	@Column(nullable = false)
	private boolean status = false;

	@Column(nullable = false)
	private boolean logado = false;

	@Column(nullable = false)
	private String terceiraPausa = "x";

	@Column(nullable = false)
	private String intervalo = "x";

	@Column(nullable = false)
	private String hms = "x";

	@Column
	private Calendar horaDaPausa = Calendar.getInstance();

	@Column
	private Calendar horaDoIntervalo = Calendar.getInstance();

	@Column
	private Calendar horaDaUPausa = Calendar.getInstance();

	public Calendar getHoraDoIntervalo() {
		return horaDoIntervalo;
	}

	public void setHoraDoIntervalo(Calendar horaDoIntervalo) {
		this.horaDoIntervalo = horaDoIntervalo;
	}

	public Calendar getHoraDaUPausa() {
		return horaDaUPausa;
	}

	public void setHoraDaUPausa(Calendar horaDaUPausa) {
		this.horaDaUPausa = horaDaUPausa;
	}

	public Calendar getHoraDaPausa() {
		return horaDaPausa;
	}

	public void setHoraDaPausa(Calendar horaDaPausa) {
		this.horaDaPausa = horaDaPausa;
	}

	public String getHms() {
		return hms;
	}

	public void setHms(String hms) {
		this.hms = hms;
	}

	public String getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(String intervalo) {
		this.intervalo = intervalo;
	}

	public String getTerceiraPausa() {
		return terceiraPausa;
	}

	public void setTerceiraPausa(String terceiraPausa) {
		this.terceiraPausa = terceiraPausa;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFlagStatus() {
		if (this.isStatus()) {
			return "Descanso";
		}
		return "Disponível";
	}

	public String getFlagLogado() {
		if (this.isLogado()) {
			return "Logado";
		}
		return "off";
	}

}
