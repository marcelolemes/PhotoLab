package br.com.login.Dao;

import br.com.login.model.Album;
import br.com.login.model.Contrato;
import br.com.login.model.User;
import br.com.login.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by marcelo on 27/09/2014.
 */
public class RegraMontagemDao implements Serializable {

    public Album NovoAlbum() throws Exception {

        Contrato contrato = contratoAtual();
        if (contrato!= null) {
            Session sessao = HibernateUtil.getSession();
            org.hibernate.Transaction transacao = sessao.beginTransaction();
            Criteria criteria = sessao.createCriteria(Album.class);
            criteria.add(Restrictions.eq("contrato", contrato));
            criteria.add(Restrictions.eq("ocupado", false));
            criteria.add(Restrictions.ge("status", 10));
            criteria.add(Restrictions.le("status", 12));
            criteria.addOrder(Order.asc("numero"));
            criteria.setMaxResults(1);
            Album retorno = (Album) criteria.uniqueResult();
            if (retorno == null) {
                contratoPronto(contrato);
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Contrato terminado",
                                "Contrato "+contrato.getNumeroContrato()+" terminado, tente pegar album novamente para iniciar um novo contrato disponível"));
            }
            else {

                retorno.setStatus(13);
                retorno.setOcupado(true);
                sessao.update(retorno);
                transacao.commit();
            }
            try {
                sessao.close();

            }
            catch (Exception e){}
            return retorno;
        }
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem contratos para o seu setor",
                        "Sem contratos para o seu setor, informe o seu superior imediatamente!"));
        return null;
    }

    public Album NovoAlbum(User user) throws Exception { //sobrecarga com "USER"

        Contrato contrato = contratoAtual();
        if (contrato!= null) {
            Session sessao = HibernateUtil.getSession();
            org.hibernate.Transaction transacao = sessao.beginTransaction();
            Criteria criteria = sessao.createCriteria(Album.class);
            criteria.add(Restrictions.eq("contrato", contrato));
            criteria.add(Restrictions.eq("ocupado", false));
            criteria.add(Restrictions.ge("status", 10));
            criteria.add(Restrictions.le("status", 12));
            criteria.addOrder(Order.asc("numero"));
            criteria.setMaxResults(1);
            Album retorno = (Album) criteria.uniqueResult();
            if (retorno == null) {
                contratoPronto(contrato);
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Contrato terminado",
                                "Contrato "+contrato.getNumeroContrato()+" terminado, tente pegar album novamente para iniciar um novo contrato disponível"));
            }
            else {
                if (user != null){
                    retorno.setUserMontagem(user);
                }

                retorno.setStatus(13);
                retorno.setOcupado(true);
                sessao.update(retorno);
                transacao.commit();
            }
            try {
                sessao.close();

            }
            catch (Exception e){}
            return retorno;
        }
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem contratos para o seu setor",
                        "Sem contratos para o seu setor, informe o seu superior imediatamente!"));
        return null;
    }

    public void albumTerminado(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        if (album!=null){
            album.setStatus(14);
            album.setOcupado(false);
        }
        sessao.update(album);
        transacao.commit();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Album "+album.getNumero()+" encerrado" ,
                        "Pegue outro album para continuar trabalhando"));
        try {
            sessao.close();
        }
        catch (Exception e) {
        }

    }

    public void albumCancelado(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        if (album!=null){
            album.setStatus(12);
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
        }
        catch (Exception e) {
        }

    }

    public Contrato contratoAtual() throws Exception {

        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        Criteria criteria = sessao.createCriteria(Contrato.class);
        criteria.add(Restrictions.ge("status",10));
        criteria.add(Restrictions.le("status", 13));
        criteria.addOrder(Order.asc("urgencia")).addOrder(Order.asc("status")).addOrder(Order.asc("cod"));
        criteria.setMaxResults(1);
        Contrato retorno = (Contrato) criteria.uniqueResult();
        if (retorno != null ) {
            if (retorno.getUrgencia() > 0) {
                retorno.setUrgencia(0);
            }
            if (retorno.getStatus()>10 ||retorno.getStatus() <13){
                retorno.setStatus(13);
            }
            sessao.update(retorno);
            transacao.commit();
        }
        else {

        }
        sessao.close();
        return retorno;
    }
    public void contratoPronto(Contrato contrato) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        Criteria criteria = sessao.createCriteria(Album.class).setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("ocupado", true));
        criteria.add(Restrictions.eq("status", 13));

        long cont = (Long) criteria.uniqueResult();
        if(cont == 0) {
            try {
                contrato.setStatus(14);
                contrato.setUrgencia(4);
                sessao.update(contrato);
                transacao.commit();
                sessao.close();

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Contrato encerrado" ,
                                "Informe ao seu superior"));
            }
            catch (Exception e){

            }


        }
        else {
            System.out.println(""+cont);
            try{
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Ainda não é possível encerrar esse contrato" ,
                                "Funcionários ainda estão nesse contrato"));

                contrato.setUrgencia(4);
                sessao.clear();
                sessao.update(contrato);
                transacao.commit();
                sessao.close();
            }
            catch (Exception e){
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Algo deu errado" ,
                                "Tente outra vez"));
            }
        }

    }
}