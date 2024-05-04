#!/bin/bash

STATE=$1
NEW_STATE=$2

cd ../squid
mkdir "${NEW_STATE}"
cp -r ${STATE}/* "${NEW_STATE}"
../scripts/find_replace.sh "${NEW_STATE}" "org.firstinspires.ftc.teamcode.squid.${STATE}" "org.firstinspires.ftc.teamcode.squid.${NEW_STATE}"

../scripts/find_replace.sh "${STATE}" "org.firstinspires.ftc.teamcode.mollusc" "org.firstinspires.ftc.teamcode.squid.${STATE}.mollusc"

cp -r ../mollusc "${STATE}"
rm -r "${STATE}/mollusc/test"
rm "${STATE}/mollusc/.git"
../scripts/find_replace.sh "${STATE}" "org.firstinspires.ftc.teamcode.mollusc" "org.firstinspires.ftc.teamcode.squid.${STATE}.mollusc"
