/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.fx;

import controller.jpa.CategoriesJpaController;
import controller.jpa.CommandesJpaController;
import controller.jpa.exceptions.NonexistentEntityException;
import entities.Categories;
import entities.Commandes;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.commandeBean;

/**
 * FXML Controller class
 *
 * @author hp
 */
public final class CommandesController implements Initializable {

    @FXML
    private TextField libelle;

    @FXML
    private TextField quantite;

    @FXML
    private Button enregistrer;

    @FXML
    private ComboBox<Categories> categorie;

    @FXML
    private Label message;

    @FXML
    private Button fermer;

    @FXML
    private TableView<commandeBean> table;

    @FXML
    private TableColumn<commandeBean,Integer> table_numero;

    @FXML
    private TableColumn<commandeBean, String> table_libelle;

    @FXML
    private TableColumn<commandeBean, String> table_categorie;

    @FXML
    private TableColumn<commandeBean, Integer> table_quantite;

    @FXML
    private TableColumn<commandeBean, String> table_delete;

    @FXML
    private TableColumn<commandeBean, String> table_modif;
    
    private CategoriesJpaController cat_ctr;
    private CommandesJpaController com_ctr;
    
    private EntityManagerFactory emf;
    
    //observable list pour prendre toute les commnandes
    private  ObservableList<Commandes> listeDesCommandes;
    private  ObservableList<commandeBean> listeCommandeBean;
    
    /**
     * Initializes the controller class.
     */
    
    //constructeur le premier a se demarrer
    public CommandesController() {
        emf=Persistence.createEntityManagerFactory("formFxPU");
        com_ctr= new CommandesJpaController(emf);
        cat_ctr= new CategoriesJpaController(emf);
        
        listeDesCommandes=retournerListeDesCommandes();
        listeCommandeBean=retournerlisteCommandeBean();
      
        //System.out.println("nombre d'elements : "+listeDesCommandes.size());
        //System.out.println("nombre d'elements bean: "+listeCommandeBean.size());
    }    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //methode 1
        /*enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Btn Cliquer");
            }
        }); */
       
        initilaiserListCategories();
        initialiserTable();
        //Methode 2
         enregistrer.setOnAction(e->{
              enregisterCommandes();
         });
         
