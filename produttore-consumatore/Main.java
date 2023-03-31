public class Main{
	static final int dim_buffer = 5;
	static final Semaforo pos_piene = new Semaforo(0);
	static final Semaforo pos_vuote = new Semaforo(dim_buffer);
	static final Semaforo mutex = new Semaforo(1);
	static Buffer buffer;
	
	public static void main(String args[]){
		buffer = new Buffer(dim_buffer); 
		Produttore p = new Produttore();
		Consumatore c = new Consumatore();
		p.start();
		c.start();
	}
}

class Semaforo{
	private int val;
	private int sospesi=0;
	
	Semaforo(int i){
		val=i;
	}
	
	public synchronized void down(){
		if(val==0){
			sospesi++;
			try{wait();}
			catch(InterruptedException e){};
			sospesi--;
		}else val--;
	}
	
	public synchronized void up(){
		if(sospesi>0) notify();
		else val++;
	}
}

class Buffer{
	private char[] c;
	private boolean[] libero;
	
	Buffer(int n){
		c = new char[n];
		libero = new boolean[n];
		for(int i=0;i<n;i++) libero[i]=true;
	}
	
	public void inserisciElemento(char el){
		int i=0;
		while(i<libero.length&&!libero[i]) i++;
		if(i!=libero.length){
			c[i]=el;
			libero[i]=false;
		}
		else System.out.println("Buffer pieno");
	}
	
	public char estraiElemento(){
		int i = 0;
		while(i<libero.length&&libero[i]) i++;
		if(i!=libero.length) {
			libero[i]=true;
			return c[i];
		}
		else{
			System.out.println("Buffer vuoto");
			return '\0';
		}
	}
	
}

class Produttore extends Thread{
	private char[] messaggio = {'B','i','a','g','i','o','S','a','l','z','i','l','l','o'};
	private int i = 0;
	
	public void run(){
		while(true){
			char c = produciElemento();
			Main.pos_vuote.down();
			Main.mutex.down();
			Main.buffer.inserisciElemento(c);
			Main.mutex.up();
			Main.pos_piene.up();
		}
	}
	
	private char produciElemento(){
		char c = messaggio[i];
		i=(i+1)%messaggio.length;
		return c;
	}
}

class Consumatore extends Thread{
	private char ricevuto;
	
	public void run(){
		while(true){
			Main.pos_piene.down();
			Main.mutex.down();
			ricevuto=Main.buffer.estraiElemento();
			Main.mutex.up();
			Main.pos_vuote.up();
			consumaElemento();
		}
	}
	
	private void consumaElemento(){
		System.out.println(ricevuto);
	}
}
