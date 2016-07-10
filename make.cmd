@setlocal && echo off

@REM ----------------------------------------------------------------------------
@REM   make.cmd
@REM
@REM   This script runs the various tasks of the TealsMC project. Supply any
@REM   combination of the following keywords:
@REM
@REM       clean - delete all generated files
@REM
@REM       dev-setup - Set up Forge for project development
@REM
@REM       build - Build the distributable project archive file in
@REM               out/TealsMC.zip.
@REM
@REM       site  - Set up GitHub pages clone for web site development in site/ dir
@REM
@REM       fresh = clean + build
@REM       all   = clean + build
@REM
@REM ----------------------------------------------------------------------------

if "%1" equ "" (
    call :help
    exit /b 0
)

:processTargets
    set target=%1
    if /i "%target%" equ "all" (
        call :clean
        call :build
    ) else if /i "%target%" equ "dev-setup" (
        call :devSetup
    ) else if /i "%target%" equ "build" (
        call :build
    ) else if /i "%target%" equ "clean" (
        call :clean
    ) else if /i "%target%" equ "fresh" (
        call :clean
        call :build
    ) else if /i "%target%" equ "site" (
        call :site
    ) else if /i "%target%" equ "help" (
        call :help
    ) else if /i "%target%" equ "/?" (
        call :help
    ) else if /i "%target%" equ "-?" (
        call :help
    ) else (
        @echo ERROR: Unrecognized target ^(%target%^). 1>&2
        call :help
    )

    shift
    if "%1" neq "" goto :processTargets

:endLoop
exit /b 0


:clean
    rmdir 2>nul: /s /q out

    mkdir out
    goto :eof


:devSetup
    robocopy /mir build\skeleton\tealsmc src\forge\production\src\tealsmc
    goto :eof


:build
    call :check7z
    echo errorlevel = %errorlevel%
    if %errorlevel% neq 0 goto :eof

    call :clean

    set robocopy=robocopy /nfl /njh /njs

    for /f "delims=" %%v in (version.txt) do (
        set archiveBasename=tealsmc-%%v
    )

    @REM -- Eclipse Release

    set archive=%archiveBasename%-eclipse
    set dest=out\%archive%\TealsMC-Eclipse

    %robocopy% /mir   "build\ide\eclipse"                     %dest%
    %robocopy% /mir   "src\forge\production\lib"              %dest%\Minecraft\production\lib
    %robocopy% /mir   "src\forge\production\src\org"          %dest%\Minecraft\src\org
    %robocopy% /mir   "src\forge\production\resources"        %dest%\Minecraft\resources
    %robocopy% /mir   "src\forge\production\lib\gradle\start" %dest%\Minecraft\launcher
    %robocopy% /e /xo "build\skeleton"                        %dest%\Minecraft\src
    copy build\README-Distrib.txt %dest%\README.txt
    copy CHANGELOG.md             %dest%
    copy version.txt              %dest%

    pushd out\%archive%
        call 7z a ..\%archive%.zip .
    popd

    @REM -- IntelliJ IDEA Release

    set archive=%archiveBasename%-intellij
    set dest=out\%archive%\TealsMC-IntelliJ

    %robocopy% /mir "build\ide\intellij"                    %dest%\Forge
    %robocopy% /mir "src\forge\production\lib"              %dest%\Forge\production\lib
    %robocopy% /mir "src\forge\production\src\org"          %dest%\Forge\src\main\java\org
    %robocopy% /mir "src\forge\production\resources"        %dest%\Forge\src\main\resources
    %robocopy% /mir "src\forge\production\lib\gradle\start" %dest%\Forge\production\lib\gradle\start
    %robocopy% /e /xo "build\skeleton"                      %dest%\Forge\src\main\java
    copy build\README-Distrib.txt %dest%\README.txt
    copy CHANGELOG.md             %dest%
    copy version.txt              %dest%

    pushd out\%archive%
        call 7z a ..\%archive%.zip .
    popd

    goto :eof


:site
    @REM -- Create a clone of the web site source
    if exist site (
        @echo ERROR: 'site' directory already exists.
        goto :eof
    )
    for /f "delims=" %%u in ('git remote get-url origin') do set repoOriginURL=%%u
    git clone %repoOriginURL% site
    pushd site
        git checkout gh-pages
    popd

    goto :eof


:check7z
    @REM -- Ensure that the 7-zip command-line executable is available on the current path.
    where /q 7z
    if %errorlevel% neq 0 echo 1>&2ERROR: Executable 7z.exe not found.
    goto :eof


:help
    @echo.
    @echo.This script runs the various tasks of the TealsMC project. Supply any
    @echo.combination of the following keywords:
    @echo.
    @echo.    clean - delete all generated files
    @echo.
    @echo.    build - Build the distributable project archive file in
    @echo.            out/TealsMC.zip.
    @echo.
    @echo.    site  - Set up GitHub pages clone for web site development in site/ dir
    @echo.
    @echo.    fresh = clean + build
    @echo.    all   = clean + build
    @echo.
    goto :eof
