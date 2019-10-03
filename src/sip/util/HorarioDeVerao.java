/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 *
 * @author jallisson
 */
public class HorarioDeVerao {
        /**
     * Metodo que pega a hora correta contando como horario de verao
     * @return Retorna a data e hora corretas com o horario de verao
     * @version 1.0
     * @author Anderson
     * 
     */
    public String horarioVerao() {
        String retorno = "";
        // Cria uma TIME ZONE correspondente ao horário de Brasília
        SimpleTimeZone pdt = new
            SimpleTimeZone(-3 * 60 * 60 * 1000,"GMT-3:00");
        // Seta as regras para o horário de verão Brasileiro
        // Começando no primeiro domingo após o dia primeiro
        //original pdt.setStartRule(Calendar.OCTOBER, 1, Calendar.SUNDAY,0);
        pdt.setStartRule(Calendar.JANUARY, 1, Calendar.SUNDAY,0);
        // Terminando no último domingo do mês de Fevereiro
        //pdt.setEndRule(Calendar.DECEMBER, -1, Calendar.SUNDAY,0);
        // Instanciando um GregorianCalendar com com a timezone de BSB
        // e levando em consideração as regras do horário deverão.
        Calendar dataHoje = new GregorianCalendar(pdt);
        // Retorna a data e hora
        retorno = dataHoje.get(dataHoje.DATE) + "/" + (dataHoje.get(dataHoje.MONTH) + 1) + "/" 
            + dataHoje.get(dataHoje.YEAR) + "  "
            + dataHoje.get(dataHoje.HOUR_OF_DAY) +":" + dataHoje.get(dataHoje.MINUTE) + 
            ":" + dataHoje.get(dataHoje.SECOND);
        // Cria um formatador
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
        // Formata a data e a hora
        try {
            java.util.Date data = formatador.parse(retorno);
            retorno = formatador.format(data);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }
}
