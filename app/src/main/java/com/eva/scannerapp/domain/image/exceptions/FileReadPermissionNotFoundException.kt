package com.eva.scannerapp.domain.image.exceptions

class FileReadPermissionNotFoundException :
	Exception("Permission to read files is not granted by the system")