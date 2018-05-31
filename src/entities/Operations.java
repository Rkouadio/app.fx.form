/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "operations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operations.findAll", query = "SELECT o FROM Operations o")
    , @NamedQuery(name = "Operations.findByQuantite", query = "SELECT o FROM Operations o WHERE o.quantite = :quantite")
    , @NamedQuery(name = "Operations.findByDate", query = "SELECT o FROM Operations o WHERE o.date = :date")
    , @NamedQuery(name = "Operations.findByDerniereModif", query = "SELECT o FROM Operations o WHERE o.derniereModif = :derniereModif")
    , @NamedQuery(name = "Operations.findById", query = "SELECT o FROM Operations o WHERE o.id = :id")})
public class Operations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "quantite")
    private Integer quantite;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "commandes", referencedColumnName = "id")
    @ManyToOne
    private Commandes commandes;

    public Operations() {
    }

    public Operations(Integer id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDerniereModif() {
        return derniereModif;
    }

    public void setDerniereModif(Date derniereModif) {
        this.derniereModif = derniereModif;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Commandes getCommandes() {
        return commandes;
    }

    public void setCommandes(Commandes commandes) {
        this.commandes = commandes;
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
        if (!(object instanceof Operations)) {
            return false;
        }
        Operations other = (Operations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Operations[ id=" + id + " ]";
    }
    
}
