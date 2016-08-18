#create pdf files
library(xts)
library(PerformanceAnalytics)
library(quantmod)


filepath<-"/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/"
#Types<-as.matrix(read.csv(paste(filepath,"UpdateCategories.csv",sep="")))
Types<-as.matrix(c("Stocks","Germany","UnitedKingdom","HongKong","China","Argentina","Japan","India","Brazil","Israel","Australia","Mexico","Italy","France","Norway","Sweden","Switzerland","Greece","Denmark"))
##Types<-as.matrix(c("Stocks","Germany","UnitedKingdom","HongKong","China","Argentina"))
OutputPath<-"~/Desktop/Composite/"

#importdates
DJIAdata<-read.delim(paste(filepath,"Indices/DJIA/DJIAdailydata.csv",sep=""))
dates<-as.matrix(DJIAdata[,1])

daylength<-c(52,104,153,256,512,1260,1764)

quantilevalues<-c(0.5,0.8,0.9,0.95,0.99)

dailydatanames<-as.matrix(c("GSPC","GDAXI","FTSE","HSI","000001.SS","MERV","N225","BSESN","BVSP","TASE","AORD","MXY","NQIT","FCHI","NQNO","OMX","SSMI","GD.AT","NQDK"))
pdfPath<-"~/Desktop/CompositePDF/"
if (!file.exists(pdfPath)) {
  dir.create(pdfPath)
}
scaleDailyData<-function(x) {
  (x-min(x))/(max(x)-min(x))
}
names<-c("CompositeFifty","CompositeEighty","CompositeNinety","CompositeNinetyFive","CompositeNinetyNine")
for (l in 1:length(quantilevalues)) {
  if (!file.exists(paste(pdfPath,names[l],"/",sep=""))) {
    dir.create(paste(pdfPath,names[l],"/",sep=""))
  }
for (i in 1:length(Types)) {
  if (!file.exists(paste(pdfPath,names[l],"/",Types[i],"/",sep=""))) {
    dir.create(paste(pdfPath,names[l],"/",Types[i],"/",sep=""))
  }
  pdf(file=paste(pdfPath,names[l],"/",Types[i],"/",Types[i],".pdf",sep=""),width=28,height=18,title=paste("The Bubble Index Composite:",Types[i],sep=""))#paper='a4r')
  
  daylength<-c(52,104,153,256,512,1260,1764)
  for (p in 1:length(daylength)) {
    standardvalue<-exp(-9.746393+3.613444*log(daylength[p]))*2+550
    filename<-paste(OutputPath,Types[i],daylength[p],"Quantile",quantilevalues[l]*100,".csv",sep="")
    if (file.exists(filename)) {
    DaysWindow<-read.csv(filename,header=FALSE)
    DaysWindow[DaysWindow == 0] <- NA
    DaysWindow<-na.omit(DaysWindow)
    maxvalue<-max(DaysWindow[,2]/standardvalue*100)
    Daily_Data<-read.delim(paste("/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/Indices/",dailydatanames[i],"/",dailydatanames[i],"dailydata.csv",sep=""),header=FALSE)
    date<-as.Date(Daily_Data[,1],"%Y-%m-%d")
    Daily_Data<-xts(scaleDailyData(log(Daily_Data[,2]))*maxvalue,date,dimnames=list(NULL,"Price"))
    date<-as.Date(DaysWindow[,1],"%Y-%m-%d")
    datafinal<-xts(DaysWindow[,2]/standardvalue*100,date,dimnames=list(NULL, "Price"))
  
    chartSeries(datafinal, TA=c(addTA(Daily_Data,on=1,col=4)), theme = chartTheme("white", up.col="black"), name = paste("The Bubble Index Composite ",quantilevalues[l]*100,"% : ", Types[i], " (",daylength[p]," days)",sep=""))
    }
  }
  dev.off()
  }
}

