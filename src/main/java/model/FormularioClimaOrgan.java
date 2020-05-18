package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clima_organizacional")
public class FormularioClimaOrgan implements Serializable{
	
	public FormularioClimaOrgan() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "p1")
	private String pergunta01; // N S com a comunicação interna do RH com você
	
	
	@Column(name = "p2")
	private String pergunta02; // N S com recurso materiais oferecidos pela fas para realização do seu trabalho
	
	@Column(name = "p3")
	private String pergunta03;// N S em relação a prática dos beneficios, remuneração e promoções
	
	@Column(name = "p4")
	private String pergunta04;// N S sobre o seu relacionamento profissional com seu colega de trabalho
	
	@Column(name = "p5")
	private String pergunta05; // N S sobre o seu relacionamento profissional com seus superiores
	
	@Column(name = "p6")
	private String pergunta06; // N S em realação a sua autonomia para propor ideias e soluções para seus superiores
	
	@Column(name = "p7")
	private String pergunta07; // N S a sua demanda de trabalho diária
	
	@Column(name = "p08")
	private String pergunta08; // N S em relação aos treinamentos
	
	@Column(name = "p9") 
	private String pergunta09; // N S em relação a valorização e reconhecimento do seu trabalho
	
	@Column(name = "p10")
	private String pergunta10; // N S geral com a FAS
	
	
	 // V & F
	@Column(name = "p11")
	private String pergunta11; // Você acredita que seu salário está de acordo com o mercado de trabalho? 
	
	@Column(name = "p12")
	private String pergunta12; // Se você trabalha na sede de manaus o programa de qualidade de vida (RPG/Fisioterapia/etc...)
	// atende suas necessidades ?
	
	@Column(name = "p13")
	private String pergunta13; // Os eventos internos (Aniversariantes do mês/Datas comemorativas/Palestras de saúde) 
	//atendem as suas expectativas?
	
	@Column(name = "p14")
	private String pergunta14; // Você sente orgulho de trabalhar na FAS (Fundação Amazonas Sustentável)?
	
	@Column(name = "p15")
	private String pergunta15; // Você conhece a visão, missão e valores da FAS?
	
	@Column(name = "p16")
	private String pergunta16; // Você se identifica com os valores da FAS?
	
	@Column(name = "p17")
	private String pergunta17; // Você conhece e se sente comprometido com os resultados da FAS?

	public String getPergunta01() {
		return pergunta01;
	}

	public void setPergunta01(String pergunta01) {
		this.pergunta01 = pergunta01;
	}

	public String getPergunta02() {
		return pergunta02;
	}

	public void setPergunta02(String pergunta02) {
		this.pergunta02 = pergunta02;
	}

	public String getPergunta03() {
		return pergunta03;
	}

	public void setPergunta03(String pergunta03) {
		this.pergunta03 = pergunta03;
	}

	public String getPergunta04() {
		return pergunta04;
	}

	public void setPergunta04(String pergunta04) {
		this.pergunta04 = pergunta04;
	}

	public String getPergunta05() {
		return pergunta05;
	}

	public void setPergunta05(String pergunta05) {
		this.pergunta05 = pergunta05;
	}

	public String getPergunta06() {
		return pergunta06;
	}

	public void setPergunta06(String pergunta06) {
		this.pergunta06 = pergunta06;
	}

	public String getPergunta07() {
		return pergunta07;
	}

	public void setPergunta07(String pergunta07) {
		this.pergunta07 = pergunta07;
	}

	public String getPergunta08() {
		return pergunta08;
	}

	public void setPergunta08(String pergunta08) {
		this.pergunta08 = pergunta08;
	}

	public String getPergunta09() {
		return pergunta09;
	}

	public void setPergunta09(String pergunta09) {
		this.pergunta09 = pergunta09;
	}

	public String getPergunta10() {
		return pergunta10;
	}

	public void setPergunta10(String pergunta10) {
		this.pergunta10 = pergunta10;
	}

	public String getPergunta11() {
		return pergunta11;
	}

	public void setPergunta11(String pergunta11) {
		this.pergunta11 = pergunta11;
	}

	public String getPergunta12() {
		return pergunta12;
	}

	public void setPergunta12(String pergunta12) {
		this.pergunta12 = pergunta12;
	}

	public String getPergunta13() {
		return pergunta13;
	}

	public void setPergunta13(String pergunta13) {
		this.pergunta13 = pergunta13;
	}

	public String getPergunta14() {
		return pergunta14;
	}

	public void setPergunta14(String pergunta14) {
		this.pergunta14 = pergunta14;
	}

	public String getPergunta15() {
		return pergunta15;
	}

	public void setPergunta15(String pergunta15) {
		this.pergunta15 = pergunta15;
	}

	public String getPergunta16() {
		return pergunta16;
	}

	public void setPergunta16(String pergunta16) {
		this.pergunta16 = pergunta16;
	}

	public String getPergunta17() {
		return pergunta17;
	}

	public void setPergunta17(String pergunta17) {
		this.pergunta17 = pergunta17;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormularioClimaOrgan other = (FormularioClimaOrgan) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
