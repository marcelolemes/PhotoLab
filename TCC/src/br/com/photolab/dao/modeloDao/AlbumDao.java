package br.com.photolab.dao.modeloDao;

import br.com.photolab.modelo.Album;
import br.com.photolab.modelo.Contrato;
import br.com.photolab.modelo.Usuario;
import br.com.photolab.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ViewScoped
public class AlbumDao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public boolean gravar(Album album) throws Exception {
        Session sessao = HibernateUtil.getSession();
        org.hibernate.Transaction transacao = sessao.beginTransaction();
        album.setStatus(album.getContrato().getStatus());
        sessao.saveOrUpdate(album);
        transacao.commit();
        sessao.close();
        return true;

    }


    public boolean GravarLista(List<Album> albuns,Contrato contrato) throws Exception {
        Session sessao = HibernateUtil.getSession();
        sessao.beginTransaction();
        System.out.println("Transaçãoo iniciada");
        for (Album album : albuns) {
            sessao.save(album);
            System.out.println("Album persistido");
        }
        contrato.setQtdAlbum(albuns.size());
        try {
            sessao.update(contrato);
        }
        catch (Exception ex)
        {

        }

        sessao.getTransaction().commit();
        sessao.close();
        return true;

    }

    public List<Album> ListarAlbuns() throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class);
        List<Album> listaRetorno = criteria.list();
        sessao.close();
        return listaRetorno;
    }

    public List<Album> ListarAlbunsFuncTratamento(Usuario usuario) throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class);
        criteria.add(Restrictions.eq("userTratamento", usuario));
        List<Album> listaRetorno = criteria.list();
        sessao.close();
        return listaRetorno;
    }
    public List<Album> ListarAlbunsFuncMontagem(Usuario usuario) throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class);
        criteria.add(Restrictions.eq("userMontagem", usuario));
        List<Album> listaRetorno = criteria.list();
        sessao.close();
        return listaRetorno;
    }

    public List<Album> ListarAlbunsContrato(Contrato contrato) throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class);
        criteria.add(Restrictions.eq("contrato", contrato));
        System.out.println("Contrato aqui: "+contrato.getNumeroContrato());
        List<Album> listaRetorno = criteria.list();
        sessao.close();
        return listaRetorno;
    }
    public long ContarAlbunsContrato(Contrato contrato) throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class).setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("contrato", contrato));
        //System.out.println("Contrato aqui: "+contrato.getNumeroContrato());
        long retorno = (Long) criteria.uniqueResult();
        sessao.close();
        return retorno;
    }
    public long ContarFotosContrato(Contrato contrato) throws Exception {
        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class).setProjection(Projections.sum("qtdFotos"));
        criteria.add(Restrictions.eq("contrato", contrato));
        //System.out.println("Contrato aqui: "+contrato.getNumeroContrato());
        long retorno = (Long) criteria.uniqueResult();
        sessao.close();
        return retorno;
    }


    public long AlbunsRestantesMontagem(Contrato contrato) throws Exception {

        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class).setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("contrato",contrato));
        System.out.println("Contrato para contagem : "+contrato.getNumeroContrato());
        criteria.add(Restrictions.le("status",12)).add(Restrictions.ge("status",10));
        long retorno = (Long) criteria.uniqueResult();
        System.out.println("Contagem restantes: "+retorno);

        sessao.close();
        return retorno;
    }

    public Album ProximoAlbum(Contrato contrato,int min, int max) throws Exception {

        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class).addOrder(Order.asc("numero"));
        criteria.add(Restrictions.eq("contrato",contrato));
        System.out.println("Contrato para contagem2 : "+contrato.getNumeroContrato());
        criteria.add(Restrictions.le("status",max)).add(Restrictions.ge("status",min)).add(Restrictions.eq("ocupado",false));
        criteria.setMaxResults(1);
        Album retorno = (Album) criteria.uniqueResult();
        sessao.close();
        return retorno;
    }

    public long AlbunsRestantesTratamento(Contrato contrato) throws Exception {

        Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Album.class).setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("contrato",contrato));
        System.out.println("Contrato para contagem : "+contrato.getNumeroContrato());
        criteria.add(Restrictions.le("status",10)).add(Restrictions.ge("status",5));
        long retorno = (Long) criteria.uniqueResult();
        System.out.println("Contagem restantes: "+retorno);

        sessao.close();
        return retorno;


    }


}
