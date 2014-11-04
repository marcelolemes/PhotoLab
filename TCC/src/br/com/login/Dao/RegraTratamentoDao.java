package br.com.login.Dao;

import br.com.login.bean.users.UserBean;
import br.com.login.model.Album;
import br.com.login.model.Contrato;
import br.com.login.model.Relatorio;
import br.com.login.model.User;
import br.com.login.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by marcelo on 27/09/2014.
 */
@ManagedBean
public class RegraTratamentoDao implements Serializable {
    private UserBean userBean;
    ContratoDao contDao = new ContratoDao();
    RelatorioDao relatorioDao = new RelatorioDao();
    AlbumDao albumDao = new AlbumDao();



    public Album NovoAlbum(User user) throws Exception { //sobrecarga com "USER"
        Album retorno = null;
        Contrato contrato =  contDao.listarContratoStatus(5, 10, 1);

        if (contrato!= null) {

            retorno = albumDao.ProximoAlbum(contrato,5,10);

            if (retorno != null) {
                if (user != null){
                    retorno.setUserTratamento(user);
                }

                retorno.setStatus(8);
                retorno.setOcupado(true);
                contrato = contDao.atualizarContratoRetorna(contrato);
                contrato.setUrgencia(0);
                contrato.setOcupado(true);
                Session sessao = HibernateUtil.getSession();
                org.hibernate.Transaction transacao = sessao.beginTransaction();
                sessao.update(contrato);
                sessao.update(retorno);
                transacao.commit();
                sessao.close();
            }
            else
            {
                contrato = contDao.atualizarContratoRetorna(contrato);
                contrato.setUrgencia(4);
                contrato.setOcupado(false);
                Session sessao = HibernateUtil.getSession();
                org.hibernate.Transaction transacao = sessao.beginTransaction();
                sessao.update(contrato);
                transacao.commit();
                sessao.close();
            }

            return retorno;
        }
        else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem albuns para o seu setor",
                            "Sem albuns para o seu setor, informe o seu superior imediatamente!"));

            return retorno;
        }
    }

    public void albumTerminado(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();


        if (album!=null){
            album.setStatus(11);
            album.setOcupado(false);
            sessao.update(album);
            transacao.commit();



            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Album "+album.getNumero()+" encerrado" ,
                            "Pegue outro album para continuar trabalhando"));
        }



        try {
            sessao.close();
            contDao.atualizarContrato(album.getContrato());
        }
        catch (Exception e) {
        }

    }

    public void albumDeletado(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        if (album!=null){
            sessao.delete(album);
        }

        transacao.commit();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Album "+album.getNumero()+" Removido" ,
                        "por favor, mova-o para a pasta 'Menos de vinte'"));
        try {
            sessao.close();
            contDao.atualizarContrato(album.getContrato());
        }
        catch (Exception e) {
        }

    }


    public void  albumCancelado(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        if (album!=null){
            album.setStatus(7);
            album.setOcupado(false);
        }
        sessao.update(album);
        transacao.commit();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Album "+album.getNumero()+" cancelado" ,
                        "Informe ao seu superior"));
        try {
            sessao.close();
            contDao.atualizarContrato(album.getContrato());
        }
        catch (Exception e) {
        }

    }


}