         fermer.setOnAction(e->{
              quitterFenetre(fermer);
         });
         
    }    
    //Methode Ajoutant une commande 
     public void enregisterCommandes()
        {
            String libele= this.libelle.getText();
            String qtite=  this.quantite.getText();
            Categories categor=this.categorie.getValue();
            //verification des differents champs si vide on affiche message
            if(libele.isEmpty() || qtite.isEmpty() || categorie==null){
                message.setStyle("-fx-text-fill : red");
                message.setText("Veillez remplir tout les champs");
            } else {
            //insertion
            Commandes temp_commande=new Commandes();
            temp_commande.setLibelle(libele);
            temp_commande.setQuantite(Integer.parseInt(qtite));
            temp_commande.setProprietaire("Roland");
            temp_commande.setDate(new Date());
            temp_commande.setCategories(categor);
            
            com_ctr.create(temp_commande);
             message.setStyle("-fx-text-fill : red");
             message.setText("Insertion Reussi");
             
             ajouterNouvelleCommande(temp_commande);
             reiniatiliserComposante();
            }
            
        }
     //methode pour charger toutes les categories
     public ObservableList<Categories> listCategories()
     {
        List<Categories> list=cat_ctr.findCategoriesEntities();
         return FXCollections.observableArrayList(list);
     }
   
     public void initilaiserListCategories()
     {
        contenuClassePresenter();
     //categorie.getItems().addAll(listCategories());
     categorie.getItems().clear();
     categorie.setItems(listCategories());
     }
     
         public void contenuClassePresenter()
    {
     // categorie.setItems(FXCollections.observableList(JpaCtrClasse.findClasseEntities()));
          categorie.getSelectionModel().selectFirst();
          categorie.setCellFactory(new Callback<ListView<Categories>,ListCell<Categories>>(){
            @Override
            public ListCell<Categories> call(ListView<Categories> l){
                return new ListCell<Categories>(){
                    @Override
                    protected void updateItem(Categories item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                         setText(item.getLibelle());
                        }
                    }
                } ;
            }
         });
           categorie.setConverter(new StringConverter<Categories>() {
              @Override
              public String toString(Categories user) {
                if (user == null){
                  return null;
                } else {
                  return user.getLibelle();
                }
              }
            @Override
            public Categories fromString(String userId) {
                return null;
            }
        });
        
    }
         
    public void reiniatiliserComposante(){
    message.setText("");
    quantite.setText("");
    libelle.setText("");
    initilaiserListCategories();
    }

    public ObservableList<Commandes> retournerListeDesCommandes()
    {
      List<Commandes> list=com_ctr.findCommandesEntities();
         return FXCollections.observableArrayList(list);
    }
    
    public ObservableList<commandeBean> retournerlisteCommandeBean()
    {
      List<commandeBean> list=new ArrayList<> ();
      commandeBean cmdBean;
      int compteur=0;
      for (Commandes commandesEnCour : listeDesCommandes) {
          compteur++;
            cmdBean = new commandeBean();
            cmdBean.setNumero(compteur);
            cmdBean.setLibelle(commandesEnCour.getLibelle());
            cmdBean.setDate(commandesEnCour.getDate());
            cmdBean.setCategorie(commandesEnCour.getCategories().getLibelle());
            cmdBean.setProprietaire(commandesEnCour.getProprietaire());
            cmdBean.setQuantite(commandesEnCour.getQuantite());
            cmdBean.setCategorieObject(commandesEnCour.getCategories());
            cmdBean.setCommande(commandesEnCour);
                
            list.add(cmdBean);
        }
        return FXCollections.observableArrayList(list);
    }
    
    public void initialiserTable() 
    {
        
      table_numero.setCellValueFactory(new PropertyValueFactory<>("numero")); 
      table_libelle.setCellValueFactory(new PropertyValueFactory<>("libelle")); 
      table_categorie.setCellValueFactory(new PropertyValueFactory<>("categorie")); 
      table_delete.setCellValueFactory(new PropertyValueFactory<>("proprietaire"));
      table_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite")); 
      
      placerBouton();
      table.setItems(listeCommandeBean);
        
    }
    
    public void ajouterNouvelleCommande(Commandes commande)
        {
          commandeBean cmd=new commandeBean();
          
          cmd.setNumero(listeCommandeBean.size()+1);
          cmd.setCategorie(commande.getCategories().getLibelle());
          cmd.setProprietaire(commande.getProprietaire());
          cmd.setCategorieObject(commande.getCategories());
          cmd.setLibelle(commande.getLibelle());
          cmd.setDate(commande.getDate());
          cmd.setQuantite(commande.getQuantite());
          cmd.setCommande(commande);
          
          listeCommandeBean.add(cmd);
        }
    
    public static void quitterFenetre(Button quitte)
     {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("fermer fenetre alerte");
            alert.setHeaderText("Confirmer la fermerture \n");
            alert.setContentText(""
                    + "VOULEZ-VOUS VRAIMENT QUITTEZ ?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();
       if (alert.getResult() == ButtonType.YES) {
           Stage stage = (Stage) quitte.getScene().getWindow();
    // do what you have to do
               stage.close();
        }                
     }
    
     //Methode supprimer 
     public void placerBouton()
    {
     table_modif.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
     
      Callback<TableColumn<commandeBean, String>, TableCell<commandeBean, String>> cellFactory = new Callback<TableColumn<commandeBean, String>, TableCell<commandeBean, String>>() {      
                    @Override
            public TableCell call(final TableColumn<commandeBean, String> param) {
                final TableCell<commandeBean, String> cell = new TableCell<commandeBean, String>() {

                    final Button btn = new Button("");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
//                         
                            btn.setOnAction(e->{
                           commandeBean element = getTableView().getItems().get(getIndex());
                            Commandes Etu=element.getCommande();
                                try {
                                    com_ctr.destroy(Etu.getId());
                                    listeCommandeBean.remove(getIndex());
                                    
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(CommandesController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                                   Image image = new Image(
                    getClass().getResourceAsStream("/images/del.png")
            );
                   btn.setGraphic(new ImageView(image));
                            setGraphic(btn);
                            
                            setGraphic(btn);
                            
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
         table_modif.setCellFactory(cellFactory);
    }
    
}
