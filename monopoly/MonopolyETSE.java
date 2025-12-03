package monopoly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class MonopolyETSE {
    public static void main(String[] args) {
        Menu menu = new Menu(args);
        menu.iniciarTablero();
    }
}
