library(xts)
library(PerformanceAnalytics)
library(quantmod)

windowsArray<-c(52,104,153,256,512,1260,1764,5040,2520,10080,200,300,350,400,450,550,600,650,700,750,800,850,900,950,1000,1050,1100,1150,1200,1500,1700,2000,2200,2700,3000,3200,3500,3700,4000,4200,4500,4700,5200,5500,5700,6000,6200,6500,6700,7000,7200,7500,7700,8000,8200,8500,8700,9000,9200,9500,9700,10200,10500,10700,11000,11200,11500,11700,12000,12200,12500,12700,13000,13200,13500,13700,14000,14200,14500,14700,15000,15200,15500,15700,16000,16200,16500,16700,17000,17200,17500,17700,18000,18200,18500,18700,19000,19200,19500,19700,20000,20160,20500,20700,21000,21200,21500,21700,22000,22200,22500,22700,23000,23200,23500,23700,24000,24200,24500,24700,25000,25200,25500,25700,26000,26200,26500,26700,27000,27200,27500,27700,28000,28200,28500,28700,29000,29200,29500,29700,30000,30200,30500,30700,31000,31200,31500,31700,32000,32200,32500,32700,33000,33200,33500,33700,34000,34200,34500,34700,35000,35200,35500)
Path<-"/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/"
valuesArray<-matrix(0,length(windowsArray),1)
TypeList<-as.matrix(read.csv(paste(Path,"UpdateCategories.csv",sep=""), header=FALSE))

for (i in 1:length(TypeList)) {
  
  Names<-as.matrix(read.csv(paste(Path,TypeList[i],".csv",sep=""), header=FALSE))
  
  for (j in 1:length(Names)) {
    
    workingdir<-paste(Path,TypeList[i], "/", Names[j],"/",sep="")
    setwd(workingdir)
    #read in data
    for (k in 1:length(windowsArray)) {
      fileExists<-file.exists(paste(workingdir,Names[j],windowsArray[k],"days.csv",sep=""))
      if (fileExists) {
        result = tryCatch({
          WindowFile<-read.csv(paste(workingdir,Names[j],windowsArray[k],"days.csv",sep=""))
          
          if (length(WindowFile) < 3) next
          if (length(WindowFile[,2]) < 50) next
          if (valuesArray[k] == 0) {
            valuesArray[k]<-mean(mean(as.matrix(WindowFile[,2])))
          } else {
            valuesArray[k]<-mean(c(valuesArray[k], mean(as.matrix(WindowFile[,2]))))
          }
        }, warning = function(w) {
          #
        }, error = function(e) {
          #
        }, finally = {
          #
        })
      }
    }
  }
}

valuesArray[14]<-valuesArray[13]

# exponential
x<-windowsArray
y<-valuesArray
ylog<-log(valuesArray)

plot(x,y, type="p", main="Avg. Value vs. Window", sub="*Aggregate results from 17,330 timeseries", xlab="Window", ylab="Avg. Output Value")
f <-function (x,a,b,d) {exp(a) * x^b + d}
curve(f(x, a=-9.746393, b=3.613444, d = 550), add = TRUE, col="green", lwd=2, sub="curve")

plot(x,ylog, type="p", main="ln(Avg. Value) vs. Window", sub="*Aggregate results from 17,330 timeseries", xlab="Window", ylab="ln(Avg. Output Value)")
f <- function(x,a,b,d) {a * exp(b * x)+d}
fit <- nls(y ~ f(x,a,b,d), start = c(a=.1, b=.01,d=100000)) 
co <- coef(fit)
curve(f(x, a=co[1], b=co[2]), add = TRUE, col="green", lwd=2)

# logarithmic
f <- function(x,a,b) {a * log(x) + b}
fit <- nls(ylog ~ f(x,a,b), start = c(a=1, b=1)) 
co <- coef(fit)
curve(f(x, a=co[1], b=co[2]), add = TRUE, col="orange", lwd=2) 

# polynomial
f <- function(x,a,b,d,e) {(a*x^3) + (b*x^2) + (d*x) + e}
fit <- nls(y ~ f(x,a,b,d,e), start = c(a=5, b=-10, d=100, e=-100000)) 
co <- coef(fit)
curve(f(x, a=co[1], b=co[2], d=co[3], e=co[4]), add = TRUE, col="pink", lwd=2)

#ln(median)
#x^a*exp(b)=median  a=3.353178 b= -8.949354
