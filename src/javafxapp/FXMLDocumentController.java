/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package javafxapp;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static java.awt.SystemColor.text;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Lenovo
 */
public class FXMLDocumentController implements Initializable {
  

   @FXML private TableView<Eleve> table;
@FXML private TableColumn<Eleve, String> IDColmn, MatriculeColmn, PrenomColmn, NomColmn, AgeColmn;
@FXML private TextField txtMatricule, txtprenom, txtNom, txtAge;

Connection conn;
PreparedStatement pst;
int myindex;
int id;

public void Connect() {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/registration", "root", "");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void table() {
    Connect();
    ObservableList<Eleve> eleves = FXCollections.observableArrayList();

    try {
        pst = conn.prepareStatement("SELECT id, matricule, prenom, nom, age FROM Eleve");
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Eleve st = new Eleve();
            st.setId(rs.getString("id"));
            st.setMatricule(rs.getString("matricule"));
            st.setPrenom(rs.getString("prenom"));
            st.setNom(rs.getString("nom"));
            st.setAge(rs.getString("age"));
            eleves.add(st);
        }

        table.setItems(eleves);
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty());
        MatriculeColmn.setCellValueFactory(f -> f.getValue().matriculeProperty());
        PrenomColmn.setCellValueFactory(f -> f.getValue().prenomProperty());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        AgeColmn.setCellValueFactory(f -> f.getValue().ageProperty());

        table.setRowFactory(tv -> {
            TableRow<Eleve> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    myindex = table.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(table.getItems().get(myindex).getId());
                    txtMatricule.setText(table.getItems().get(myindex).getMatricule());
                    txtprenom.setText(table.getItems().get(myindex).getPrenom());
                    txtNom.setText(table.getItems().get(myindex).getNom());
                    txtAge.setText(table.getItems().get(myindex).getAge());
                }
            });
            return row;
        });

    } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

