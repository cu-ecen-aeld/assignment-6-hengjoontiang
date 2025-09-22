#!/bin/bash
# Script to build image for qemu.
# Author: Siddhant Jajoo.

git submodule init
git submodule sync
git submodule update

 eval "$(ssh-agent -s)"
 
# local.conf won't exist until this step on first execution
#bitbake qemuarm64-poky-linux
source poky/oe-init-build-env
#source poky/oe-init-build-env qemuarm64-poky-linux
#source oe-init-build-env build-qemu-arm64
export MACHINE="qemuarm64"
export BB_NUMBER_THREADS="8"
#export IMAGE_INSTALL_append = "aesd-assignments"   
CONFLINE="MACHINE = \"qemuarm64\""

cat conf/local.conf | grep "${CONFLINE}" > /dev/null
local_conf_info=$?

if [ $local_conf_info -ne 0 ];then
	echo "Append ${CONFLINE} in the local.conf file"
	echo ${CONFLINE} >> conf/local.conf
	
else
	echo "${CONFLINE} already exists in the local.conf file"
fi

#CONFLINE="TARGET_PREFIX = 'aarch64-linux-gnu-'"
#cat conf/local.conf | grep "${CONFLINE}" > /dev/null
#local_conf_info=$?

#if [ $local_conf_info -ne 0 ];then
#	echo "Append ${CONFLINE} in the local.conf file"
#	echo ${CONFLINE} >> conf/local.conf
#else
#	echo "${CONFLINE} already exists in the local.conf file"
#fi


bitbake-layers show-layers | grep "meta-aesd" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-aesd layer"
	bitbake-layers add-layer ../meta-aesd
else
	echo "meta-aesd layer already exists"
fi

set -e
#force clean
#bitbake core-image-aesd -c cleanall  
#bitbake core-image-aesd -c cleansstate
#bitbake core-image-aesd -c compile
bitbake core-image-aesd
#MACHINE=qemuarm64 bitbake -g aesd-assignments
#bitbake -b aesd-assignments_git
