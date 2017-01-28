/**
 * Created by kalirajkalimuthu on 11/26/16.
 *
 * This class difines the nodes in the DCRep Model
 *
 */
public class Node {

   private final static int avg_processing_time = 1 ; // 1 milliseconds for file size 50
   private final static int avg_file_size = 50; // in KB
   private  int node_id;
   private int cluster_id ;

    public Node(int id){
        this.node_id = id;
    }

    public int getNodeId() {
        return node_id;
    }

    public void setNodeId(int id) {
        this.node_id = id;
    }

    public int getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
    }

    public int get_processing_time(int file_size){

        return ((file_size/avg_file_size )*avg_processing_time);
    }

    public String toString(){
        return "Node"+" "+node_id;
    }

}
