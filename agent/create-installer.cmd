@REM Do działania wymagane WiX Toolset  https://wixtoolset.org/
@REM Skrypt tworzy gotową szybką instalkę (wraz z własnym JRE, więc nie jest potrzebna wcześniejsza instalacja Javy na komputerze)
cd ..
call mvn clean package -Dmaven.test.skip=true
cd agent
rd /s /q installimage
md installimage
copy target\agent-1.0.jar installimage
jpackage --type msi --input installimage --name Agent --main-jar agent-1.0.jar --icon icons\factory.ico --win-menu --win-menu-group Agent --win-console --win-dir-chooser --win-shortcut --win-upgrade-uuid f1fdf1e5-79bc-42af-8fbc-c521668cfaa1
timeout 3
