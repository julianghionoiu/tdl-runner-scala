@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Record startup script for Windows
@rem
@rem ##########################################################################
@rem
@rem  The script needs to be tested under different versions of Windows:
@rem    - Windows 7
@rem    - Windows 8
@rem    - Windows 10
@rem
@rem  Each version may have their sub-types i.e. Home, Professional, Enterprise, etc... but targetting one type per main version type should be sufficient.
@rem
@rem  Also these use cases should be tested (manual tests):
@rem
@rem  - Executing script with JAVA_HOME not set ( set to empty: set JAVA_HOME= )
@rem  - Executing script with JAVA_HOME set to a valid JDK/JRE path ( set JAVA_HOME=/path/to/JDK or JRE )
@rem  - Executing script with JDK/JRE pointing to Java 9 JDK/JRE ( JAVA_HOME or PATH points to JDK/JRE 9.0.x )
@rem  - Executing script with JDK/JRE pointing to pre-Java 9 JDK/JRE ( JAVA_HOME or PATH points to JDK/JRE 1.n.x, where n is 7 or 8, any lower versions are NOT supported )
@rem  - Finally combination/permutation of the above (a JAVA_HOME type with JDK/JRE type), if necessary
@rem
@rem ##########################################################################

echo.
echo Displaying operating system specific systeminfo...
systeminfo | findstr /C:"OS"

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and RECORD_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
@rem A bit about the usage of for and value substitution here 
@rem See docs for https://en.wikibooks.org/wiki/Windows_Batch_Scripting#FOR
@rem       %%~s1       -   Modify of f, n and x to use short name
@rem                              or
@rem       %%~sI       -  expanded path contains short names only
@rem                     (see description at https://stackoverflow.com/questions/1333589/how-do-i-transform-the-working-directory-into-a-8-3-short-file-name-using-batch?answertab=active#tab-top)
@rem
@rem %%..1 or %%..I in the above example, refers to the input parameter (whole string) to 'for'
@rem Note: in a batch script we need to use %% instead of just % as per the docs.

for %%f in ("%JAVA_HOME%") do set JAVA_HOME=%%~sf
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

echo.
echo JAVA_HOME=%JAVA_HOME%

echo.
echo JAVA_EXE=%JAVA_EXE%

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set JARFILE=%APP_HOME%\record\record-and-upload-capsule.jar
set PARAM_CONFIG_FILE=--config %APP_HOME%\config\credentials.config
set PARAM_STORE_DIR=--store %APP_HOME%\record\localstore
set PARAM_SOURCECODE_DIR=--sourcecode %APP_HOME%

@echo on
for /f "tokens=3" %%g in ('%JAVA_EXE% -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_FULL_VERSION=%%g
)
@echo off

if defined JAVA_FULL_VERSION (
  set JAVA_FULL_VERSION=%JAVA_FULL_VERSION:"=%
) else (
  echo.
  echo Due to some reason, we could not determine the Java version via the '%JAVA_EXE% -version' command
  set JAVA_FULL_VERSION=""
)

echo.
echo JAVA_FULL_VERSION=%JAVA_FULL_VERSION%

for /f "delims=. tokens=1-3" %%v in ("%JAVA_FULL_VERSION%") do (
    if "%%v" == "1" (
    	set JAVA_VERSION=%%w
    ) else (
    	set JAVA_VERSION=%%v
    )
)

echo.
echo JAVA_VERSION=%JAVA_VERSION%

if "%JAVA_VERSION%" LSS "9" (
   echo "--- Pre-Java 9 detected (Java version %JAVA_VERSION%) ---"
   echo "Using DEFAULT_JVM_OPTS variable with value '%DEFAULT_JVM_OPTS%'"
) else (
   echo "--- Java 9 or higher detected (Java version %JAVA_VERSION%) ---"
   set DEFAULT_JVM_OPTS=--illegal-access=warn --add-modules=java.xml.bind,java.activation %DEFAULT_JVM_OPTS%
   echo "Adding JVM args to the DEFAULT_JVM_OPTS variable, new value set to '%DEFAULT_JVM_OPTS%'"
   echo "--------------------------------------------------------------------------------------------------------------"
)

@echo on
@rem Execute Record
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %RECORD_OPTS% -jar "%JARFILE%" %PARAM_CONFIG_FILE% %PARAM_STORE_DIR% %PARAM_SOURCECODE_DIR% %CMD_LINE_ARGS%
@echo off

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable RECORD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%RECORD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
