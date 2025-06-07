package  unam.fi.compilers.g5.E09.Compiler.MiniC;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer.*;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.Ast.*;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.Parser.*;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.CodeGen.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

public class Main {
        public static void main(String[] args) throws Exception {
            // Selector de archivo de entrada (.c)
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona el archivo de entrada .c");
            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                System.err.println("No se seleccionó archivo de entrada.");
                System.exit(1);
            }
            File inputFile = fileChooser.getSelectedFile();

            // Derivar archivo de salida con misma ruta y nombre, pero extensión .asm
            String inputPath = inputFile.getAbsolutePath();
            String outputPath = inputPath.replaceAll("\\.c$", "") + ".asm";
            File outputFile = new File(outputPath);

            // Lectura del archivo fuente
            String source = new String(java.nio.file.Files.readAllBytes(inputFile.toPath()));

            // Compilación
            Lexer lexer = new Lexer(source);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
            CodeGen codegen = new CodeGen(program, pw);
            codegen.generate();
            pw.close();

            System.out.println("Compilación finalizada. Archivo generado: " + outputFile.getAbsolutePath());
        }
}
