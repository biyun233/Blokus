package Modele;

import java.io.Serializable;

public class Position implements Serializable {
    public int l;
    public int c;
    public Position(int i,int j){
        this.l=i;
        this.c=j;
    }
}