
helper = function(x,keyword){
	grepl(keyword,x)
}

seperateKeywords = function(keywords,textTable){
	z= as.vector(0)
	for(i in 1:length(keywords)){
		answerTable = table(sapply(textTable,helper,keyword = keywords[i]))
		nrTable = answerTable[names(answerTable) == TRUE]
		if(length(nrTable) == 0)
		  z[i] = 0
		else{
          nr = nrTable[[1]]
		  z[i] = nr
		}
	}
	z	
}

onlyText = sqldf("SELECT tweettext FROM tweets ")
onlyTextVector = as.vector(onlyText[[1]])
keywords = scan('keywordsv3.txt',what="",sep=",")

tester = seperateKeywords(keywords,onlyTextVector) 
tester = sort(tester,decreasing = TRUE)
verticalMax = max(tester) + (10 - (max(tester) %% 10))
z = barplot(tester,names.arg = keywords,ylim = c(0,verticalMax),xpd = FALSE,yaxt="n")
yaxe = seq(0,verticalMax,50)
yaxe[length(yaxe) + 1] = verticalMax
axis(2,at =yaxe)
framer = data.frame(keywords,tester)

dbDisconnect(con)