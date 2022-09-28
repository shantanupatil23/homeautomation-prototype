#!/bin/sh
declare -i percent

# Set you'r laptop's files
exec < /sys/class/power_supply/BAT1/capacity
read percent
status=true

#Connection config
ip=101 #Ardiono's IP Address
output_pin=1 #Ardiono's O/P Pin

#Battery Range config
low=13
high=90

if [ $percent -lt $high ]; 
then
	wget http://192.168.0.$ip/0$output_pin
	status=false
	rm 0$output_pin
elif [ $percent -ge $high ]; 
then
	wget http://192.168.0.$ip/1$output_pin
	status=true
	rm 1$output_pin
fi

while [ 1 ]
do
	sleep 3m
	exec < /sys/class/power_supply/BAT1/capacity
	read percent
	if [ $percent -le $low ];
	then
		wget http://192.168.0.$ip/0$output_pin
		status=false
		rm 0$output_pin
		sleep 1h
	elif [ $percent -ge $high ];
	then
		wget http://192.168.0.$ip/1$output_pin
		status=true
		rm 1$output_pin
		sleep 1h
	fi
done
