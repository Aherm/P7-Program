source("dbcon.r")

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
keywords = scan("keywords.txt",what="",sep = ",")

resultTable = table(sapply(onlyTextVector, myFun,keywords = keywords))

barplot(resultTable)

dbDisconnect(con)