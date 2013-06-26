package org.cometdocs;

enum Status
{
    OK,
    InternalError,
    UnsupportedAPIMethod,
    MethodInvocationError,
    HttpsRequired,
    InvalidToken,
    BadParameters,
    PermissionError,
    AvailableSpaceExceeded,
    AvailableConversionsExceeded,
    SimultaneousConversionsNotAllowed,
    APSBusy,
    FileSizeLimitExceeded,
    UnsupportedFileExtension,
    APILimitsExceeded,
    FileTransferLimitsExceeded,
    FileTransferRecipientEmailError
}
