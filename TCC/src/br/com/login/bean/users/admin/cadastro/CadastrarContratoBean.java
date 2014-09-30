package br.com.login.bean.users.admin.cadastro;

import br.com.login.Dao.ContratoDao;
import br.com.login.bean.users.admin.listar.ListarFichas;
import br.com.login.model.Contrato;
import br.com.login.model.Ficha;
import br.com.login.model.Metricas;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class CadastrarContratoBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */

    ContratoDao contDao = new ContratoDao();
    private Contrato contratoCadastro = new Contrato();
    private Metricas metricas = new Metricas();

    private Ficha fichaSelecionada;

    {
        fichaSelecionada = ListarFichas.getFichaSelecionada();
    }

    public Ficha getFichaSelecionada() {
        return fichaSelecionada;
    }

    public void setFichaSelecionada(Ficha fichaSelecionada) {
        this.fichaSelecionada = fichaSelecionada;
    }

    public String gravarContrato() throws Exception {
        contratoCadastro.setFicha(fichaSelecionada);
        contratoCadastro.setQtdAlbum(0);
        if (contDao.Gravar(contratoCadastro)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "gravar",
                            "Gravado com sucesso"));
            return "/pages/admin/visualizarcursos_ficha.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "gravar",
                            "Falha ao gravar!!!"));
            return null;
        }

    }

    public Contrato getContratoCadastro() {
        return contratoCadastro;
    }

    public void setContratoCadastro(Contrato contratoCadastro) {
        this.contratoCadastro = contratoCadastro;
    }

    public ContratoDao getContDao() {
        return contDao;
    }

    public void setContDao(ContratoDao contDao) {
        this.contDao = contDao;
    }

    public Metricas getMetricas() {
        return metricas;
    }

    public void setMetricas(Metricas metricas) {
        this.metricas = metricas;
    }

}
