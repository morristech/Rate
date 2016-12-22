# API Document for Rate Assistant
This is the REST API document for Rate Assistant, a currency exchange rate search and subscription application.

1. User
====
(1)`api/user/register`

   - Register a new account.
   - method: POST
   - param:
      - email(String): email of an account
      - password(String): password of this account
      - telephone(String): telephone of this account
      - uname(String): user name
   - return:
      - uid(String): physical id of this account.
   - error:
      - ErrorCodeEmailExsit(300): This email has been signed up.
      - ErrorCodeTelephoneExsit(200): This telephone has been signed up.

(2)`api/user/login`

   - Login at first time.
   - method: POST
   - param:
      - email(String): email of an account
      - password(String): password of this account
      - device_token(String): device token from APNs server
      - os(String): name of operating system and system version
      - did(String): uuid of device
      - lan(String): main language of this device
   - return:
      - login_token(String): token for this device
   - error:
      - ErrorCodePasswordWrong(301): Password is wrong.
      - ErrorCodeAccountNotFound(302): This account is not exist.

(3)`api/user/logout`

   - Logout an account.
   - method: DELETE
   - header:
      - token(String): authentication token
   - return:
      - status(String): logout status
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(4)`api/user/change_uname`

   - Modify user name.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - uname(String): new user name
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(5)`api/user/device_token`

   - Resubmit device token when app is opened.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - device_token(String): the new device token from APNs server.
      - lan(String): main language of this device
   - return:
      - token(String): new authentication token
      - uname(String): name of this user
      - telephone(int): telephone of this user
      - email(String): email of this user
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(6)`api/user/favorite`

   - Add or remove a favorite currency.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - cid(String): currency id
      - favorite(boolean): add(use true) or remove(user false) this favorite currency
   - return:
      - fid(String): physical id of this favorite reocrd
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(7)`api/user/subscribe`

   - Add a new subscription.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - sname(String): Subscription name
      - from(String): Physical id of from currency
      - to(String): Physical id of to currency
      - isEnable(boolean): This subscription is enable or not
      - isSendEmail(boolean): Send email to user
      - isSendSms(boolean): Send SMS to user
      - threshold(double): threshold
      - isAbove(boolean): Notify user when current rate value is above than threshold or not.
   - return:
      - sid(String): physical id of this subscription
   - error:
      - ErrorCodeTokenError(350): Authentication token error.
      - ErrorCodeMailNeedActivate(330): Email not active.

(8)`api/user/subscribe`

   - Add a new subscription.
   - method: DELETE
   - header:
      - token(String): authentication token
   - param:
      - sid(String): physical id of this subscription
   - return:
      - isDeleted(boolean): this subscription is deleted successfully or not
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(9)`api/user/subscribe/update`

   - Add a new subscription.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - sid(String): physical id of this subscription
      - sname(String): Subscription name
      - isEnable(boolean): This subscription is enable or not
      - isSendEmail(boolean): Send email to user
      - isSendSms(boolean): Send SMS to user
      - threshold(double): threshold
      - isAbove(boolean): Notify user when current rate value is above than threshold or not.
   - return:
      - sid(String): physical id of this subscription
   - error:
      - ErrorCodeTokenError(350): Authentication token error.
      - ErrorCodeMailNeedActivate(330): Email not active.

(10)`api/user/subscribes`

   - Add a new subscription.
   - method: PUT
   - header:
      - token(String): authentication token
   - param:
      - rev(int): revision id
      - sids(Array\<String>): physical id list of subscriptions
   - return:
      - isUpdated(boolean): subscriptions are updated or not
      - data.createdOrUpdated(Array\<Subscribe>): new subscriptions or updated subscriptions.
      - data.deletedSubcribes(Array\<String>): physical id list of deleted subscriptions
      - data.rates(Dictionary\<String, double>): current rates for subscriptions
      - current(int): current revision
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(11)`api/user/notification`

   - Change notification state.
   - method: POST
   - header:
      - token(String): authentication token
   - param:
      - enable(enable): remote notification for subscription is enable or not
   - return:
      - status(String): status
   - error:
      - ErrorCodeTokenError(350): Authentication token error.

(12)`api/user/add_feedback`

   - Add a feedback.
   - method: POST
   - param:
      - type(int): type of faceback
      - content(String): content of feedback
      - contact(String): contact way of sender
   - return:
      - fdid(String): physical id of this feedback

(13)`api/user/verification_code`

   - Send a verification code to user's email for modifying password.
   - method: POST
   - param:
      - email(String): email of an account
   - return:
      - status(String): status
   - error:
      - ErrorCodeMailNotExist(360): This email is not exist in database.

(14)`api/user/change_password`

   - Modify password by submitting verification code.
   - method: POST
   - param:
      - email(String): email of an account
      - password(String): new password of this account
      - vertificationCode(String): verification code
   - return:
      - status(String): status
   - error:
      - ErrorCodeMailNotExist(360): This email is not exist in database.
      - ErrorCodeInvalidVerification(343): Verification code is error.
      - ErrorCodeVerificationExpiration(344): Verification code is out of date.





