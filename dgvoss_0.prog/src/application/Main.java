package application; 

import java.io.File;
import java.util.List;

import Hardware.CPU;
import Software.AddProcess;
import Software.EventManage;
import Software.GestionRessource;
import Software.Processus;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
//import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;
import vue.AlertAbout;
import vue.ConfirmBox;
import vue.CopyTask;
import vue.Display;
public class Main extends Application{ 

	public static void main(String[] args) {   
		Application.launch(Main.class, args);   
	} 
	//750, 500
	/////////////////////////////////////////////////////////////////////////////////
	@Override   
	public void start(Stage primaryStage) throws Exception {
		//creation de notre theatre, de notre scene et de notre root qui nous permet d'ajouter des elements sur notre scene
		primaryStage.setTitle("SIMILATION SYSTEME D'EXPLOITATION");     
		BorderPane root = new BorderPane();   
		Scene scene = new Scene(root, 1000, 600,Color.GREEN);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e->{
			e.consume();
			closeprograme();});
		
		//ajout d'un bar de menu en haut de notre scene

		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("Fichier");
		Menu help = new Menu("Aide");
		Menu insertion = new Menu("Insertion");
		Menu design = new Menu("Disign");
		Menu transitions = new Menu("Transitions");
		Menu animation = new Menu("Animation");
		//Menu help = new Menu("Aide");
		
