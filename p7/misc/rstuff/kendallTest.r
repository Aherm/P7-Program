data = read.csv(file = "kendallTest.csv", header = FALSE, sep=";")
dohmh = data[[1]]
our = data[[2]]
test1 = cor(dohmh,our,method = "kendall")
test2 = cor.test(dohmh,our,method = "kendall") 