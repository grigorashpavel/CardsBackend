package com.pasha.database

object ColumnData {
    // USERS
    const val COL_USER_ID_NAME = "user_id"

    const val COL_USERNAME_NAME = "username"
    const val USERNAME_LEN = 20

    const val COL_AVATAR_NAME = "avatar_path"

    const val COL_PASSWORD_NAME = "password"
    const val PASSWORD_LEN = 20

    const val COL_PHONE_NUMBER_NAME = "phone_number"
    const val PHONE_LEN = 20

    // EMAILS
    const val COL_EMAIL_ID_NAME = "email_id"
    const val COL_EMAIL_NAME = "email"
    const val EMAIL_LEN = 32

    // DEVICES
    const val COL_DEVICE_ID_NAME = "device_id"
    const val COL_DEVICE_NAME = "device_name"
    const val DEVICE_LEN = 32

    // TOKENS
    const val COL_TOKEN_ID_NAME = "token_id"
    const val COL_REVOKE_STATUS_NAME = "revoked"
    const val COL_EXPIRE_TIME_NAME = "expire_time"

    // CARDS
    const val COL_CARD_NAME_NAME = "card_name"
    const val CARD_NAME_LEN = 32
    const val COL_CARD_PATH_NAME = "card_path"
    const val COL_CARD_CREATION_NAME = "card_time"

    const val SYSTEM_PATH_LEN = 255
}