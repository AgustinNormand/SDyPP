while true; do
  #curl http://192.168.0.62:9090/queueMessage;
  curl http://35.239.80.211/queueMessage;
  sleep $1;
done
