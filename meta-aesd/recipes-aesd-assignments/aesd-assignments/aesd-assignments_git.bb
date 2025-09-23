#inherit core-image-aesd
#inherit externalsrc
#inherit autotools
inherit update-rc.d
MACHINE = "qemuarm64"
#INHIBIT_PACKAGE_STRIP = "1" 
#TARGET_ARCH="arm"
#TUNE_ARCH="cortexa57"

# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
#CHECK THAT compile to correct machine format ; rather than cortex


# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
#SRC_URI = "git://git@github.com:cu-ecen-aeld/assignments-3-and-later-hengjoontiang.git; branch=main; protocol=ssh; tag=assignment-6-part-1"
#SRC_URI = "git://git@github.com:cu-ecen-aeld/assignments-3-and-later-hengjoontiang.git;branch=main;protocol=ssh;tag=assignment-6-part-1;"
#check the src-url,seemed to be having an error
SRC_URI="git://github.com/cu-ecen-aeld/assignments-3-and-later-hengjoontiang.git;protocol=ssh;branch=main;protocol=ssh;"
#SRC_URI="git://github.com/cu-ecen-aeld/assignments-3-and-later-hengjoontiang.git;"
#SRC_URI = "git://github.com/cu-ecen-aeld/assignments-3-and-later-hengjoontiang.git;"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
#SRCREV = "f99b82a5d4cb2a22810104f89d4126f52f4dfaba"
#SRCREV="e586739d429c512ce6ddb830c34e56b8a59ca194"
SRCREV="ebb0f36bea0aeefb01e8475c418e4c8e3f7da8d5"
#SRCREV = "877ffcc"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
#/home/hengjt/Desktop/Training/Coursera/LinuxSystemProgrammingandIntroductiontoBuildroot/Assignment6/assignment-6-part-2/poky/build/tmp/work/core2-64-poky-linux/aesd-assignments/1.0+gitAUTOINC+e586739d42-r0/git/server
#S="${WORKDIR}/${BP}"
S="${WORKDIR}/git/server"
B="${S}"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN}+="${bindir}/aesdsocket"
FILES:${PN}+="${bindir}/aesdsocket-start-stop"
FILES:${PN}+="${sbindir}/start-stop-daemon"

# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-pthread -lrt -lc"
TARGET_CC_ARCH += "${LDFLAGS}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
#echo "ln -s /sbin/start-stop-daemon /path/to/new/link_name" > ${S}/start-stop-daemon

INITSCRIPT_PACKAGES="${PN}"
INITSCRIPT_NAME:${PN}="aesdsocket-start-stop"
EXTRA_OECONF = "--with-start-stop-daemon"


do_configure () {
	:
	
}
#check logfile
#${WORKDIR}=/home/hengjt/Desktop/Training/Coursera/LinuxSystemProgrammingandIntroductiontoBuildroot/Assignment6/assignment-6-part-2/build/tmp/work/qemuarm64-poky-linux/core-image-aesd/1.0-r
#${WORKDIR}/temp/log.do_taskname

#export CC
#CC=""
do_compile () {
        #cd ${S}
        bbplain "LAYERDIR=${LAYERDIR}"
        bbplain "S=${S}"
        bbplain "CROSS_COMPILE=${CROSS_COMPILE}"
	bbplain "CC=${CC}"
	oe_runmake clean 
	oe_runmake all
}
#check logfile
#${WORKDIR}/temp/log.do_taskname
do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	#cd ${B}
	#rm ${D}${bindir}
	bbplain "D=${D}"
	bbplain "bindir=${D}${bindir}"
	if [ -f "${D}${base_sbindir}/start-stop-daemon" ]; then
			bbplain "start-stop-daemon found in ${D}${base_sbindir}"
        	install -m 0755 -d "${D}${sbindir}"
        	install -m 0755 "${D}${base_sbindir}/start-stop-daemon" "${D}${sbindir}/"
    fi
    #if [ -f "${D}${bindir}/start-stop-daemon" ]; then
			bbplain "start-stop-daemon found in ${D}${bindir}"
			
        	install -m 0755 -d "${D}${sbindir}"
        	cp "${D}${bindir}/start-stop-daemon" "${D}${sbindir}/start-stop-daemon"
    fi
	
	install -d ${D}${bindir}
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/init.d/
	#update-rc.d -r ${D}${sysconfdir}/init.d/ S99aesdsocket defaults
	
}

#log files
#/home/hengjt/Desktop/Training/Coursera/LinuxSystemProgrammingandIntroductiontoBuildroot/Assignment6/assignment-6-part-2/build/tmp/stamps/qemuarm64-poky-linux/core-image-aesd
