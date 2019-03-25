
import java.util.ArrayList;
import java.util.Arrays;

public class programa {
	//Comentario añadido desde 'feature/addComment'
    public static ArrayList<Integer> outputsNoAccesibles(int transiciones[][][], int ENTRADAS,int ESTADOS, int inaccesible) {

        ArrayList<Integer> salidas=new ArrayList<>();
            for (int j=0;j<ENTRADAS;j++) {
                for (int k=0;k<3;k++) {
                    if (transiciones[inaccesible][j][k] != -1) {
                        if (!salidas.contains(transiciones[inaccesible][j][k])) {
                            salidas.add(transiciones[inaccesible][j][k]);
                        }
                    }
                }
            }

        return salidas;
    }

    //Devuelve en un arraylist los estados inaccesibles
    public static ArrayList<Integer> estadosInaccesibles(int transiciones[][][], int ENTRADAS, int ESTADOS, int enlaces []) {
        boolean todosAccesibles=true;
        ArrayList<Integer> inaccesibles = new ArrayList<>();
        ArrayList<Integer> outputsConflictivos = new ArrayList<>();
        while(todosAccesibles){ //MIENTRAS NO ESTÉN TODOS ACCESIBLES
            todosAccesibles = false;
            int i = 0;
            while(i<enlaces.length){ //recorro los enlaces para ver cuales tienen 0 inputs
                if(enlaces[i]==0 && !inaccesibles.contains(i)) {
                    inaccesibles.add(i);
                    outputsConflictivos = outputsNoAccesibles(transiciones,ENTRADAS,ESTADOS,i);
                    int j=0;
                    while(j<outputsConflictivos.size()) {
                        if(outputsConflictivos.size() == 1 && enlaces[outputsConflictivos.get(j)] == 1){
                            enlaces[outputsConflictivos.get(j)]--;
                            todosAccesibles = true;
                        }else{
                            enlaces[outputsConflictivos.get(j)]--;
                        }
                         j++;
                    }
                }
            i++;
            }

        }
        return inaccesibles;
    }
    //DEVUELVE LAS LETRAS CON LAS QUE HAY ESTADOS INFINITOS EN UN ESTADO FINAL
    public static ArrayList<String> finalesInfinitos(int transiciones[][][],ArrayList<Integer> finales,int ENTRADAS,ArrayList<String>letras) {
        ArrayList<String> salidas = new ArrayList<>();
        for (int i=0;i<finales.size();i++) {
            for (int j = 0; j < ENTRADAS; j++) {
                for (int k = 0; k < 3; k++) {
                    if (transiciones[finales.get(i)][j][k] == finales.get(i) ) {
                        if (!salidas.contains(transiciones[i][j][k])) {
                            salidas.add(letras.get(j));
                        }
                    }
                }
            }
        }
        return salidas;
    }
    //DEVUELVE LOS ESTADOS FIN EN UN ARRAYLIST DE ENTEROS CON LAS POSICIONES DEL ARRAY DE ESTADOS
    public static ArrayList<Integer> getPosicionFinales(ArrayList<String> estados,ArrayList<String> estadosFin) {
        ArrayList<Integer> finales = new ArrayList<>();
        for(int i=0;i<estadosFin.size();i++){
            finales.add(estados.indexOf(estadosFin.get(i)));
        }
        return finales;
    }
    //DEVUELVE UN ARRAY CON EL NUMERO DE INPUTS DE CADA ESTADO
    public static int[] calculoInputs(int transiciones[][][],int ENTRADAS,int ESTADOS,int inicial) {
        int[] enlaces = new int[ESTADOS];
        for (int i=0;i<ESTADOS;i++) {
            for (int j = 0; j < ENTRADAS; j++) {
                for (int k = 0; k < 3; k++) {
                    if (transiciones[i][j][k] != i && transiciones[i][j][k]!= -1) {
                        enlaces[transiciones[i][j][k]]++;
                    }
                }
            }
        }
        enlaces[inicial]+=1;
        return enlaces;
    }
    //comprobar que no hay transiciones en la matriz
    public static boolean outputsEstadoInicial(int estadoInicial, int[][][] transiciones,int ENTRADAS){
        int i=0,j=0;
        boolean vacio=true;
        while(i<ENTRADAS && vacio){
            while(j<3 && vacio){ //profundidad
                if(transiciones[estadoInicial][i][j] != -1){
                  vacio = false;
                }
                j++;
            }
            i++;
        }
        return vacio; //devuelve true si está vacio
    }
    public static void test1(){

        //INPUTS
        String estadoInicial;
        int[][][] transiciones; //MATRIZ DE [ESTADOS][ENTRADAS][PROFUNDIDAD] //EL NIVEL DE PROFUNDIDAD ES POR SER NO DETERMINISTA
        ArrayList<String> entradas = new ArrayList<String>();
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> estadosFin = new ArrayList<String>();

        int ENTRADAS; //NUMERO ENTRADAS
        int ESTADOS; //NUMERO ESTADOS

        // CARGAMOS DATOS CASO DE PRUEBA 1
        estadoInicial = "S";

        entradas.add("A");
        entradas.add("B");
        entradas.add("C");

        estados.add("S");
        estados.add("A");
        estados.add("B");
        estados.add("C");
        estados.add("D");
        estados.add("E");
        estados.add("F");

        estadosFin.add("F");

        ArrayList<Integer> finales = getPosicionFinales(estados,estadosFin);
        ENTRADAS = entradas.size();
        ESTADOS = estados.size();

        transiciones = new int[][][]{{{1,6,4},{3,4,6},{0,-1,-1}}, //S
                {{-1,-1,-1},{-1,-1,-1}, {-1,-1,-1}}, //A
                {{-1,-1,-1},{1,2,6},{-1,-1,-1}},//B
                {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}},//C
                {{3,-1,-1},{-1,-1,-1},{5,-1,-1}},//D
                {{-1,-1,-1},{6,-1,-1},{-1,-1,-1}},//E
                {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}}};//F
        System.out.println("---------RESULTADOS DATOS DE TEST 1:----------");
        //COPROBACIÓN SI AUTÓMATA ACEPTA ESTADOS VACÍOS
        //COMPROBAR QUE NO HAY TRANSICIONES EN EL ESTADO INICIAL
        if(outputsEstadoInicial(estados.indexOf(estadoInicial),transiciones,ENTRADAS)){
            //COMPROBAR QUE EL ESTADO INICIAL ES UN ESTADO FINAL
            if(estadosFin.contains(estadoInicial)){
                System.out.println("El autómata acepta lenguaje vacío." + "\n");
            }
        }else{
            //COMPROBAR NÚMERO DE INPUTS DE CADA ESTADO
            int [] enlaces = calculoInputs(transiciones, ENTRADAS, ESTADOS,estados.indexOf(estadoInicial));
            //COMPROBAR LOS ESTADOS INACCESIBLES
            ArrayList<Integer> inaccesibles = estadosInaccesibles(transiciones,ENTRADAS,ESTADOS,enlaces);
            System.out.println("Estados inaccesibles: " + inaccesibles);
            System.out.println("Estados finales: " + finales);
            //ELIMINAR ESTADOS FINALES INACCESIBLES
            for(int i=0;i<finales.size();i++){
                if(inaccesibles.contains(finales.get(i))){
                    finales.remove(i);
                }
            }
            //COMPROBAR ESTADOS FINALES CON BUCLE
            ArrayList<String> infinitosAceptados = finalesInfinitos(transiciones,finales,ENTRADAS,entradas);
            //MOSTRAR EL LENGUAJE ACEPTADO
            if(infinitosAceptados.size()==0){
                System.out.println("El autómata no acepta lenguaje vacío ni infinito."+ "\n");
            }else {
                System.out.println("El autómata acepta lenguaje infinito con: " + infinitosAceptados+ "\n");
            }
        }
    }

    public static void test2(){

        //INPUTS
        String estadoInicial;
        int[][][] transiciones; //MATRIZ DE [ESTADOS][ENTRADAS][PROFUNDIDAD] //EL NIVEL DE PROFUNDIDAD ES POR SER NO DETERMINISTA
        ArrayList<String> entradas = new ArrayList<String>();
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> estadosFin = new ArrayList<String>();

        int ENTRADAS; //NUMERO ENTRADAS
        int ESTADOS; //NUMERO ESTADOS

        // CARGAMOS DATOS CASO DE PRUEBA 2
        estadoInicial = "q0";

        entradas.add("a");
        entradas.add("b");

        estados.addAll(Arrays.asList("q0","q1","q2","q3","q4","q5","q6","q7","q8","q9","q10","q11","q12","q13"));

        estadosFin.add("q7");
        ArrayList<Integer> finales = getPosicionFinales(estados,estadosFin);
        ENTRADAS = entradas.size();
        ESTADOS = estados.size();

        transiciones = new int[][][]{{{1,-1,-1},{8,-1,-1}}, //q0
                {{2,-1,-1},{-1,-1,-1}}, //q1
                {{-1,-1,-1},{3,-1,-1}},//q2
                {{4,6,-1},{6,-1,-1}},//q3
                {{-1,-1,-1},{5,-1,-1}},//q4
                {{4,6,-1},{-1,-1,-1}},//q5
                {{-1,-1,-1},{-1,-1,-1}},//q6
                {{6,-1,-1},{13,-1,-1}}, //q7
                {{-1,-1,-1},{9,-1,-1}},//q8
                {{10,-1,-1},{-1,-1,-1}},//q9
                {{-1,-1,-1},{11,13,-1}},//q10
                {{12,-1,-1},{-1,-1,-1}},//q11
                {{-1,-1,-1},{11,13,-1}},//q12
                {{-1,-1,-1},{-1,-1,-1}}};//q13

        System.out.println("---------RESULTADOS DATOS DE TEST 2:----------");
        //COPROBACIÓN SI AUTÓMATA ACEPTA ESTADOS VACÍOS
        //COMPROBAR QUE NO HAY TRANSICIONES EN EL ESTADO INICIAL
        if(outputsEstadoInicial(estados.indexOf(estadoInicial),transiciones,ENTRADAS)){
            //COMPROBAR QUE EL ESTADO INICIAL ES UN ESTADO FINAL
            if(estadosFin.contains(estadoInicial)){
                System.out.println("El autómata acepta lenguaje vacío." + "\n");
            }
        }else{
            //COMPROBAR NÚMERO DE INPUTS DE CADA ESTADO
            int [] enlaces = calculoInputs(transiciones, ENTRADAS, ESTADOS,estados.indexOf(estadoInicial));
            //COMPROBAR LOS ESTADOS INACCESIBLES
            ArrayList<Integer> inaccesibles = estadosInaccesibles(transiciones,ENTRADAS,ESTADOS,enlaces);
            System.out.println("Estados inaccesibles: " + inaccesibles);
            System.out.println("Estados finales: " + finales);
            //ELIMINAR ESTADOS FINALES INACCESIBLES
            for(int i=0;i<finales.size();i++){
                if(inaccesibles.contains(finales.get(i))){
                    finales.remove(i);
                }
            }
            //COMPROBAR ESTADOS FINALES CON BUCLE
            ArrayList<String> infinitosAceptados = finalesInfinitos(transiciones,finales,ENTRADAS,entradas);
            //MOSTRAR EL LENGUAJE ACEPTADO
            if(infinitosAceptados.size()==0){
                System.out.println("El autómata no acepta lenguaje vacío ni infinito." + "\n");
            }else {
                System.out.println("El autómata acepta lenguaje infinito con: " + infinitosAceptados + "\n");
            }
        }
    }

    public static void test3(){

        //INPUTS
        String estadoInicial;
        int[][][] transiciones; //MATRIZ DE [ESTADOS][ENTRADAS][PROFUNDIDAD] //EL NIVEL DE PROFUNDIDAD ES POR SER NO DETERMINISTA
        ArrayList<String> entradas = new ArrayList<String>();
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> estadosFin = new ArrayList<String>();

        int ENTRADAS; //NUMERO ENTRADAS
        int ESTADOS; //NUMERO ESTADOS

        //DATOS CASO DE PRUEBA SIMILAR AL 1 AÑADIENDO UN BUCLE INFINITO EN ESTADO FINAL
        estadoInicial = "S";

        entradas.add("A");
        entradas.add("B");
        entradas.add("C");

        estados.add("S");
        estados.add("A");
        estados.add("B");
        estados.add("C");
        estados.add("D");
        estados.add("E");
        estados.add("F");

        estadosFin.add("F");
        ArrayList<Integer> finales = getPosicionFinales(estados,estadosFin);
        ENTRADAS = entradas.size();
        ESTADOS = estados.size();

        transiciones = new int[][][]{{{1,6,4},{3,4,6},{0,-1,-1}}, //S
                {{-1,-1,-1},{-1,-1,-1}, {-1,-1,-1}}, //A
                {{-1,-1,-1},{1,2,6},{-1,-1,-1}},//B
                {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}},//C
                {{3,-1,-1},{-1,-1,-1},{5,-1,-1}},//D
                {{-1,-1,-1},{6,-1,-1},{-1,-1,-1}},//E
                {{-1,-1,-1},{-1,6,-1},{-1,6,-1}}};//F

        System.out.println("---------RESULTADOS DATOS DE TEST 3:----------");
        //COPROBACIÓN SI AUTÓMATA ACEPTA ESTADOS VACÍOS
        //COMPROBAR QUE NO HAY TRANSICIONES EN EL ESTADO INICIAL
        if(outputsEstadoInicial(estados.indexOf(estadoInicial),transiciones,ENTRADAS)){
            //COMPROBAR QUE EL ESTADO INICIAL ES UN ESTADO FINAL
            if(estadosFin.contains(estadoInicial)){
                System.out.println("El autómata acepta lenguaje vacío." + "\n");
            }
        }else{
            //COMPROBAR NÚMERO DE INPUTS DE CADA ESTADO
            int [] enlaces = calculoInputs(transiciones, ENTRADAS, ESTADOS,estados.indexOf(estadoInicial));
            //COMPROBAR LOS ESTADOS INACCESIBLES
            ArrayList<Integer> inaccesibles = estadosInaccesibles(transiciones,ENTRADAS,ESTADOS,enlaces);
            System.out.println("Estados inaccesibles: " + inaccesibles);
            System.out.println("Estados finales: " + finales);
            //ELIMINAR ESTADOS FINALES INACCESIBLES
            for(int i=0;i<finales.size();i++){
                if(inaccesibles.contains(finales.get(i))){
                    finales.remove(i);
                }
            }
            //COMPROBAR ESTADOS FINALES CON BUCLE
            ArrayList<String> infinitosAceptados = finalesInfinitos(transiciones,finales,ENTRADAS,entradas);
            //MOSTRAR EL LENGUAJE ACEPTADO
            if(infinitosAceptados.size()==0){
                System.out.println("El autómata no acepta lenguaje vacío ni infinito." + "\n");
            }else {
                System.out.println("El autómata acepta lenguaje infinito con: " + infinitosAceptados + "\n");
            }
        }
    }

    public static void test4(){
        //TEST VACIO
        //INPUTS
        String estadoInicial;
        int[][][] transiciones; //MATRIZ DE [ESTADOS][ENTRADAS][PROFUNDIDAD] //EL NIVEL DE PROFUNDIDAD ES POR SER NO DETERMINISTA
        ArrayList<String> entradas = new ArrayList<String>();
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> estadosFin = new ArrayList<String>();

        int ENTRADAS; //NUMERO ENTRADAS
        int ESTADOS; //NUMERO ESTADOS

        // CARGAMOS DATOS CASO DE PRUEBA 1
        estadoInicial = "S";

        entradas.add("A");
        entradas.add("B");
        entradas.add("C");

        estados.add("S");
        estados.add("A");
        estados.add("B");
        estados.add("C");
        estados.add("D");
        estados.add("E");
        estados.add("F");

        estadosFin.add("F");
        estadosFin.add("S");

        ArrayList<Integer> finales = getPosicionFinales(estados,estadosFin);
        ENTRADAS = entradas.size();
        ESTADOS = estados.size();

        transiciones = new int[][][]{{{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}}, //S
                {{-1,-1,-1},{-1,-1,-1}, {-1,-1,-1}}, //A
                {{-1,-1,-1},{1,2,6},{-1,-1,-1}},//B
                {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}},//C
                {{3,-1,-1},{-1,-1,-1},{5,-1,-1}},//D
                {{-1,-1,-1},{6,-1,-1},{-1,-1,-1}},//E
                {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}}};//F
        System.out.println("---------RESULTADOS DATOS DE TEST 4:----------");
        //COPROBACIÓN SI AUTÓMATA ACEPTA ESTADOS VACÍOS
        //COMPROBAR QUE NO HAY TRANSICIONES EN EL ESTADO INICIAL
        if(outputsEstadoInicial(estados.indexOf(estadoInicial),transiciones,ENTRADAS)){
            //COMPROBAR QUE EL ESTADO INICIAL ES UN ESTADO FINAL
            if(estadosFin.contains(estadoInicial)){
                System.out.println("El autómata acepta lenguaje vacío." + "\n");
            }
        }else{
            //COMPROBAR NÚMERO DE INPUTS DE CADA ESTADO
            int [] enlaces = calculoInputs(transiciones, ENTRADAS, ESTADOS,estados.indexOf(estadoInicial));
            //COMPROBAR LOS ESTADOS INACCESIBLES
            ArrayList<Integer> inaccesibles = estadosInaccesibles(transiciones,ENTRADAS,ESTADOS,enlaces);
            System.out.println("Estados inaccesibles: " + inaccesibles);
            System.out.println("Estados finales: " + finales);
            //ELIMINAR ESTADOS FINALES INACCESIBLES
            for(int i=0;i<finales.size();i++){
                if(inaccesibles.contains(finales.get(i))){
                    finales.remove(i);
                }
            }
            //COMPROBAR ESTADOS FINALES CON BUCLE
            ArrayList<String> infinitosAceptados = finalesInfinitos(transiciones,finales,ENTRADAS,entradas);
            //MOSTRAR EL LENGUAJE ACEPTADO
            if(infinitosAceptados.size()==0){
                System.out.println("El autómata no acepta lenguaje vacío ni infinito."+ "\n");
            }else {
                System.out.println("El autómata acepta lenguaje infinito con: " + infinitosAceptados+ "\n");
            }
        }
    }

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

}
