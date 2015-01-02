# M A K E F I L E
#
# ARCHICTECTURE = linux 
#

FCOMPLR   = ifort 
#gfortran
#ifort 
FCFLAGS   =  -O2
#FCFLAGS   =  -O2 -axT
#FCFLAGS   = -p -g -warn all -check all

BIN_DIR= .
#./bin
OBJ_DIR= .
#../bin/obj

OBJECT = \
			$(OBJ_DIR)/mhag.o \
			$(OBJ_DIR)/mhag_gui.o \
			$(OBJ_DIR)/main.o 

default: all

all : $(BIN_DIR)/mhag

$(BIN_DIR)/mhag : $(OBJECT) 
	$(FCOMPLR) $(FCFLAGS) $(OBJECT) -o $@ 

$(OBJ_DIR)/mhag.o : mhag.f90
	$(FCOMPLR) $(FCFLAGS) -c -o $(OBJ_DIR)/mhag.o $<

$(OBJ_DIR)/mhag_gui.o : mhag_gui.f90
	$(FCOMPLR) $(FCFLAGS) -c -o $(OBJ_DIR)/mhag_gui.o $<

$(OBJ_DIR)/main.o : main.f90
	$(FCOMPLR) $(FCFLAGS) -c -o $(OBJ_DIR)/main.o $<

clean :
	rm -f *~ *.mod $(BIN_DIR)/mhag $(OBJECT)

