import java.util.Random;

public class Main {
	static final int N = 5;
	static final int K = 12;
	static final Random rnd = new Random();
	static Semaforo[] s = new Semaforo[N];
	static Deposito d = new Deposito(K);
	
	public static void main(String args[]){
		MyThread[] t = new MyThread[N];
		System.out.println("Num di thread: "+N);
		System.out.println("Num di token: "+K);
		for(int i = 0; i<N ; i++){
			if(i==0) s[i]=new Semaforo(1);
			else s[i]=new Semaforo(0);
		}
		for(int i = 0; i<N ; i++){
			t[i]= new MyThread(i);
			t[i].start();
		}
	}
}

class MyThread extends Thread{
	private int id;
	private int gettoni = 0;
	
	MyThread(int i){
		super();
		id=i;
	}
	
	public void run(){
		int next;
		boolean c = true;
		while(c){
			Main.s[id].down();
			prelevare();
			System.out.println("Nel deposito restano "+Main.d.getGettoni()+" gettoni");
			if(Main.d.getGettoni()>=Main.N){
				do{
					next = Main.rnd.nextInt(Main.N);
				}while(next==id);
				Main.s[next].up();
			}else{
				c=false;
				Main.s[(id+1)%Main.N].up();
			}
		}
		System.out.println("il thread num: "+id+" termina avendo raccolto "+gettoni+" gettoni");
	}
	
	public void prelevare(){
		Main.d.prelievo();
		gettoni++;
		System.out.println("il thread num: "+id+" preleva un gettone dal deposito");
	}
}

class Deposito{
	private int gettoni;
	
	Deposito(int k){
		gettoni = k;
	}
	
	public int getGettoni(){
		return gettoni;
	}
	
	public void prelievo(){
		gettoni--;
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
