package com.mifyai.mifyawale;

import android.util.Log;

/**
 * Created by HaroldKS on 14/03/2018.
 */

public class Obf implements ArtificialIntelligence
{
    private short profondeur = 6;
    private Awale maGrille[] = new Awale[profondeur + 1];
    private int minChezMoi=0,minChezLadversaire=6;
    private short side;


    public short searchBestPlay(Awale awale, short side){
        this.side = side;
        Log.d("MyApp",""+ side);
        int hk = minMax(this.profondeur, side, awale.points[side], awale.points[Awale.getAdversarySide(side)],awale);
        Log.d("MyApp",""+ hk);
        return (short) hk;
    }

    private short minMax(short depth, short side, short scoreAppelant, short scoreAdversaire, Awale awale){

        int indiceiDonnantMaximum=-12;
        int plus_grand_des_minimums=-48;
        int mon_nombre_de_cases_non_jouables=0;

        for(int i = 0; i < awale.size; i++){
            if(awale.canPlayHere(side, (short) i)){
                maGrille[0] = new Awale(awale);
                int minimum=49;
                Log.d("MyApp","Ici");
                int nombre_de_graines_capturees_par_moi=nombre_de_graines_capturees(i,side,0);
                int nombre_de_casesj_non_jouables=0;
                Log.d("MyApp",""+ awale.size + "  " + i);

                for(short j = 0; j < awale.size; j++){
                    Log.d("MyApp","Boucle J");
                    maGrille[0].changeCurrentSide();
                    if(maGrille[0].canPlayHere(Awale.getAdversarySide(side), j)){
                        Log.d("MyApp","Am in.");
                        maGrille[1] = new Awale(maGrille[0]);
                        Log.d("MyApp",""+ i + "  " + j);
                        Log.d("MyApp","Je veux entrer dans le piège");
                        int nombre_de_graines_capturees_par_ladversaire=nombre_de_graines_capturees(j,Awale.getAdversarySide(side),1);
                        maGrille[0].changeCurrentSide();
                        Log.d("MyApp","Je suis sorti piège");
                        int nombre_de_graines_que_je_gagne_en_differentiel=nombre_de_graines_capturees_par_moi-nombre_de_graines_capturees_par_ladversaire+differentiel(2,minimum-(nombre_de_graines_capturees_par_moi-nombre_de_graines_capturees_par_ladversaire));
                        if(nombre_de_graines_que_je_gagne_en_differentiel<minimum)
                        {
                            minimum=nombre_de_graines_que_je_gagne_en_differentiel;
                            Log.d("MyApp", "minimum "+minimum);

                        }
                    }
                    else {
                        maGrille[0].changeCurrentSide();
                        Log.d("MyApp","Je suis ici");
                        nombre_de_casesj_non_jouables++;
                    }
                    if(minimum<=plus_grand_des_minimums)
                    {
                        break;
                    }
                }
                if(nombre_de_casesj_non_jouables!=6 && plus_grand_des_minimums<minimum)
                {
                    plus_grand_des_minimums=minimum;
                    indiceiDonnantMaximum=i;
                }
            }
            else
            {
                mon_nombre_de_cases_non_jouables++;
            }

            Log.d("MyApp", "plusgrandmin "+plus_grand_des_minimums);

        }
        int retour=indiceiDonnantMaximum;
        Log.d("MyApp", "retour "+retour);
        //            if(!(mon_nombre_de_cases_non_jouables<5 && retour>=minChezMoi && retour<minChezMoi+6 && grille.getNombreBilleParCase(retour)!=0))
//            {
//                retour=grille.premiereCaseJouableOrdi(numeroJoueur+1);
//            }
        return (short) retour;
    }

