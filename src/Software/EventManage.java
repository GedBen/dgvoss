package Software;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import Software.Frame;
import Software.GenerateEvent;
import Software.GestionRessource;
import Software.MMU;
import Software.PCB;
import Software.Process;
import javafx.scene.Node;

public class EventManage {
	
	GenerateEvent event=new GenerateEvent();
	MMU mmu=new MMU();
	private boolean value=false,processcreate=false,isInterrupt=false;
	private int random;
	private int essaie=10;
	Frame Table[]=new Frame [100];
	PCB pcb = new PCB();
	PCB Pcb = new PCB();
	private int address=0;
	//Queue <PCB> queue=new PriorityQueue<PCB>();
	Frame frame=new Frame();
	int frames[]=new int[100];
	
	ConcurrentLinkedQueue<PCB> queuePr=new ConcurrentLinkedQueue<PCB>();
	ConcurrentLinkedQueue<PCB> queueBl=new ConcurrentLinkedQueue<PCB>();
	ConcurrentLinkedQueue<PCB> ecran=new ConcurrentLinkedQueue<PCB>();
	ConcurrentLinkedQueue<PCB> clavier=new ConcurrentLinkedQueue<PCB>();
	ConcurrentLinkedQueue<Process> proc=new ConcurrentLinkedQueue<Process>();
	GestionRessource gest;

	public void Execute() {
	
		
		int valeur=0;
		int interruption;
		PCB1 TempPcb;
		int Pid;
		boolean verif=event.isProcesscreate();
	
				try {
					Thread.sleep(2500);
				}
				catch(InterruptedException e) {
					
				}
		
		
		while(true) {

			Pcb=queuePr.poll();
			if(Pcb!=null) {
		
		System.out.println(Pcb.getPid());
		
		while(isInterrupt==false) {
			try {
				Thread.sleep(300);
			}
				catch(InterruptedException e) {
				}
			interruption=event.GenerateInterrupt();
			switch(interruption) {
			case 0:
				//System.out.println(Pcb.getPid());
				KillProcess(Pcb);
				isInterrupt=true;
			case 2:
				isInterrupt=false;
				//System.out.println(Pcb.getPid());
				//TempPcb=new PCB(Pcb.getPid(),Pcb.getAlloc_Mem(),Pcb.getSize_Process());
				//Pcb.setRessource(2);
				//ecran.offer(TempPcb);
				gest=new GestionRessource(2,Pcb.getPid());
				
				//event.setInterrupt(true);
				break;
			case 9:
				isInterrupt=true;
				//System.out.println(Pcb.getPid());
				//event.setInterrupt(true);
				//TempPcb=new PCB(Pcb.getPid(),Pcb.getAlloc_Mem(),Pcb.getSize_Process());
				//Pcb.setRessource(3);
				gest=new GestionRessource(9,Pcb.getPid());
			
			//	imprimante.offer(TempPcb);
				break;
			case 12:
				isInterrupt=true;
				//System.out.println(Pcb.getPid());
				//TempPcb=new PCB(Pcb.getPid(),Pcb.getAlloc_Mem(),Pcb.getSize_Process());
				//Pcb.setRessource(1);
				//clavier.offer(TempPcb);
				//event.setInterrupt(true);
				gest=new GestionRessource(12,Pcb.getPid());
			
			}
			
			}
		gest.start();

		isInterrupt=false;
		}
		}
		}
	
		public void Initialiser() {
		int i;
		for(i=0;i<100;i++) {
			frames[i]=-1;
			/*frame.setPid(-1);
			frame.setNumber(-1);
			frame.setValue(false);
			Table[i]=frame;*/
			}
	}
	
	
	
	public void AddPcb() {
		int compteur=0;
		Process process;
		Initialiser();
		
		while(true) {

			process=event.GenerateProcess();
			if(Verified_Space(process)) {
			System.out.println("processus de PID generer" +process.getPid());
			proc.add(process);
			pcb.setPid(process.getPid());
			pcb.setAlloc_Mem(getAddress());
			pcb.setSize_Process(process.getSize());
			queuePr.offer(pcb);
			ReaffecterMemory(pcb);
			processcreate=true;
			}
		}
			
	}
	
	public boolean Verified_Space(Process process) {
		int SizeProcess;
		int Compt=0;
		int i=0,j=0,size=0;
		int addr=0;
		boolean value=true,verif=false;
		SizeProcess=process.getSize();
		
	 while(frames[i]!=-1 && i<100 && value==true) {
		 if(frames[i]==process.getPid()) {
			 System.out.println("Ce processus est deja charge");
			 value=false;
		 }
		 else
		 i++;
	 }
	 if(value==true && process.getSize()<100-i) {
		 System.out.println("Espace suffisant");
		 value=true;
		 setAddress(i);
	 }
	 else if(value==true && process.getSize()>100-i) {
		 System.out.println("Espace insuffisant");
		 value=false;
	 }
		
		return value;
	}
	
	
	
	
	public void ReaffecterMemory(PCB pcb) {
		
	
		for(int i=pcb.getAlloc_Mem();i<pcb.getAlloc_Mem()+pcb.getSize_Process();i++) {
			frames[i]=pcb.getPid();
		}
		
		System.out.println("Etat de la moire. Les cases ayant pour valuer -1 sont vides");
		
		for(int i=0;i<100;i++) {
			System.out.println("Case["+i+"]="+frames[i]);
		}
		
		}
		
	
	
	public void KillProcess(PCB pcb2) {
		System.out.println("Processus de PID "+pcb2.getPid()+" est sorti de la file");
		for(int i=pcb2.getAlloc_Mem()+pcb2.getSize_Process()+1;i<100;i++) {
			frames[i-pcb2.getSize_Process()]=frames[i];
		}
		
		
}

	
	public void ManageFile() {
		if(event.isInterrupt()) {
			switch(Pcb.getRessource()) {
			case 1:
				System.out.println("Fin d'utlisation du clavier par le processus "+Pcb.getPid()+"");
				if(clavier.poll()!=null) {
				queuePr.offer(clavier.poll());
				}
				break;
			case 2:
				System.out.println("Fin d'utilisation de l'ecran par le procesus "+Pcb.getPid()+"");
				if(ecran.poll()!=null) {
					queuePr.offer(ecran.poll());
				}
				break;
			case 3:
				System.out.println("Fin d'utilisation de l'imprimante par le processus "+Pcb.getPid()+"");
				
		}
		}
	}
	
	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
	
}