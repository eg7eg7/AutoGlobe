import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
public class Whiskers  extends Application{
	public static String version_num = "1.0";
	private static int CHOSEN_SHEET = 8;
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
	private TextField file_text = new TextField();
	private Label credits = new Label();
	private Label choose_sheet = new Label();
	private ChoiceBox<Integer> choiceBox = new ChoiceBox<Integer>();
	public static void main(String[] args) {
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
		

		//db.addProgramming(new Programming("04.xls"));
		launch(args);

		//db.addProgramming(new Programming("01.xls"));
		//db.addProgramming(new Programming("02.xls"));
		//db.addProgramming(new Programming("03.xls"));
	//	db.printPrettyProgram(6);
		
		//db2.fillDatabaseFromFile();
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println(db);
		db.saveToFile();
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		 primaryStage.setTitle("Whiskers - Weekly Programming Manager " + version_num);
	        load_file_btn.setText("Load file");
	        choose_file_btn.setText("Choose file");
	        choose_sheet.setText("Choose Sheet:\n(8 is Kfar Saba)");
	        credits.setText("Creator - Eden Dupont   ");
	        file_text.setEditable(false);
	        load_file_btn.setOnAction(new EventHandler<ActionEvent>() {
	        
	            @Override
	            public void handle(ActionEvent event) {
	            	String s = file_text.getText();
	            	if(!s.contentEquals(""))
	            	{
	            	CHOSEN_SHEET = choiceBox.getValue();
	            	Programming p = new Programming(s);
	                db.addProgramming(p);
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
	        GridPane.setRowIndex(credits, 0);
	        GridPane.setColumnIndex(credits, 0);
	        GridPane.setRowIndex(load_file_btn, 0);
	        GridPane.setColumnIndex(load_file_btn, 2);
	        GridPane.setRowIndex(file_text, 0);
	        GridPane.setColumnIndex(file_text, 3);
	        GridPane.setRowIndex(choose_file_btn, 0);
	        GridPane.setColumnIndex(choose_file_btn, 5);
	        
	        GridPane.setRowIndex(choose_sheet, 1);
	        GridPane.setColumnIndex(choose_sheet, 0);
	        GridPane.setRowIndex(choiceBox, 1);
	        GridPane.setColumnIndex(choiceBox, 1);
	        
	        file_text.setMinWidth(350);
	        root.getChildren().add(load_file_btn);
	        root.getChildren().add(file_text);
	        root.getChildren().add(choose_file_btn);
	        root.getChildren().add(credits);
	        root.getChildren().add(choiceBox);
	        root.getChildren().add(choose_sheet);
	        
	        primaryStage.setScene(new Scene(root, 800, 800));
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

