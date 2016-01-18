all: get_java ejecutar

get_java:
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java8-installer


ejecutar:
	java -jar pintame.jar
