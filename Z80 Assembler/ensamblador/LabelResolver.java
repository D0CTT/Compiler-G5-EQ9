package Ensamblador;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LabelResolver {
    public static Map<String, String> primeraPasada(List<String> lines){
        Map<String, String> tabla_etiquetas = new TreeMap<>();
        int address_decimal = 0;
        String address_hexadecimal;

        for (String line : lines){
            Instruction instruction = Instruction.parseLine(line);
            if (instruction == null) continue;
            
            if (instruction.getLabel() != null) {
                address_hexadecimal = String.format("%04X", address_decimal);
                tabla_etiquetas.put(instruction.getLabel().toUpperCase(), address_hexadecimal);
            }
            if(instruction.getMnemonic()!=null){
                if(instruction.getMnemonic().equalsIgnoreCase("CPU") || instruction.getMnemonic().equalsIgnoreCase("HOF")) continue;
                if(instruction.getMnemonic().equalsIgnoreCase("ORG")){
                    String offset = instruction.getOperand();
                    if(offset.endsWith("H")){
                        String binary = offset.substring(0, offset.length() - 1);
                        address_decimal = Integer.parseInt(binary, 16);
                    }else{
                        address_decimal = Integer.parseInt(offset);
                    }
                    continue;
                }
            }else{
                continue;
            }
            String opcode = CodeTable.getHexa(instruction.getMnemonic().toUpperCase(), instruction.getOperand(), 1, tabla_etiquetas, address_decimal);
            address_decimal += opcode.length() / 2;
            Principal.size = Principal.size + opcode.length() / 2;
            //System.out.println(CodeTable.getHexa(instruction.getMnemonic().toUpperCase(), instruction.getOperand(), 1, tabla_etiquetas, address_decimal));
        }
        return tabla_etiquetas;
    }

    public static void segundaPasadaLST(List<String> lines, Map<String, String> tabla_etiquetas, String outputPath){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            int address_decimal = 0;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Instruction instruction = Instruction.parseLine(line);
                if (instruction == null) continue;
                if(instruction.getMnemonic()!=null){
                    if(instruction.getMnemonic().equalsIgnoreCase("CPU") || instruction.getMnemonic().equalsIgnoreCase("HOF")){
                        bw.write(String.format("%-20s %-3s %-20s", String.format("%04X", address_decimal), instruction.getMnemonic(), instruction.getOperand())+"\n");
                        continue;
                    } 
                    if(instruction.getMnemonic().equalsIgnoreCase("ORG")){
                        String offset = instruction.getOperand();
                        if(offset.endsWith("H")){
                            String binary = offset.substring(0, offset.length() - 1);
                            address_decimal = Integer.parseInt(binary, 16);
                        }else{
                            address_decimal = Integer.parseInt(offset);
                        }
                        bw.write(String.format("%-20s %-3s %-20s", String.format("%04X", address_decimal), instruction.getMnemonic(), instruction.getOperand())+"\n");
                        continue;
                    }
                }else{
                    bw.write(String.format("%4s              %-6s", String.format("%04X", address_decimal), instruction.getLabel()+":")+"\n");
                    continue;
                }
                String opcode = CodeTable.getHexa(instruction.getMnemonic().toUpperCase(), instruction.getOperand(), 2, tabla_etiquetas, address_decimal);
                

                if(instruction.getLabel() != null){
                    bw.write(String.format("%-4s %-12s %-6s", String.format("%04X", address_decimal), opcode, instruction.getLabel()+":"));
                }else{
                    bw.write(String.format("%-4s %-15s", String.format("%04X", address_decimal), opcode));
                }
                bw.write(" "+instruction.getMnemonic());
                if(instruction.getOperand()!=null){
                    bw.write(" "+instruction.getOperand()+"\n");
                }else{
                    if(i<lines.size()-1)
                        bw.write("\n");
                    else
                        bw.write(" ");
                }
                address_decimal += opcode.length() / 2;
            }
            bw.write("0000\t\t\tEND\n");
            bw.write('\f');
            int i=0;
            for(Map.Entry<String, String> actual : tabla_etiquetas.entrySet()){
                bw.write(String.format("%-5s %-26s", actual.getValue(), actual.getKey()));
                i++;
                if(i%3==0)
                    bw.write("\n");
            }
            bw.write('\f');
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void segundaPasadaHEX(List<String> lines, Map<String, String> tabla_etiquetas, String outputPath){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))){
            int left_size = Principal.size;
            int address_decimal = 0;
            int inicio = 0;
            int linea, contador=0, checksum, contador_renglones=0, z=0, actual_size=0;
            String hexa_inicio;
            List<String> hexas = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++){
                String line = lines.get(i);
                Instruction instruction = Instruction.parseLine(line);
                if (instruction == null) continue;
                if(instruction.getMnemonic()!=null){
                    if(instruction.getMnemonic().equalsIgnoreCase("CPU") || instruction.getMnemonic().equalsIgnoreCase("HOF")){
                        continue;
                    } 
                    if(instruction.getMnemonic().equalsIgnoreCase("ORG")){
                        if(hexas.size()>0){
                            left_size = hexas.size()-actual_size;
                            if(z>0){
                                contador_renglones++;
                                z=0;
                            }
                            for(int k=0; k<contador_renglones; k++){
                                checksum=0;
                                bw.write(":");                                          //Cada línea en el archivo .hex comienza con un carácter de dos puntos :.
                                if(left_size-16>=0){
                                    linea = 16;
                                    bw.write(String.format("%02x", linea).toUpperCase());                     //Los dos siguientes caracteres (un byte) representan el número de bytes de datos en esta línea.
                                    left_size = left_size-16;
                                }else{
                                    linea = left_size%16;
                                    bw.write(String.format("%02x", linea).toUpperCase());   //Los dos siguientes caracteres (un byte) representan el número de bytes de datos en esta línea.
                                }
                                checksum = checksum + linea;
                                hexa_inicio = String.format("%04x", inicio).toUpperCase();
                                bw.write(hexa_inicio);            //Los siguientes cuatro caracteres (dos bytes) indican la dirección de memoria donde se deben cargar estos datos.
                                checksum = checksum + Integer.parseInt(hexa_inicio.substring(0, 2), 16) + Integer.parseInt(hexa_inicio.substring(2, 4), 16);
                                inicio = inicio + 16;
                                bw.write("00");                                     //Los siguientes dos caracteres (un byte) especifican el tipo de registro. Los valores comunes son.
                                while(linea>0){
                                    bw.write(hexas.get(contador));
                                    actual_size++;
                                    checksum = checksum + Integer.parseInt(hexas.get(contador), 16);
                                    contador++;
                                    linea--;
                                }
                                bw.write(String.format("%02X", 256-(checksum % 256)));
                                
                                bw.write("\n");
                            }
                            contador_renglones = 0;
                        }
                        String offset = instruction.getOperand();
                        if(offset.endsWith("H")){
                            String binary = offset.substring(0, offset.length() - 1);
                            address_decimal = Integer.parseInt(binary, 16);
                            inicio = Integer.parseInt(binary, 16);
                        }else{
                            address_decimal = Integer.parseInt(offset);
                            inicio = Integer.parseInt(offset);
                        }
                        
                        continue;
                    }
                }else{
                    continue;
                }
                String opcode = CodeTable.getHexa(instruction.getMnemonic().toUpperCase(), instruction.getOperand(), 2, tabla_etiquetas, address_decimal);
                for (int j = 0; j < opcode.length(); j += 2) {
                    if (j + 2 <= opcode.length()) {
                        hexas.add(opcode.substring(j, j + 2));
                        z++;
                        if(z==16){
                            contador_renglones++;
                            z=0;
                        }
                    }
                }
                address_decimal += opcode.length() / 2;
            }
            left_size = hexas.size()-actual_size;
            if(z>0){
                contador_renglones++;
                z=0;
            }
            for(int i=0; i<contador_renglones; i++){
                checksum=0;
                bw.write(":");                                          //Cada línea en el archivo .hex comienza con un carácter de dos puntos :.
                if(left_size-16>=0){
                    linea = 16;
                    bw.write(String.format("%02x", linea).toUpperCase());                     //Los dos siguientes caracteres (un byte) representan el número de bytes de datos en esta línea.
                    left_size = left_size-16;
                }else{
                    linea = left_size%16;
                    bw.write(String.format("%02x", linea).toUpperCase());   //Los dos siguientes caracteres (un byte) representan el número de bytes de datos en esta línea.
                }
                checksum = checksum + linea;
                hexa_inicio = String.format("%04x", inicio).toUpperCase();
                bw.write(hexa_inicio);            //Los siguientes cuatro caracteres (dos bytes) indican la dirección de memoria donde se deben cargar estos datos.
                checksum = checksum + Integer.parseInt(hexa_inicio.substring(0, 2), 16) + Integer.parseInt(hexa_inicio.substring(2, 4), 16);
                inicio = inicio + 16;
                bw.write("00");                                     //Los siguientes dos caracteres (un byte) especifican el tipo de registro. Los valores comunes son.
                while(linea>0){
                    bw.write(hexas.get(contador));
                    checksum = checksum + Integer.parseInt(hexas.get(contador), 16);
                    contador++;
                    linea--;
                }
                bw.write(String.format("%02X", 256-(checksum % 256)));
                
                bw.write("\n");
            }
            bw.write(":00000001FF\n");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}