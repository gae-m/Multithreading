public class Main{
	static final Semaforo[] squadra = {new Semaforo(1),new Semaforo(0)};
	static int scambio = 0;
	
	public static void main(String[] args){
		Semaforo[] squadrab = {new Semaforo(1),new Semaforo(0)};
		Semaforo[] squadran = {new Semaforo(1),new Semaforo(1)};
		Giocatore b1 = new Giocatore("B",1,squadrab);
		Giocatore b2 = new Giocatore("B",2,squadrab);
		Giocatore n1 = new Giocatore("N",1,squadran);
		Giocatore n2 = new Giocatore("N",2,squadran);
		b1.start();
		b2.start();
		n1.start();
		n2.start();
	}
}

class Semaforo{
	private int val;
	private int sosp = 0;
	
	Semaforo(int i){
		val = i;
	}
	
	synchronized void down(){
		if(val==0){
			sosp++;
			try{wait();}
			catch(InterruptedException e){};
			sosp--;
		}
		else val--;
	}
	
	synchronized void up(){
		if(sosp>0) notify();
		else val++;
	}
}

class Giocatore extends Thread{
	private String squadra;
	private int num;
	private Semaforo[] turno;
	
	Giocatore(String s, int n, Semaforo[] t){
		squadra = s;
		num = n;
		turno = t;
	}
	
	public void run(){
		int team;
		if(squadra=="B") team = 0;
		else team = 1;
		while(true){
			turno[num-1].down();
			Main.squadra[team].down();
			Main.scambio++;
			System.out.println("Colpisce il giocatore "+squadra+num);
			if(Main.scambio!=2) turno[1-(num-1)].up();
			Main.squadra[1-team].up();
		}
	}
}

