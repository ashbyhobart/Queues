import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
//import java.util.Timer;




public class simulator {
    //Returns random value distributed to an exponential distribution with lamba = lambda
    public static double exp(double lambda) {
        Random x = new Random();
        double expped = (-1 / lambda) * Math.log(1 - x.nextDouble());

        return expped;
    }


    private double lambda, serviceTime; //stuff needed
    private LinkedList<Task> tasks; //waiting to be served
    private double stopwatch; //"timer" for simulation
    private double arrival, departure; //For when it occurs
    private int nextReq, finished;
    private int qSum;
    private double qT, sT;
    //private LinkedList<String> names;


    public simulator(double lam, double Ts) {
        lambda = lam;
        serviceTime = Ts;
        tasks = new LinkedList<Task>();
        stopwatch = 0;
        nextReq = 0;
		arrival = 0;
        finished = 0;
        qSum = 0;
        qT = 0;
        sT= 0;
        //names = new LinkedList<String>();
    }


    public void birth(double currentTime) {

        if(tasks.isEmpty()) { //something is about to be born! Schedule its death
            scheduleDeath(currentTime, nextReq + "");
        } else {
            tasks.add(new Task(currentTime, nextReq + ""));
        }

        arrival += exp(lambda);
    }


    public Task kill() {
        Task killed = tasks.removeFirst();

        if(tasks.isEmpty() == false) {
            Task killer = tasks.removeFirst();
            scheduleDeath(killer.getArr(), killer.getName());
        } else {
            arrival += exp(lambda);
            departure = 1000000000;
        }

        return killed;
    }


    public void scheduleDeath(double when, String which) {
        departure = stopwatch + exp(1 / serviceTime);
        tasks.addFirst(new Task(departure, which));
        qT += departure - when;
        sT += departure - stopwatch;

        finished++;
    }


    //System.out.println("R" + nextReq + " ARR: " + currentTime);
    //System.out.println("R" + nextReq + " START: " + currentTime);

    public void snapshot() {
        qSum += tasks.size();
    }


    //simulates M/M/1
    public void simulate(double time) {
        double snapshots = time / 50;
        double current_snap = snapshots;
        int snap_count = 0;

        while(stopwatch <= time) {

            if(stopwatch > current_snap && snap_count < 50) {
                snapshot();
                snap_count++;
                current_snap += snapshots;

            }

            if(arrival <= departure || arrival == 0) {
                
				if(arrival == 0){
                    System.out.println("we made it here");
                    arrival = exp(lambda);
                }
				
                stopwatch = arrival;
                System.out.println("R" + nextReq + " ARR: " + stopwatch);

                if(nextReq == 0 || tasks.isEmpty()) {
                    System.out.println("R" + nextReq + " START: " + stopwatch);
                }

                birth(arrival);
                nextReq++;
            } else {
                stopwatch = departure;
                Task dead = kill();
                //System.out.println(nextReq);
                System.out.println("R" + dead.getName() + " DONE: " + stopwatch);
                //nextReq++;

                if(tasks.isEmpty() == false) {
                    System.out.println("R" + tasks.getFirst().getName() + " START: " + stopwatch);
                }

            }

        }
        
        double Tq = qT / finished;
        double Ts = sT / finished;
        double util = lambda * Ts;
        
        int qAvg = qSum / snap_count;
        System.out.println("\nUTIL: " + util);
        System.out.println("QLEN: " + qAvg);
        System.out.println("TRESP: " + Tq);
    }


    public static void main(String[] args){
		
		Scanner reader = new Scanner(System.in);
		
		System.out.println("Enter a duration in milliseconds: ");
        double duration = reader.nextDouble();
		
		System.out.println("Enter a lambda value (requests per millisecond): ");
        double lam = reader.nextDouble();
		
		System.out.println("Enter an average service time in milliseconds: ");
        double Ts = reader.nextDouble();

        simulator s = new simulator(lam, Ts);
        s.simulate(duration);
    }

}