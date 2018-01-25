package com.mifyai.mifyawale;

/**
 * This interface define function used by AI algoritms.
 * Created by Parikiri13 on 25/01/2018.
 */

public interface ArtificialIntelligence {

    /**
     * Searches the best position to play for the current state of the awale on
     * the given side.
     *
     * @return the best position or <code>Short.MIN_VALUE</code>, if none is found
     */
    short searchBestPlay(Awale awale, short side);
}
