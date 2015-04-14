
# prod - prod

echo '1) unzip'
cd /home/ubuntu/tmp
rm -Rf tz-tajo-1.0-SNAPSHOT
cd /home/ubuntu/tmp/dist
unzip tz-tajo-1.0-SNAPSHOT.zip

echo '2) kill process'
cd /home/ubuntu/tz-tajo
pid=`cat RUNNING_PID`
kill -9 $pid
rm -Rf RUNNING_PID
rm -Rf /home/ubuntu/tz-tajo/./RUNNING_PID
cd ..

echo '3) move current directory'
rm -Rf /home/ubuntu/tmp/tz-tajo
mv /home/ubuntu/tz-tajo /home/ubuntu/tmp

echo '4) copy other files'
cp /home/ubuntu/tmp/conf/prod.conf /home/ubuntu/tmp/dist/tz-tajo-1.0-SNAPSHOT
cp /home/ubuntu/tmp/conf/prod-logger.xml /home/ubuntu/tmp/dist/tz-tajo-1.0-SNAPSHOT
cp /home/ubuntu/tmp/prod.sh /home/ubuntu/tmp/dist/tz-tajo-1.0-SNAPSHOT

echo '5) move new directory'
mv /home/ubuntu/tmp/dist/tz-tajo-1.0-SNAPSHOT /home/ubuntu/tz-tajo
cd /home/ubuntu/tz-tajo

echo '6) lauch play'
chmod 777 start
./start -Dconfig.resource=prod.conf -Dlogger.file=/home/ubuntu/tz-tajo/prod-logger.xml &

echo '7) end'
exit 0 
