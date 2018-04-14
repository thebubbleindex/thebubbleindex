To run you need to do a few things.

In setenv-overrides file in bin folder set the properties to match your own lookup service id and ip addresses:
XAP_LOOKUP_LOCATORS=my-computer-id:4174
XAP_NIC_ADDRESS=192.168.1.100

On gnu-linux OS you need to also edit your /etc/hosts file to include ALL machines and their ip alias mapping.

Then start a lookup service on each machine. Once this is done you can start the polling containers. The finalizer and the client standalone application should both be started only on the machine which has the actual input and output bubble index files.