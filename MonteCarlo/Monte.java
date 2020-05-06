import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Monte {
    //global variable
    static Random rand = new Random();

    public static void main(String[] args) {

        int days = 0;
        int pc;
        int infected = 1;
        int num_Infected = 0;
        int nTrial;
        int num_Trials;
        int removal;
        int removalCount;
        boolean isInfected;
        int list;
        int listCount;
        double probability;

        //Variables for total amounts - based on array
        double infectedOnce = 0;
        double daysOfAllTrials = 0;
        double infectedOfAllTrials = 0;


        //Scanner for user input if we want to change values
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the desired number of trials: ");
        num_Trials = sc.nextInt();
        System.out.print("Enter the desired number of Pc's: ");
        pc = sc.nextInt();
        System.out.print("Enter the desired probability of infection: ");
        probability = sc.nextDouble();
        System.out.print("Enter the desired number of removals per day: ");
        removal = sc.nextInt();
        removalCount = removal;
        nTrial = num_Trials;
        System.out.println();

        //Array to keep track of day to day infections.
        int[] techDay = new int[pc];
        refresher(techDay);

        //Array to track if pc has been infected before.
        int[] infectedPc = new int[pc];
        refresher(infectedPc);

        //Temp ArrayList
        ArrayList<Integer> tmp = new ArrayList<>();

        //Deals with the monte carlo repeating day simulation
        while (nTrial != 0) {
            while(infected > 0) {
                days++;
                for(int i = 0; i < pc; i++) {
                    if(techDay[i] == 1) {
                        //If infected run bernoulli trial
                        for(int j = 0; j < pc; j++) {
                            isInfected = bernoulli(probability);
                            if((j!=i && isInfected) && ((!(tmp.contains(j))) && (techDay[j] != 1))) {
                                tmp.add(j);
                            }
                        }
                    }
                }

                listCount = tmp.size() - 1;

                while(listCount != -1) {
                    list = tmp.get(listCount);
                    techDay[list] = 1;
                    listCount--;
                    infected++;
                }

                //Track infected pc
                for(int i = 0; i < pc; i++) {
                    if(infectedPc[i] != 1 && techDay[i] == 1) {
                        infectedPc[i] = 1;
                    }
                }

                //Technician removal
                for(int i = techDay.length-1; i >= 0; i--) {
                    if((removalCount != 0) && (techDay[i] == 1)) {
                        techDay[i] = 0;
                        removalCount--;
                        infected--;
                    }
                }
                //Reset values if pcs are still infected.
                removalCount = removal;
                tmp.clear();
            }
            for(int i = 0; i < pc; i++) {
                num_Infected = infectedPc[i] + num_Infected;
            }
            //Check to see if all pc were infected once
            if(num_Infected == pc) {
                infectedOnce++;
            }
            //Accumulators to calculate total after each trial
            daysOfAllTrials = days + daysOfAllTrials;
            infectedOfAllTrials = num_Infected + infectedOfAllTrials;

            //nth Trial information
            System.out.println("Trial: " + nTrial + " #-Days: " + days + ", Inf Once: " + infectedOnce + ", Number Inf: " + num_Infected);

            nTrial--;
            infected = 1;
            num_Infected = 0;
            days = 0;
            refresher(techDay);
            refresher(infectedPc);
        }
        System.out.println();
        System.out.println("Expected time to remove the virus from the whole network: " + daysOfAllTrials/num_Trials + " days");
        System.out.println("Probability that each computer gets infected at least once: " + infectedOnce/num_Trials);
        System.out.println("Expected number of computers that get infected: " + infectedOfAllTrials/num_Trials);
    }

    //returns a random real number between [0,1)
    private static double uniform() {
        return rand.nextDouble();
    }

    //bernoulli trial to determine if pc infection based off probability
    private static boolean bernoulli(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        return uniform() < p;
    }

    //Values for reset array when doing trial
    private static void refresher(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            if(i == 0) {
                arr[i] = 1;
            }else {
                arr[i] = 0;
            }
        }
    }
}
