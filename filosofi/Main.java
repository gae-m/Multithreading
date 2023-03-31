public class Main{
	static final int AFFAMATO = 0;
	static final int MANGIANTE = 1;
	static final int PENSANTE = 2;
	static final int N = 5;
	static int[] stato;
	static Semaforo[] sem;
	static Filosofo[] fil;
	
	public static void main(String args[]){
		stato = new int[N];
		sem = new Semaforo[N];
		fil = new Filosofo[N];
		for(int i = 0; i<N ; i++){
			stato[i]=PENSANTE;
			sem[i]= new Semaforo(0);
			fil[i] = new Filosofo();
		}
		for(int i = 0; i<N ;i++) fil[i].start();
		
	}
}
	
class Filosofo extends Thread{
	private static int ID = 0;
	private static Semaforo mutex = new Semaforo(1);
	private int id;
	
	Filosofo(){
		id = ID;
		ID++;
	}
	
	public void run(){
		while(true){
			pensa();
			prendiForchette();
			mangia();
			posaForchette();
		}
	}
	
	void prendiForchette(){
		mutex.down();
		Main.stato[id] = Main.AFFAMATO;
		test(id);
		mutex.up();
		Main.sem[id].down();
		System.out.println("Fil."+id+": prendo forchette");
	}
	
	void posaForchette(){
		mutex.down();
		System.out.println("Fil."+id+": poso forchette");
		Main.stato[id]=Main.PENSANTE;
		test(sx(id));
		test(dx(id));
		mutex.up();
	}
	void test(int i){
		if(Main.stato[i]==Main.AFFAMATO&&Main.stato[sx(i)]!=Main.MANGIANTE&&Main.stato[dx(i)]!=Main.MANGIANTE){
			Main.stato[i]=Main.MANGIANTE;
			Main.sem[i].up();
		}
	}
	int sx(int i){
		return (i+Main.N-1)%Main.N;
	}
	int dx(int i){
		return (id+1)%Main.N;
	}
	void pensa(){
		System.out.println("Fil."+id+": sto pensando");
		
	}
	void mangia(){
		System.out.println("Fil."+id+": sto mangiando");
	}
}
	
class Semaforo{
	private int val;
	private int sosp = 0;
	
	Semaforo(int i){
		val=i;
	}
	
	public synchronized void down(){
		if(val==0){
			sosp++;
			try{wait();}
			catch(InterruptedException e){};
			sosp--;
		}else val--;
	}
	
	public synchronized void up(){
		if(sosp>0) notify();
		else val++;
	}
}
