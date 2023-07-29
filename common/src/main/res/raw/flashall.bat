@echo off
title Mauronofrio Fastboot Rom Flasher :P
:choice
set /P c=Do you want to wipe all the data ( Reccomended )[Y/N]?
if /I "%c%" EQU "Y" goto :wipe
if /I "%c%" EQU "N" goto :continue
goto :choice
:wipe
fastboot -w
goto :continue
:continue
fastboot flash boot boot.img
fastboot flash dtbo dtbo.img
fastboot flash modem modem.img
fastboot flash reserve reserve.img
fastboot flash recovery recovery.img
fastboot --disable-verity flash vbmeta vbmeta.img
fastboot --disable-verity flash vbmeta_system vbmeta_system.img
fastboot reboot fastboot
fastboot flash abl abl.img
fastboot flash aop aop.img
fastboot flash bluetooth bluetooth.img
fastboot flash cmnlib cmnlib.img
fastboot flash cmnlib64 cmnlib64.img
fastboot flash devcfg devcfg.img
fastboot flash dsp dsp.img
fastboot flash hyp hyp.img
fastboot flash imagefv imagefv.img
fastboot flash keymaster keymaster.img
fastboot flash LOGO LOGO.img
fastboot flash multiimgoem multiimgoem.img
fastboot flash odm odm.img
fastboot flash oem_stanvbk oem_stanvbk.img
fastboot flash opproduct opproduct.img
fastboot flash qupfw qupfw.img
fastboot flash storsec storsec.img
fastboot flash tz tz.img
fastboot flash uefisecapp uefisecapp.img
fastboot flash xbl xbl.img
fastboot flash xbl_config xbl_config.img
fastboot flash system system.img
fastboot flash vendor vendor.img
fastboot flash product product.img
fastboot reboot
pause