gridtest = read.csv(file = "avg.csv", header = FALSE, sep =",")
x = gridtest[[1]]
y = gridtest[[2]]
plot(x,y,ylab="Time in ms",xlab ="cps")
lines(lowess(x,y))