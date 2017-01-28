import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kalirajkalimuthu on 12/4/16.
 */
public class TestMoreNodes {

    private static final int NODE_COUNT = 50;
    private static final float RTT_Threshold = 4; // Response Time Threshold to join a cluster.
    private static final int CLUSTER_MAX_SIZE = 5;
    private static final int communication_delay = 1; // in milliseconds.
    private static int clusterCount = 0;

    private static int avg_response_time = 1; // ms.

    private Cluster primaryCluster;
    private LinkedList<Cluster>   clusterList;
    private LinkedList<Node> nodeList ;
    private float[][] responseTimeMatrix;

    private int[] fileSizeList = new int[] {50}; // size in KB's


    public TestMoreNodes(){

        clusterList = new LinkedList<Cluster>();
        nodeList = new LinkedList<>();
        responseTimeMatrix = ResponseTimeMatrixGenerator.generateResponseTimeMatrix(NODE_COUNT);
    }


    public static int getAvg_response_time() {
        return avg_response_time;
    }

    public static void setAvg_response_time(int avg_response_time) {
        TestMoreNodes.avg_response_time = avg_response_time;
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public void setClusterList(LinkedList<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(LinkedList<Node> nodeList) {
        this.nodeList = nodeList;
    }



    /**
     * This method creates the dynamic clusters based on the response time matrix and node list.
     */
    public void createClusters(){

        Cluster cluster = null;
        Float minRTT = 0.0F;

        for (Node node : nodeList) {
            if (primaryCluster == null) {
                cluster = new Cluster(clusterCount++);
                primaryCluster = cluster;
                cluster.setPrimaryCluster(true);
                cluster.setMaster(node);
                cluster.addNodes(node);
                node.setCluster_id(cluster.getCluster_Id());
                clusterList.add(cluster);

            } else {
                Map minRTTCluster = getMinRTTCluster(node);
                for (Object key : minRTTCluster.keySet()) {
                    if(key != null) {
                        cluster = (Cluster) key;
                        minRTT = (Float) minRTTCluster.get(key);
                    }
                }

                if (cluster != null && (minRTT < RTT_Threshold) && cluster.getSize() < CLUSTER_MAX_SIZE) {
                    cluster.addNodes(node);
                    node.setCluster_id(cluster.getCluster_Id());

                } else {
                    cluster = new Cluster(clusterCount++);
                    cluster.addNodes(node);
                    cluster.setMaster(node);
                    node.setCluster_id(cluster.getCluster_Id());
                    clusterList.add(cluster);
                }
            }
        }
    }


    /**
     * This methdd finds the closest cluster to which a node can join.
     * Calculate the respone time between each masters and node.
     * returns the a cluster with minimum response time
     */
    public Map<Cluster, Float> getMinRTTCluster(Node node){

        Map<Cluster, Float> minRTTClusterMap = new LinkedHashMap<Cluster, Float>();
        Cluster minCluster = null;
        Float minRTT = new Float(Float.MAX_VALUE);
        for(Cluster cluster:clusterList){
            if(!(cluster.getSize() >= CLUSTER_MAX_SIZE)) {
                float response_time = getRTT(node, cluster.getMaster());
                if (response_time < minRTT) {
                    minCluster = cluster;
                    minRTT = response_time;
                }
            }
        }
        minRTTClusterMap.put(minCluster,minRTT);
        return minRTTClusterMap;
    }

    /**
     *This method generate the list of nodes to construct the clusters and run the algorithm.
     */
    public void generateNodes(){

        System.out. println("---------------------------- Generating the Nodes for the distributed replica system ------------------------------------");
        for (int i=0; i< NODE_COUNT;i++){
            Node node = new Node(i);
            nodeList.add(node);
            System.out.print(node.toString());
            System.out.print("\t");
        }

    }


    /**
     * This method performs the actual implementation of DCRep Algorithm.
     * Run ths algorithm with set of inuput file sizes and ouput the update latency
     * in DCRep Dynamic clustering model.
     */
    public void runDCRepModel(){

        System.out. println("------------------------- ****************************** --------------------------------");
        System.out. println("---------------------------- Running DCRep Algorithm ------------------------------------");
        System.out. println("------------------------- ****************************** --------------------------------");

        float updateLatency = 0;
        Cluster cluster = null;

        this.createClusters();
        this.displayAllClusters();
        for(int size : fileSizeList){
            for(int i=1; i<clusterList.size();i++) {
                updateLatency = updateLatency + (getRTT(this.primaryCluster.getMaster(),clusterList.get(i).getMaster())
                        + clusterList.get(i).getMaster().get_processing_time(size));
            }
            Cluster  last_cluster = clusterList.getLast();
            List node_list = last_cluster.getCluster_nodes();

            for(int i=1; i < last_cluster.getSize();i++) {
                updateLatency = updateLatency + (getRTT((Node)node_list.get(0),(Node)node_list.get(i))
                        + ((Node)node_list.get(i)).get_processing_time(size));
            }
            System.out.println(" File Size : "+size+" KB"+ "  "+" Update Latency:"+ updateLatency +" ms");
        }

        System.out. println("------------------------- Completed DCRep Algorithm --------------------------------");
    }

    /**
     * This method run the algorithm for conventional model
     * It runs the algorithm with set input file sizes and output the update latency in the conventional model.
     */

    public void runConventionalModel(){
        System.out.println();
        System.out. println("------------------------- ****************************** -------------------------------");
        System.out. println("------------------------ Running Conventional Algorithm--------------------------------");
        System.out. println("------------------------- ****************************** --------------------------------");

        float updateLatency = 0 ;
        for(int size : fileSizeList){
            for(int i=1; i < nodeList.size();i++) {
                updateLatency = updateLatency +(getRTT(nodeList.get(0),nodeList.get(i))+ nodeList.get(i).get_processing_time(size));
            }
            System.out.println(" File Size : "+ size +" KB"+"  "+" Update Latency:"+ updateLatency+" ms");
        }
        System.out. println("------------------------ Completed Conventional Algorithm --------------------------------");
    }

    /**
     * This method gets the ResponseTime between two node.(from node1 to node2)
     * @param node1
     * @param node2
     * @return
     */

    public float getRTT(Node node1, Node node2){
        float response_time = -1;
        int i = node1.getNodeId(),j=node2.getNodeId() ;
        response_time = responseTimeMatrix[i][j];
        return response_time;
    }

    /**
     * This method displays the clusters formed by DCRep Algorithm.
     */
    public void displayAllClusters() {

        System.out.println("Maximum Cluster Size = "+ CLUSTER_MAX_SIZE);
        System.out.println("-------------------------------------------");
        System.out.println("Total Number of Clusters formed : " + clusterList.size());
        System.out.println("-------------------------------------------");

        for (Cluster cluster : clusterList) {
            cluster.display();
        }
        System.out.println();
        System.out.println("---------------------------------------------");

    }

    public static void main(String args[]){

        TestMoreNodes model = new TestMoreNodes();
        System.out.println("Total Nodes in the System : "+ NODE_COUNT);
        model.generateNodes();
        model.runConventionalModel();
        model.runDCRepModel();
    }

}
