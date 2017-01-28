import java.util.Random;

/**
 * Created by kalirajkalimuthu on 12/3/16.
 * This class generates the response time matrix for all the nodes in dcrep model
 */
public  class ResponseTimeMatrixGenerator {

    public static float[][] generateResponseTimeMatrix(int nodes_count) {

        int numberOfNodes = nodes_count;

        int minimum = 100;
        int maximum = 200;
        float assumedAverageResponseTime = generateResponseTime(minimum, maximum);
        float approximateVariation = 0.5f;

        int lowerBound = minimum, upperBound = maximum;

        boolean odd = true;
        //int requiredClusters = (int)numberOfNodes % MAX_CLUSTER_SIZE;

        float[][] responseTimeMatrix = new float[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (i == j)
                    responseTimeMatrix[i][j] = 0;
                else {
                    if (odd) {
                        lowerBound = (int) (assumedAverageResponseTime - approximateVariation) * 100;
                        responseTimeMatrix[i][j] = generateResponseTime(lowerBound, (int) assumedAverageResponseTime * 100);
                        odd = !odd;
                    } else {
                        upperBound = (int) (assumedAverageResponseTime + approximateVariation) * 100;
                        responseTimeMatrix[i][j] = generateResponseTime((int) assumedAverageResponseTime * 100, upperBound);
                        odd = !odd;
                    }
                }
            }
        }

     /*   System.out.println(assumedAverageResponseTime);
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++)
                System.out.print(responseTimeMatrix[i][j] + "\t\t\t");
             System.out.println();
        } */

        return responseTimeMatrix;
    }

    public static float generateResponseTime(int lowerBound, int upperBound)
    {
        Random random = new Random();
        return (lowerBound + random.nextInt((upperBound - lowerBound) + 1)) * 0.01f;
    }

}
