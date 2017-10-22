
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jimstewart
 */
public class SatTester {
        public static ArrayList<int[]> naiveSatTester(ArrayList<ArrayList<Integer>> sol){
        ArrayList<int[]> solutions = new ArrayList<>();
        boolean truthVal = true;
        int maxInd = 0;
        for(ArrayList<Integer> clause:sol){
            for(Integer boolInd:clause){
                int i = Math.abs(boolInd);
                if(i>maxInd)
                    maxInd=i;
            }
        }
//        BitSet[] possibleSolutions = new BitSet[1];
//        long[] tmpLong = {(long)0x1041022};
//        possibleSolutions[0] = BitSet.valueOf(tmpLong);
        int[] result = new int[maxInd];
        for (int mask = 0; mask < (1 << maxInd); ++mask) {
            for (int i = 0; i < maxInd; ++i) {
                result[i] = (mask >> i) & 1;
            }

            boolean formulaIsSatisfied = true;

            for (ArrayList<Integer> clause: sol) {
                int firstVar = clause.get(0);
                int secondVar = clause.get(1);
                boolean clauseIsSatisfied = false;
                if ((result[Math.abs(firstVar) - 1] == 1) == (firstVar < 0)) {
                    clauseIsSatisfied = true;
                }
                if ((result[Math.abs(secondVar) - 1] == 1) == (secondVar < 0)) {
                    clauseIsSatisfied = true;
                }
                if (!clauseIsSatisfied) {
                    formulaIsSatisfied = false;
                    break;
                }
            }

            if (formulaIsSatisfied) {
                int[] s = new int[maxInd];
                for(int i=0;i<maxInd;i++){
                    s[i]=result[i]==1?-(i+1):i+1;
                }
                solutions.add(s);
            }
        }
//        boolean[][] possibleSolutions = new boolean[1<<maxInd][maxInd];
//        for(int i=0;i<possibleSolutions.length;i++){
//            for(int j=0;j<maxInd;j++){
//                int n = (i>>j) & 1;
//                possibleSolutions[i][j] = ((i>>j) & 1)==1;
//            }
//        }
//
//        for(boolean[] ps:possibleSolutions){
//            truthVal = true;
//            for(int i=0;i<sol.size();i++){
//                ArrayList<Integer> clause=sol.get(i);
//                boolean subVal = false;
//                for(Integer boolInd:clause){
//                    int ind = Math.abs(boolInd)-1;
//                    boolean b = boolInd<0?!ps[ind]:ps[ind];
//                    subVal = subVal || b;
//                }
//                truthVal = truthVal && subVal;
//            }
//            if(truthVal) {
//                int[] s = new int[ps.length];
//                for(int i=0;i<ps.length;i++){
//                    s[i]=ps[i]?i+1:(i+1)*-1;
//                }
//                solutions.add(s);
//            }
//        }
        return solutions;
    }
        
        


}
