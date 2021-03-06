package br.com.photolab.dao.modeloDao;

import br.com.photolab.modelo.Ficha;
import br.com.photolab.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ViewScoped
public class FichaDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean Gravar(Ficha ficha) throws Exception {

		Session sessao = HibernateUtil.getSession();
		org.hibernate.Transaction transacao = sessao.beginTransaction();
		sessao.saveOrUpdate(ficha);
		transacao.commit();
		sessao.close();
		return true;
	}

	public List<Ficha> ListarFichas() throws Exception {
		Session sessao = HibernateUtil.getSession();
        Criteria criteria = sessao.createCriteria(Ficha.class);
        criteria.addOrder(Order.asc("ano"));
        criteria.addOrder(Order.desc("numero"));
		List<Ficha> listaRetorno = criteria.list();
		sessao.close();
		return listaRetorno;
	}
}
