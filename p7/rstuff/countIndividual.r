source("dbcon.r")
helper = function(x,keyword){
	grepl(keyword,x)
}

seperateKeywords = function(keywords,textTable){
	z= as.vector(0)
	for(i in 1:length(keywords)){
		answerTable = table(sapply(textTable,helper,keyword = keywords[i]))
		nrTable = answerTable[names(answerTable) == TRUE]
		if(0 == length(nrTable))
			z[i] = 0
		else{
			nr = nrTable[[1]]
			z[i] = nr
		}
	}
	z	
}

onlyText = sqldf("SELECT tweettext FROM tweets")
onlyTextVector = as.vector(onlyText[[1]])
keywords = scan("eh.txt",what="",sep=",")

tester = seperateKeywords(keywords,onlyTextVector)  
barplot(tester,names.arg = keywords)

dbDisconnect(con)