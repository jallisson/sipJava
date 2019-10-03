/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.bean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author TI
 */
@Entity
@Table(name = "nivel_acesso",schema = "")
@NamedQueries({
    @NamedQuery(name = "NivelAcesso.findAll", query = "SELECT n FROM NivelAcesso n"),
    @NamedQuery(name = "NivelAcesso.findById", query = "SELECT n FROM NivelAcesso n WHERE n.id = :id"),
    @NamedQuery(name = "NivelAcesso.findByNomeModulo", query = "SELECT n FROM NivelAcesso n WHERE n.nomeModulo = :nomeModulo"),
    @NamedQuery(name = "NivelAcesso.findByIdUsuario", query = "SELECT n FROM NivelAcesso n WHERE n.idUsuario = :idUsuario"),
    @NamedQuery(name = "NivelAcesso.verifiqueAcesso", query = "SELECT n FROM NivelAcesso n WHERE n.idUsuario = :idUsuario and n.nomeModulo = :nomeModulo")
})
public class NivelAcesso implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    
    
    @Id 
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(generator="s_id")
    @SequenceGenerator(name="s_id",sequenceName="ID",allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOME_MODULO")
    private String nomeModulo;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private int idUsuario;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private Usuario idUsuario2;

  

    public NivelAcesso() {
    }

    public NivelAcesso(Integer id) {
        this.id = id;
    }

    public NivelAcesso(Integer id, String nomeModulo, int idUsuario) {
        this.id = id;
        this.nomeModulo = nomeModulo;
        this.idUsuario = idUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getNomeModulo() {
        return nomeModulo;
    }

    public void setNomeModulo(String nomeModulo) {
        String oldNomeModulo = this.nomeModulo;
        this.nomeModulo = nomeModulo;
        changeSupport.firePropertyChange("nomeModulo", oldNomeModulo, nomeModulo);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        int oldIdUsuario = this.idUsuario;
        this.idUsuario = idUsuario;
        changeSupport.firePropertyChange("idUsuario", oldIdUsuario, idUsuario);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelAcesso)) {
            return false;
        }
        NivelAcesso other = (NivelAcesso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.t2ti.javastarter.utilitarios.NivelAcesso[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public Usuario getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(Usuario idUsuario2) {
        Usuario oldIdUsuario2 = this.idUsuario2;
        this.idUsuario2 = idUsuario2;
        changeSupport.firePropertyChange("idUsuario2", oldIdUsuario2, idUsuario2);
    }

 
    /**
     * @return the monitor2
     */
    
}
