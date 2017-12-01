#!/usr/bin/env bash

GH_LEIN_SCRIPT_IX=https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
GH_LEIN_SCRIPT_WIN=https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein.bat
LEIN_CMD=lein
CURL_CMD=curl

which $LEIN_CMD &> /dev/null
LEIN_FOUND=$?

RED='\033[0;31m'
GREEN='\033[0;32m'
LINK_COLOR=$GREEN
NC='\033[0m'

if [ "x$LEIN_FOUND" = "x0" ]; then
    printf "Looks like you have lein installed. ${GREEN}Yay!${NC}\n"
else
    printf "${RED}No leiningen found.${NC}\n"
    PREFERRED_PATH=$HOME/bin
    PREFERRED_PATH_SET=false
    PATH_SPLIT=`echo $PATH | tr : '\n'`
    for p in $PATH_SPLIT
    do
	if [ "x$p" = "x$PREFERRED_PATH" ]; then
	    PREFERRED_PATH_SET=true
	fi
    done
    if [ "x$PREFERRED_PATH_SET" = "xtrue" ]; then
	echo "You have $PREFERRED_PATH in PATH."
    else
	printf "Add ${RED}$PREFERRED_PATH${NC} to \$PATH please.\n"
    fi
    printf "Download leiningen from either of the following locations\n 1. ${LINK_COLOR}$GH_LEIN_SCRIPT_IX${NC} (Linux or Mac), or\n 2. ${LINK_COLOR}$GH_LEIN_SCRIPT_WIN${NC} (Windows)\n depending on your OS, and into $PREFERRED_PATH\n"
fi
