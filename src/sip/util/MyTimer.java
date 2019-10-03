/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

import java.util.Timer;
import java.util.TimerTask;
import sip.tramitacao.TramitacaoMonitorFrame;

/**
 *
 * @author jallisson
 */
public class MyTimer {
    public static final long TEMPO = (1000 * 60); // atualiza o site a cada 1 minuto
      
        public void teste(){
            System.out.println("inicio");
		Timer timer = null;
		if (timer == null) {
			timer = new Timer();
			TimerTask tarefa = new TimerTask() {
				public void run() {
					
						System.out.println("Teste agendador");
						//chamar metodo
                                                TramitacaoMonitorFrame monitor = new TramitacaoMonitorFrame();
                                                monitor.atualizaTabela();
					
				}
			};
			timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
		}
        }
}
