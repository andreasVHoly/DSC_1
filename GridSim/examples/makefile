JFLAGS = -g
JC = javac
J = java
CP = -classpath
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = \
	InputCreator.java
default: classes
classes: $(CLASSES:.java=.class)
compile:
	$(JC) $(CP) ../jars/gridsim.jar:. datagrid/example04/DataGridSim.java
run:
	$(J) $(CP) ../jars/gridsim.jar:. datagrid.example04.DataGridSim parameters.txt
runinput:
	$(J) InputCreator
clean:
	$(RM) *.class
