make sure jdk and maven is installed
mvn install:install-file -Dfile=${project.basedir}/src/main/resources/yeppp-bundle.jar -DgroupId=yeppp -DartifactId=yeppp-java -Dversion=1.0 -Dpackaging=jar
mvn clean compile assembly:single