## Templates to create/update niginx containers for reverse proxy & split traffic between old webmethods and new jag-jci api

### Template for Nginx 1 - To reverse proxy and split traffic between new jag-jci api & the other Nginx 2 server container.
* defaultNetworkPolicies.yaml (downloaded QuickStart.yaml from above link)

### Command to execute template
1) Login to OC using login command
2) Run below command in each env. namespace dev/test/prod/tools
   ``oc process -f nginx_jag_jci.yaml --param-file=nginx_jag_jci.env | oc apply -f -``
