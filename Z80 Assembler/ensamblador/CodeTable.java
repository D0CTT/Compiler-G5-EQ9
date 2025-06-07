package Ensamblador;
import java.util.List;
import java.util.Map;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTable{

    public static String getHexa(String mnemonic, String operand, int no_pasada, Map<String, String> tabla_etiquetas, int PC){
        Dictionary<String, String> registers = new Hashtable<>();
        registers.put("B", "000");
        registers.put("C", "001");
        registers.put("D", "010");
        registers.put("E", "011");
        registers.put("H", "100");
        registers.put("L", "101");
        registers.put("A", "111");

        Dictionary<String, String> ddpair = new Hashtable<>();
        ddpair.put("BC", "00");
        ddpair.put("DE", "01");
        ddpair.put("HL", "10");
        ddpair.put("SP", "11");

        Dictionary<String, String> qqpair = new Hashtable<>();
        qqpair.put("BC", "00");
        qqpair.put("DE", "01");
        qqpair.put("HL", "10");
        qqpair.put("AF", "11");

        Dictionary<String, String> ppReg = new Hashtable<>();
        ppReg.put("BC", "00");
        ppReg.put("DE", "01");
        ppReg.put("IX", "10");
        ppReg.put("SP", "11");

        Dictionary<String, String> rrReg = new Hashtable<>();
        rrReg.put("BC", "00");
        rrReg.put("DE", "01");
        rrReg.put("IY", "10");
        rrReg.put("SP", "11");
        
        Dictionary<String, String> bit_tested = new Hashtable<>();
        bit_tested.put("0", "000");
        bit_tested.put("1", "001");
        bit_tested.put("2", "010");
        bit_tested.put("3", "011");
        bit_tested.put("4", "100");
        bit_tested.put("5", "101");
        bit_tested.put("6", "110");
        bit_tested.put("7", "111");

        Dictionary<String, String> condicion = new Hashtable<>();
        condicion.put("NZ", "000");
        condicion.put("Z", "001");
        condicion.put("NC", "010");
        condicion.put("C", "011");
        condicion.put("PO", "100");
        condicion.put("PE", "101");
        condicion.put("P", "110");
        condicion.put("M", "111");

        Dictionary<String, String> RST_P = new Hashtable<>();
        RST_P.put("00H", "000");
        RST_P.put("08H", "001");
        RST_P.put("10H", "010");
        RST_P.put("18H", "011");
        RST_P.put("20H", "100");
        RST_P.put("28H", "101");
        RST_P.put("30H", "110");
        RST_P.put("38H", "111");

        List<String> mnemonics = List.of("LD", "PUSH", "POP", "EX", "EXX", "LDI", "LDIR", "LDD", "LDDR", "CPI", "CPIR", "CPD", "CPDR", "ADD", "ADC", "SUB", "SBC", "AND", "OR",
                                        "XOR", "CP", "INC", "DEC", "DAA", "CPL", "NEG", "CCF", "SCF", "NOP", "HALT", "DI", "EI", "IM", "IM2", "RLCA", "RLA", "RRCA", "RRA", "RLC",
                                        "RL", "RRC", "RR", "SLA", "SRA", "SRL", "RLD", "RRD", "BIT", "SET", "RES", "JP", "JR", "DJNZ", "CALL", "RET", "RETI", "RETN", "RST", "IN", "INI",
                                        "INIR", "IND", "INDR", "OUT", "OUTI", "OTIR", "OUTD", "OTDR"
                                );
        String hexadecimal = "";
        StringBuilder sb = new StringBuilder();
        for(String str : mnemonics){
            if(mnemonic.equals(str)){
                switch (str){
                    case "LD":
                        Pattern r_r = Pattern.compile("([ABCDEHL]),([ABCDEHL])");
                        Pattern r_n = Pattern.compile("([ABCDEHL]),(-?[0-9]+)");
                        Pattern r_HL = Pattern.compile("([ABCDEHL]),(\\(HL\\))");
                        Pattern r_IX_d = Pattern.compile("([ABCDEHL]),\\(IX\\+(-?[0-9]+)\\)");
                        Pattern r_IY_d = Pattern.compile("([ABCDEHL]),\\(IY\\+(-?[0-9]+)\\)");
                        Pattern HL_r = Pattern.compile("(\\(HL\\)),([ABCDEHL])");
                        Pattern IX_d_r = Pattern.compile("\\(IX\\+(-?[0-9]+)\\),([ABCDEHL])");
                        Pattern IY_d_r = Pattern.compile("\\(IY\\+(-?[0-9]+)\\),([ABCDEHL])");
                        Pattern HL_n = Pattern.compile("(\\(HL\\)),(-?[0-9]+)");
                        Pattern IX_d_n = Pattern.compile("\\(IX\\+(-?[0-9]+)\\),(-?[0-9]+)");
                        Pattern IY_d_n = Pattern.compile("\\(IY\\+(-?[0-9]+)\\),(-?[0-9]+)");
                        Pattern A_BC = Pattern.compile("(A),(\\(BC\\))");
                        Pattern A_DE = Pattern.compile("(A),(\\(DE\\))");
                        Pattern A_nn = Pattern.compile("(A),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern BC_A = Pattern.compile("(\\(BC\\)),(A)");
                        Pattern DE_A = Pattern.compile("(\\(DE\\)),(A)");
                        Pattern nn_A = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(A)$");
                        Pattern A_I = Pattern.compile("(A),(I)");
                        Pattern A_R = Pattern.compile("(A),(R)");
                        Pattern I_A = Pattern.compile("(I),(A)");
                        Pattern R_A = Pattern.compile("(R),(A)");
                        Pattern dd_nn_no_parentesis = Pattern.compile("(BC|DE|HL|SP),(\\d+|[A-F\\d]+H)$");
                        Pattern IX_nn_no_parentesis = Pattern.compile("(IX),(\\d+|[A-F\\d]+H)$");
                        Pattern IY_nn_no_parentesis = Pattern.compile("(IY),(\\d+|[A-F\\d]+H)$");
                        Pattern HL_nn = Pattern.compile("(HL),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern dd_nn = Pattern.compile("(BC|DE|HL|SP),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern IX_nn = Pattern.compile("(IX),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern IY_nn = Pattern.compile("(IY),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern nn_HL = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(HL)$");
                        Pattern nn_dd = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(BC|DE|HL|SP)$");
                        Pattern nn_IX = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(IX)$");
                        Pattern nn_IY = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(IY)$");
                        Pattern SP_HL = Pattern.compile("(SP),(HL)");
                        Pattern SP_IX = Pattern.compile("(SP),(IX)");
                        Pattern SP_IY = Pattern.compile("(SP),(IY)");

                        Matcher matcher;
                        if((matcher=r_r.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("01").append(registers.get(r1)).append(registers.get(r2));
                        }else if((matcher=r_n.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("00").append(registers.get(r1)).append("110");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=r_HL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("01").append(registers.get(r1)).append("110");
                        }else if((matcher=r_IX_d.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110101").append(registers.get(r1)).append("110");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=r_IY_d.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110101").append(registers.get(r1)).append("110");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=HL_r.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("01110").append(registers.get(r2));
                        }else if((matcher=IX_d_r.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110101110").append(registers.get(r2));
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_r.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110101110").append(registers.get(r2));
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_n.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("00110110");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=IX_d_n.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110100110110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=IY_d_n.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110100110110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                        }else if((matcher=A_BC.matcher(operand)).matches()){
                            sb.append("00001010");  
                        }else if((matcher=A_DE.matcher(operand)).matches()){
                            sb.append("00011010");  
                        }else if((matcher=A_nn.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("00111010");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=BC_A.matcher(operand)).matches()){
                            sb.append("00000010"); 
                        }else if((matcher=DE_A.matcher(operand)).matches()){
                            sb.append("00010010"); 
                        }else if((matcher=nn_A.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00110010");
                            if(r1.endsWith("H")){
                                String binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=A_I.matcher(operand)).matches()){
                            sb.append("1110110101010111"); 
                        }else if((matcher=A_R.matcher(operand)).matches()){
                            sb.append("1110110101011111"); 
                        }else if((matcher=I_A.matcher(operand)).matches()){
                            sb.append("1110110101000111"); 
                        }else if((matcher=R_A.matcher(operand)).matches()){
                            sb.append("1110110101001111"); 
                        }else if((matcher=dd_nn_no_parentesis.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("00").append(ddpair.get(r1)).append("0001");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=IX_nn_no_parentesis.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("1101110100100001");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=IY_nn_no_parentesis.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("1111110100100001");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=HL_nn.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("00101010");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=dd_nn.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1110110101").append(ddpair.get(r1)).append("1011");
                            String binary;
                            if(r2.endsWith("H")){
                                binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=IX_nn.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("1101110100101010");
                            String binary;
                            if(r2.endsWith("H")){
                                binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=IY_nn.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("1111110100101010");
                            String binary;
                            if(r2.endsWith("H")){
                                binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=nn_HL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00100010");
                            String binary;
                            if(r1.endsWith("H")){
                                binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=nn_dd.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1110110101").append(ddpair.get(r2)).append("0011");
                            String binary;
                            if(r1.endsWith("H")){
                                binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=nn_IX.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110100100010");
                            String binary;
                            if(r1.endsWith("H")){
                                binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=nn_IY.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110100100010");
                            String binary;
                            if(r1.endsWith("H")){
                                binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                            }else{
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                            }
                            sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                        }else if((matcher=SP_HL.matcher(operand)).matches()){
                            sb.append("11111001"); 
                        }else if((matcher=SP_IX.matcher(operand)).matches()){
                            sb.append("1101110111111001"); 
                        }else if((matcher=SP_IY.matcher(operand)).matches()){
                            sb.append("1111110111111001"); 
                        }
                        break;
                    case "PUSH":
                        Pattern qq = Pattern.compile("(BC|DE|HL|AF)");
                        Pattern IX = Pattern.compile("(IX)");
                        Pattern IY = Pattern.compile("(IY)");

                        if((matcher=qq.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(qqpair.get(r1)).append("0101");
                        }else if((matcher=IX.matcher(operand)).matches()){
                            sb.append("1101110111100101");
                        }else if((matcher=IY.matcher(operand)).matches()){
                            sb.append("1111110111100101");
                        }
                        break;
                    
                    case "POP":
                        Pattern qq_pop = Pattern.compile("(BC|DE|HL|AF)");
                        Pattern IX_pop = Pattern.compile("(IX)");
                        Pattern IY_pop = Pattern.compile("(IY)");

                        if((matcher=qq_pop.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(qqpair.get(r1)).append("0001");
                        }else if((matcher=IX_pop.matcher(operand)).matches()){
                            sb.append("1101110111100001");
                        }else if((matcher=IY_pop.matcher(operand)).matches()){
                            sb.append("1111110111100001");
                        }
                        break;

                    case "EX":
                        Pattern DE_HL_EX = Pattern.compile("(DE,HL)");
                        Pattern AF_AFC_EX = Pattern.compile("(AF,AF')");
                        Pattern SP_HL_EX = Pattern.compile("(\\(SP\\),HL)");
                        Pattern SP_IX_EX = Pattern.compile("(\\(SP\\),IX)");
                        Pattern SP_IY_EX = Pattern.compile("(\\(SP\\),IY)");

                        if((matcher=DE_HL_EX.matcher(operand)).matches()){
                            sb.append("11101011");
                        }else if((matcher=AF_AFC_EX.matcher(operand)).matches()){
                            sb.append("00001000");
                        }else if((matcher=SP_HL_EX.matcher(operand)).matches()){
                            sb.append("11100011");
                        }else if((matcher=SP_IX_EX.matcher(operand)).matches()){
                            sb.append("1101110111100011");
                        }else if((matcher=SP_IY_EX.matcher(operand)).matches()){
                            sb.append("1111110111100011");
                        }
                        break;
                    case "EXX":
                        if(operand==null){
                            sb.append("11011001");
                        }
                        break;
                    case "LDI":
                        if(operand==null){
                            sb.append("1110110110100000");
                        }
                        break;
                    case "LDIR":
                        if(operand==null){
                            sb.append("1110110110110000");
                        }
                        break;
                    case "LDD":
                        if(operand==null){
                            sb.append("1110110110101000");
                        }
                        break;
                    case "LDDR":
                        if(operand==null){
                            sb.append("1110110110111000");
                        }
                        break;
                    case "CPI":
                        if(operand==null){
                            sb.append("1110110110100001");
                        }
                        break;
                    case "CPIR":
                        if(operand==null){
                            sb.append("1110110110110001");
                        }
                        break;
                    case "CPD":
                        if(operand==null){
                            sb.append("1110110110101001");
                        }
                        break;
                    case "CPDR":
                        if(operand==null){
                            sb.append("1110110110111001");
                        }
                        break;
                    case "ADD":
                        Pattern r_ADD = Pattern.compile("A,([ABCDEHL])");
                        Pattern n_ADD = Pattern.compile("A,(\\d+|[A-F\\d]+H)$");
                        Pattern HL_ADD = Pattern.compile("A,(\\(HL\\))");
                        Pattern IX_d_ADD = Pattern.compile("A,\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_ADD = Pattern.compile("A,\\(IY\\+(-?[0-9]+)\\)");
                        Pattern HL_ss_ADD = Pattern.compile("HL,(BC|DE|HL|SP)");
                        Pattern IX_pp_ADD = Pattern.compile("IX,(BC|DE|IX|SP)");
                        Pattern IY_rr_ADD = Pattern.compile("IY,(BC|DE|IY|SP)");

                        if((matcher=r_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10000").append(registers.get(r1));
                        }else if((matcher=n_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11000110");
                            if(r1.endsWith("H")){
                                String binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary);
                            }else{
                                if(Integer.parseInt(r1)<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            }
                        }else if((matcher=HL_ADD.matcher(operand)).matches()){
                            sb.append("10000110");
                        }else if((matcher=IX_d_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110000110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110000110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_ss_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00").append(ddpair.get(r1)).append("1001");
                        }else if((matcher=IX_pp_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110100").append(ppReg.get(r1)).append("1001");
                        }else if((matcher=IY_rr_ADD.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110100").append(rrReg.get(r1)).append("1001");
                        }
                        break;
                    case "ADC":
                        Pattern r_ADC = Pattern.compile("A,([ABCDEHL])");
                        Pattern n_ADC = Pattern.compile("A,(-?[0-9]+)");
                        Pattern HL_ADC = Pattern.compile("A,(\\(HL\\))");
                        Pattern IX_d_ADC = Pattern.compile("A,\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_ADC = Pattern.compile("A,\\(IY\\+(-?[0-9]+)\\)");
                        Pattern HL_ss_ADC = Pattern.compile("HL,(BC|DE|HL|SP)");

                        if((matcher=r_ADC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10001").append(registers.get(r1));
                        }else if((matcher=n_ADC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11001110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_ADC.matcher(operand)).matches()){
                            sb.append("10001110");
                        }else if((matcher=IX_d_ADC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110001110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_ADC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110001110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_ss_ADC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1110110101").append(ddpair.get(r1)).append("1010");
                        }
                        break;
                    case "SUB":
                        Pattern r_SUB = Pattern.compile("([ABCDEHL])");
                        Pattern n_SUB = Pattern.compile("(-?[0-9]+)");
                        Pattern HL_SUB = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_SUB = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_SUB = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_SUB.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10010").append(registers.get(r1));
                        }else if((matcher=n_SUB.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11010110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_SUB.matcher(operand)).matches()){
                            sb.append("10010110");
                        }else if((matcher=IX_d_SUB.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110010110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_SUB.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110010110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }
                        break;
                    case "SBC":
                        Pattern r_SBC = Pattern.compile("A,([ABCDEHL])");
                        Pattern n_SBC = Pattern.compile("A,(-?[0-9]+)");
                        Pattern HL_SBC = Pattern.compile("A,(\\(HL\\))");
                        Pattern IX_d_SBC = Pattern.compile("A,\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_SBC = Pattern.compile("A,\\(IY\\+(-?[0-9]+)\\)");
                        Pattern HL_ss_SBC = Pattern.compile("HL,(BC|DE|HL|SP)");

                        if((matcher=r_SBC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10011").append(registers.get(r1));
                        }else if((matcher=n_SBC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11011110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_SBC.matcher(operand)).matches()){
                            sb.append("10011110");
                        }else if((matcher=IX_d_SBC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110011110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_SBC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110011110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_ss_SBC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1110110101").append(ddpair.get(r1)).append("0010");
                        }
                        break;
                    case "AND":
                        Pattern r_AND = Pattern.compile("([ABCDEHL])");
                        Pattern n_AND = Pattern.compile("(-?[0-9]+)");
                        Pattern HL_AND = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_AND = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_AND = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_AND.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10100").append(registers.get(r1));
                        }else if((matcher=n_AND.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11100110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_AND.matcher(operand)).matches()){
                            sb.append("10100110");
                        }else if((matcher=IX_d_AND.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110100110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_AND.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110100110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }
                        break;
                    case "OR":
                        Pattern r_OR = Pattern.compile("([ABCDEHL])");
                        Pattern n_OR = Pattern.compile("(-?[0-9]+)");
                        Pattern HL_OR = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_OR = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_OR = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_OR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10110").append(registers.get(r1));
                        }else if((matcher=n_OR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11110110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_OR.matcher(operand)).matches()){
                            sb.append("10110110");
                        }else if((matcher=IX_d_OR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110110110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_OR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110110110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }
                        break;
                    case "XOR":
                        Pattern r_XOR = Pattern.compile("([ABCDEHL])");
                        Pattern n_XOR = Pattern.compile("(-?[0-9]+)");
                        Pattern HL_XOR = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_XOR = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_XOR = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_XOR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10101").append(registers.get(r1));
                        }else if((matcher=n_XOR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11101110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_XOR.matcher(operand)).matches()){
                            sb.append("10101110");
                        }else if((matcher=IX_d_XOR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110101110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_XOR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110101110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }
                        break;
                    case "CP":
                        Pattern r_CP = Pattern.compile("([ABCDEHL])");
                        Pattern n_CP = Pattern.compile("(-?[0-9]+)");
                        Pattern HL_CP = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_CP = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_CP = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_CP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("10111").append(registers.get(r1));
                        }else if((matcher=n_CP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11111110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=HL_CP.matcher(operand)).matches()){
                            sb.append("10111110");
                        }else if((matcher=IX_d_CP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110110111110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_CP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110110111110");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }
                        break;
                    case "INC":
                        Pattern r_INC = Pattern.compile("([ABCDEHL])");
                        Pattern HL_INC = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_INC = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_INC = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");
                        Pattern ss_INC = Pattern.compile("(BC|DE|HL|SP)");
                        Pattern IX_INC = Pattern.compile("(IX)");
                        Pattern IY_INC = Pattern.compile("(IY)");

                        if((matcher=r_INC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00").append(registers.get(r1)).append("100");
                        }else if((matcher=HL_INC.matcher(operand)).matches()){
                            sb.append("00110100");
                        }else if((matcher=IX_d_INC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110100110100");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_INC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110100110100");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=ss_INC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00").append(ddpair.get(r1)).append("0011");
                        }else if((matcher=IX_INC.matcher(operand)).matches()){
                            sb.append("1101110100100011");
                        }else if((matcher=IY_INC.matcher(operand)).matches()){
                            sb.append("1111110100100011");
                        }
                        break;
                    case "DEC":
                        Pattern r_DEC = Pattern.compile("([ABCDEHL])");
                        Pattern HL_DEC = Pattern.compile("(\\(HL\\))");
                        Pattern IX_d_DEC = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_DEC = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");
                        Pattern ss_DEC = Pattern.compile("(BC|DE|HL|SP)");
                        Pattern IX_DEC = Pattern.compile("(IX)");
                        Pattern IY_DEC = Pattern.compile("(IY)");

                        if((matcher=r_DEC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00").append(registers.get(r1)).append("101");
                        }else if((matcher=HL_DEC.matcher(operand)).matches()){
                            sb.append("00110101");
                        }else if((matcher=IX_d_DEC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110100110101");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=IY_d_DEC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110100110101");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                        }else if((matcher=ss_DEC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("00").append(ddpair.get(r1)).append("1011");
                        }else if((matcher=IX_DEC.matcher(operand)).matches()){
                            sb.append("1101110100101011");
                        }else if((matcher=IY_DEC.matcher(operand)).matches()){
                            sb.append("1111110100101011");
                        }
                        break;
                    case "DAA":
                        if(operand==null){
                            sb.append("00100111");
                        }
                        break;
                    case "CPL":
                        if(operand==null){
                            sb.append("00101111");
                        }
                        break;
                    case "NEG":
                        if(operand==null){
                            sb.append("1110110101000100");
                        }
                        break;
                    case "CCF":
                        if(operand==null){
                            sb.append("00111111");
                        }
                        break;
                    case "SCF":
                        if(operand==null){
                            sb.append("00110111");
                        }
                        break;
                    case "NOP":
                        if(operand==null){
                            sb.append("00000000");
                        }
                        break;
                    case "HALT":
                        if(operand==null){
                            sb.append("01110110");
                        }
                        break;
                    case "DI":
                        if(operand==null){
                            sb.append("11110011");
                        }
                        break;
                    case "EI":
                        if(operand==null){
                            sb.append("11111011");
                        }
                        break;
                    case "IM":
                        Pattern zero_IM = Pattern.compile("(0)");
                        Pattern one_IM = Pattern.compile("(1)");

                        if((matcher=zero_IM.matcher(operand)).matches()){
                            sb.append("1110110101000110");
                        }else if((matcher=one_IM.matcher(operand)).matches()){
                            sb.append("1110110101010110");
                        }
                        break;
                    case "IM2":
                        if(operand==null){
                            sb.append("1110110101011110");
                        }
                        break;
                    case "RLCA":
                        if(operand==null){
                            sb.append("00000111");
                        }
                        break;
                    case "RLA":
                        if(operand==null){
                            sb.append("00010111");
                        }
                        break;
                    case "RRCA":
                        if(operand==null){
                            sb.append("00001111");
                        }
                        break;
                    case "RRA":
                        if(operand==null){
                            sb.append("00011111");
                        }
                        break;
                    case "RLC":
                        Pattern r_RLC = Pattern.compile("([ABCDEHL])");
                        Pattern HL_RLC = Pattern.compile("(HL)");
                        Pattern IX_d_RLC = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_RLC = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_RLC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100000").append(registers.get(r1));
                        }else if((matcher=HL_RLC.matcher(operand)).matches()){
                            sb.append("1100101100000110");
                        }else if((matcher=IX_d_RLC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00000110");
                        }else if((matcher=IY_d_RLC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00000110");
                        }
                        break;
                    case "RL":
                        Pattern r_RL = Pattern.compile("([ABCDEHL])");
                        Pattern HL_RL = Pattern.compile("(HL)");
                        Pattern IX_d_RL = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_RL = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_RL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100010").append(registers.get(r1));
                        }else if((matcher=HL_RL.matcher(operand)).matches()){
                            sb.append("1100101100010110");
                        }else if((matcher=IX_d_RL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00010110");
                        }else if((matcher=IY_d_RL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00010110");
                        }
                        break;
                    case "RRC":
                        Pattern r_RRC = Pattern.compile("([ABCDEHL])");
                        Pattern HL_RRC = Pattern.compile("(HL)");
                        Pattern IX_d_RRC = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_RRC = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_RRC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100001").append(registers.get(r1));
                        }else if((matcher=HL_RRC.matcher(operand)).matches()){
                            sb.append("1100101100001110");
                        }else if((matcher=IX_d_RRC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00001110");
                        }else if((matcher=IY_d_RRC.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00001110");
                        }
                        break;
                    case "RR":
                        Pattern r_RR = Pattern.compile("([ABCDEHL])");
                        Pattern HL_RR = Pattern.compile("(HL)");
                        Pattern IX_d_RR = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_RR = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_RR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100011").append(registers.get(r1));
                        }else if((matcher=HL_RR.matcher(operand)).matches()){
                            sb.append("1100101100011110");
                        }else if((matcher=IX_d_RR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00011110");
                        }else if((matcher=IY_d_RR.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00011110");
                        }
                        break;
                    case "SLA":
                        Pattern r_SLA = Pattern.compile("([ABCDEHL])");
                        Pattern HL_SLA = Pattern.compile("(HL)");
                        Pattern IX_d_SLA = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_SLA = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_SLA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100100").append(registers.get(r1));
                        }else if((matcher=HL_SLA.matcher(operand)).matches()){
                            sb.append("1100101100100110");
                        }else if((matcher=IX_d_SLA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00100110");
                        }else if((matcher=IY_d_SLA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00100110");
                        }
                        break;
                    case "SRA":
                        Pattern r_SRA = Pattern.compile("([ABCDEHL])");
                        Pattern HL_SRA = Pattern.compile("(HL)");
                        Pattern IX_d_SRA = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_SRA = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_SRA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100101").append(registers.get(r1));
                        }else if((matcher=HL_SRA.matcher(operand)).matches()){
                            sb.append("1100101100101110");
                        }else if((matcher=IX_d_SRA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00101110");
                        }else if((matcher=IY_d_SRA.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00101110");
                        }
                        break;
                    case "SRL":
                        Pattern r_SRL = Pattern.compile("([ABCDEHL])");
                        Pattern HL_SRL = Pattern.compile("(HL)");
                        Pattern IX_d_SRL = Pattern.compile("\\(IX\\+(-?[0-9]+)\\)");
                        Pattern IY_d_SRL = Pattern.compile("\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=r_SRL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101100111").append(registers.get(r1));
                        }else if((matcher=HL_SRL.matcher(operand)).matches()){
                            sb.append("1100101100111110");
                        }else if((matcher=IX_d_SRL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00111110");
                        }else if((matcher=IY_d_SRL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r1)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r1))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            sb.append("00111110");
                        }
                        break;
                    case "RLD":
                        if(operand==null){
                            sb.append("1110110101101111");
                        }
                        break;
                    case "RRD":
                        if(operand==null){
                            sb.append("1110110101100111");
                        }
                        break;
                    case "BIT":
                        Pattern b_r_BIT = Pattern.compile("([0-7]),([ABCDEHL])");
                        Pattern b_HL_BIT = Pattern.compile("([0-7]),(HL)");
                        Pattern b_IX_d_BIT = Pattern.compile("([0-7]),\\(IX\\+(-?[0-9]+)\\)");
                        Pattern b_IY_d_BIT = Pattern.compile("([0-7]),\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=b_r_BIT.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1100101101").append(bit_tested.get(r1)).append(registers.get(r2));
                        }else if((matcher=b_HL_BIT.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101101").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IX_d_BIT.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("01").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IY_d_BIT.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("01").append(bit_tested.get(r1)).append("110");
                        }
                        break;
                    case "SET":
                        Pattern b_r_SET = Pattern.compile("([0-7]),([ABCDEHL])");
                        Pattern b_HL_SET = Pattern.compile("([0-7]),(HL)");
                        Pattern b_IX_d_SET = Pattern.compile("([0-7]),\\(IX\\+(-?[0-9]+)\\)");
                        Pattern b_IY_d_SET = Pattern.compile("([0-7]),\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=b_r_SET.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1100101111").append(bit_tested.get(r1)).append(registers.get(r2));
                        }else if((matcher=b_HL_SET.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101111").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IX_d_SET.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("11").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IY_d_SET.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("11").append(bit_tested.get(r1)).append("110");
                        }
                        break;
                    case "RES":
                        Pattern b_r_RES = Pattern.compile("([0-7]),([ABCDEHL])");
                        Pattern b_HL_RES = Pattern.compile("([0-7]),(HL)");
                        Pattern b_IX_d_RES = Pattern.compile("([0-7]),\\(IX\\+(-?[0-9]+)\\)");
                        Pattern b_IY_d_RES = Pattern.compile("([0-7]),\\(IY\\+(-?[0-9]+)\\)");

                        if((matcher=b_r_RES.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1100101110").append(bit_tested.get(r1)).append(registers.get(r2));
                        }else if((matcher=b_HL_RES.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1100101110").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IX_d_RES.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1101110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("10").append(bit_tested.get(r1)).append("110");
                        }else if((matcher=b_IY_d_RES.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("1111110111001011");
                            if(Integer.parseInt(r2)<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+Integer.parseInt(r2))).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            sb.append("10").append(bit_tested.get(r1)).append("110");
                        }
                        break;
                    case "JP":
                        Pattern nn_JP = Pattern.compile("(\\d+|[A-F\\d]+H)$");
                        Pattern cc_nn_JP = Pattern.compile("(NZ|Z|NC|C|PO|PE|P|M),(\\d+|[A-F\\d]+H)$");
                        Pattern cc_e_JP = Pattern.compile("(NZ|Z|NC|C|PO|PE|P|M),.*");
                        Pattern HL_JP = Pattern.compile("(HL)");
                        Pattern IX_JP = Pattern.compile("(IX)");
                        Pattern IY_JP = Pattern.compile("(IY)");

                        String[] operadores = operand.split(",");
                        if((matcher=nn_JP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11000011");
                            if(r1.endsWith("H")){
                                String binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=cc_nn_JP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("11").append(condicion.get(r1)).append("010");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=cc_e_JP.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(condicion.get(r1)).append("010");
                            if(no_pasada==1){
                                sb.append("0000000000000000");
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=HL_JP.matcher(operand)).matches()){
                            sb.append("11101001");
                        }else if((matcher=IX_JP.matcher(operand)).matches()){
                            sb.append("1101110111101001");
                        }else if((matcher=IY_JP.matcher(operand)).matches()){
                            sb.append("1111110111101001");
                        }else{
                            if(no_pasada==1){
                                sb.append("110000110000000000000000");
                            }else{
                                sb.append("11000011");
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(tabla_etiquetas.get(operadores[0].toUpperCase()), 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }
                        break;
                    case "JR":
                        Pattern C_e_JR = Pattern.compile("(C).*");
                        Pattern NC_e_JR = Pattern.compile("(NC).*");
                        Pattern Z_e_JR = Pattern.compile("(Z).*");
                        Pattern NZ_e_JR = Pattern.compile("(NZ).*");

                        operadores = operand.split(",");
                        if((matcher=C_e_JR.matcher(operand)).matches()){
                            if(no_pasada==1){
                                sb.append("0011100000000000");
                            }else{
                                sb.append("00111000");
                                int salto = Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16)-(PC+2);
                                if(salto<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                            }
                        }else if((matcher=NC_e_JR.matcher(operand)).matches()){
                            if(no_pasada==1){
                                sb.append("0011000000000000");
                            }else{
                                sb.append("00110000");
                                int salto = Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16)-(PC+2);
                                if(salto<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                            }
                        }else if((matcher=Z_e_JR.matcher(operand)).matches()){
                            if(no_pasada==1){
                                sb.append("0010100000000000");
                            }else{
                                sb.append("00101000");
                                int salto = Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16)-(PC+2);
                                if(salto<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                            }
                        }else if((matcher=NZ_e_JR.matcher(operand)).matches()){
                            if(no_pasada==1){
                                sb.append("0010000000000000");
                            }else{
                                sb.append("00100000");
                                int salto = Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16)-(PC+2);
                                if(salto<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                            }
                        }else{
                            if(no_pasada==1){
                                sb.append("0001100000000000");
                            }else{
                                sb.append("00011000");
                                int salto = Integer.parseInt(tabla_etiquetas.get(operadores[0].toUpperCase()), 16)-(PC+2);
                                if(salto<0)
                                    sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                                else
                                    sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                            }
                        }
                        break;
                    case "DJNZ":
                        operadores = operand.split(",");
                        if(no_pasada==1){
                            sb.append("0001000000000000");
                        }else{
                            sb.append("00010000");
                            int salto = Integer.parseInt(tabla_etiquetas.get(operadores[0].toUpperCase()), 16)-(PC+2);
                            if(salto<0)
                                sb.append(String.format("%8s", Integer.toBinaryString(256+salto)).replace(' ', '0'));
                            else
                                sb.append(String.format("%8s", Integer.toBinaryString(salto)).replace(' ', '0'));
                        }
                        break;
                    case "CALL":
                        Pattern nn_CALL = Pattern.compile("(\\d+|[A-F\\d]+H)$");
                        Pattern cc_nn_CALL = Pattern.compile("(NZ|Z|NC|C|PO|PE|P|M),(\\d+|[A-F\\d]+H)$");
                        Pattern cc_e_CALL = Pattern.compile("(NZ|Z|NC|C|PO|PE|P|M),.*");

                        operadores = operand.split(",");
                        if((matcher=nn_CALL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11001101");
                            if(r1.endsWith("H")){
                                String binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=cc_nn_CALL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            String r2 = matcher.group(2);
                            sb.append("11").append(condicion.get(r1)).append("100");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else if((matcher=cc_e_CALL.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(condicion.get(r1)).append("100");
                            if(no_pasada==1){
                                sb.append("0000000000000000");
                            }else{
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(tabla_etiquetas.get(operadores[1].toUpperCase()), 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }else{
                            if(no_pasada==1){
                                sb.append("110011010000000000000000");
                            }else{
                                sb.append("11001101");
                                String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(tabla_etiquetas.get(operadores[0].toUpperCase()), 16))).replace(' ', '0');
                                sb.append(binary.substring(8, 16)).append(binary.substring(0, 8));
                            }
                        }
                        break;
                    case "RET":
                        Pattern cc_RET = Pattern.compile("(NZ|Z|NC|C|PO|PE|P|M)");

                        if(operand==null){
                            sb.append("11001001");
                        }else if((matcher=cc_RET.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(condicion.get(r1)).append("000");
                        }
                        break;
                    case "RETI":
                        if(operand==null){
                            sb.append("1110110101001101");
                        }
                        break;
                    case "RETN":
                        if(operand==null){
                            sb.append("1110110101000101");
                        }
                        break;
                    case "RST":
                        Pattern p_RST = Pattern.compile("(00H|08H|10H|18H|20H|28H|30H|38H)");

                        if((matcher=p_RST.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11").append(RST_P.get(r1)).append("111");
                        }
                        break;
                    case "IN":
                        Pattern A_n_IN = Pattern.compile("(A),\\((\\d+|[A-F\\d]+H)\\)$");
                        Pattern r_C_IN = Pattern.compile("([ABCDEHL]),\\((C)\\)");

                        if((matcher=A_n_IN.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("11011011");
                            if(r2.endsWith("H")){
                                String binary = r2.substring(0, r2.length() - 1);
                                binary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary);
                            }else{
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r2))).replace(' ', '0'));
                            }
                        }else if((matcher=r_C_IN.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("1110110101").append(registers.get(r1)).append("000");
                        }
                        break;
                    case "INI":
                        if(operand==null){
                            sb.append("1110110110100010");
                        }
                        break;
                    case "INIR":
                        if(operand==null){
                            sb.append("1110110110110010");
                        }
                        break;
                    case "IND":
                        if(operand==null){
                            sb.append("1110110110101010");
                        }
                        break;
                    case "INDR":
                        if(operand==null){
                            sb.append("1110110110111010");
                        }
                        break;
                    case "OUT":
                        Pattern n_A_OUT = Pattern.compile("\\((\\d+|[A-F\\d]+H)\\),(A)$");
                        Pattern C_r_IN = Pattern.compile("\\((C)\\),([ABCDEHL])");

                        if((matcher=n_A_OUT.matcher(operand)).matches()){
                            String r1 = matcher.group(1);
                            sb.append("11010011");
                            if(r1.endsWith("H")){
                                String binary = r1.substring(0, r1.length() - 1);
                                binary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(binary, 16))).replace(' ', '0');
                                sb.append(binary);
                            }else{
                                sb.append(String.format("%8s", Integer.toBinaryString(Integer.parseInt(r1))).replace(' ', '0'));
                            }
                        }else if((matcher=C_r_IN.matcher(operand)).matches()){
                            String r2 = matcher.group(2);
                            sb.append("1110110101").append(registers.get(r2)).append("001");
                        }
                        break;
                    case "OUTI":
                        if(operand==null){
                            sb.append("1110110110100011");
                        }
                        break;
                    case "OTIR":
                        if(operand==null){
                            sb.append("1110110110110011");
                        }
                        break;
                    case "OUTD":
                        if(operand==null){
                            sb.append("1110110110101011");
                        }
                        break;
                    case "OTDR":
                        if(operand==null){
                            sb.append("1110110110111011");
                        }
                        break;
                }
            }
            hexadecimal = sb.toString();
        }
        return binaryToHex(hexadecimal);
    }

    public static String binaryToHex(String binaryString) {
        // Asegurarse de que la longitud de la cadena sea un múltiplo de 4
        int length = binaryString.length();
        int remainder = length % 4;
        if (remainder != 0) {
            // Rellenar con ceros a la izquierda para que la longitud sea un múltiplo de 4
            int padding = 4 - remainder;
            binaryString = "0".repeat(padding) + binaryString;
        }

        // Convertir cada grupo de 4 bits a su equivalente hexadecimal
        StringBuilder hexStringBuilder = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 4) {
            String fourBits = binaryString.substring(i, i + 4);
            int decimalValue = Integer.parseInt(fourBits, 2);
            String hexValue = Integer.toHexString(decimalValue);
            hexStringBuilder.append(hexValue);
        }

        return hexStringBuilder.toString().toUpperCase(); // Convertir a mayúsculas para una mejor legibilidad
    }

}