package br.com.photolab.regra;

import br.com.photolab.bean.usuarios.usuario.UsuarioBean;
import br.com.photolab.dao.modelo.AlbumDao;
import br.com.photolab.dao.modelo.ContratoDao;
import br.com.photolab.dao.modelo.UsuarioDao;
import br.com.photolab.dao.regra.RegraImpressaoDao;
import br.com.photolab.dao.regra.RegraMontagemDao;
import br.com.photolab.dao.relatorio.RelatorioDao;
import br.com.photolab.dao.relatorio.RelatorioDiarioDao;
import br.com.photolab.modelo.Album;
import br.com.photolab.modelo.Contrato;
import br.com.photolab.relatorio.Relatorio;
import br.com.photolab.relatorio.RelatorioDiario;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by marcelo on 26/09/2014.
 */
@ManagedBean
@ViewScoped
public class RegraImpressao implements Serializable {
    private RegraImpressaoDao regDao = new RegraImpressaoDao();
    AlbumDao albumDao = new AlbumDao();
    ContratoDao contDao = new ContratoDao();
    private String cont;
    private List<Album> listaAlbuns=new ArrayList<Album>();
    @PostConstruct
    public void iniciar(){

        try {
            listaAlbuns = albumDao.ListarAlbunsContrato(contDao.listarContratoStatus(14,15));
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void btImpresso(Album album) throws Exception {

        album.setQtdFotos(qtdFotosAtual(album.getContrato().getCaminho()+File.separator+album.getNumero()));
        regDao.albumImpresso(album);

    }

    public int qtdFotosAtual(String caminho){
        try {
            File file = new File(caminho);
            return file.listFiles(fotoFilter).length;
        }
        catch (Exception e){
            return 0;
        }

    }

    FilenameFilter fotoFilter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            String lowercaseName = name.toLowerCase();
            if (lowercaseName.endsWith(".jpg")) {
                return true;
            } else {
                return false;
            }

        }
    };

    public List<Album> getListaAlbuns() {
        return listaAlbuns;
    }

    public void setListaAlbuns(List<Album> listaAlbuns) {
        this.listaAlbuns = listaAlbuns;
    }
}
