package com.mifyai.mifyawale;

import java.util.Arrays;

/**
 * This class implements the alpha/beta algorithm in order to search the best
 * solution.
 * Created by Parikiri13 on 25/01/2018.
 */

public class Mlvelocity implements ArtificialIntelligence {

    private static class AlphaBetaData {
        public int alpha = Integer.MIN_VALUE;
        public int beta = Integer.MAX_VALUE;
    }

    private static final int MAX_DEPTH = 2;

    private static class Value {
        public int evaluation = Integer.MIN_VALUE;
        public short position = Short.MIN_VALUE;
    }

    @Override
    public short searchBestPlay(Awale awale, short side) {
        Value searchBestPlay = searchBestPlay(awale, side, false,
                new AlphaBetaData(), 0);
        // it means that a choice has been found
        if (searchBestPlay.evaluation != Integer.MIN_VALUE) {
            if (DEBUG) {
                System.out.println("*********************************");
                System.out.println("evaluation :" + searchBestPlay.evaluation);
                System.out.println("*********************************");
            }
            return searchBestPlay.position;
        }

        // TODO modify later
        // find the first case with at least one seed
        short position = Short.MIN_VALUE;
        for (short i = 0; i < awale.territory[side].length
                && position == Short.MIN_VALUE; i++) {
            if (awale.territory[side][i] > 0) {
                position = i;
            }
        }
        return position;
    }

    private static final boolean DEBUG = true;

    private Value searchBestPlay(Awale awale, short side, boolean isMin,
                                 AlphaBetaData alphaBetaData, int depth) {
        Value value = new Value();
        boolean isAdversaryHungry = Awale.isAdversaryHungry(side,
                awale.territory);
        // test if i am hungry
        if (Awale.isAdversaryHungry(Awale.getAdversarySide(side),
                awale.territory)
                || depth == MAX_DEPTH) {
            value.evaluation = evaluate(side, awale.territory, awale.points);
            if (DEBUG) {
                System.out.println("---------------------------------");
                System.out.println(Arrays.toString(awale.territory[0]));
                System.out.println(Arrays.toString(awale.territory[1]));
                System.out.println("evaluation :" + value.evaluation);
                System.out.println("depth :" + depth);
                System.out.println("side :" + side);
                System.out.println("points :" + awale.points[0] + ","
                        + awale.points[1]);
                System.out.println("side :" + side);
                System.out.println("---------------------------------");
            }
            return value;
        }

        depth++;
        if (isMin) {
            value.evaluation = Integer.MAX_VALUE;
            int length = awale.territory[side].length;
            for (short i = 0; i < length; i++) {
                // test if the case is not empty, and that the opponent is not
                // hungry or if he is that we will replenish him
                if (awale.territory[side][i] != 0
                        && (!isAdversaryHungry || (awale.territory[side][i] >= length
                        - i))) {
                    Awale currentAwale = new Awale(awale);
                    currentAwale.play(side, i, currentAwale.territory, false);
                    // if this play has starved the opponent we don't go further
                    if (!Awale.isAdversaryHungry(side, currentAwale.territory)) {
                        int evaluation = searchBestPlay(currentAwale, Awale
                                        .getAdversarySide(side), !isMin, alphaBetaData,
                                depth).evaluation;
                        if (evaluation < value.evaluation) {
                            value.evaluation = evaluation;
                            value.position = i;
                        }
                    }
                }
                if (alphaBetaData.alpha > value.evaluation) {
                    value.position = i;
                    return value;
                }
                alphaBetaData.beta = Math.min(alphaBetaData.beta,
                        value.evaluation);
            }
        } else {
            value.evaluation = Integer.MIN_VALUE;
            int length = awale.territory[side].length;
            for (short i = 0; i < length; i++) {
                if (awale.territory[side][i] != 0
                        && (!isAdversaryHungry || (awale.territory[side][i] >= length
                        - i))) {
                    Awale currentAwale = new Awale(awale);
                    currentAwale.play(side, i, currentAwale.territory, false);
                    // if this play has starved the opponent we don't go further
                    if (!Awale.isAdversaryHungry(side, currentAwale.territory)) {
                        int evaluation = searchBestPlay(currentAwale, Awale
                                        .getAdversarySide(side), !isMin, alphaBetaData,
                                depth).evaluation;
                        if (evaluation > value.evaluation) {
                            value.evaluation = evaluation;
                            value.position = i;
                        }
                    }
                }
                if (alphaBetaData.beta < value.evaluation) {
                    value.position = i;
                    return value;
                }
                alphaBetaData.alpha = Math.max(alphaBetaData.alpha,
                        value.evaluation);
            }
        }
        return value;
    }

