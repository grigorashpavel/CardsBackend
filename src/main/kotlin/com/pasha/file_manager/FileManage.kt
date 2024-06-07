package com.pasha.file_manager

import com.pasha.util.Constants
import io.ktor.http.content.*
import java.io.File

class FileManager private constructor() {
    companion object {
        fun createDirectoryIfNotExist(email: String): String {
            val directory = File("${Constants.USR_DIRS_PATH}$email")
            if (directory.exists().not()) directory.mkdirs()

            return "${Constants.USR_DIRS_PATH}$email"
        }

        fun saveFileByPartData(parts: PartData.FileItem?, path: String): Boolean {
            if (parts != null) {
                val originalFileName = parts.originalFileName ?: return false
                //val fileExtension = originalFileName.substringAfterLast('.')
                val fileExtension = "jpg"
                val fileName = "avatar.$fileExtension"

                val file = File(path, fileName)

                parts.streamProvider().use { input ->
                    file.outputStream().buffered().use { output ->
                        input.copyTo(output)
                    }
                }

                return true
            }
            return false
        }
    }
}