    private int nombre_de_graines_capturees(int indice_de_la_case_choisie, short side, int grille){

        int sonMin=0;
        if(side==1)
        {
            sonMin=6;
        }

        int [] valeur = new int [12];
        int size = maGrille[grille].size;
        for( int i = 0; i < size; i++){
            valeur[i] = maGrille[grille].territory[side][i];
            valeur[i+size] = maGrille[grille].territory[Awale.getAdversarySide(side)][i];
        }
        int nombre_de_deplacements=valeur[indice_de_la_case_choisie];
        Log.d("MyApp", ""+nombre_de_deplacements);
        valeur[indice_de_la_case_choisie]=0;
        int indice=1+indice_de_la_case_choisie;
        for(int a=1;a<=nombre_de_deplacements;a++,indice++)
        {
            if(indice==indice_de_la_case_choisie)
            {
                indice++;
            }
            if(indice==12)
            {
                indice=0;
            }
            valeur[indice]++;
        }
        if(indice==0)
        {
            indice=11;
        }
        else
        {
            indice=indice-1;
        }
        int nombre_graines_capturees=0;
        while(indice>=0 && (valeur[indice]==2 || valeur[indice]==3) &&(indice>=sonMin) && (indice<sonMin+6))
        {
            nombre_graines_capturees+=valeur[indice];
            valeur[indice]=0;
            indice--;
        }
        maGrille[grille].play(side, (short) indice_de_la_case_choisie, maGrille[grille].territory, true);



        return  nombre_graines_capturees;
    }

    private int differentiel(int indiceDeMaGrille, int m){

        int mon_nombre_de_cases_non_jouables=0;
        int plus_grand_des_minimums=-48;

        for(int i=0;i<6;i++) {
            if (maGrille[-1 + indiceDeMaGrille].canPlayHere(this.side, (short) i)) {
                maGrille[indiceDeMaGrille] = new Awale(maGrille[-1 + indiceDeMaGrille]);

                int nombre_de_graines_capturees_par_moi = nombre_de_graines_capturees(i, this.side, indiceDeMaGrille);

                if (indiceDeMaGrille == profondeur) {
                    if (plus_grand_des_minimums < nombre_de_graines_capturees_par_moi) {
                        plus_grand_des_minimums = nombre_de_graines_capturees_par_moi;
                    }

                }
                else{

                    int nombre_de_casesj_non_jouables=0;
                    int minimum=49;
                    for(int j=0;j<6;j++)
                    {
                        if(maGrille[indiceDeMaGrille].canPlayHere(Awale.getAdversarySide(side),(short) j))
                        {
                            maGrille[1+indiceDeMaGrille]= new Awale(maGrille[indiceDeMaGrille]);
                            int nombre_de_graines_capturees_par_ladversaire=nombre_de_graines_capturees(j,Awale.getAdversarySide(side),1+indiceDeMaGrille);
                            int nombre_de_graines_que_je_gagne_en_differentiel=nombre_de_graines_capturees_par_moi-nombre_de_graines_capturees_par_ladversaire+differentiel(indiceDeMaGrille+2,minimum-(nombre_de_graines_capturees_par_moi-nombre_de_graines_capturees_par_ladversaire));
                            if(nombre_de_graines_que_je_gagne_en_differentiel<minimum)
                            {
                                minimum=nombre_de_graines_que_je_gagne_en_differentiel;
                            }
                        }
                        else
                        {
                            nombre_de_casesj_non_jouables++;
                        }
                        if(minimum<=plus_grand_des_minimums)
                        {
                            break;
                        }
                    }
                    if(nombre_de_casesj_non_jouables!=6 && plus_grand_des_minimums<minimum)
                    {
                        plus_grand_des_minimums=minimum;
                    }
                }
            }
            else{
                mon_nombre_de_cases_non_jouables++;
            }
            if(plus_grand_des_minimums>=m)
            {
                break;
            }
        }

        if(mon_nombre_de_cases_non_jouables==6)
        {
            return 0;
        }
        else
        {
            return plus_grand_des_minimums;
        }
    }
}
