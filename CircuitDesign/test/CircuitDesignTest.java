/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jimstewart
 */
public class CircuitDesignTest {
    SatTester sat;
    private final CircuitDesign.InputReader reader;
    private final CircuitDesign.OutputWriter writer;
  
    public CircuitDesignTest() {
        sat = new SatTester();
        reader = new CircuitDesign.InputReader(System.in);
        writer = new CircuitDesign.OutputWriter(System.out);
       }
    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
    /**
     * Test of main method, of class CircuitDesign.
     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        CircuitDesign.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of run method, of class CircuitDesign.
//     */
//    @Test
//    public void testRun() {
//        System.out.println("run");
//        CircuitDesign instance = null;
//        instance.run();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    @Test
    public void testIsSatisfiable(){
        ArrayList<ArrayList<int[]>> trialInputs = new ArrayList<>();
//        int[] inputs = {3,-2,-1,-2,-3,2};
//        int inputNum = 0;
//        for(int i=0;i<3;i++){
//            ArrayList<int[]> input = new ArrayList<>();
//            int[] inputArray = new int[2];
//            for(int j=0;j<2;j++){
//                inputArray[j] = inputs[inputNum];
//                inputNum++;
//            }
//            input.add(inputArray);
//            trialInputs.add(input);
//        }
            
        
        CircuitDesign cd = new CircuitDesign(reader, writer);
        int trials = trialInputs.isEmpty()?10:trialInputs.size();
        int minVars = 2;
        int maxVars = 5;
        int minClauses = 2;
        int maxClauses = 3;
        Random rnd = new Random();
        for(int trial=0;trial<trials;trial++){
            int n;
            int m;
            if(trialInputs.isEmpty()){
                n = rnd.nextInt(maxVars-minVars)+minVars + 1;
                maxClauses = combinatorial(n,2);
                m = rnd.nextInt(maxClauses - minClauses)+minClauses + 1;
            } else {
                n = 0;
                for(int[] trialInput:trialInputs.get(trial)){
                    for(int t:trialInput){
                        int tAbs = Math.abs(t);
                        if(tAbs>n)
                            n=tAbs;
                    }
                }
                m = trialInputs.size();
            }
            CircuitDesign.TwoSatisfiability twoSat = cd.new TwoSatisfiability(n, m);
            if(trialInputs.isEmpty()){
                boolean[][] pairExists = new boolean[2*n+1][2*n+1];
                for(int i=0;i<m;i++){
                    int[] adjustedClause = new int[2];
                    for(int j=0;j<m;j++)
                        Arrays.fill(pairExists[j], false);
                    do{
                        twoSat.clauses[i].firstVar = (rnd.nextInt(n)+1)*randomSign();
                            do{

                                twoSat.clauses[i].secondVar = (rnd.nextInt(n) +1)*randomSign();
                             } while (Math.abs(twoSat.clauses[i].secondVar)==Math.abs(twoSat.clauses[i].firstVar));
                        adjustedClause[0] = twoSat.clauses[i].firstVar+n;
                        adjustedClause[1] = twoSat.clauses[i].secondVar+n;
//                        if(adjustedClause[0]<0 || adjustedClause[1]<0 || adjustedClause[0]>=pairExists.length || adjustedClause[1]>=pairExists.length)
//                            System.out.print("");
                    } while (pairExists[adjustedClause[0]][adjustedClause[1]]);
                    pairExists[adjustedClause[0]][adjustedClause[1]]=true;
                    pairExists[adjustedClause[1]][adjustedClause[0]]=true;
                    //System.out.println();
                } 
            } else{
                for(int i=0;i<m;i++){
                    twoSat.clauses[i].firstVar = trialInputs.get(i).get(0)[0];
                    twoSat.clauses[i].secondVar = trialInputs.get(i).get(0)[1];
                    System.out.println();
                }
            }
        
            int[] result = new int[n];
            boolean satisfiable = twoSat.isSatisfiable(result);
            ArrayList<ArrayList<Integer>> sol = new ArrayList<>();
            for(CircuitDesign.Clause c:twoSat.clauses){
                ArrayList<Integer> l = new ArrayList<>();
                l.add(c.firstVar);
                l.add(c.secondVar);
                sol.add(l);
            }
            ArrayList<int[]> solutions= SatTester.naiveSatTester(sol);
            boolean realSatisfiable = !solutions.isEmpty();
            if(realSatisfiable != satisfiable){
                System.out.println("Test failed.");
                String satText = realSatisfiable?"Satisfiable":"Unsatisfiable";
                System.out.println("Unit test result: " + satText);
                System.out.printf("%d %d%n",n, m);
                for(CircuitDesign.Clause c:twoSat.clauses)
                    System.out.printf("%d %d%n",c.firstVar, c.secondVar);
                assert false;
                continue;
            } 
            if(satisfiable){
            boolean validSolution = false;
                for(int[] solution:solutions){
                    if(validSolution = Arrays.equals(solution, result))
                        break;
                }
                if(!validSolution){
                    System.out.println("Test failed. ");
                    System.out.printf("%d %d%n",n, m);
                    for(CircuitDesign.Clause c:twoSat.clauses)
                        System.out.printf("%d %d%n",c.firstVar, c.secondVar);
                    assert false;
                    continue;
                }
                System.out.println("Test passed.");
                assert true;
            }
        }
        System.out.println();
    }
    
    private int randomSign(){
        Random rnd = new Random();
        return rnd.nextInt(2)==1?1:-1;
    }
    private int combinatorial(int n, int m){
        ArrayList<Integer> numerator = new ArrayList<>();
        ArrayList<Integer> denominator = new ArrayList<>();
        for(int i=2;i<=n;i++){
            numerator.add(i);
        }
        for(int i=2;i<=m;i++){
            if(numerator.contains(i))
                numerator.remove(numerator.indexOf(i));
            else
                denominator.add(i);
        }
        for(int i=2;i<=n-m;i++){
            if(numerator.contains(i))
                numerator.remove(numerator.indexOf(i));
            else
                denominator.add(i);
        }
        for(int i=0;i<denominator.size();i++)
            try{
            for(int j=0;j<numerator.size();j++){
                if(denominator.isEmpty())
                    break;
                if(numerator.get(j)%denominator.get(i)==0){
                    numerator.set(j, numerator.get(j)/denominator.get(i));
                    denominator.remove(i);
                }
            }
            } catch (IndexOutOfBoundsException e){
                System.out.println();
            }
        int c = 1;
        for(int i=0;i<numerator.size();i++){
            c*=numerator.get(i);
        }
        for(int i=0;i<denominator.size();i++){
            c/=denominator.get(i);
        }
        return c;
    }

}
