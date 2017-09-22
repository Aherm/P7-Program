
checkIfKeywordInString = function(string,nr,keywords){
	if(is.na(keywords[nr]))
		FALSE
	else{
		isKeywordIn = grepl(keywords[nr],string); 
		if(isKeywordIn)
			isKeywordIn 
		else
			checkIfKeywordInString(string,nr + 1,keywords)
	}
}

myFun = function(x,keywords){
	checkIfKeywordInString(x,1,keywords)
}

onlyText = sqldf("SELECT tweettext FROM tweets")
onlyTextVector = as.vector(onlyText[[1]])
keywords = scan("keywordsv3.txt",what="",sep = ",")

resultTable = table(sapply(onlyTextVector, myFun,keywords = keywords))

+barplot(resultTable,names.arg = c("No Keywords","Minimum one keyword"))

dbDisconnect(con)