#install.packages("GGally")
library(GGally)


#load data 
control_data <- read.table('/Users/evelynbarragan/documents/CSCD429/synthetic_control_data.txt', header = FALSE, sep = "")

# k-means clustering with 6 clusters 
total_cluster <- 6 
result <- kmeans(control_data, centers = total_cluster)

#extract the cluster assignments 
clusters <- result$cluster

#add clusters assignments 
clusters_data <- data.frame(control_data, Cluster = clusters)

#convert data to matrix for parallel coordinates plot 
clusters_matrix <- as.matrix(clusters_data)

#Visualize the clusters using parallel coordinates plot
ggparcoord(clusters_matrix, columns = 1:60, groupColumn = "Cluster", alphaLines = 0.5)

