/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Categories;
import entities.Commandes;
import java.util.Date;

/**
 *
 * @author hp
 */
public class commandeBean {
    private Integer numero;
    private String libelle;
    private Integer Quantite;
    private Date  date;
    private String categorie;
    private String proprietaire;
    
    private Commandes commande;
    private Categories categorieObject;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getQuantite() {
        return Quantite;
    }

    public void setQuantite(Integer Quantite) {
        this.Quantite = Quantite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Commandes getCommande() {
        return commande;
    }

    public void setCommande(Commandes commande) {
        this.commande = commande;
    }

    public Categories getCategorieObject() {
        return categorieObject;
    }

    public void setCategorieObject(Categories categorieObject) {
        this.categorieObject = categorieObject;
    }
    
    
    
}
