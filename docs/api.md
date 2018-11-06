<!-- --- title: Mpen API设计规范 -->

# <span id="normalized_string">1 URI字符串规范 </span>

一个规范字符串只包含百分号编码字符以及URI非保留字符。

<span id="unreserved_chars">URI非保留字符（unreserved characters）</span>

* [RFC 3986](http://tools.ietf.org/html/rfc3986#section-2.3)规定URI非保留字符包括以下字符：字母（A-Z，a-z）、数字（0-9）、连字号（`-`）、点号（`.`）、下划线（`_`)、波浪线（`~`）。

将任意一个字符串转换为规范字符串的方式是:

1. 将字符串转换成UTF-8编码的字节流
2. 保留所有URI非保留字符原样不变
3. 对其余字节做一次RFC 3986中规定的百分号编码（percent-encoding），即一个%后面跟着两个表示该字节值的十六进制字母。字母一律采用大写形式。

示例：

* 原字符串：`this is an encoding test for 测试`
* 转换后的字符串：`this%20is%20an%encoding%20test%20for%20%E6%B5%8B%E8%AF%95`


# <span id="datetime">2 日期与时间</span>

统一一律采用UTC时间，遵循[ISO 8601](http://www.w3.org/TR/NOTE-datetime)，并做以下约束：

1. 日期：`YYYY-MM-DD`，例如`2017-07-01`表示2017年6月1日
2. 时间：`hh:mm:ss`，在最后加一个大写字母Z表示UTC时间。例如`23:00:20Z`表示UTC时间23点0分20秒。
3. 日期和时间：`2017-06-01T23:00:20Z`表示UTC时间2017年6月1日23点0分20秒。

# <span id="authorization">3 认证机制</span>

所有API的安全认证一律采用Access Key与请求签名机制。

Access Key由Access Key ID和Secret Access Key组成，均为字符串。

对于每个HTTP请求，使用下面所描述的算法生成一个认证字符串。提交认证字符串可以有三种方式：

1. 放在`Authorization`头域里。这是最常用的场景，大部分都应该使用这种方式实现。
2. 放在Query String的`authorization`参数中。常用于生成URL给第三方使用的场景，例如要临时把某个数据开放给他人下载。
3. 放在Form Post Data的`authorization`域中。在允许用户直接使用HTML Form提交数据的场景下，需要把认证字符串放在Form Data里面。

具体支持哪些场景，由服务自行决定。服务端需要根据生成算法验证认证字符串的正确性。

认证字符串的格式为`mpen-auth-v{version}/{accessKeyId}/{timestamp}/{expirationPeriodInSeconds}/{signedHeaders}/{signature}`。

* version是正整数，目前取值为1。
* timestamp是生成签名时的时间，格式符合[前文描述](#datetime)。
* expirationPeriodInSeconds表示签名有效期限。
* signedHeaders是签名算法中涉及到的头域列表。头域名之间用分号（`;`）分隔，如`host;range;x-mpen-date`。列表按照字典序排列。当signedHeaders为空时表示取默认值，详见[下文](#canonical_headers)。
* signature是256位签名的十六进制表示，由64个小写字母组成。其生成算法见下。

##5.1 签名算法

签名中使用的Hash算法使用[HMAC SHA256](http://tools.ietf.org/html/rfc4868)。Hash结果转换成小写形式的十六进制字符串。后文一律使用SHA256-HEX(key, string)的方式表示，不再赘述。

Normalize(str)表示将str转换为[规范字符串](#normalized_string)。

具体算法如下：

###5.1.1 生成SigningKey

SigningKey = SHA256-HEX("`{secretAccessKey}`", "`mpen-auth-v{version}/{accessKeyId}/{timestamp}/{expirationPeriodInSeconds}`")

###5.1.2 生成CanonicalRequest

CanonicalRequest是一个以换行符（`\n`）分隔的字符串，由HTTP Method、URI、QueryString、Headers五部分组成。

CanonicalRequest = toUpperCase(`HTTP Method`) + "\n" + CanonicalURI + "\n" + CanonicalQueryString + "\n" + CanonicalHeaders

####5.1.2.1 生成CanonicalURI

CanonicalURI = NormalizeExceptSlash(`URL Path`)，也就是说将Path看成是以斜杠（`/`）分隔的字符串，对每个部分做[规范化](#normalized_string)。

例如`http://mpen.com/v1/example/测试`，其URL Path为`/v1/example/测试`，将之[规范化](#normalized_string)得到`/v1/example/%E6%B5%8B%E8%AF%95`。

####5.1.2.2 生成CanonicalQueryString

所有参数除了`authorization`之外都应该被加入到CanonicalQueryString中。

首先对要签名的所有参数名和参数值做[规范化](#normalized_string)，然后用等号（`=`）连接每个参数名和参数值。将所有字符串按照字典序排列后用与号（`&`）连接起来得到CanonicalQueryString。

例如`http://mpen.com/v1/instance/rdsmstmcrpo3qxh?restore&snapshotId=5BQwvH0i8vrghDq`，其Query String为`restore&snapshotId=5BQwvH0i8vrghDq`，包含两个参数restore和snapshotId，其中restore的参数值为空。

将restore和snapshotId分别做[规范化](#normalized_string)，并用等号连接参数名和参数值后得到`restore=`以及`snapshotId=5BQwvH0i8vrghDq`。注意这里restore后面多了一个等号（`=`）。这是因为它的值是空白字符串。

最后将两个字符串按字典序排序后，再用与号（`&`）连接起来之后得到`restore=&snapshotId=5BQwvH0i8vrghDq`。

####<span id="canonical_headers">5.1.2.3 生成CanonicalHeaders</span>

Header中并非所有域都需要签名，很多HTTP头域是否加入签名不影响安全性。服务应自行定义需要签名的头域，但需遵循以下约束。

必须被签名的头域包括Host。对于有Content的请求并且需要对内容进行签名的时候，应使用`[x-mpen-content-sha256](#req_header_x-mpen-content-sha256)`头域，并将值设为SHA256-HEX(Content)。注意这里内容指HTTP Request Payload Body。即Content部分在被HTTP encode之前的原始数据。

在signedHeaders为空时，默认值为host以及所有以x-mpen-开头的头域。当头部包含Content-Length、Content-Type、Content-MD5中的一个或多个时，也需要将它们包含在内。

对于所有要被签名的头域，首先将每个头域的名字转换成小写，并去掉值开头和结尾的空白字符。然后忽略掉所有值为空的头域，对于剩余的头域将名字和值分别[规范化](#normalized_string)之后用冒号（`:`）连接起来。最后将所有字符串按照字典序排列后用换行符（`\n`）连接起来得到CanonicalHeaders。

一个最终的CanonicalHeaders示例如下
```text
host:rds.bj.baidumpen.com
x-mpen-content-sha256:e3d5591edbab45d28817fcb1463501d9e68ecb9730f440ac975da368ca4adbfc
x-mpen-date:2013-07-08T22%3A08%3A55Z
```
注意x-mpen-date里面的冒号（`:`）已经被[规范化](#normalized_string)成`%3A`。

###5.1.3 生成最终签名

Signature = SHA256-HEX(SigningKey, CanonicalRequest)

##5.2 服务端验证签名

在服务端验证签名时，应重复上文所述的签名算法，和Signature进行比较。如果一致则验证通过，否则返回[`SignatureDoesNotMatch`](#error_code_SignatureDoesNotMatch)。

#<span id="restful_spec">9 RESTful API设计规范</span>

##6.1 通用说明

数据交换格式使用[JSON](http://www.json.org/)。

`Content-Type`使用`applicaton/json`。

所有request/response body内容均使用UTF-8编码。后续再考虑支持多encoding。

###<span id="request_id">6.1.1 Request Id</span>

所有请求都应该唯一地对应一个ID，用于标识该请求。requestId可用于问题定位、性能分析等等多个场景。所有的日志都应该带有requestId以便后续分析。

requestId使用[UUID version 4](http://tools.ietf.org/html/rfc4122#section-4.4)，暂时由各服务自行生成，后续考虑统一由BFE生成。

[所有响应的头部](#response_header)都应该包含requestId。在出错时[返回结果](#errors)也应该同时包含requestId。正常结果中**不应该**带有requestId。

###<span id="parameter_with_unit">6.1.2 带单位的参数</span>

对于某些带单位的参数，例如capacity。需要在参数名中显式注明其单位，例如capacityInBytes，capacityInGB这样。

###<span id="etag">6.1.3 ETag的支持</span>

所有服务必须支持[ETag](http://tools.ietf.org/html/rfc2616#section-14.19)。出于认证机制支持的考虑，不使用标准的`If-Match`和`If-None-Match`头域，而是使用自定义的`x-mpen-if-match`和`x-mpen-if-none-match`。

ETag与URL的内容有着对应关系。当URL内容发生变化时，ETag也随着变化。通常实现可以是用内容的hash签名或者更新时间戳。

ETag常用于内容缓冲，可以避免重复获取同一份数据的内容，提升效率。但在RESTful API设计中有着更为重要的意义。`ETag`和[`x-mpen-if-match`](#req_header_x-mpen-if-match)配合使用可以实现`Conditional Write`，可用于实现原子更新。

考虑以下场景：A提交了一份配置，然后B又提交了另一份。B会覆盖掉A的更改。一个更为合适的做法是B在提交之前应该读取当前最新的配置，合并之后再提交。为了保证这个读取到再提交中间没有其他更新被提交，B在提交时将[`x-mpen-if-match`](#req_header_x-mpen-if-match)头域设为之前读到的ETag值。服务会比较是否和当前的ETag值一致，只有在一致时才允许更新，否则返回[`PreconditionFailed`](#error_code_PreconditionFailed)。当B收到[`PreconditionFailed`](#error_code_PreconditionFailed)时，应重新读取最新配置，合并之后再提交。

`Conditional Write`本质上是一种乐观锁的实现。在冲突密集的时候表现很差。对于这样的场景，应考虑其他解决方案。

特别要注意的是ETag对应的是整个URL的内容，包括了query string，并不仅仅是URL path。因此对于一个RESTful URL来说ETag不一定对应的是资源本身，也可能是资源的配置，视具体URL而定。

例如`http://mpen.com/v1/example`的ETag对应的是example的Object列表，而`http://mpen.com/v1/example?configuration`的ETag对应的是example的配置。

但在某些情况下可以复用ETag。例如`http://mpen.com/v1/instance/rdsmstmcrpo3qxh`和`http://mpen.com/v1/instance/rdsmstmcrpo3qxh?backupPolicy`，前者是Instance的配置，后者是Instance配置中的备份策略部分。后者可以直接复用前者的ETag，这样实现最为简单。配置的数据量很小，即便在备份策略不变的情况下ETag发生了变化，重复读取这部分内容也不会有多少效率损失。

支持ETag会增加实现复杂度。在一期中，如果某些API没有保证原子更新的场景需求，可以考虑暂时不支持，但应在API文档中明确注明。

###<span id="timestamp">6.1.4 时间戳与请求超时</span>

所有请求都必须使用头域`Date`或者[`x-mpen-date`](#req_header_x-mpen-date)指定时间戳。当服务器收到请求时，需要比较时间戳与当前服务器时间。当两者相差超过30分钟时，返回[`RequestExpired`](#error_code_RequestExpired)错误。这样可以在一定程度上防止重放。

###<span id="date_type">6.1.5 数据类型</span>

下表列出了所有API可使用的通用数据类型。每个服务在必要的时候应考虑自定义数据类型并独立加以说明。

类型 | 说明
--- | ---
<span id="data_type_int">Int</span> | 64位有符号整数。对于超出该范围需求的整数，应使用string，然后自行转换。考虑多语言需求，不支持无符号整数。
<span id="data_type_double">Double</span> | 所有浮点数都应使用双精度类型。可以采用科学记数法表示。
<span id="data_type_boolean">Boolean</span> | true和false，大小写不敏感。
<span id="data_type_date">Date</span> | 符合本规范的日期格式约束的字符串
<span id="data_type_time">Time</span> | 符合本规范的时间格式约束的字符串
<span id="data_type_datetime">DateTime</span> | 符合本规范的日期时间格式约束的字符串
<span id="data_type_string">String</span> | UTF-8编码字符串
<span id="data_type_list">List<`{ElementType}`></span> | 列表，`{ElementType}`应替换成列表中的元素的实际类型。

在需要枚举类型时，一律使用`String`类型，并在API文档中说明具体的取值列表。禁止使用`Int`表示枚举类型。

##<span id="url">6.2 URL</span>

RESTful API的URL应满足以下条件：

1. URL Host必须是服务端点。
2. URL路径（Path）必须以`/v{version}/`开头。version必须是一个正整数。对RESTful API的修正可以在原先基础上不断添加新的API，而不需要频繁改动版本号。只有在大的原则发生变化或者认为变动已经足够大的时候才需要升级版本号。
3. URL路径（Path）应唯一地指向一个“资源”。一个大的参考原则是路径中不能出现任何动词。
    路径中的名词用单数形式。如：bos中的bucket/object, rds中的instance/database。
    路径中资源的层级关系是包含语义，满足低层级的资源无法脱离上级资源存在，删除高层级资源会导致所有下级资源删除。如bos中bucket/object是层级关系，而rds中instance和snapshot不是层级关系。
4. 查询字符串（Query String）中的参数名（Parameter Name）必须使用lowerCamelCase表示。`authorization`是认证专用参数名，各服务API不得占用。
5. 出于实现简单的考虑，查询字符串（Query String）对于参数名（Parameter Name）大小写敏感。但在API设计中，严禁使用大小写来区分不同的参数。使用相近的参数名也应该避免。

##<span id="http_method">9.3 HTTP method</span>

仅允许使用以下HTTP method

1. GET
    读取资源
2. POST
    可以有两个含义：
    * 创建资源
    * 一些较为通用的修改需求，主观感觉不适合用PUT方法的。例如bos用POST方法对某个历史版本的Object进行恢复。
3. PUT
    修改资源。对于不区分创建和修改资源的服务，可以一律用PUT，例如bos。
4. DELETE
    删除资源
5. HEAD
    轻量级获取资源状态。例如bos用HEAD方法实现对资源存在性的检查。
6. OPTIONS
    极少使用。一般可用于返回对资源的访问权限。

设计中禁用PATCH方法。PATCH方法可以实现partial update，但除非引入很不直观JSON patch格式(RFC 6902)，否则在语义表达上会存在很多含糊的地方，不利于简单原则。凡是需要PATCH语义的场景，我们一律使用PUT加query string的形式来替代。以bos为例：
    * PUT Object：修改Object本身
    * PUT Object?acl：修改Object的ACL

对于资源的操作可以有很多种，单一的PUT方法无法区分不同的操作。对于这种场景，我们采用query string方式加以区分，通常形式为`?<action>`，例如`?reboot`。

##<span id="request_header">6.4 公共请求头（Common Request Header）</span>

下表列出了所有API应该支持的公共请求头。HTTP协议的标准头域如Content-Length、Content-Type等如无必要不再重复说明。

头域（Header） | 是否必须 | 说明
--- | --- | ---
<span id="req_header_authorization">Authorization</span> | 必须 | 详见[认证机制](#authorization)
<span id="req_header_x-mpen-date">x-mpen-date</span> | 可选 | 表示日期的字符串，遵循[本规范规定的日期时间格式](#datetime)。如果用户使用了标准的Date域，该头域可以不填。当两者同时存在时，以x-mpen-date为准。
<span id="req_header_x-mpen-content-sha256">x-mpen-content-sha256</span> | 可选 | 表示内容部分的SHA256签名的十六进制字符串。这里内容指HTTP Request Payload Body。即Content部分在被HTTP encode之前的原始数据。
<span id="req_header_x-mpen-content-if-not-match">x-mpen-if-match</span> | 可选 | 同[If-Match](http://tools.ietf.org/html/rfc2616#section-14.24)语义。
<span id="req_header_x-mpen-content-if-none-match">x-mpen-if-none-match</span> | 可选 | 同[If-None-Match](http://tools.ietf.org/html/rfc2616#section-14.26)语义。

##<span id="response_header">6.5 公共响应头（Common Response Header）</span>

下表列出了所有API应该支持的公共响应头

头域（Header） | 说明
---- | ---
<span id="res_header_content-type">Content-Type</span> | 在返回结果是json的时候，应使用`application/json; charset=utf-8`。由于我们一期编码只支持utf-8，所以这里charset是固定的。后续如果考虑支持多编码时再修改。
<span id="res_header_x-mpen-request-id">x-mpen-request-id</span> | 对应请求的requestId

##<span id="status_code">9.6 响应状态码（Status Code）</span>

应使用标准的HTTP状态码（Status Code），遵循[RFC 2616 section 6.1.1](http://tools.ietf.org/html/rfc2616#section-6.1.1)。

* 1xx: Informational - Request received, continuing process
* 2xx: Success - The action was successfully received, understood, and accepted
*  3xx: Redirection - Further action must be taken in order to complete the request
*  4xx: Client Error - The request contains bad syntax or cannot be fulfilled
* 5xx: Server Error - The server failed to fulfill an apparently valid request

状态码含义应符合[标准定义](http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml)

##<span id="errors">6.7 错误</span>

所有错误除了使用HTTP状态码以外，应同时在内容中至少包含下表的参数。各服务应该适当地增加错误时返回的参数以方便定位。

参数名 | 类型 | 说明
--- | --- | ---
requestId | [String](#data_type_string) | 导致该错误的requestId
code | [String](#data_type_string) | 字符串，用于表示具体错误类型
message | [String](#data_type_string) | 有关该错误的详细说明

示例：
```json
{
    "requestId" : "ae2225f7-1c2e-427a-a1ad-5413b762957d",
    "code" : "NoSuchKey",
    "message" : "The resource you requested does not exist"
}
```

###<span id="error_code">6.7.1 公共错误码（Error Code）</span>

下表列出了所有API的公共错误码。各服务应该在此基础上自定义错误码。

<table border="1">
    <tr>
        <th>Code</th>
        <th>Message</th>
        <th>HTTP Status Code</th>
        <th>说明</th>
    </tr>
    <tr>
        <td><span id="error_code_AccessDenied">AccessDenied</span></td>
        <td>Access denied.</td>
        <td>403 Forbidden</td>
        <td>无权限访问对应的资源</td>
    </tr>
    <tr>
        <td><span id="error_code_InappropriateJSON">InappropriateJSON</span></td>
        <td>The JSON you provided was well-formed and valid, but not appropriate for this operation.</td>
        <td>400 Bad Request</td>
        <td>请求中的JSON格式正确，但语义上不符合要求。如缺少某个必需项，或者值类型不匹配等。出于兼容性考虑，对于所有无法识别的项应直接忽略，不应该返回这个错误。</td>
    </tr>
    <tr>
        <td><span id="error_code_InternalError">InternalError</span></td>
        <td>We encountered an internal error. Please try again.</td>
        <td>500 Internal Server Error</td>
        <td>所有未定义的其他错误。在有明确对应的其他类型的错误时（包括通用的和服务自定义的）不应该使用。</td>
    </tr>
    <tr>
        <td><span id="error_code_InvalidAccessKeyId">InvalidAccessKeyId</span></td>
        <td>The Access Key ID you provided does not exist in our records.</td>
        <td>403 Forbidden</td>
        <td>Access Key ID不存在</td>
    </tr>
    <tr>
        <td><span id="error_code_InvalidHTTPAuthHeader">InvalidHTTPAuthHeader</span></td>
        <td>The HTTP authorization header is invalid. Consult the service documentation for details.</td>
        <td>400 Bad Request</td>
        <td>Authorization头域格式错误</td>
    </tr>
    <tr>
        <td><span id="error_code_InvalidHTTPRequest">InvalidHTTPRequest</span></td>
        <td>There was an error in the body of your HTTP request.</td>
        <td>400 Bad Request</td>
        <td>HTTP body格式错误。例如不符合指定的Encoding等</td>
    </tr>
    <tr>
        <td><span id="error_code_InvalidURI">InvalidURI</span></td>
        <td>Could not parse the specified URI.</td>
        <td>400 Bad Request</td>
        <td>URI形式不正确。例如一些服务定义的关键词不匹配等。对于ID不匹配等问题，应定义更加具体的错误码，例如NoSuchKey。</td>
    </tr>
    <tr>
        <td><span id="error_code_MalformedJSON">MalformedJSON</span></td>
        <td>The JSON you provided was not well-formed.</td>
        <td>400 Bad Request</td>
        <td>JSON格式不合法</td>
    </tr>
    <tr>
        <td><span id="error_code_InvalidVersion">InvalidVersion</span></td>
        <td>The API version specified was invalid.</td>
        <td>404 Not Found</td>    
        <td>URI的版本号不合法</td>
    </tr>
    <tr>
        <td><span id="error_code_OptInRequired">OptInRequired</span></td>
        <td>A subscription for the service is required.</td>
        <td>403 Forbidden</td>    
        <td>没有开通对应的服务</td>
    </tr><tr>
        <td><span id="error_code_PreconditionFailed">PreconditionFailed</span></td>
        <td>The specified If-Match header doesn't match the ETag header.</td>
        <td>412 Precondition Failed</td>    
        <td>详见[ETag的使用](#etag)</td>
    </tr>
    <tr>
        <td><span id="error_code_RequestExpired">RequestExpired</span></td>
        <td>Request has expired. Timestamp date is XXX. </td>
        <td>400 Bad Request</td>    
        <td>请求超时。XXX要改成[x-mpen-date](#req_header_x-mpen-date)的值。如果请求中只有Date，则需要将Date转换为[本规范指定的格式](#datetime)。</td>
    </tr>
    <tr>
        <td><span id="error_code_IdempotentParameterMismatch">IdempotentParameterMismatch</span></td>
        <td>The request uses the same client token as a previous, but non-identical request.</td>
        <td>403 Forbidden</td>
        <td>clientToken对应的API参数不一样。</td>
    </tr>
    <tr>
        <td><span id="error_code_SignatureDoesNotMatch">SignatureDoesNotMatch</span></td>
        <td>The request signature we calculated does not match the signature you provided. Check your Secret Access Key and signing method. Consult the service documentation for details.</td>
        <td>400 Bad Request</td>
        <td>Authorization头域中附带的签名和服务端验证不一致</td>
    </tr>
</table>

##<span id="json_spec">9.8 JSON规范</span>

* 必须是标准的JSON格式。注意object类型的key必须使用双引号（`"`）括起来。
* object类型的key必须使用`lowerCamelCase`表示。

##<span id="batch_operation">6.9 批量操作</span>

在API设计中，经常遇到希望在同一个请求里面操作多个对象的情况，这时候就需要用到批量操作。

批量操作通常是将多个请求合并放在body的一个列表里面。

举例：BQS的SendMessage

```text
POST /v1/queue/bqs0fdsjwe823ld/message HTTP/1.1

{
    "messages": [
        {
            "messageBody":"Base64 Encoded Message1",
            "delaySeconds":30
        },

        {
            "messageBody":"Base64 Encoded Message2",
        }
    ]
}
```

有的时候需要区分单个操作和批量操作的接口。原则是单个操作需要在URL中指定ID，批量操作的ID移到body里面。

举例：假设要为rds的修改数据库名字增加批量接口。

原先单个接口

```text
PUT /v1/instance/rdsmxiaozhiwen0?name HTTP/1.1

{
    "instanceName":"mysql55"
}
```

批量接口应该修改如下


```text
PUT /v1/instance?name HTTP/1.1

{
    "instances": [
        {
            "instanceId": "rdsmxiaozhiwen0",
            "instanceName":"mysql55"
        }
    ]
}
```

对于已经是单个操作的接口，可以以增加`?batch`的方式来标识批量操作。

举例：假设rds要支持不同配置的数据库实例批量创建

原先接口：

```text
POST /v1/instance HTTP/1.1

{
    "orderId"                       : "20140604012312400345678",
    "instanceAmount"                : 2,

    "instanceParameters"            : {
        "instanceName"              : "mysql5145",
        "engine"                    : "mysql",
        "engineVersion"             : "5.1",
        "instanceClass"             : "db1.micro",
        "allocatedMemoryInMB"       : 256,
        "allocatedStorageInGB"      : 5,

        "backupPolicy"              : {
            "preferredBackupDays"   : "0,1,2,4,5",
            "preferredBackupWindow" : "17:00:00Z-19:00:00Z"
        },

        "publiclyAccessible"        : true,
        "instanceExpireTime"        : "2014-07-01T12:00:00Z"
    },
}
```

因为已经设计为只能操作单一配置，增加数组不方便，因此可以加上`?batch`，做如下修改：

```text
POST /v1/instance?batch HTTP/1.1

{
    "instances": [
        {
            "orderId"                       : "20140604012312400345678",
            "instanceAmount"                : 2,

            "instanceParameters"            : {
                "instanceName"              : "mysql5145",
                "engine"                    : "mysql",
                "engineVersion"             : "5.1",
                "instanceClass"             : "db1.micro",
                "allocatedMemoryInMB"       : 256,
                "allocatedStorageInGB"      : 5,

                "backupPolicy"              : {
                    "preferredBackupDays"   : "0,1,2,4,5",
                    "preferredBackupWindow" : "17:00:00Z-19:00:00Z"
                },

                "publiclyAccessible"        : true,
                "instanceExpireTime"        : "2014-07-01T12:00:00Z"
            },
        }
    ]
}
```

为控制HTTP请求的大小，批量操作的对象数量应有上限，通常不超过1000。单条记录特别大时该数量还应该进一步减少。

##<span id="paging">6.10 结果分页</span>

当API返回结果过多时，应限制返回数量，采用分页机制来获取全列表。例如bos的ListObject操作。所有分页应统一采用marker机制。

marker是一个系统生成的字符串，用来标记查询的起始位置。

mark设计的常见误区是用序号表示marker。当目标对象集合发生变化时，序号对应的对象也随之改变。这将导致分页结果重复或者缺失。例如某个用户想要获取对象全列表，会多次调用API来获取。第一次获取前1000个对象，第二次获取第1000-2000个对象。在这两次调用期间如果有其他用户添加了一个排序靠前的对象，就会导致原先的第1000个对象出现在第二次的返回结果里面，从而出现重复。

为保证查询稳定性，marker通常设计为某个关键字的值或者是对值进行某种编码之后的结果，返回结果按照该关键字排序。

marker通常作为query string的一部分传给服务端。服务端返回分页结果时，应包含以下域：

* marker：服务端收到的marker值。
* isTruncated：true表示后面还有数据，false表示已经是最后一页。
* nextMarker：获取下一页所需要传递的marker值。当isTruncated为false时，该域不出现。

服务还应该提供一个参数允许用户指定每页包含的最大数量。为控制HTTP响应的大小，这个最大数量通常不超过1000。单条记录特别大时该数量还应该进一步减少。

##<span id="idempotency">6.11 幂等性</span>

###6.11.1 幂等性概述

在调用API时，很容易出现由于网络等问题导致客户端没收到响应连接就中断的情况。此时客户端无法得知服务器端是否收到了请求，重试又可能导致问题。例如一个创建实例的请求被多次发送就可能出现重复创建。对此，应加入幂等性的机制来加以应对。

幂等性的意思是无论同一个请求被重复发送多次，其结果都和发送一次一样。

mpen API采用clientToken机制来保证API调用的幂等性。

###6.11.2 clientToken

clientToken是一个长度不超过64位的ASCII字符串，通常放在query string里面，如`http://mpen.com/v1/instance?clientToken=be31b98c-5e41-4838-9830-9be700de5a20`。

clientToken的唯一性与服务及用户相关。不同用户的clientToken互不相关，因此用户无需关注和其他用户的clientToken冲突问题。对于允许匿名调用的API，所有匿名用户视为同一个用户。匿名用户要保证clientToken的唯一性时，应采用随机生成长token的方式，将冲突概率降到足够小。不同服务的clientToken也互不相关。同时一个非全局唯一的服务的不同region之间也可以重复使用clientToken。

clientToken的有效期为至少24小时，以服务端最后一次收到该clientToken为准。也就是说，如果客户端不断发送同一个clientToken，那么该clientToken将长期有效。

###6.11.3 服务端逻辑

当服务端收到带有clientToken的请求时，首先检查发起调用的用户是否曾经发送过同一个clientToken。如是，则应检查API的参数是否完全一致。当不一致时，返回[IdempotentParameterMismatch](#error_code_IdempotentParameterMismatch)。如完全一致，应返回正常结果。

例如创建一个实例时，通常会返回实例状态。如果clientToken相同，则不再重复创建实例，直接返回对应实例当前状态。

这里的API参数特指会对结果产生影响的url、query string和header等。某些不产生影响的部分如Authorization不包含在内。
