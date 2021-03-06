package Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Utilities class to manage a dataset stored in a external file, for the ECLAT algorithm
 */
public class ECLATDataSet {
    private final TreeMap<Integer, Set<Integer> > verticalRepresentation;
    private final int transactionNumber;

    /**
     * Constructor: reads the dataset and initialises fields.
     * @param filePath the path to the dataset file. It is assumed to have the following format:
     *                 Each line corresponds to a transaction. Blank lines might be present and will be ignored.
     *                 Items in a transaction are represented by integers separated by single spaces.
     */
    public ECLATDataSet ( String filePath) {
        int transactionNumber = 1;
        int temp;
        verticalRepresentation = new TreeMap <>();
        // Counting items and initialising transactions and items structures
        ArrayList<int[]> database = new ArrayList <>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while (reader.ready()) {
                String line = reader.readLine();
                if(line.matches("^\\s*$")) continue; //Skipping blank lines
                int[] transaction = Stream.of(line.trim().split(" ")).mapToInt(Integer::parseInt).toArray();
                database.add( transaction );
            }

            reader.close();
        }
        catch (IOException e) {
            System.err.println("Unable to read dataset file!");
            e.printStackTrace();
        }
        for (int i = 0; i < database.size(); i++) {
            // for each item in that transaction
            for (Integer item : database.get(i)) {

                Set < Integer > set = verticalRepresentation.computeIfAbsent( item, k -> new LinkedHashSet < Integer >() );
                set.add(i+1);
            }
            transactionNumber++;
        }
        this.transactionNumber = transactionNumber;
    }

    public TreeMap < Integer, Set<Integer> > getVerticalRepresentation () {
        return verticalRepresentation;
    }

    public int getTransactionNumber () {
        return transactionNumber;
    }
}
