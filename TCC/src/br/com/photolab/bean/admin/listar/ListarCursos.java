package br.com.photolab.bean.admin.listar;

import br.com.photolab.dao.modeloDao.ContratoDao;
import br.com.photolab.modelo.Contrato;
import br.com.photolab.modelo.apoio.Metricas;
import com.lowagie.text.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped //arrumar para outro tipo de escopo assim que possivel, mas manter pois ele precisa do contrato
public class ListarCursos implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */

    ContratoDao contDao = new ContratoDao();
    private Metricas metricas = new Metricas();
    private List<Contrato> listaContrato;
    private List<Contrato> contratosFiltrados;
    public ListarCursos() throws Exception {
        contDao = new ContratoDao();
        listaContrato = contDao.listarContratos();

    }

    public String btUpdateCursos() {
        return "/pages/conteudo/editarcursos_index.xhtml";
    }




    public void atualizar() throws Exception {
        try {
            this.listaContrato.clear();
            this. contDao.listarContratos().clear();
            this.listaContrato = contDao.listarContratos();
        } catch (Exception ex) {
        }
        listaContrato = contDao.listarContratos();


    }

    public String parserStatus(Contrato contrato) {
        return metricas.getStatusContratoLista().get(contrato.getStatus())
                .getLabel();
    }

    public String parserUrgencia(Contrato contrato) {
        return metricas.getUrgenciaLista().get(contrato.getUrgencia())
                .getLabel();
    }

    public String parserOcupado(Contrato contrato) {
        if (contrato.isOcupado()){
            return  metricas.getOcupadoEditar().get(1).getLabel();
        }
        else
            return  metricas.getOcupadoEditar().get(0).getLabel();
    }



    public String updateContrato(Contrato contrato) throws Exception {
        if (contDao.Update(contrato)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Update",
                            "Gravado com sucesso"));
            return "/pages/visualizarcursos_index.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Update",
                            "Falha ao gravar!!!"));
            return "/pages/visualizarcursos_index.xhtml";
        }

    }


    public void preProcessPDF(Object document) throws IOException,
            BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.add(new Paragraph("Ficha: "+new ListarFichas().getFichaSelecionada().getNumero()+"/"+new ListarFichas().getFichaSelecionada().getAno()));
        pdf.setPageSize(PageSize.A4.rotate());


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

    public List<Contrato> getListaContrato() {
        return listaContrato;
    }

    public void setListaContrato(List<Contrato> listaContrato) {
        this.listaContrato = listaContrato;
    }


    public List<Contrato> getContratosFiltrados() {
        return contratosFiltrados;
    }

    public void setContratosFiltrados(List<Contrato> contratosFiltrados) {
        this.contratosFiltrados = contratosFiltrados;
    }


}
