# Define the Java compiler
JC = javac

# Set the flags for the Java compiler
JFLAGS = -g

# Define the directory structure
SRC_DIR = src
BIN_DIR = bin

# Define the source files and their corresponding class files
SRCS = $(wildcard $(SRC_DIR)/*.java)
CLASSES = $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(SRCS))

# Default target to compile all Java files
all: $(CLASSES)

# Rule to compile each Java source file into class file
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JC) $(JFLAGS) -d $(BIN_DIR) $<

# Clean target to remove all compiled class files
clean:
	rm -rf $(BIN_DIR)

# Run target to compile all files and run the main class
run: all
	java -cp $(BIN_DIR) Main

# Phony targets to avoid conflicts with file names
.PHONY: all clean run
