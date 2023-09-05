import java.io.*;
import java.util.ArrayList;
import java.util.List;
        class Data { //defining a class to represent data point w/ features
            private final double[] myFeatures; //Declaring array to store the data features

            public Data(double[] myFeatures) { //constructor to create a data point with the features
                this.myFeatures = myFeatures; //this.features refers to instance variable of the data object
                // features refers to the parameter passed to the constructor
            }

            public double[] getmyFeatures() { //get method to retrieve features
                return myFeatures;
            }
        }

        class Cluster { //defining a cluster class to represent cluster of data points
            private final List<Data> Datapoint = new ArrayList<>(); //declaring points variable that will store data
            // points belonging to the cluster
            private Data centroid; //declaring centroid variable which represents the center of the cluster
            // and will store the centroid of the cluster

            public List<Data> getDatapoint() { //get method that allows access to the data points belonging to the cluster
                // outside the cluster class
                return Datapoint;
            }

            public void addPoint(Data point) { //method allows adding a data point to the cluster
                Datapoint.add(point);
            }

            public Data getCentroid() { //get method returns centroid of the cluster
                return centroid;
            }

            public void setCentroid(Data centroid) { //setting method assigns centroid to the instance variable
                this.centroid = centroid;
            }

            public void clear() { //emptys out cluster
                Datapoint.clear();
            }
        }

public class K_Means {

    private final int totalClusters; //declaring variable that stores the total number of clusters
    private final List<Data> dataPoints; //declaring variable that stores list of data points that will be clustered
    private final List<Cluster> clusters; //declaring variable that stores list of clusters that will be formed

    public K_Means(int totalClusters, List<Data> dataPoints) { //constructor
        this.totalClusters = totalClusters;
        this.dataPoints = dataPoints;
        this.clusters = new ArrayList<>();

        for (int i = 0; i < totalClusters; i++) { //creates a new cluster for each iteration
            Cluster cluster = new Cluster(); //creates a new cluster object
            cluster.setCentroid(dataPoints.get(i)); //sets the centroid of the cluster
            clusters.add(cluster); //adds new cluster object to the clusters list
        }
    }

    private double euclideanDistance(Data x, Data y) { //calculating euchlidean Distance
        double[] X_feature = x.getmyFeatures();
        double[] Y_feature = y.getmyFeatures();

        double sum = 0;
        for (int i = 0; i < X_feature.length; i++) {
            sum += Math.pow(X_feature[i] - Y_feature[i], 2);
        }

        return Math.sqrt(sum);
    }

    // This method runs the k-means clustering algorithm for a given number of iterations.
    // It takes 'max' as input, which specifies the maximum number of iterations to run the algorithm.
    public void K_Means_Algo(int max) {
        for (int i = 0; i < max; i++) { // Loop over the specified number of iterations.
            for (Cluster cluster : clusters) {
                cluster.clear(); // Clear all points from each cluster in the 'clusters' list.
            }
            // For each data point in the 'dataPoints' list, find the cluster with the closest centroid.
            // Initialize variables to track the minimum distance and the closest cluster.
            for (Data point : dataPoints) {
                double min = Double.MAX_VALUE;
                Cluster minCluster = null;
                // Loop through each cluster to calculate the Euclidean distance between the data point
                // and each cluster's centroid. Identify the cluster with the minimum distance as the closest.
                for (Cluster cluster : clusters) {
                    double distance = euclideanDistance(point, cluster.getCentroid());
                    if (distance < min) {
                        min = distance;
                        minCluster = cluster;
                    }
                }

                minCluster.addPoint(point); // Add the data point to the closest cluster, assigning it to that cluster.
            }
            // After assigning all data points to their closest clusters, calculate new centroids for each cluster.
            // The new centroid is the mean of all data points assigned to that cluster.
            for (Cluster cluster : clusters) {
                Data newCentroid = calculate(cluster.getDatapoint());
                cluster.setCentroid(newCentroid);
            }
        }
    }

    // This private method calculates the centroid of a cluster based on the given list of data points.
    // It takes a list of 'points' as input, where each data point contains an array of features.
    private Data calculate(List<Data> points) {
        // Determine the total number of features in each data point.
        int totalFeature = points.get(0).getmyFeatures().length;
        // Initialize an array to store the accumulated sum of each feature for calculating the centroid.
        double[] centFeature = new double[totalFeature];
        // Loop through each data point in the 'points' list.
        for (Data point : points) {
            // Get the array of features for the current data point.
            double[] features = point.getmyFeatures();
            // Accumulate the feature values for each dimension to calculate the sum.
            for (int i = 0; i < totalFeature; i++) {
                centFeature[i] += features[i];
            }
        }
        // Calculate the mean value for each feature dimension by dividing the sum by the number of data points.
        for (int i = 0; i < totalFeature; i++) {
            centFeature[i] /= points.size();
        }
        // Create a new Data object representing the centroid with the calculated feature values.
        // Return the centroid Data object.
        return new Data(centFeature);
    }

    // This method returns the list of clusters managed by the k-means algorithm.
    // It simply returns the 'clusters' list.
    public List<Cluster> getClusters() {
        return clusters;
    }

    public static void main(String[] args) {
        // Create an ArrayList to store the data points read from the file.
        List<Data> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/evelynbarragan/Documents/CSCD429/K_Means/src/synthetic_control_data.txt"))) {
            String lines;
            // Read each line from the file.
            while ((lines = br.readLine()) != null) {
                // Split the line by whitespace and store individual values in 'Strvalue' array.
                String[] Strvalue = lines.trim().split("\\s+");
                // Create a new array 'feature' to store parsed double values from 'Strvalue'.
                double[] feature = new double[Strvalue.length];
                // Convert the string values to double and store them in the 'feature' array.
                for (int i = 0; i < Strvalue.length; i++) {
                    feature[i] = Double.parseDouble(Strvalue[i]);
                }
                // Create a new 'Data' object using the 'feature' array and add it to the 'points' list.
                points.add(new Data(feature));
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs during file reading.
            e.printStackTrace();
            return;
        }
        // Set the total number of clusters to create.
        int totalClusters = 6;
        // Create a new 'K_Means' object with the specified number of clusters and the data points list.
        K_Means kMeans = new K_Means(totalClusters, points);
        // Set the maximum number of iterations for the k-means algorithm.
        int max = 100;
        // Run the k-means algorithm to cluster the data points.
        kMeans.K_Means_Algo(max);
        // Get the resulting clusters after running the k-means algorithm.
        List<Cluster> clusters = kMeans.getClusters();
        // Loop through each cluster and write its data points to a separate file.
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            // Try-with-resources block to write cluster data to a file.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("Kcluster_" + (i + 1) + ".txt"))) {
                // Loop through each data point in the cluster.
                for (Data point : cluster.getDatapoint()) {
                    double[] features = point.getmyFeatures();
                    // Write each feature value followed by a space to the file.
                    for (int j = 0; j < features.length; j++) {
                        bw.write(features[j] + " ");
                    }
                    bw.newLine();
                }
            } catch (IOException e) {
                // Print the stack trace if an IOException occurs during file writing.
                e.printStackTrace();
                return;
            }
        }

        System.out.println("Six .txt files have been generated.");
    }
}

