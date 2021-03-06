package br.com.photolab.relatorio;

import br.com.photolab.modelo.Usuario;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by marcelo on 26/09/2014.
 */

@Entity
@ManagedBean
@Table(name = "relatoriodiario")
public class RelatorioDiario implements Serializable{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod;
    @OneToOne
    @JoinColumn(name = "func", referencedColumnName = "cod")
    private Usuario funcionario;
    @Column
    private int fotos;
    @Column
       private int qtdAlbuns;
    @Column
    private Timestamp dataRelatorio;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public Usuario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuario funcionario) {
        this.funcionario = funcionario;
    }

    public Timestamp getDataOperacao() {
        return dataRelatorio;
    }

    public void setDataOperacao(Timestamp dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public int getFotos() {
        return fotos;
    }

    public void setFotos(int fotos) {
        this.fotos = fotos;
    }

    public int getQtdAlbuns() {
        return qtdAlbuns;
    }

    public Timestamp getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(Timestamp dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public void setQtdAlbuns(int qtdAlbuns) {
        this.qtdAlbuns = qtdAlbuns;

    }
}
