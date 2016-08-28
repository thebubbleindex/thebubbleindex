library(xts)
library(PerformanceAnalytics)
library(quantmod)

Path<-"/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/"
OutputPath<-"~/Desktop/PDF/"

scaleDailyData<-function(x) {
  (x-min(x))/(max(x)-min(x))
}

`52daySept61932Value`<-exp(-9.746393+3.613444*log(52))*2+550
`104dayDec241931value`<-exp(-9.746393+3.613444*log(104))*2+550
`153dayOct41932Value`<-exp(-9.746393+3.613444*log(153))*2+550
`256dayFeb21931Value`<-exp(-9.746393+3.613444*log(256))*2+550
`512dayOct01Value`<-exp(-9.746393+3.613444*log(512))*2+550
`1260dayOct01Value`<-exp(-9.746393+3.613444*log(1260))*2+550
`1764dayOct01Value`<-exp(-9.746393+3.613444*log(1764))*2+550
`2520dayOct01Value`<-exp(-9.746393+3.613444*log(2520))*2+550
`5040dayOct01Value`<-exp(-9.746393+3.613444*log(5040))*2+550
`10080dayNov162007Value`<-exp(-9.746393+3.613444*log(10080))*2+550
`20160dayValue`<-exp(-9.746393+3.613444*log(20160))*2+550

if (!file.exists(OutputPath)) {
  dir.create(OutputPath)
}

TypeList<-as.matrix(read.csv(paste(Path,"UpdateCategories.csv",sep=""), header=FALSE))

