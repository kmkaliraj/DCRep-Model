/**
 * Created by kalirajkalimuthu on 11/26/16.
 * This class difines the structure of the cluster in DCRep Model
 */


import java.util.LinkedList;
import java.util.List;

public class Cluster {
    private int  cluster_id;
    private List<Node> cluster_nodes = new LinkedList<Node>();
    private Node Master;
    Boolean isPrimaryCluster = false;

    public Cluster(int cluster_id){
        this.cluster_id = cluster_id;

    }

    public int getCluster_Id() {
        return cluster_id;
    }

    public void setCluster_Id(int cluster_Id) {
         cluster_Id = cluster_Id;
    }

    public List<Node> getCluster_nodes() {
        return cluster_nodes;
    }

    public void setCluster_nodes(List<Node> cluster_nodes) {
        this.cluster_nodes = cluster_nodes;
    }

    public Node getMaster() {
        return Master;
    }

    public void setMaster(Node master) {
        Master = master;
    }

    public int getSize(){
        return cluster_nodes.size();
    }

    public void setPrimaryCluster(Boolean isPrimary){
        this.isPrimaryCluster = isPrimary;
    }

    public Boolean getIsPrimaryCluster(){
        return isPrimaryCluster;
    }


    public void addNodes(Node node){
        this.cluster_nodes.add(node);
    }
    public boolean removeNodes(int nodeId){

        for(int i= 0; i<cluster_nodes.size();i++){
            Node node = cluster_nodes.get(i);
            if(node.getNodeId() == nodeId){
              cluster_nodes.add(node);
                return true;
            }
        }

        return false;
    }

    public void display(){

      //  System.out.println("------------------------------------");

        if(isPrimaryCluster){
            System.out.println("Primary Cluster :" + this.cluster_id);
            System.out.println("Primary Master: "+ this.getMaster());
        }
        else{
            System.out.println();
            System.out.println("Secondary Cluster :" + this.cluster_id);
            System.out.println("Virtual Master: "+ this.getMaster());
        }

        for(int i= 1; i<cluster_nodes.size();i++){
            System.out.print(cluster_nodes.get(i)+" ");
        }

       // System.out.println("------------------------------------");
    }

}
