all: run
run:
	@java -classpath compile/.:lib/jcommander-1.48.jar:lib/log4j-api-2.4.1.jar:lib/log4j-core-2.4.1.jar:lib main.Main ${ARGS}
runMvn:
	@cp target/1.0-1.0-SNAPSHOT-jar-with-dependencies.jar .
	@java -jar 1.0-1.0-SNAPSHOT-jar-with-dependencies.jar
cleanMvn:
	@rm -rf target
	@rm -f 1.0-1.0-SNAPSHOT-jar-with-dependencies.jar
clean:
	@rm -rf compile
compileMvn: cleanMvn
	@mvn compile assembly:single
compile: clean
	@echo -e 'Javac works'
	@mkdir compile
	@javac -d compile -classpath lib/jcommander-1.48.jar:lib/log4j-core-2.4.1.jar:lib/log4j-api-2.4.1.jar src/**/*.java src/**/**/*.java src/**/**/**/*.java
	@echo -e 'Build jar'
	@cp cfg/log4j2.xml compile/.
	@echo -e 'Success!'
