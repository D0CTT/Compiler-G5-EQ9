package  unam.fi.compilers.g5.E09.Compiler.MiniC;
import  java.io.File;
import  java.io.FileWriter;
import  java.io.PrintWriter;
import  javax.swing.*;
import unam.fi.compilers.g5.E09.Compiler.MiniC.Ast.*;
import unam.fi.compilers.g5.E09.Compiler.MiniC.CodeGen.*;
import unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer.*;
import unam.fi.compilers.g5.E09.Compiler.MiniC.Parser.*;

public class Main {
    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle("Selecciona el archivo de entrada .c");
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "No se seleccionó archivo de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        File inputFile = fileChooser.getSelectedFile();

        String inputPath = inputFile.getAbsolutePath();
        String outputPath = inputPath.replaceAll("\\.c$", "") + ".asm";
        File outputFile = new File(outputPath);

        String source = new String(java.nio.file.Files.readAllBytes(inputFile.toPath()));

        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
        CodeGen codegen = new CodeGen(program, pw);
        codegen.generate();
        pw.close();

        JOptionPane.showMessageDialog(null, "Compilación finalizada.\nArchivo generado:\n" + outputFile.getAbsolutePath(), 
                                      "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