@FXML
void Add(ActionEvent event) {
    String matricule = txtMatricule.getText();
    String prenom = txtprenom.getText();
    String nom = txtNom.getText();
    String age = txtAge.getText();

    try {
        pst = conn.prepareStatement("INSERT INTO Eleve(matricule, prenom, nom, age) VALUES (?, ?, ?, ?)");
        pst.setString(1, matricule);
        pst.setString(2, prenom);
        pst.setString(3, nom);
        pst.setString(4, age);
        pst.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout");
        alert.setHeaderText("Ajout avec succès !");
        alert.showAndWait();

        table(); // recharger la table
        clearFields();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

@FXML
void Update(ActionEvent event) {
    String matricule = txtMatricule.getText();
    String prenom = txtprenom.getText();
    String nom = txtNom.getText();
    String age = txtAge.getText();

    try {
        pst = conn.prepareStatement("UPDATE Eleve SET matricule = ?, prenom = ?, nom = ?, age = ? WHERE id = ?");
        pst.setString(1, matricule);
        pst.setString(2, prenom);
        pst.setString(3, nom);
        pst.setString(4, age);
        pst.setInt(5, id);
        pst.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mise à jour");
        alert.setHeaderText("Modification avec succès !");
        alert.showAndWait();

        table(); // recharger la table
        clearFields();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

@FXML
void Delete(ActionEvent event) {
    try {
        pst = conn.prepareStatement("DELETE FROM Eleve WHERE id = ?");
        pst.setInt(1, id);
        pst.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Suppression réussie !");
        alert.showAndWait();

        table(); // recharger la table
        clearFields();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void clearFields() {
    txtMatricule.clear();
    txtprenom.clear();
    txtNom.clear();
    txtAge.clear();
}

  @FXML
    void PrintEXCEL(ActionEvent event) throws IOException {
exporterListeEleve();

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Export Excel");
    alert.setHeaderText(null);
    alert.setContentText("La liste des élèves a été exportée avec succès !");
    alert.showAndWait();
    java.awt.Desktop.getDesktop().open(new java.io.File("ListeEleves.xlsx"));
    }

    @FXML
    void PrintHTML(ActionEvent event) {
exporterListeElevesEnHTML(table, "liste_eleves.html");

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Export HTML");
    alert.setHeaderText(null);
    alert.setContentText("La liste a été exportée en HTML et ouverte dans le navigateur.");
    alert.showAndWait();
    }

    @FXML
    void PrintPDF(ActionEvent event) throws IOException {
     exporterListeEleves(table, "ListeEleves.pdf");

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Export PDF");
    alert.setHeaderText(null);
    alert.setContentText("La liste a été exportée avec succès !");
    alert.showAndWait();
    java.awt.Desktop.getDesktop().open(new java.io.File("ListeEleves.pdf"));
    }
    //impression en pdf
public void exporterListeEleves(TableView<Eleve> table, String fichierPdf) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fichierPdf));
            document.open();

            // Titre
            Paragraph titre = new Paragraph("Liste des Élèves",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(Chunk.NEWLINE);

            // Création du tableau
            PdfPTable pdfTable = new PdfPTable(5); // 5 colonnes : id, matricule, prénom, nom, âge
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10f);

            // En-têtes
            String[] headers = {"ID", "Matricule", "Prénom", "Nom", "Âge"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            // Données
            ObservableList<Eleve> eleves = table.getItems();
            for (Eleve e : eleves) {
                pdfTable.addCell(e.getId());
                pdfTable.addCell(e.getMatricule());
                pdfTable.addCell(e.getPrenom());
                pdfTable.addCell(e.getNom());
                pdfTable.addCell(e.getAge());
            }

            document.add(pdfTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

/* public void exporterListeEleve(TableView<Eleve> table, String nomFichier) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Élèves");

        // Style de l'entête
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Entête
        Row headerRow = sheet.createRow(0);
        String[] colonnes = {"ID", "Matricule", "Prénom", "Nom", "Âge"};

        for (int i = 0; i < colonnes.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(colonnes[i]);
            cell.setCellStyle(headerStyle);
        }

        // Données
        ObservableList<Eleve> eleves = table.getItems();
        int rowNum = 1;

        for (Eleve e : eleves) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getId());
            row.createCell(1).setCellValue(e.getMatricule());
            row.createCell(2).setCellValue(e.getPrenom());
            row.createCell(3).setCellValue(e.getNom());
            row.createCell(4).setCellValue(e.getAge());
        }

        // Redimensionner les colonnes
        for (int i = 0; i < colonnes.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Sauvegarde dans un fichier
        try (FileOutputStream fileOut = new FileOutputStream(nomFichier)) {
            workbook.write(fileOut);
            workbook.close();

            System.out.println("Fichier Excel exporté : " + nomFichier);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
   */ 
//impression en EXCEL
public void exporterListeEleve() {
    // Données à exporter (ex: vos données depuis TableView)
    ObservableList<Eleve> liste = table.getItems();

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Liste des élèves");

    // En-têtes
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("ID");
    header.createCell(1).setCellValue("Matricule");
    header.createCell(2).setCellValue("Prénom");
    header.createCell(3).setCellValue("Nom");
    header.createCell(4).setCellValue("Âge");

    // Données
    int rowIndex = 1;
    for (Eleve eleve : liste) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(eleve.getId());
        row.createCell(1).setCellValue(eleve.getMatricule());
        row.createCell(2).setCellValue(eleve.getPrenom());
        row.createCell(3).setCellValue(eleve.getNom());
        row.createCell(4).setCellValue(eleve.getAge());
    }

    // Ajuster la largeur des colonnes automatiquement
    for (int i = 0; i <= 4; i++) {
        sheet.autoSizeColumn(i);
    }

    // Sauvegarde sur le disque
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         FileOutputStream fileOut = new FileOutputStream("Liste_Eleves.xlsx")) {

        workbook.write(out);
        fileOut.write(out.toByteArray());
        workbook.close();

        System.out.println("✅ Fichier Excel généré avec succès : Liste_Eleves.xlsx");

    } catch (IOException e) {
        e.printStackTrace();
    }
}
//impression en HTML
 public void exporterListeElevesEnHTML(TableView<Eleve> table, String cheminFichier) {
        ObservableList<Eleve> eleves = table.getItems();

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<title>Liste des Élèves</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #000; padding: 8px; text-align: center; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("button { padding: 10px 20px; font-size: 16px; margin-top: 20px; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<h2 style='text-align:center;'>Liste des Élèves</h2>");
        html.append("<button onclick='window.print()'>Imprimer</button>");
        html.append("<table>");
        html.append("<tr><th>ID</th><th>Matricule</th><th>Prénom</th><th>Nom</th><th>Âge</th></tr>");

        for (Eleve e : eleves) {
            html.append("<tr>")
                .append("<td>").append(e.getId()).append("</td>")
                .append("<td>").append(e.getMatricule()).append("</td>")
                .append("<td>").append(e.getPrenom()).append("</td>")
                .append("<td>").append(e.getNom()).append("</td>")
                .append("<td>").append(e.getAge()).append("</td>")
                .append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            writer.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ouvrir automatiquement dans le navigateur
        try {
            File file = new File(cheminFichier);
            java.awt.Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        table();
    }    
    
}
