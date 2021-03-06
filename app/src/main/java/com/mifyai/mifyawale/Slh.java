package com.mifyai.mifyawale;

import android.util.Log;

/**
 * Created by HaroldKS on 14/03/2018.
 */

public class Slh implements ArtificialIntelligence
{
    private static class AlphaBetaData {
        public int alpha = Integer.MIN_VALUE;
        public int beta = Integer.MAX_VALUE;
    }

    private static final int MAX_DEPTH = 4;

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
                System.out.println("Yo evaluation :" + searchBestPlay.evaluation);
                System.out.print("taille de " +side);
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
                System.out.println("depth :" + depth);
                System.out.println("---------------------------------");
            }
            for(int i=0;i<6;i++)
            {
                if(awale.canPlayHere((short) 1,(short) i)){
                    value.position=(short) i;
                    break;
                }
            }
            return value;
        }
        boolean dernier=true;
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


    public int nbreCasesJouables(int side,short[][] territory)
    {
        int nbre=0;
        for(int i=0;i<5;i++)
        {
            nbre+=territory[side][i];
        }
        return nbre;
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
        int heuristique = 0;
        heuristique=nbreCasesJouables(0,territory)-nbreCasesJouables(0,territory);
        if(heuristique<0)
        {
            heuristique=0;
        }
        return heuristique;
    }









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
}
