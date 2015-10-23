all: run
run:
	@java -jar target/1.0-1.0-SNAPSHOT-jar-with-dependencies.jar -port 80
clean:
	@rm -rf target
compileMvn: clean
	@mvn compile assembly:single
compile: clean
	@echo -e 'Javac works'
	@javac -d compile -classpath lib/jcommander-1.7.jar:lib/log4j-core-2.4.1.jar:lib/log4j-api-2.4.1.jar src/**/*.java src/**/**/*.java src/**/**/**/*.java
	@echo -e 'Build jar'
	@echo -e 'Success!'
