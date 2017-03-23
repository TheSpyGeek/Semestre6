

list_serv=`ps -a | grep echoserveri | awk '{print $1}'`

# echo $list_serv

for i in $list_serv
do
	echo $i
	kill $i
done
