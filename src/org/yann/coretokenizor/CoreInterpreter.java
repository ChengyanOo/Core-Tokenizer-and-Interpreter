package org.yann.coretokenizor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CoreInterpreter {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java CoreInterpreter <core_program_file> <data_file>");
            System.exit(1);
        }

        String coreProgramFilePath = args[0];
        String dataFilePath = args[1];

        try {
            Parser parser = new Parser(coreProgramFilePath);
            NodeProg nodeProg = new NodeProg();
            parser.parseProg(nodeProg);
            Printer printer = new Printer();
            System.out.println();
            System.out.println();
            System.out.println("*********The following is the pretty printing output*********");
            printer.printProg(nodeProg);

            File file = new File(dataFilePath);
            Executer executer = new Executer(file);
            System.out.println();
            System.out.println();
            System.out.println("*********The following is the execution output*********");
            executer.executeProg(nodeProg);

        } catch (IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
            System.exit(1);
        }
    }

}