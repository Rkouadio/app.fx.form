/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "commandes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commandes.findAll", query = "SELECT c FROM Commandes c")
    , @NamedQuery(name = "Commandes.findByLibelle", query = "SELECT c FROM Commandes c WHERE c.libelle = :libelle")
    , @NamedQuery(name = "Commandes.findByDate", query = "SELECT c FROM Commandes c WHERE c.date = :date")
    , @NamedQuery(name = "Commandes.findByProprietaire", query = "SELECT c FROM Commandes c WHERE c.proprietaire = :proprietaire")
    , @NamedQuery(name = "Commandes.findByQuantite", query = "SELECT c FROM Commandes c WHERE c.quantite = :quantite")
    , @NamedQuery(name = "Commandes.findById", query = "SELECT c FROM Commandes c WHERE c.id = :id")})
public class Commandes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "proprietaire")
    private String proprietaire;
    @Column(name = "quantite")
    private Integer quantite;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "categories", referencedColumnName = "id")
    @ManyToOne
    private Categories categories;
    @OneToMany(mappedBy = "commandes")
    private Collection<Operations> operationsCollection;

    public Commandes() {
    }

    public Commandes(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    @XmlTransient
    public Collection<Operations> getOperationsCollection() {
        return operationsCollection;
    }

    public void setOperationsCollection(Collection<Operations> operationsCollection) {
        this.operationsCollection = operationsCollection;
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
        if (!(object instanceof Commandes)) {
            return false;
        }
        Commandes other = (Commandes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Commandes[ id=" + id + " ]";
    }
    
}
