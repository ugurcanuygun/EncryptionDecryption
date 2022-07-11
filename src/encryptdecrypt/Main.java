package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Algorithm algorithm = null;

        String alg="shift";
        String mode = "enc";
        String data = "";
        int key = 0;

        String result = "";

        for (int i = 0; i < args.length; i++) {
            if ("-in".equals(args[i])) {
                try {
                    File dataIn = new File(args[i + 1]);
                    Scanner scanner = new Scanner(dataIn);
                    while (scanner.hasNext()) {
                        data += scanner.nextLine();
                    }
                    scanner.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if ("-alg".equals(args[i])) {
                alg = args[i + 1];
            }

            if ("unicode".equals(alg)) {
                algorithm = new Unicode();
            } else {
                algorithm = new Shift();
            }

            if ("-data".equals(args[i]) && !Arrays.asList(args).contains("-in")) {
                data = args[i + 1];
            }

            if ("-mode".equals(args[i])) {
                mode = args[i + 1];
            }

            if ("-key".equals(args[i])) {
                key = Integer.parseInt(args[i + 1]);
            }

        }

        switch (mode) {
            case "enc":
                result = algorithm.encode(data, key);
                break;
            case "dec":
                result = algorithm.decode(data, key);
                break;
        }

        String dataOutStr = "";

        for (int i = 0; i < args.length; i++) {
            if ("-out".equals(args[i])) {
                dataOutStr = args[i + 1];
            }
        }

        if (!dataOutStr.equals("")) {
            try {
                File dataOut = new File(dataOutStr);
                FileWriter fileWriter = new FileWriter(dataOut);
                fileWriter.write(result);
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(result);
        }

    }

}

interface Algorithm {

    String encode(String text, int shift);

    String decode(String text, int shift);

}

class Shift implements Algorithm {

    String lowerCaseAlphabet = "abcdefghijklmnopqrstuvwxyz";
    String upperCaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public String encode(String text, int shift) {
        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (Character.isAlphabetic(text.charAt(i))) {

                if (lowerCaseAlphabet.indexOf(text.charAt(i)) != -1) {
                    int alphabetIndex = lowerCaseAlphabet.indexOf(text.charAt(i));
                    encoded.append(lowerCaseAlphabet.charAt((alphabetIndex + shift) % 26));
                } else {
                    int alphabetIndex = upperCaseAlphabet.indexOf(text.charAt(i));
                    encoded.append(upperCaseAlphabet.charAt((alphabetIndex + shift) % 26));
                }

            } else {
                encoded.append(text.charAt(i));
            }
        }

        return encoded.toString();
    }

    @Override
    public String decode(String text, int shift) {
        StringBuilder decoded = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (Character.isAlphabetic(text.charAt(i))) {

                if (lowerCaseAlphabet.indexOf(text.charAt(i)) != -1) {
                    int alphabetIndex = lowerCaseAlphabet.indexOf(text.charAt(i));
                    decoded.append(lowerCaseAlphabet.charAt((alphabetIndex - shift + 26) % 26));
                } else {
                    int alphabetIndex = upperCaseAlphabet.indexOf(text.charAt(i));
                    decoded.append(upperCaseAlphabet.charAt((alphabetIndex - shift + 26) % 26));
                }

            } else {
                decoded.append(text.charAt(i));
            }
        }

        return decoded.toString();
    }
}

class Unicode implements Algorithm {

    @Override
    public String encode(String text, int shift) {
        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int indexOld = text.charAt(i);
            int indexNew = indexOld + shift;
            encoded.append((char) indexNew);
        }

        return encoded.toString();
    }

    @Override
    public String decode(String text, int shift) {
        StringBuilder decoded = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int indexOld = text.charAt(i);
            int indexNew = indexOld - shift;
            decoded.append((char) indexNew);
        }

        return decoded.toString();
    }
}