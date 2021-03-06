package SequenceMining;

/**
 * Implements the Information gain Supervised Closed sequence mining algorithm
 */
public class InformationGain extends AbsoluteWracc {

    /**
     * P / (P+N)
     */
    private double IMP_P_DIVIDED_BY_P_PLUS_N;
    private float P_PLUS_N;


    public InformationGain ( int k ) {
        super( k );
    }

    /**
     * Computes the logarithm in base b of a
     * @return the requested logarithm
     */
    private static double logb ( double a, double b ) {
        return Math.log( a ) / Math.log( b );
    }

    /**
     * Computes the logarithm in base 2 of a
     * @return the requested logarithm
     */
    private static double log2 ( double a ) {
        return logb( a, 2 );
    }

    private static String fixINGIniousBug ( String filepath ) {
        int lastIndex = 0;
        int count = 0;
        String findStr = ".txt";

        while ( lastIndex != -1 ) {

            lastIndex = filepath.indexOf( findStr, lastIndex );

            if ( lastIndex != -1 ) {
                count++;
                lastIndex += findStr.length();
            }
        }
        if ( count > 1 ) {
            filepath = filepath.substring( 0, filepath.length() - 4 );
        }
        return filepath;
    }

    public static void main ( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println( "Incorrect number of arguments! Aborting execution." );
        } else {
            String filepathPositive = fixINGIniousBug( args[ 0 ] );
            String filepathNegative = fixINGIniousBug( args[ 1 ] );

            int k = Integer.parseInt( args[ 2 ] );
            GenericAlgorithm genericAlgorithm = new InformationGain( k );
            System.out.println( genericAlgorithm.start( filepathPositive, filepathNegative, 5 ) );
            System.out.println( ( ( InformationGain ) genericAlgorithm ).computeEvaluationFunction( 4, 3 ) );
        }

    }

    public static String performances ( String[] args ) {
        String filepathPositive = args[ 0 ];
        String filepathNegative = args[ 1 ];
        int k = Integer.parseInt( args[ 2 ] );
        GenericAlgorithm genericAlgorithm = new InformationGain( k );
        return genericAlgorithm.start( filepathPositive, filepathNegative, 5 );
    }

    /**
     * {@inheritDoc}
     * This implementation also initialize some constants, useful to avoid unnecessary
     * recomputations during the algorithm's elaboration
     */
    @Override
    protected void initializeDataSet ( String filePathPositive, String filePathNegative ) {
        super.initializeDataSet( filePathPositive, filePathNegative );
        this.P_PLUS_N = P + N;
        this.IMP_P_DIVIDED_BY_P_PLUS_N = imp( ( float ) P / ( P_PLUS_N ) );

    }

    /**
     * No constants must be recomputed when the threshold changes
     */
    @Override
    void computeConstraintConstants () {
        //nothing to do here
    }

    /**
     * {@inheritDoc}
     * This implementation apply only a pruning technique,
     * as described by the professor on the forum
     */
    @Override
    boolean lowerBoundConstraints ( int p, int n ) {
        if ( computeEvaluationFunction( 0, n ) < getMinEvaluationFunction() &&
                computeEvaluationFunction( p, 0 ) < getMinEvaluationFunction() ) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * This implementation computes the Information Gain's formula
     */
    @Override
    float computeEvaluationFunction ( Integer p, Integer n ) {
        if ( ( P == p ) && ( N == n ) ) {
            return 0;
        }
        double b = IMP_P_DIVIDED_BY_P_PLUS_N - ( ( double ) ( p + n ) / P_PLUS_N ) * imp( ( ( double ) p / ( double ) ( p + n ) ) );
        double c = ( ( P_PLUS_N - p - n ) / ( P_PLUS_N ) );
        double d = ( P - p ) / ( P_PLUS_N - p - n );
        double a = ( b - c * imp( d ) );
        return ( float ) ( Math.round( a * 100000d ) / 100000d );


    }

    /**
     * Support method that computes the impurity function
     * @param x value fo which the function has to be evaluated
     * @return the impurity function's value of x
     */
    private double imp ( double x ) {
        if ( x == 0 || x == 1 ) {
            return 0;
        }
        return -( x * log2( x ) ) - ( ( 1 - x ) * log2( 1 - x ) );
    }
}
