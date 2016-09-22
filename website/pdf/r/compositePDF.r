#create pdf files
library(xts)
library(PerformanceAnalytics)
library(quantmod)

filepath<-"/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/"
Types<-as.matrix(c("Stocks","Germany","UnitedKingdom","HongKong","China","Argentina","Japan","India","Brazil","Israel","Australia","Mexico","Italy","France","Norway","Sweden","Switzerland","Greece","Denmark"))
InputPath<-"/media/green/Data/Dropbox/BubbleIndex/Version4/Composite/"

#importdates
DJIAdata<-read.delim(paste(filepath,"Indices/DJIA/DJIAdailydata.csv",sep=""))
dates<-as.matrix(DJIAdata[,1])

daylength<-c(52,104,153,256,512,1260,1764)
#daylength<-cbind(52,104,153,200,256,300,350,400,450,512,550,600,650,700,750,800,850,900,950,1000,1050,1100,1150,1200,1260,1500,1700,1764,2000,2200,2520,2700,3000,3200,3500,3700,4000,4200,4500,4700,5040,5200,5500,5700,6000,6200,6500,6700,7000,7200,7500,7700,8000,8200,8500,8700,9000,9200,9500,9700,10080,10200,10500,10700,11000,11200,11500,11700,12000,12200,12500,12700,13000,13200,13500,13700,14000,14200,14500,14700,15000,15200,15500,15700,16000,16200,16500,16700,17000,17200,17500,17700,18000,18200,18500,18700,19000,19200,19500,19700,20000,20160,20500,20700,21000,21200,21500,21700,22000,22200,22500,22700,23000,23200,23500,23700,24000,24200,24500,24700,25000,25200,25500,25700,26000,26200,26500,26700,27000,27200,27500,27700,28000,28200,28500,28700,29000,29200,29500,29700,30000,30200,30500,30700,31000,31200,31500,31700,32000,32200,32500,32700,33000,33200,33500,33700,34000,34200,34500,34700,35000,35200,35500)

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
InputNames<-c("Fifty","Eighty","Ninety","NinetyFive","NinetyNine")

for (l in 1:length(quantilevalues)) {
  if (!file.exists(paste(pdfPath,names[l],"/",sep=""))) {
    dir.create(paste(pdfPath,names[l],"/",sep=""))
  }
for (i in 1:length(Types)) {
  if (!file.exists(paste(pdfPath,names[l],"/",Types[i],"/",sep=""))) {
    dir.create(paste(pdfPath,names[l],"/",Types[i],"/",sep=""))
  }
  pdf(file=paste(pdfPath,names[l],"/",Types[i],"/",Types[i],".pdf",sep=""),width=28,height=18,title=paste("The Bubble Index Composite:",Types[i],sep=""))#paper='a4r')
  
  for (p in 1:length(daylength)) {
    standardvalue<-exp(-9.746393+3.613444*log(daylength[p]))*2+550
    filename<-paste(InputPath,Types[i],"Quantile","/",Types[i],"Quantile",InputNames[l],"/",Types[i],"Quantile",InputNames[l],daylength[p],"days.csv",sep="")
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

