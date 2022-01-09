rem ---------------------------------------------------------------------------
rem jcp.cmd -- Script for appending libraries to the JAVA_CLASSPATH
rem environment variable.
rem
rem usage - jcp.cmd [library]
rem
rem example - to append mylib.zip:
rem ---- call "%SCRIPT_HOME%jcp.cmd" mylib.zip
rem
rem example - to append all jar files in a directory:
rem ---- for %%i in ("%SFX_LIB%\*.jar") do call "%SCRIPT_HOME%jcp.cmd" "%%i"
rem
rem ---------------------------------------------------------------------------
set JAVA_CLASSPATH=%JAVA_CLASSPATH%;%1

