package javafxapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Eleve {

    private final StringProperty id;
    private final StringProperty matricule;
    private final StringProperty prenom;
    private final StringProperty nom;
    private final StringProperty age;

    public Eleve() {
        id = new SimpleStringProperty(this, "id");
        matricule = new SimpleStringProperty(this, "matricule");
        prenom = new SimpleStringProperty(this, "prenom");
        nom = new SimpleStringProperty(this, "nom");
        age = new SimpleStringProperty(this, "age");
    }

    // ID
    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String newId) { id.set(newId); }

    // Matricule
    public StringProperty matriculeProperty() { return matricule; }
    public String getMatricule() { return matricule.get(); }
    public void setMatricule(String newMatricule) { matricule.set(newMatricule); }

    // Prénom
    public StringProperty prenomProperty() { return prenom; }
    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String newPrenom) { prenom.set(newPrenom); }

    // Nom
    public StringProperty nomProperty() { return nom; }
    public String getNom() { return nom.get(); }
    public void setNom(String newNom) { nom.set(newNom); }

    // Âge
    public StringProperty ageProperty() { return age; }
    public String getAge() { return age.get(); }
    public void setAge(String newAge) { age.set(newAge); }
}