    /**
     * Indicates if the player can't play.
     */
    // private boolean cantPlay(short side, short[][] territory,
    // boolean isAdversaryHungry) {
    // boolean cantPlay = true;
    // int length = territory[side].length;
    // for (int i = 0; i < length && cantPlay; i++) {
    // // test the the case is not empty and that the
    // cantPlay = (territory[side][i] == 0 || (isAdversaryHungry &&
    // territory[side][i] < length
    // - i));
    // }
    // return cantPlay;
    // }
    /**
     * Evaluates the status of this territory.
     */
    public int evaluate(short side, short[][] territory, short[] points) {
        int sum = 0;
        int length = territory[side].length;
        boolean previousCanBeEaten = false;

        //computes points won or probably
        for (int i = 0; i < length; i++) {
            if (territory[side][i] > 3) {
                sum++;
            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
                if (previousCanBeEaten) {
                    sum -= (territory[side][i] + 1);
                } else {
                    sum -= territory[side][i];
                }
                previousCanBeEaten = true;
            }
        }
        previousCanBeEaten = false;
        sum += 2*points[side];

        //computes points lost or probably because of the opponent
        side = Awale.getAdversarySide(side);
        length = territory[side].length;
        for (int i = 0; i < length; i++) {
            if (territory[side][i] > 3) {
                sum--;
            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
                if (previousCanBeEaten) {
                    sum += (territory[side][i] + 1);
                } else {
                    sum += territory[side][i];
                }
                previousCanBeEaten = true;
            }
        }
        sum -= 2*points[side];

        return sum;
    }
}

















