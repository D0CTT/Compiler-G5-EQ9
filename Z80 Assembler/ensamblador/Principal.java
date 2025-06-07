package Ensamblador;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class Principal {
    static int size = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Selector de Archivo ASM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();

        JButton selectFileButton = new JButton("Seleccionar archivo ASM");

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el directorio de trabajo actual
                String workingDirectory = System.getProperty("user.dir");

                // Crear un JFileChooser con el directorio de trabajo actual
                JFileChooser fileChooser = new JFileChooser(workingDirectory);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("ASM files", "asm");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(frame);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(frame, "Archivo seleccionado: " + selectedFile.getAbsolutePath());

                    try { 
                        // Crear un nuevo archivo para manipularlo sin modificar el original
                        File modifiedFile = new File(workingDirectory + File.separator + "temp.asm");
                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(modifiedFile));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Eliminar el símbolo ';' y todo lo que esté a su derecha
                            line = line.replaceAll(";.*$", "");
                            // Escribir la línea modificada en el nuevo archivo
                            writer.write(line + "\n");
                        }

                        // Cerrar los recursos
                        reader.close();
                        writer.close();
                        String fileNameWithoutExtension = selectedFile.getName().replaceFirst("[.][^.]+$", "");
                        String outputFilePathLST = workingDirectory + File.separator + fileNameWithoutExtension + ".LST";
                        String outputFilePathHEX = workingDirectory + File.separator + fileNameWithoutExtension + ".HEX";
                        Map<String, String> tabla_etiquetas;
                        List<String> lines = Ensamblador.readFile(modifiedFile.getAbsolutePath());
                        tabla_etiquetas = LabelResolver.primeraPasada(lines);
                        LabelResolver.segundaPasadaLST(lines, tabla_etiquetas, outputFilePathLST);
                        LabelResolver.segundaPasadaHEX(lines, tabla_etiquetas, outputFilePathHEX);
                        JOptionPane.showMessageDialog(frame, "Ensamblado exitoso.");
                        modifiedFile.delete();
                        frame.dispose();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
            

                } else {
                    JOptionPane.showMessageDialog(frame, "No se seleccionó ningún archivo.");
                }
            }
        });

        panel.add(selectFileButton);
        frame.add(panel);
        frame.setVisible(true);
    }
}
