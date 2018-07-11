package igor.com.br.tccrestwsandroid;

/**
 * Created by Igor on 15/06/2017.
 */

public class Constantes {

    public enum Perguntas{
        SATISFACAO(14), FREQUENCIA1(15),FREQUENCIA2(16),
        SAUDE1(0), SAUDE2(1), SAUDE3(2),SAUDE4(3),SAUDE5(4),
        SOCIAL(5),
        INTELECTO1(6),INTELECTO2(7),INTELECTO3(8),INTELECTO4(9),
        ARTISTICO1(10),ARTISTICO2(11),ARTISTICO3(12),ARTISTICO4(13);

        private final int valor;
        Perguntas(int valorOpcao){
            valor = valorOpcao;
        }
        public int getValor(){
            return valor;
        }
    }
}
