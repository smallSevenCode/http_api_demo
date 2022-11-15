## 公私钥生成步骤



>  通过服务器使用输入以下命令行生成



#### 生成私钥

```shell
# 生成rsa私钥，以X509编码，指定生成的密钥的位数: 2048
openssl genrsa -out rsa_private_key_2048.pem 2048

# 将上一步生成的rsa私钥转换成PKCS#8编码
openssl pkcs8 -topk8 -in rsa_private_key_2048.pem -out pkcs8_rsa_private_key_2048.pem -nocrypt
```

#### 生成公钥

```shell
openssl rsa -in rsa_private_key_2048.pem -out rsa_public_key_2048.pem -pubout
```



#### 测试步骤

```html
当前测试项目中替换Account类中的账户和公私钥参数

HttpApiDemoTest测试类中替换url地址,根据接口文档填入请求参数

发送请求
```

