package pandora.hera.asm;

import java.io.IOException;

public class AssemblyRunner {
    public static void main(String[] args) {
        try {
            // Compile the assembly code using NASM (replace with your assembly file name)
            Process compileProcess = Runtime.getRuntime().exec("nasm -f elf64 and.asm");
            compileProcess.waitFor();
            
            // Link the object file to create an executable (replace with your object file name)
            Process linkProcess = Runtime.getRuntime().exec("ld -s -o and.o");
            linkProcess.waitFor();
            
            // Run the executable
            Process runProcess = Runtime.getRuntime().exec("./and.o");
            runProcess.waitFor();
            
            // Print the output (if any)
            ProcessOutputReader outputReader = new ProcessOutputReader(runProcess);
            outputReader.start();
            outputReader.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
}

class ProcessOutputReader extends Thread {
    private final Process process;

    public ProcessOutputReader(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
