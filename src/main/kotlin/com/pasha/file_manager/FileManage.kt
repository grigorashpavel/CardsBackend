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

        fun createCardDirectoryIfNotExist(email: String): String {
            val directory = File("${Constants.USR_DIRS_PATH}$email/cards/")
            if (directory.exists().not()) directory.mkdirs()

            return "${Constants.USR_DIRS_PATH}$email/cards/"
        }


        fun saveFileByPartData(parts: PartData.FileItem?, path: String, fileName: String = ""): Boolean {
            if (parts != null) {
                val name = fileName.ifEmpty { "avatar.jpg" }
                val file = File(path, name)

                parts.streamProvider().use { input ->
                    file.outputStream().buffered().use { output ->
                        input.copyTo(output)
                    }
                }

                return true
            }
            return false
        }

        fun removeFileByPath(path: String): Boolean {
            val file = File(path)
            if (file.exists()) {
                file.delete()
                return true
            }

            return false
        }

        fun getFile(path: String): File? {
            val file = File(path)
            if (file.exists()) {
                return file
            } else {
                return null
            }
        }
    }
}