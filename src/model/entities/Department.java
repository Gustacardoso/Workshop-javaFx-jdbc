package model.entities;

import java.io.Serializable;

public class Department implements Serializable {
	
	/*a serialização significa salvar o estado atual dos objetos em aruivos
	 * em ofrmao binario para o seu computador, sendo assim esse poderá ser 
	 * recuparado posteriormente recriando o objeto em memoria assim como 
	 * ele estava no momento da sua serialização*/
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	
	public Department() {
	
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	//para ser conparado pelos conteudos, nao pela referencia
			//neste caso so iremos comparar por id
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
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//vamos jerar um toString padrao, mais para testar nossa alicação
	
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
	
	
	
	
	

}