//----------------------------------------------------------------------------Code MLVelo Ecris Par JB ci dessous------------------------------------------------------------------
//package com.mifyai.mifyawale;
//
//import android.util.Log;
//
///**
// * Created by HaroldKS on 14/03/2018.
// */
//
//public class Mlvelocity implements ArtificialIntelligence
//{
//    private static class AlphaBetaData {
//        public int alpha = Integer.MIN_VALUE;
//        public int beta = Integer.MAX_VALUE;
//    }
//
//    private static final int MAX_DEPTH = 5;
//
//    private static class Value {
//        public int evaluation = Integer.MIN_VALUE;
//        public short position = Short.MIN_VALUE;
//    }
//
//    @Override
//    public short searchBestPlay(Awale awale, short side) {
//        Value searchBestPlay = searchBestPlay(awale, side, false,
//                new AlphaBetaData(), 0);
//        // it means that a choice has been found
//        if (searchBestPlay.evaluation != Integer.MIN_VALUE) {
//            if (DEBUG) {
//                System.out.println("*********************************");
//                System.out.println("Yo evaluation :" + searchBestPlay.evaluation);
//                System.out.print("taille de " +side);
//                System.out.println("*********************************");
//            }
//
//            return searchBestPlay.position;
//        }
//
//        // TODO modify later
//        // find the first case with at least one seed
//        short position = Short.MIN_VALUE;
//        for (short i = 0; i < awale.territory[side].length
//                && position == Short.MIN_VALUE; i++) {
//            if (awale.territory[side][i] > 0) {
//                position = i;
//            }
//        }
//        return position;
//    }
//
//    private static final boolean DEBUG = true;
//    private int count,profondeurAParcourir;
//    private Value searchBestPlay(Awale awale, short side, boolean isMin,
//                                 AlphaBetaData alphaBetaData, int depth) {
//        count=0;
//        profondeurAParcourir=0;
//        Value value = new Value();
//        int choix=0;
//        if(profondeurAParcourir==0){
//            int casesVul=0;
//            for (int i = 0; i < 6; i++) {
//                Awale currentAwale = new Awale(awale);
//                currentAwale.play(side,(short)  i, currentAwale.territory, false);
//                if (awale.territory[side][i] < 3 && awale.territory[side][i] >= 0) ++casesVul;
//            }
//            if(awale.points[1]>=25)
//            {
//                if(awale.canPlayHere((short) (1),(short) choix))
//                {
//                    value.position=(short)choix;
//                }
//                else
//                {
//                    for(int j=0;j<6;j++)
//                    {
//                        if(awale.canPlayHere((short) (1),(short) choix))
//                        {
//                            value.position=(short)choix;
//                            break;
//                        }
//                    }
//                }
//                return value;
//            }
//            else
//            {
//                value.evaluation = evaluate(side, awale.territory, awale.points);
//                int score;
//                depth++;
////            value.evaluation = Integer.MAX_VALUE;
//                int length = awale.territory[side].length;
//                boolean isAdversaryHungry = Awale.isAdversaryHungry(side,
//                        awale.territory);
//                if (isMin) {
////                value.evaluation = Integer.MAX_VALUE;
////                 length = awale.territory[side].length;
////                for (short i = 0; i < length; i++) {
////                    // test if the case is not empty, and that the opponent is not
////                    // hungry or if he is that we will replenish him
////                    if (awale.territory[side][i] != 0
////                            && (!isAdversaryHungry || (awale.territory[side][i] >= length
////                            - i))) {
////                        Awale currentAwale = new Awale(awale);
////                        currentAwale.play(side, i, currentAwale.territory, false);
////                        // if this play has starved the opponent we don't go further
////                        if (!Awale.isAdversaryHungry(side, currentAwale.territory)) {
////                            int evaluation = searchBestPlay(currentAwale, Awale
////                                            .getAdversarySide(side), !isMin, alphaBetaData,
////                                    depth).evaluation;
////                            if (evaluation < value.evaluation) {
////                                value.evaluation = evaluation;
////                                value.position = i;
////                            }
////                        }
////                    }
////                    if (alphaBetaData.alpha > value.evaluation) {
////                        value.position = i;
////                        return value;
////                    }
////                    alphaBetaData.beta = Math.min(alphaBetaData.beta,
////                            value.evaluation);
////                }
//                } else {
//                    value.evaluation = Integer.MIN_VALUE;
//                    length = awale.territory[side].length;
//                    for (short i = 0; i < length; i++) {
//                        if (awale.territory[side][i] != 0
//                                && (!isAdversaryHungry || (awale.territory[side][i] >= length
//                                - i))) {
//                            Awale currentAwale = new Awale(awale);
//                            currentAwale.play(side, i, currentAwale.territory, false);
//                            // if this play has starved the opponent we don't go further
//                            if (!Awale.isAdversaryHungry(side, currentAwale.territory)) {
//                                int evaluation = searchBestPlay(currentAwale, Awale
//                                                .getAdversarySide(side), !isMin, alphaBetaData,
//                                        depth).evaluation;
//                                if (evaluation > value.evaluation) {
//                                    value.evaluation = evaluation;
//                                    value.position = i;
//                                }
//                            }
//                        }
//                        if (alphaBetaData.beta < value.evaluation) {
//                            value.position = i;
//                            return value;
//                        }
//                        alphaBetaData.alpha = Math.max(alphaBetaData.alpha,
//                                value.evaluation);
//                    }
//                }
//                return value;
//            }
//        }
//
//        return value;
//    }
//
//
//    public int nbreCasesJouables(int side,short[][] territory)
//    {
//        int nbre=0;
//        for(int i=0;i<5;i++)
//        {
//            nbre+=territory[side][i];
//        }
//        return nbre;
//    }
//
//
//    /**
//     * Indicates if the player can't play.
//     */
//    // private boolean cantPlay(short side, short[][] territory,
//    // boolean isAdversaryHungry) {
//    // boolean cantPlay = true;
//    // int length = territory[side].length;
//    // for (int i = 0; i < length && cantPlay; i++) {
//    // // test the the case is not empty and that the
//    // cantPlay = (territory[side][i] == 0 || (isAdversaryHungry &&
//    // territory[side][i] < length
//    // - i));
//    // }
//    // return cantPlay;
//    // }
//    /**
//     * Evaluates the status of this territory.
//     */
//
//
//
//
//
//    public int evaluate(short side, short[][] territory, short[] points) {
//        int sum = 0;
//        int length = territory[side].length;
//        boolean previousCanBeEaten = false;
//
//        //computes points won or probably
//        for (int i = 0; i < length; i++) {
//            if (territory[side][i] > 3) {
//                sum++;
//            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
//                if (previousCanBeEaten) {
//                    sum -= (territory[side][i] + 1);
//                } else {
//                    sum -= territory[side][i];
//                }
//                previousCanBeEaten = true;
//            }
//        }
//        previousCanBeEaten = false;
//        sum += 2*points[side];
//
//        //computes points lost or probably because of the opponent
//        side = Awale.getAdversarySide(side);
//        length = territory[side].length;
//        for (int i = 0; i < length; i++) {
//            if (territory[side][i] > 3) {
//                sum--;
//            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
//                if (previousCanBeEaten) {
//                    sum += (territory[side][i] + 1);
//                } else {
//                    sum += territory[side][i];
//                }
//                previousCanBeEaten = true;
//            }
//        }
//        sum -= 2*points[side];
//
//        return sum;
//    }
//
//
//
//
//
//
//
//
//
////    public int evaluate(short side, short[][] territory, short[] points) {
////        int sum = 0;
////        int length = territory[side].length;
////        boolean previousCanBeEaten = false;
////
////        //computes points won or probably
////        for (int i = 0; i < length; i++) {
////            if (territory[side][i] > 3) {
////                sum++;
////            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
////                if (previousCanBeEaten) {
////                    sum -= (territory[side][i] + 1);
////                } else {
////                    sum -= territory[side][i];
////                }
////                previousCanBeEaten = true;
////            }
////        }
////        previousCanBeEaten = false;
////        sum += 2*points[side];
////
////        //computes points lost or probably because of the opponent
////        side = Awale.getAdversarySide(side);
////        length = territory[side].length;
////        for (int i = 0; i < length; i++) {
////            if (territory[side][i] > 3) {
////                sum--;
////            } else if (territory[side][i] > 0 && territory[side][i] <= 2) {
////                if (previousCanBeEaten) {
////                    sum += (territory[side][i] + 1);
////                } else {
////                    sum += territory[side][i];
////                }
////                previousCanBeEaten = true;
////            }
////        }
////        sum -= 2*points[side];
////
////        return sum;
////    }
//}
