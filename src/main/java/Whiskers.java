import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
public class Whiskers  extends Application{
	public static String version_num = "1.5";
	private static int CHOSEN_SHEET = 7;
	private static int MAX_HALL = 18;
	private static final boolean showLog = false;
	private static final boolean printLog = true;
	public static final String MOVIE = "Movie";
	public static final String EVENT = "Event";
	public static final String NO_BREAK = "No intermission";
	public static String log_name = "WeeklyProg.log";
	public static File f = new File(log_name);
	public static BufferedWriter bw = null;
	public static FileWriter fw = null;

	private static int line_num=0;
	private static Database db;

	private Button load_file_btn = new Button();
	private Button choose_file_btn = new Button();
	private Button print_to_file_btn = new Button();
	private Button remove_btn = new Button();
	private TextField file_text = new TextField();
	private Label credits = new Label();
	private Label warning = new Label();
	private Label choose_sheet = new Label();
	private ChoiceBox<Integer> choiceBox = new ChoiceBox<Integer>();

	private Label choose_date = new Label();
	private ChoiceBox<String> choiceBoxDates = new ChoiceBox<String>();

	public void updateChoiceBoxMenu()
	{
		ObservableList<String> list=null;
		list = db.getProgramList();
		choiceBoxDates.setItems(list);
	}
	public static void main(String[] args) {

		System.out.println("Running with encoding " + java.nio.charset.Charset.defaultCharset() + "\nRun with parameter: -Dfile.encoding=UTF-8 if any problem occurs with charset");
		
		db = new Database();
		db.fillDatabaseFromFile();
		try {
			if (f.exists())
				f.delete();
			f.createNewFile();



			fw = new FileWriter(log_name, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		launch(args);

		try {
			if (bw != null)
				bw.close();
			if (fw != null)
				fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(db);
		writeStringToFile("Programming files in database\n\n" + db.toString(),"Whiskers_database_info.txt", false);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Whiskers - Weekly Programming Manager " + version_num);
		load_file_btn.setText("Load file");
		choose_file_btn.setText("Choose file");
		choose_sheet.setText("Choose Sheet to load:\r\n(7 should be Kfar Saba)");
		credits.setText("Creator - Eden Dupont");
		warning.setText("Note - Always verify the file against the original excel file\nFor accuracy only add programming files that belong to same branch\n\r\n\r\nEnjoy!");
		print_to_file_btn.setText("Print program");
		remove_btn.setText("Remove programming");
		print_to_file_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String key = choiceBoxDates.getValue();
				Programming p = db.getProgramming(key);
				if(p!=null)
					writeStringToFile(db.printPrettyProgram(p),"Whisked_" + p.getFilename() +"_sheet_" + p.getSheet_num() + ".txt", true);

			}
		});

		remove_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String key = choiceBoxDates.getValue();
				db.removeProgram(key);
				db.saveToFile();
				updateChoiceBoxMenu();
			}
		});
		file_text.setEditable(false);
		updateChoiceBoxMenu();
		choose_date.setText("Choose Programming:");

		load_file_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String s = file_text.getText();
				if(!s.contentEquals(""))
				{
					CHOSEN_SHEET = choiceBox.getValue();
					Programming p;
					try {
						p = new Programming(s);
						db.addProgramming(p);
						db.saveToFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					updateChoiceBoxMenu();
				}
			}
		});
		choose_file_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				String s = Paths.get(".").toAbsolutePath().normalize().toString();
				fileChooser.getExtensionFilters().addAll(
						new ExtensionFilter("Excel Files)))", "*.xls", "*.xlsx")
						);
				fileChooser.setInitialDirectory(new File(s));
				fileChooser.setTitle("Open Resource File");
				File f = fileChooser.showOpenDialog(primaryStage);
				if(f!=null)
				{
					Workbook workbook=null;
					FileInputStream inputStream = null;
					ObservableList<Integer> list = FXCollections.observableArrayList();
					file_text.setText(f.getAbsolutePath());
					try {
						inputStream = new FileInputStream(file_text.getText());
						workbook = Programming.getRelevantWorkbook(inputStream, file_text.getText());
						for(int i=0;i<workbook.getNumberOfSheets();i++)
							list.add(i);
						choiceBox.setItems(list);
						choiceBox.setValue(CHOSEN_SHEET);
					} catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		GridPane root = new GridPane();
		GridPane.setMargin(remove_btn, new Insets(5.0));
		GridPane.setMargin(print_to_file_btn, new Insets(5.0));
		GridPane.setMargin(choiceBoxDates, new Insets(5.0));
		GridPane.setRowIndex(credits, 0);
		GridPane.setColumnIndex(credits, 0);
		GridPane.setRowIndex(load_file_btn, 0);
		GridPane.setColumnIndex(load_file_btn, 3);
		GridPane.setRowIndex(file_text, 0);
		GridPane.setColumnIndex(file_text, 1);
		GridPane.setRowIndex(choose_file_btn, 0);
		GridPane.setColumnIndex(choose_file_btn, 2);

		GridPane.setRowIndex(choose_sheet, 1);
		GridPane.setColumnIndex(choose_sheet, 0);
		GridPane.setRowIndex(choiceBox, 1);
		GridPane.setColumnIndex(choiceBox, 1);

		GridPane.setRowIndex(choose_date, 2);
		GridPane.setColumnIndex(choose_date, 0);
		GridPane.setRowIndex(choiceBoxDates, 2);
		GridPane.setColumnIndex(choiceBoxDates, 1);

		GridPane.setRowIndex(print_to_file_btn, 2);
		GridPane.setColumnIndex(print_to_file_btn, 2);
		GridPane.setRowIndex(remove_btn, 2);
		GridPane.setColumnIndex(remove_btn, 3);
		GridPane.setRowIndex(warning, 3);
		GridPane.setColumnIndex(warning, 1);


		file_text.setMinWidth(350);
		root.getChildren().add(load_file_btn);
		root.getChildren().add(file_text);
		root.getChildren().add(choose_file_btn);
		root.getChildren().add(credits);
		root.getChildren().add(choiceBox);
		root.getChildren().add(choose_sheet);
		root.getChildren().add(choose_date);
		root.getChildren().add(choiceBoxDates);
		root.getChildren().add(remove_btn);
		root.getChildren().add(print_to_file_btn);
		root.getChildren().add(warning);

		primaryStage.setScene(new Scene(root, 800, 180));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	public static void log(String string) {
		if (showLog)
			System.out.println(string);
		if (printLog) {
			try {
				writeLog(++line_num + " - "+ string);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeLog(String info) throws IOException {
		if (!f.exists())
			f.createNewFile();

		try {
			bw.write(info);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void writeStringToFile(String s, String path, boolean open_file)
	{
		
		
		  try
	      {
	         File fileDir = new File(path);
	         Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
	         String newString = new String(s.getBytes(), "UTF8");
	         out.append(newString).append("\r\n");
	         
	         out.flush();
	         out.close();
	         Desktop desktop = Desktop.getDesktop();
	         try {
	        	 if(open_file)
					desktop.open(new File(path));
				} catch (IOException e) {
					e.printStackTrace();
				}
	      } catch (UnsupportedEncodingException e)
	      {
	         System.out.println(e.getMessage());
	      } catch (IOException e)
	      {
	         System.out.println(e.getMessage());
	      } catch (Exception e)
	      {
	         System.out.println(e.getMessage());
	      }
	}
	public static int getCHOSEN_SHEET() {
		return CHOSEN_SHEET;
	}

	public static void setCHOSEN_SHEET(int cHOSEN_SHEET) {
		CHOSEN_SHEET = cHOSEN_SHEET;
	}

	public static int getMAX_HALL() {
		return MAX_HALL;
	}

	public static void setMAX_HALL(int mAX_HALL) {
		MAX_HALL = mAX_HALL;
	}



}

