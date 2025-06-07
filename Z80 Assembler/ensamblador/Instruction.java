package Ensamblador;
import java.util.Arrays;

    public class Instruction {
        private String label;
        private String mnemonic;
        private String operand;

        public Instruction(String label, String mnemonic, String operand) {
            this.label = label;
            this.mnemonic = mnemonic;
            this.operand = operand;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getMnemonic() {
            return this.mnemonic;
        }

        public void setMnemonic(String mnemonic) {
            this.mnemonic = mnemonic;
        }

        public String getOperand() {
            return this.operand;
        }

        public void setOperand(String operand) {
            this.operand = operand;
        }

        // Getters and toString method

        public static Instruction parseLine(String line) {
            String label = null, mnemonic = null, operand = null;

            // Split by spaces and handle labels and comments
            String[] parts = line.split("\\s+");
            if (parts.length == 0) return null;

            // Determine if there's a label
            if (parts[0].endsWith(":")) {
                label = parts[0].substring(0, parts[0].length() - 1);
                parts = Arrays.copyOfRange(parts, 1, parts.length);
            }

            // Extract mnemonic and operand
            if (parts.length > 0) mnemonic = parts[0].toUpperCase();
            if (parts.length > 1){
                StringBuilder sb = new StringBuilder();
                int n = 1;
                while(parts.length > n){
                    sb.append(parts[n].toUpperCase());
                    n++;
                }
                operand = sb.toString();
            }
            
            return new Instruction(label, mnemonic, operand);
        }
}