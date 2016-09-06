library(xts)
library(PerformanceAnalytics)
library(quantmod)

Path<-"/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/"
OutputPath<-"~/Desktop/testPDF/"

scaleDailyData<-function(x) {
  (x-min(x))/(max(x)-min(x))
}

windowStdValue<-function(x) {
  exp(-9.746393+3.613444*log(x))*2+550
}

windows<-cbind(52,104,153,256,512,1260,1764,2520,5040,10080,20160)
#windows<-cbind(52,104,153,200,256,300,350,400,450,512,550,600,650,700,750,800,850,900,950,1000,1050,1100,1150,1200,1260,1500,1700,1764,2000,2200,2520,2700,3000,3200,3500,3700,4000,4200,4500,4700,5040,5200,5500,5700,6000,6200,6500,6700,7000,7200,7500,7700,8000,8200,8500,8700,9000,9200,9500,9700,10080,10200,10500,10700,11000,11200,11500,11700,12000,12200,12500,12700,13000,13200,13500,13700,14000,14200,14500,14700,15000,15200,15500,15700,16000,16200,16500,16700,17000,17200,17500,17700,18000,18200,18500,18700,19000,19200,19500,19700,20000,20160,20500,20700,21000,21200,21500,21700,22000,22200,22500,22700,23000,23200,23500,23700,24000,24200,24500,24700,25000,25200,25500,25700,26000,26200,26500,26700,27000,27200,27500,27700,28000,28200,28500,28700,29000,29200,29500,29700,30000,30200,30500,30700,31000,31200,31500,31700,32000,32200,32500,32700,33000,33200,33500,33700,34000,34200,34500,34700,35000,35200,35500)
if (!file.exists(OutputPath)) {
  dir.create(OutputPath)
}

TypeList<-as.matrix(read.csv(paste(Path,"UpdateCategories.csv",sep=""), header=FALSE))
#TypeList<-cbind("Indices","Commodities","Currencies")
for (i in 1:length(TypeList)) {

  if (!file.exists(paste(OutputPath,TypeList[i],"/",sep=""))) {
    dir.create(paste(OutputPath,TypeList[i],"/",sep=""))
  }
  
  Names<-as.matrix(read.csv(paste(Path,TypeList[i],".csv",sep=""), header=FALSE))
  
  for (j in 1:length(Names)) {
    if (!file.exists(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))) {
      dir.create(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))
    }
    
    workingdir<-paste(Path,TypeList[i], "/", Names[j],"/",sep="")
    setwd(workingdir)
    pdf(file=paste(OutputPath,TypeList[i],"/",Names[j],"/",Names[j],".pdf",sep=""),width=28,height=18,title=paste("The Bubble Index:",Names[j],sep=""))#paper='a4r')
    
    for (k in 1:length(windows)) {
      fileExists<-file.exists(paste(workingdir,Names[j],windows[k],"days.csv",sep=""))
      if (fileExists) {
        Days_Window<-read.csv(paste(workingdir,Names[j],windows[k],"days.csv",sep=""))
        if (!file.exists(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))) {
          dir.create(paste(OutputPath,TypeList[i],"/",Names[j],"/",sep=""))
        }
        tryCatch({
          if (fileExists) {
            maxvalue1<-max(Days_Window[,2]/windowStdValue(windows[k])*100)
            Daily_Data<-read.delim(paste(workingdir,Names[j],"dailydata.csv",sep=""),header=FALSE)
            #Remove any dates where the price is 0.0
            row_sub<-apply(Daily_Data, 1, function(row) all(row > 0.001)) 
            Daily_Data<-Daily_Data[row_sub,]
            date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
            Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue1,date,dimnames=list(NULL,"Price"))
            date<-as.Date(Days_Window[,3],"%Y-%m-%d")
            datafinal<-xts(Days_Window[,2]/windowStdValue(windows[k])*100,date,dimnames=list(NULL, "Price"))
            chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index: ", Names[j], " (", windows[k], " days)",sep=""),yrange= c(0, maxvalue1))
          }
      }, error=function(e){
        dev.off()
        Names[j]
      })
      }
    }
    dev.off()
    }
}
