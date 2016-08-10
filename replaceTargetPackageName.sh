echo "----------------------------------"
echo "you can input:(1,2,3)"
echo "1.replace to express package"
echo "2.replace to int package"
echo "3.Exit Menu"
echo "**********************************"
echo "After complete, Please clean project and rebuild!!"
echo "**********************************"
echo "----------------------------------"
echo "please input your choise: "
filepath=$(cd "$(dirname "$0")"; pwd)
read input
case $input in
	"1")
		sed -i -e 's/targetPackage=".*"/targetPackage="com.dolphin.browser.express.web"/g' $filepath/bvt/*/AndroidManifest.xml
		;;
	"2")
		sed -i -e 's/targetPackage=".*"/targetPackage="mobi.mgeek.TunnyBrowser"/g' $filepath/bvt/*/AndroidManifest.xml
		;;
	*)
		echo "exit"
		;;
esac