for (i in 1:length(TypeList)) {  
  if (!file.exists(paste(OutputPath,TypeList[i],"/",sep=""))) {
    dir.create(paste(OutputPath,TypeList[i],"/",sep=""))
  }
  
  Names<-as.matrix(read.csv(paste(Path,TypeList[i],".csv",sep=""), header=FALSE))
  
  for (j in 1:length(Names)) {
    
    workingdir<-paste(Path,TypeList[i], "/", Names[j],"/",sep="")
    setwd(workingdir)
    
    #read in data
    fileExists<-file.exists(paste(workingdir,Names[j],"52days.csv",sep=""), paste(workingdir,Names[j],"104days.csv",sep=""), paste(workingdir,Names[j],"153days.csv",sep=""), paste(workingdir,Names[j],"256days.csv",sep=""), paste(workingdir,Names[j],"512days.csv",sep=""), paste(workingdir,Names[j],"1260days.csv",sep=""), paste(workingdir,Names[j],"1764days.csv",sep=""),paste(workingdir,Names[j],"2520days.csv",sep=""),paste(workingdir,Names[j],"5040days.csv",sep=""),paste(workingdir,Names[j],"10080days.csv",sep=""),paste(workingdir,Names[j],"20160days.csv",sep=""))
    
    if (fileExists[1]) {
      Days_52_Window<-read.csv(paste(workingdir,Names[j],"52days.csv",sep=""))
    }
    if (fileExists[2]) {
      Days_104_Window<-read.csv(paste(workingdir,Names[j],"104days.csv",sep=""))
    }
    if (fileExists[3]) {
      Days_153_Window<-read.csv(paste(workingdir,Names[j],"153days.csv",sep=""))
    }
    if (fileExists[4]) {
      Days_256_Window<-read.csv(paste(workingdir,Names[j],"256days.csv",sep=""))
    }
    if (fileExists[5]) {
      Days_512_Window<-read.csv(paste(workingdir,Names[j],"512days.csv",sep=""))
    }
    if (fileExists[6]) {
      Days_1260_Window<-read.csv(paste(workingdir,Names[j],"1260days.csv",sep=""))
    }
    if (fileExists[7]) {
      Days_1764_Window<-read.csv(paste(workingdir,Names[j],"1764days.csv",sep=""))
    }
    if (fileExists[8]) {
      Days_2520_Window<-read.csv(paste(workingdir,Names[j],"2520days.csv",sep=""))
    }
    if (fileExists[9]) {
      Days_5040_Window<-read.csv(paste(workingdir,Names[j],"5040days.csv",sep=""))
    }
    if (fileExists[10]) {
      Days_10080_Window<-read.csv(paste(workingdir,Names[j],"10080days.csv",sep=""))
    }
    
    if (fileExists[11]) {
      Days_20160_Window<-read.csv(paste(workingdir,Names[j],"20160days.csv",sep=""))
    }
    
    if (!file.exists(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))) {
      dir.create(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))
    }
    tryCatch({
    pdf(file=paste(OutputPath,TypeList[i],"/",Names[j],"/",Names[j],".pdf",sep=""),width=28,height=18,title=paste("The Bubble Index:",Names[j],sep=""))#paper='a4r')
    
    if (fileExists[1]) {
      maxvalue1<-max(Days_52_Window[,2]/`52daySept61932Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue1,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (52 days)",sep=""),yrange= c(0, maxvalue1))
    }
    
    if (fileExists[2]) {
      maxvalue2<-max(Days_104_Window[,2]/`104dayDec241931value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)  
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue2,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (104 days)",sep=""),yrange=c(0,maxvalue2))
    }
    
    if (fileExists[3]) {
      maxvalue3<-max(Days_153_Window[,2]/`153dayOct41932Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue3,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (153 days)",sep=""),yrange=c(0,maxvalue3))
    }
    
    if (fileExists[4]) {
      maxvalue4<-max(Days_256_Window[,2]/`256dayFeb21931Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[256:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[256:length(Daily_Data[,2]),2]))*maxvalue4,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
	  
      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (256 days)",sep=""),yrange=c(0,maxvalue4))
    }
    
    if (fileExists[5]) {
      maxvalue5<-max(Days_512_Window[,2]/`512dayOct01Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[512:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[512:length(Daily_Data[,2]),2]))*maxvalue5,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (512 days)",sep=""),yrange=c(0,maxvalue5))
    }
    
    if (fileExists[6]) {
      maxvalue6<-max(Days_1260_Window[,2]/`1260dayOct01Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[1260:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[1260:length(Daily_Data[,2]),2]))*maxvalue6,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
	  
      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (1260 days)",sep=""),yrange= c(0, maxvalue6))
    }
    
    if (fileExists[7]) {
      maxvalue7<-max(Days_1764_Window[,2]/`1764dayOct01Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[1764:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[1764:length(Daily_Data[,2]),2]))*maxvalue7,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_1764_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_1764_Window[,2]/`1764dayOct01Value`*100,date,dimnames=list(NULL, "Price"))

	  chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (1764 days)",sep=""),yrange=c(0,maxvalue7))
    }
    
    if (fileExists[8]) {
      maxvalue8<-max(Days_2520_Window[,2]/`2520dayOct01Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[2520:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[2520:length(Daily_Data[,2]),2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_2520_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_2520_Window[,2]/`2520dayOct01Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (2520 days)",sep=""),yrange=c(0,maxvalue8))
    }
    
    if (fileExists[9]) {
      maxvalue9<-max(Days_5040_Window[,2]/`5040dayOct01Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[5040:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[5040:length(Daily_Data[,2]),2]))*maxvalue9,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_5040_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_5040_Window[,2]/`5040dayOct01Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (5040 days)",sep=""),yrange=c(0,maxvalue9))
    }
    
    if (fileExists[10]) {
      maxvalue10<-max(Days_10080_Window[,2]/`10080dayNov162007Value`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[10080:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[10080:length(Daily_Data[,2]),2]))*maxvalue10,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_10080_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_10080_Window[,2]/`10080dayNov162007Value`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (10080 days)",sep=""),yrange=c(0,maxvalue10))
    }
    
    if (fileExists[11]) {
      maxvalue11<-max(Days_20160_Window[,2]/`20160dayValue`*100)
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[20160:length(Daily_Data[,2]),1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[20160:length(Daily_Data[,2]),2]))*maxvalue11,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_20160_Window[,3],"%Y-%m-%d")
      datafinal<-xts(Days_20160_Window[,2]/`20160dayValue`*100,date,dimnames=list(NULL, "Price"))

      chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (20160 days)",sep=""),yrange=c(0,maxvalue11))
    }
    
    if (fileExists[10]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue11<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5,maxvalue6,maxvalue7,maxvalue8,maxvalue9,maxvalue10))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue11,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      `1260_Days`<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1764_Window[,3],"%Y-%m-%d")
      `1764_Days`<-xts(Days_1764_Window[,2]/`1764dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_2520_Window[,3],"%Y-%m-%d")
      `2520_Days`<-xts(Days_2520_Window[,2]/`2520dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_5040_Window[,3],"%Y-%m-%d")
      `5040_Days`<-xts(Days_5040_Window[,2]/`5040dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_10080_Window[,3],"%Y-%m-%d")
      `10080_Days`<-xts(Days_10080_Window[,2]/`10080dayNov162007Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6),addTA(`512_Days`,on=1,col=3),addTA(`1260_Days`,on=1,col=2),addTA(`1764_Days`,on=1,col=1),addTA(`2520_Days`,on=1,col=3),addTA(`5040_Days`,on=1,col=4),addTA(`10080_Days`,on=1,col=5)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue11))
    } else if (fileExists[9]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue10<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5,maxvalue6,maxvalue7,maxvalue8,maxvalue9))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue10,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      `1260_Days`<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1764_Window[,3],"%Y-%m-%d")
      `1764_Days`<-xts(Days_1764_Window[,2]/`1764dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_2520_Window[,3],"%Y-%m-%d")
      `2520_Days`<-xts(Days_2520_Window[,2]/`2520dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_5040_Window[,3],"%Y-%m-%d")
      `5040_Days`<-xts(Days_5040_Window[,2]/`5040dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6),addTA(`512_Days`,on=1,col=3),addTA(`1260_Days`,on=1,col=2),addTA(`1764_Days`,on=1,col=1),addTA(`2520_Days`,on=1,col=3),addTA(`5040_Days`,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue10))
    } else if (fileExists[8]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue9<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5,maxvalue6,maxvalue7,maxvalue8))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue9,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      `1260_Days`<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1764_Window[,3],"%Y-%m-%d")
      `1764_Days`<-xts(Days_1764_Window[,2]/`1764dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_2520_Window[,3],"%Y-%m-%d")
      `2520_Days`<-xts(Days_2520_Window[,2]/`2520dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6),addTA(`512_Days`,on=1,col=3),addTA(`1260_Days`,on=1,col=2),addTA(`1764_Days`,on=1,col=1),addTA(`2520_Days`,on=1,col=3)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue9))
    } else if (fileExists[7]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5,maxvalue6,maxvalue7))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      `1260_Days`<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1764_Window[,3],"%Y-%m-%d")
      `1764_Days`<-xts(Days_1764_Window[,2]/`1764dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6),addTA(`512_Days`,on=1,col=3),addTA(`1260_Days`,on=1,col=1),addTA(`1764_Days`,on=1,col=2)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))
    } else if (fileExists[6]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5,maxvalue6))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_1260_Window[,3],"%Y-%m-%d")
      `1260_Days`<-xts(Days_1260_Window[,2]/`1260dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6),addTA(`512_Days`,on=1,col=6),addTA(`1260_Days`,on=1,col=7)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))
    } else if (fileExists[5]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4,maxvalue5))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_512_Window[,3],"%Y-%m-%d")
      `512_Days`<-xts(Days_512_Window[,2]/`512dayOct01Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=2),addTA(`512_Days`,on=1,col=6)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))    
    } else if (fileExists[4]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2,maxvalue3,maxvalue4))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_256_Window[,3],"%Y-%m-%d")
      `256_Days`<-xts(Days_256_Window[,2]/`256dayFeb21931Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5),addTA(`256_Days`,on=1,col=6)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))        
    } else if (fileExists[3]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2,maxvalue3))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_153_Window[,3],"%Y-%m-%d")
      `153_Days`<-xts(Days_153_Window[,2]/`153dayOct41932Value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4),addTA(`153_Days`,on=1,col=5)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))        
    } else if (fileExists[2]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1,maxvalue2))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))
      date<-as.Date(Days_104_Window[,3],"%Y-%m-%d")
      `104_Days`<-xts(Days_104_Window[,2]/`104dayDec241931value`*100,date,dimnames=list(NULL, "Price"))
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=2),addTA(`104_Days`,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))        
    } else if (fileExists[1]) {
      Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
      maxvalue8<-max(c(maxvalue1))
      #Remove any dates where the price is 0.0
      row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001))
      Daily_Data<-Daily_Data[row_sub,]
      date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
      Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue8,date,dimnames=list(NULL,"Price"))
      date<-as.Date(Days_52_Window[,3],"%Y-%m-%d")
      `52_Days`<-xts(Days_52_Window[,2]/`52daySept61932Value`*100,date,dimnames=list(NULL, "Price"))    
      chartSeries(Daily_Data, TA=c(addTA(Daily_Data,on=1,col=1),addTA(`52_Days`,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (ALL Days)",sep=""),yrange=c(0,maxvalue8))      
    }
    dev.off()
    }, error=function(e){
      dev.off()
      Names[j]
    })}
}