		MenuItem start = new MenuItem("Commencer");
		MenuItem cancel = new MenuItem("Annuler");
		cancel.setOnAction(e->{
			
			Boolean answer = ConfirmBox.display("Title", "etes-vous sur de vouloir annuler ?");
			System.out.println(answer);
			if(answer) {

				TabPane tabpane = new TabPane();
				//tabpane.setSide(Side.LEFT);
				tabpane.setNodeOrientation(NodeOrientation.INHERIT);
				Tab tab_disque = new Tab("Disk");
				tab_disque.setClosable(false);
				tabpane.getTabs().setAll(Display.OverView_Init(), Display.tabmemoryInit(), tab_disque);
				BorderPane.setAlignment(tabpane, Pos.TOP_RIGHT);
				BorderPane.setMargin(tabpane, new Insets(5, 5, 3, 5));
				//ajout de notre tabpane a droite de la scene 3,5,3,5
				root.setCenter(tabpane);
			}
			
		});
		start.setOnAction(e->{
			final ProgressBar progressBar = new ProgressBar(0);
			final ProgressIndicator progressIndicator = new ProgressIndicator(-1);
			// Create a Task.
			Display.copyTask = new CopyTask();

			// Unbind progress property
			progressBar.progressProperty().unbind();

			// Bind progress property
			progressBar.progressProperty().bind(Display.copyTask.progressProperty());

			// Hủy bỏ kết nối thuộc tính progress
			progressIndicator.progressProperty().unbind();

			// Bind progress property.
			progressIndicator.progressProperty().bind(Display.copyTask.progressProperty());
			// When completed tasks
			Display.copyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent t) {
					List<File> copied = Display.copyTask.getValue();
					//System.out.println("papa");
					TabPane tabpane = new TabPane();
					//tabpane.setSide(Side.LEFT);
					tabpane.setNodeOrientation(NodeOrientation.INHERIT);
					Tab tab_disque = new Tab("Disk");
					tab_disque.setClosable(false);
					tabpane.getTabs().setAll(Display.info_Process(), Display.tabmemoryInfo(), tab_disque);
					BorderPane.setAlignment(tabpane, Pos.TOP_RIGHT);
					BorderPane.setMargin(tabpane, new Insets(5, 5, 3, 5));
					//ajout de notre tabpane a droite de la scene
					root.setCenter(tabpane);
				}
			});

			// Start the Task.
			new Thread(Display.copyTask).start();

			Tab overview = new Tab("OverView");
			///////////////////////////////////////////////////////////////////////////////
			VBox label_io_ready_queue = new VBox();
			label_io_ready_queue.setSpacing(10);
			label_io_ready_queue.setAlignment(Pos.CENTER);
			Label label1 = new Label("IO QUEUE");
			label1.setTextFill(Color.BLUE);
			label1.setFont(Font.font("", 15));
			Label label_ready_queue = new Label("READY QUEUE");
			label_ready_queue.setTextFill(Color.BLUE);
			label_ready_queue.setFont(Font.font("", 15));
			VBox global_vbox = new VBox();
			HBox hbox = new HBox();
			VBox label_vbox = new VBox();
			VBox label_progress = new VBox();
			label_progress.setSpacing(10);
			
			/*label_progress.setAlignment(Pos.TOP_CENTER);
			Label label10 = new Label("SERGE -- GETTRO -- VALNOR ");
			label10.setTextFill(Color.RED);
			label10.setFont(Font.font("Cambria", 15));*/
			
			label_progress.setAlignment(Pos.CENTER);
			Label label11 = new Label("SIMULATION DU  SYSTEME D'EXPLOITATION ");
			label11.setTextFill(Color.RED);
			label11.setFont(Font.font("Cambria", 18));
			label_vbox.setPadding(new Insets(3, 5, 0, 5));
			label_vbox.setSpacing(5);
			
			///////////////////////////////////////////////////////////////////////
			ScrollPane s1 = new ScrollPane();
	        s1.setPrefSize(200, 200);
	        s1.setVvalue(1.0);
			s1.setContent(Display.text_test());
			s1.setPadding(new Insets(15, 15, 15, 15));
			VBox.setMargin(s1, new Insets(2,1,1,1));
			label_vbox.getChildren().add(s1);
			TableView<Processus> table = Display.tabview();
			hbox.setAlignment(Pos.CENTER);
			HBox.setMargin(table, new Insets(0, 0, 0, 150));
			// 90,0,0,170
			HBox.setMargin(label_progress , new Insets(90, 0, 0, 170));
			label_progress.getChildren().addAll(label11, progressBar, progressIndicator);
			hbox.getChildren().addAll(label_progress, table);
			hbox.setPadding(new Insets(7, 5, 5, 5));
			hbox.setSpacing(5);
			label_vbox.setAlignment(Pos.TOP_CENTER);
			global_vbox.getChildren().addAll(hbox, label_vbox);
			overview.setContent(global_vbox);
			overview.setClosable(false);
			TabPane tabpane = new TabPane();
			//tabpane.setSide(Side.LEFT);
			tabpane.setNodeOrientation(NodeOrientation.INHERIT);
			Tab tab_disque = new Tab("Disk");
			tab_disque.setClosable(false);
			tabpane.getTabs().setAll(overview, Display.tabmemoryOS(), tab_disque);
			BorderPane.setAlignment(tabpane, Pos.TOP_RIGHT);
			BorderPane.setMargin(tabpane, new Insets(5, 5, 3, 5));
			
			//ajout de notre tabpane a droite de la scene
			root.setCenter(tabpane);
		});
		MenuItem exit = new MenuItem("quitter");
		exit.setOnAction(e->{closeprograme();});
		MenuItem about = new MenuItem("Realisateurs");
		about.setOnAction(e->{
			AlertAbout.display("title", "Pierre saint serge, Buissreth Getro, Pavelus Valnor");
		});
		menuBar.getMenus().add(file);
		menuBar.getMenus().add(help);
		menuBar.getMenus().add(insertion);
		menuBar.getMenus().add(design);
		menuBar.getMenus().add(transitions);
		menuBar.getMenus().add(animation);
		
		file.getItems().add(start);
		file.getItems().add(cancel);
		file.getItems().add(exit);
		help.getItems().add(about);
		
		//ajout du menu bar
		root.setTop(menuBar);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		

		//creation d'un tabpane de 2 tab
		//memoire
		TabPane tabpane = new TabPane();
		//tabpane.setSide(Side.LEFT);
		tabpane.setNodeOrientation(NodeOrientation.INHERIT);
		Tab tab_disque = new Tab("Disk");
		tab_disque.setClosable(false);
		tabpane.getTabs().setAll(Display.OverView_Init(), Display.tabmemoryInit(), tab_disque);
		BorderPane.setAlignment(tabpane, Pos.TOP_RIGHT);
		BorderPane.setMargin(tabpane, new Insets(5, 5, 3, 5));
		//ajout de notre tabpane a droite de la scene
		root.setCenter(tabpane);
		////////////////////////////////////////////////////////////////////
		Processus processus = new Processus();
		//processus.init_process();
		primaryStage.setResizable(false);
		primaryStage.show();
		////////////////////////////////////////////////////////////////////////////////////
		EventManage event= new EventManage ();
		AddProcess add =new AddProcess(event);
		GestionRessource gest=new GestionRessource (event);
		//GenerateEvent t1=new GenerateEvent ();
		CPU t2=new CPU (event);
		add.start();
		t2.start();
		gest.stop();
			
	}
	
	/*public static void println(String s){
	    Platform.runLater(new Runnable() {//in case you call from other thread
	        @Override
	        public void run() {
            Display.text().setText(Display.text().getText()+s+"\n");
            System.out.println(s);//for echo if you want
	        }
	    });
	}*/
	
	private void closeprograme() {
		// TODO Auto-generated method stub
		Boolean answer = ConfirmBox.display("Title", "Etes-vous sur de vouloir quiter ?");
		System.out.println(answer);
		if(answer) {
			System.exit(0);
		}
		
	}
		
}
