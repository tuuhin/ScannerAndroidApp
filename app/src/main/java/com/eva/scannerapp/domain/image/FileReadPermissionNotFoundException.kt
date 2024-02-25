package com.eva.scannerapp.domain.image

class FileReadPermissionNotFoundException :
	Exception("Permission to read files is not granted by the system")