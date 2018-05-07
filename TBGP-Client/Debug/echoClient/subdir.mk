################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../echoClient/connectionHandler.cpp \
../echoClient/echoClient.cpp 

OBJS += \
./echoClient/connectionHandler.o \
./echoClient/echoClient.o 

CPP_DEPS += \
./echoClient/connectionHandler.d \
./echoClient/echoClient.d 


# Each subdirectory must supply rules for building sources it contributes
echoClient/%.o: ../echoClient/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


