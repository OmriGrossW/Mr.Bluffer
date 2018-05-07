################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../playerClient/connectionHandler.cpp \
../playerClient/playerClient.cpp 

OBJS += \
./playerClient/connectionHandler.o \
./playerClient/playerClient.o 

CPP_DEPS += \
./playerClient/connectionHandler.d \
./playerClient/playerClient.d 


# Each subdirectory must supply rules for building sources it contributes
playerClient/%.o: ../playerClient/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


