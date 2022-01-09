;SetCompressor lzma

!include "MUI2.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!define MUI_LICENSEPAGE_RADIOBUTTONS
!insertmacro MUI_PAGE_LICENSE "..\docs\LICENSE"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\bin\${DAOQI_QUICK_LOGIN_CMD}"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "${LANGUAGE}"

; Reserve files
; TODO: Commented. Will this affect anything?
;!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "${OUTPUT_FILE}"
InstallDir "$PROGRAMFILES\Daoqi"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "MainSection" SEC01
  System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i("DAOQI_HOME", "$INSTDIR").r0'
  SetOutPath "$INSTDIR"
  CreateDirectory "$SMPROGRAMS\${PRODUCT_SHORT_NAME}"
  ;CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_CLIENT}.lnk" "$INSTDIR\bin\${DAOQI_CLIENT_CMD}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_BOARD}.lnk" "$INSTDIR\bin\${DAOQI_BOARD_CMD}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_QUICK_LOGIN}.lnk" "$INSTDIR\bin\${DAOQI_QUICK_LOGIN_CMD}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_HOMEPAGE}.lnk" "$INSTDIR\${DAOQI_HOMEPAGE_URL_FILE}"
  CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_USERGUIDE}.lnk" "$INSTDIR\${DAOQI_USERGUIDE_URL_FILE}"
  CreateShortCut "$DESKTOP\${DAOQI_BOARD}.lnk" "$INSTDIR\bin\${DAOQI_BOARD_CMD}"
  CreateShortCut "$DESKTOP\${DAOQI_QUICK_LOGIN}.lnk" "$INSTDIR\bin\${DAOQI_QUICK_LOGIN_CMD}"
SectionEnd

Section -AdditionalIcons
  CreateShortCut "$SMPROGRAMS\${PRODUCT_SHORT_NAME}\${DAOQI_UNINSTALL}.lnk" "$INSTDIR\uninst.exe"
  SetOutPath "$INSTDIR\docs"
  File "..\docs\*.url"
  SetOutPath "$INSTDIR\bin"
  File "..\bin\*.*"
  SetOutPath "$INSTDIR\lib"
  File "..\target\daoqi.jar"
  SetOutPath "$INSTDIR\jre"
  ;File /r "..\..\j2re1.4.2_07-min\*.*"
  File /r "..\..\j2re1.4.2_13\*.*"
  ;File /r "C:\Program Files\Java\jre1.5.0_12\*.*"
  ;File /r "C:\Program Files\Java\jre1.6.0_02\*.*"
  SetOutPath "$INSTDIR\games"
  File /r "..\games\*.*"
SectionEnd

!define ALL_USERS
!include WriteEnvStr.nsh # or the name you chose
Section "Add Env Var"
  Push "DAOQI_HOME"
  Push "$INSTDIR"
  Call WriteEnvStr
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" ""
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd

Function un.onInit
  !insertmacro MUI_UNGETLANGUAGE
  FindWindow $0 "${PRODUCT_SHORT_NAME}" "${PRODUCT_SHORT_NAME}"
  StrCmp $0 "0"  0 +3     ;if window active goto +3
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "${UNINSTALL_CONFIRM}" IDYES +4
  Abort
  MessageBox MB_ICONEXCLAMATION|MB_OK "${APPLICATION_RUNNING}"
  Abort
FunctionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "${UNINSTALL_SUCCESS}"
FunctionEnd

Section Uninstall
  Delete "$DESKTOP\${DAOQI_BOARD}.lnk"
  Delete "$DESKTOP\${DAOQI_QUICK_LOGIN}.lnk"
  RMDir /r "$SMPROGRAMS\${PRODUCT_SHORT_NAME}"
  RMDir /r "$INSTDIR"
  # BEGIN remove environment variable
  Push "DAOQI_HOME"
  Call un.DeleteEnvStr
  # END
  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd