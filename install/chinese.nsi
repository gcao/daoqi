!define LANGUAGE "SimpChinese"
!define OUTPUT_FILE "DaoqiSetup.exe"

!define PRODUCT_NAME "������ĳ���"
!define PRODUCT_SHORT_NAME "����"
!define PRODUCT_VERSION "1.10"
!define PRODUCT_PUBLISHER "���尮����Э��"
!define PRODUCT_WEB_SITE "http://www.daoqigame.com"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_UNINST_KEY "Software\Daoqi\CurrentVersion\Uninstall\${PRODUCT_NAME}"

!define DAOQI_HOMEPAGE "���尮����Э��"
!define DAOQI_HOMEPAGE_URL_FILE "docs\daoqi-cn.url"
!define DAOQI_USERGUIDE "ʹ��˵��"
!define DAOQI_USERGUIDE_URL_FILE "docs\userguide-cn.url"
!define DAOQI_BOARD "��������"
!define DAOQI_BOARD_CMD "openboard-cn.exe"
!define DAOQI_QUICK_LOGIN "������ٵ�¼����"
!define DAOQI_QUICK_LOGIN_CMD "quicklogin-cn.exe"
!define DAOQI_CLIENT "������ĳ���"
!define DAOQI_CLIENT_CMD "run-cn.bat"
!define DAOQI_UNINSTALL "ж�ص�����ĳ���"

!define UNINSTALL_SUCCESS "$(^Name) �ѳɹ��ش���ļ�����Ƴ���"
!define UNINSTALL_CONFIRM "��ȷʵҪ��ȫ�Ƴ� $(^Name) ���������е������"
!define APPLICATION_RUNNING "������ĳ��������������С�����ֹ�ó�������ԣ�"

!include "base.nsi"