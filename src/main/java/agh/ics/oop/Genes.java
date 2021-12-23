package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;
import static java.lang.Math.random;

public class Genes {
    int[] genes;

    public Genes(){
        this.genes = new int[32];
    }

    public void createGenesForAdamAndEwa(){
        for(int i = 0; i < 32 ; i++){
            this.genes[i] = (int)(random()*8);
        }
        Arrays.sort(this.genes);
    }

    public void createGenesForChild(int[] genes1,int[] genes2, int point){
        for(int i = 0; i <point; i++){
            this.genes[i]= genes1[i];
        }

        for(int i=point;i<32;i++){
            this.genes[i] = genes2[i];
        }
    }

    public boolean equalGenes(Genes genes1){
        for(int i = 0 ;i<32 ;i++){
            if(this.genes[i]!=genes1.genes[i]) return false;
        }
        return true;
    }

    public int drawGene(){
        int rnd = new Random().nextInt(this.genes.length);
        return this.genes[rnd];
    }
}
