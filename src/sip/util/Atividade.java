/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

import java.util.Scanner;

/**
 *
 * @author jallisson
 */
public class Atividade {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner entrada = new Scanner(System.in);
        int time = 0;
        int contJogador = 0;
        int idJogador = 0;
        int peso  = 0;
        int somaPeso = 0;
        int mediaPeso = 0;
        int mediaPesoTime = 0;
        int divisaoTime = 0;
        String terminar = "nao";
        
         for (int i = 1; i <= 4; i++) {
        contJogador = + i;
        System.out.println("Digite o peso do jogador: "+contJogador);
        peso = entrada.nextInt();
        System.out.println("Digite o qual time do jogador: "+contJogador);
        time = entrada.nextInt();
        
        somaPeso += peso;   
        }
        mediaPeso = somaPeso / contJogador;
        divisaoTime = somaPeso / time;
        mediaPesoTime = divisaoTime /time;
        System.out.printf("Media de Peso dos jogadores: %d\n",mediaPeso);
        System.out.printf("Media de Peso por Time: %d\n",mediaPesoTime);
        
    }
}
